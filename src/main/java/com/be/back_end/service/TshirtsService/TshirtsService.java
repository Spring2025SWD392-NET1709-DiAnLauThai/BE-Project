package com.be.back_end.service.TshirtsService;

import com.be.back_end.dto.response.TshirtsDTO;

import com.be.back_end.dto.request.TshirtCreateRequest;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.BookingEnums;
import com.be.back_end.model.Bookingdetails;
import com.be.back_end.model.Color;
import com.be.back_end.model.TShirtColor;
import com.be.back_end.model.Tshirts;
import com.be.back_end.repository.*;
import com.be.back_end.utils.AccountUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TshirtsService implements  ITshirtsService{

    private final AccountUtils accountUtils;
    private final TshirtsRepository tshirtsRepository;
    private final AccountRepository accountRepository;
    private final ColorRepository  colorRepository;
    private final TshirtColorRepository tshirtColorRepository;
    private final BookingDetailsRepository bookingDetailsRepository;
    public TshirtsService(AccountUtils accountUtils, TshirtsRepository tshirtsRepository, AccountRepository accountRepository, ColorRepository colorRepository, TshirtColorRepository tshirtColorRepository, BookingDetailsRepository bookingDetailsRepository) {
        this.accountUtils = accountUtils;
        this.tshirtsRepository = tshirtsRepository;
        this.accountRepository = accountRepository;
        this.colorRepository = colorRepository;
        this.tshirtColorRepository = tshirtColorRepository;
        this.bookingDetailsRepository = bookingDetailsRepository;
    }
    private TshirtsDTO mapToDTO(Tshirts tshirts) {
        TshirtsDTO dto = new TshirtsDTO();
        dto.setTshirtId(tshirts.getId());
        dto.setDescription(tshirts.getDescription());
        dto.setName(tshirts.getName());
        dto.setCreatedAt(tshirts.getCreatedAt());
        dto.setImageUrl(tshirts.getImage_url());

        return dto;
    }

    private Tshirts mapToEntity(TshirtsDTO dto) {
        Tshirts tshirt = new Tshirts();

        tshirt.setName(dto.getName());
        tshirt.setDescription(dto.getDescription());
        tshirt.setCreatedAt(LocalDateTime.now());
        tshirt.setImage_url(dto.getImageUrl());
        tshirt.setStatus(ActivationEnums.ACTIVE);

        return tshirt;
    }
   @Override
   public Tshirts saveTshirt(TshirtCreateRequest tshirtCreateRequest) {

       Tshirts tshirt = new Tshirts();
       tshirt.setDescription(tshirtCreateRequest.getDescription());
       tshirt.setStatus(ActivationEnums.INACTIVE);
       tshirt.setName(tshirtCreateRequest.getTshirtname());
       tshirt.setImage_url(tshirtCreateRequest.getImgurl());
       tshirt.setImagesfile(tshirtCreateRequest.getImagefile());
       Tshirts savedTshirt = tshirtsRepository.save(tshirt);
       List<Color> colors = colorRepository.findAllById(tshirtCreateRequest.getColorlist());
       if (colors.isEmpty()) {
           return null;
       }
       for (Color color : colors) {
           TShirtColor tShirtColor = new TShirtColor();
           tShirtColor.setTshirt(savedTshirt);
           tShirtColor.setColor(color);
           tshirtColorRepository.save(tShirtColor);
       }

       return savedTshirt;
   }
    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDTO<TshirtsDTO> getAllTshirts(String keyword,
                                                          int page,
                                                          int size,
                                                          LocalDateTime dateFrom,
                                                          LocalDateTime dateTo,
                                                          String sortDir,
                                                          String sortBy) {
        Sort.Direction sort;
        if(sortDir.equals("asc")){
            sort=Sort.Direction.ASC;
        }else{
        sort= Sort.Direction.DESC;}
        Pageable pageable= PageRequest.of(page-1,size,sort,sortBy);
        Page<Tshirts> tshirts;
        if(dateFrom!=null&&dateTo!=null)
        {
           tshirts= tshirtsRepository.findByCreatedAtBetween(dateFrom,dateTo,pageable);
        }else if(keyword!=null)
        {
            tshirts=tshirtsRepository.findByNameContainingIgnoreCase(keyword,pageable);
        }else{
        tshirts=tshirtsRepository.findAll(pageable);}
        List<TshirtsDTO> tshirtsDTOList= new ArrayList<>();
        for(Tshirts tshirt:tshirts.getContent())
        {
            tshirtsDTOList.add(mapToDTO(tshirt));
        }
        PaginatedResponseDTO<TshirtsDTO>response =new PaginatedResponseDTO<>();
        response.setContent(tshirtsDTOList);
        response.setPageSize(tshirts.getSize());
        response.setPageNumber(tshirts.getNumber());
        response.setTotalPages(tshirts.getTotalPages());
        response.setTotalElements(tshirts.getTotalElements());
        return  response;


    }
    @Override
    public TshirtsDTO getTshirtById(String id) {
        Tshirts tshirt= tshirtsRepository.findById(id).orElse(null);
        return mapToDTO(tshirt);
    }
    @Override
    public boolean updateTshirt(TshirtsDTO tshirt){
        Tshirts updateTshirt= tshirtsRepository.findById(tshirt.getTshirtId()).orElse(null);
        if(updateTshirt==null){
            return false;
        }
        boolean hasActiveBooking = bookingDetailsRepository.existsByTshirtIdAndBooking_StatusNot(
                tshirt.getTshirtId(), BookingEnums.COMPLETED
        );
        if (!hasActiveBooking) {
            return false;
        }
        updateTshirt=mapToEntity(tshirt);
        tshirtsRepository.save(updateTshirt);
        return true;
    }

}
