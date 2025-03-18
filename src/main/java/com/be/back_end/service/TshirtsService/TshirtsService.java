package com.be.back_end.service.TshirtsService;

import com.be.back_end.dto.response.TshirtsListAvailableResponse;
import com.be.back_end.dto.response.TshirtsListDesignerResponse;

import com.be.back_end.dto.request.TshirtCreateRequest;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.dto.response.TshirtsListsResponse;
import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.BookingEnums;
import com.be.back_end.model.*;
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
    private final BookingRepository bookingRepository;
    private final TshirtsRepository tshirtsRepository;
    private final AccountRepository accountRepository;
    private final ColorRepository  colorRepository;
    private final TshirtColorRepository tshirtColorRepository;
    private final BookingDetailsRepository bookingDetailsRepository;
    public TshirtsService(AccountUtils accountUtils, BookingRepository bookingRepository, TshirtsRepository tshirtsRepository, AccountRepository accountRepository, ColorRepository colorRepository, TshirtColorRepository tshirtColorRepository, BookingDetailsRepository bookingDetailsRepository) {
        this.accountUtils = accountUtils;
        this.bookingRepository = bookingRepository;
        this.tshirtsRepository = tshirtsRepository;
        this.accountRepository = accountRepository;
        this.colorRepository = colorRepository;
        this.tshirtColorRepository = tshirtColorRepository;
        this.bookingDetailsRepository = bookingDetailsRepository;
    }
    private TshirtsListDesignerResponse mapToDTO(Tshirts tshirts) {
        TshirtsListDesignerResponse dto = new TshirtsListDesignerResponse();
        dto.setTshirtId(tshirts.getId());
        dto.setDescription(tshirts.getDescription());
        dto.setName(tshirts.getName());
        dto.setCreatedAt(tshirts.getCreatedAt());
        dto.setImageUrl(tshirts.getImage_url());
        dto.setImageFile(tshirts.getImagesfile());
        return dto;
    }

    private Tshirts mapToEntity(TshirtsListDesignerResponse dto) {
        Tshirts tshirt = new Tshirts();

        tshirt.setName(dto.getName());
        tshirt.setDescription(dto.getDescription());
        tshirt.setCreatedAt(LocalDateTime.now());
        tshirt.setImage_url(dto.getImageUrl());


        return tshirt;
    }
   @Override
   public Tshirts saveTshirt(TshirtCreateRequest tshirtCreateRequest) {
        Account account= accountUtils.getCurrentAccount();
       Tshirts tshirt = new Tshirts();
       tshirt.setDescription(tshirtCreateRequest.getDescription());
       tshirt.setAccount(account);

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
    public List<TshirtsListAvailableResponse> getAllTshirtsAvailable() {
        String id=accountUtils.getCurrentAccount().getId();
        List<Tshirts>tshirtsList=tshirtsRepository.findByBookingdetailsIsNullAndAccount_Id(id);
        List<TshirtsListAvailableResponse> listAvailableResponses= new ArrayList<>();
        for(Tshirts tshirt:tshirtsList)
        {
            TshirtsListAvailableResponse response= new TshirtsListAvailableResponse();
            response.setTshirtId(tshirt.getId());
            response.setName(tshirt.getName());
            response.setImageUrl(tshirt.getImage_url());
            response.setDescription(tshirt.getDescription());
            listAvailableResponses.add(response);
        }
        return  listAvailableResponses;


    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDTO<TshirtsListDesignerResponse> getAllTshirtsDesigner(String keyword,
                                                                           int page,
                                                                           int size,
                                                                           LocalDateTime dateFrom,
                                                                           LocalDateTime dateTo,
                                                                           String sortDir,
                                                                           String sortBy) {
        String id=accountUtils.getCurrentAccount().getId();
        Sort.Direction sort;
        if(sortDir.equals("asc")){
            sort=Sort.Direction.ASC;
        }else{
        sort= Sort.Direction.DESC;}
        Pageable pageable= PageRequest.of(page-1,size,sort,sortBy);
        Page<Tshirts> tshirts;
        if(dateFrom!=null&&dateTo!=null)
        {
           tshirts= tshirtsRepository.findByCreatedAtBetweenAndAccount_Id(dateFrom,dateTo,id,pageable);
        }else if(keyword!=null)
        {
            tshirts=tshirtsRepository.findByNameContainingIgnoreCaseAndAccount_Id(keyword,id,pageable);
        }else{
        tshirts=tshirtsRepository.findByAccount_Id(id,pageable);}
        List<TshirtsListDesignerResponse> tshirtsListDesignerResponseList = new ArrayList<>();
        for(Tshirts tshirt:tshirts.getContent())
        {
            tshirtsListDesignerResponseList.add(mapToDTO(tshirt));
        }
        PaginatedResponseDTO<TshirtsListDesignerResponse>response =new PaginatedResponseDTO<>();
        response.setContent(tshirtsListDesignerResponseList);
        response.setPageSize(tshirts.getSize());
        response.setPageNumber(tshirts.getNumber());
        response.setTotalPages(tshirts.getTotalPages());
        response.setTotalElements(tshirts.getTotalElements());
        return  response;
    }
    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDTO<TshirtsListsResponse> getAllTshirtCatalog(String keyword,
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
        if(keyword!=null&&dateFrom!=null&&dateTo!=null)
        {
            tshirts=tshirtsRepository.findByBookingdetails_Booking_IspublicTrueAndNameContainingIgnoreCaseAndCreatedAtBetween(keyword,dateFrom,dateTo,pageable);
        }
        else if(dateFrom!=null&&dateTo!=null)
        {
            tshirts= tshirtsRepository.findByBookingdetails_Booking_IspublicTrueAndCreatedAtBetween(dateFrom,dateTo,pageable);
        }

        else if(keyword!=null)
        {
            tshirts=tshirtsRepository.findByBookingdetails_Booking_IspublicTrueAndNameContainingIgnoreCase(keyword,pageable);
        }
        else{
            tshirts=tshirtsRepository.findByBookingdetails_Booking_IspublicTrue(pageable);}
        List<TshirtsListsResponse> tshirtsListsResponses = new ArrayList<>();
        for(Tshirts tshirt:tshirts.getContent())
        {
            tshirtsListsResponses.add(mapToDTOCatalog(tshirt));
        }
        PaginatedResponseDTO<TshirtsListsResponse>response =new PaginatedResponseDTO<>();
        response.setContent(tshirtsListsResponses);
        response.setPageSize(tshirts.getSize());
        response.setPageNumber(tshirts.getNumber());
        response.setTotalPages(tshirts.getTotalPages());
        response.setTotalElements(tshirts.getTotalElements());
        return  response;
    }
    private TshirtsListsResponse mapToDTOCatalog(Tshirts tshirt) {
        TshirtsListsResponse response = new TshirtsListsResponse();
        response.setId(tshirt.getId());
        response.setName(tshirt.getName());
        response.setAccountName(tshirt.getAccount().getName());
        response.setImageUrl(tshirt.getImage_url());
        double rating = tshirt.getTshirtFeedbacks().stream()
                .mapToInt(tfb -> tfb.getFeedback().getRating())
                .average()
                .orElse(0.0);
        response.setRating(String.valueOf(rating));
        return response;
    }

    @Override
    public TshirtsListDesignerResponse getTshirtById(String id) {
        Tshirts tshirt= tshirtsRepository.findById(id).orElse(null);
        return mapToDTO(tshirt);
    }
    @Override
    public boolean updateTshirt(TshirtsListDesignerResponse tshirt){
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
        Bookingdetails bookingDetails = bookingDetailsRepository.findByTshirtId(tshirt.getTshirtId());
        if (bookingDetails != null) {
            Bookings booking = bookingDetails.getBooking();
            booking.setUpdateddate(LocalDateTime.now());
            bookingRepository.save(booking);
        }
        updateTshirt=mapToEntity(tshirt);
        tshirtsRepository.save(updateTshirt);
        return true;
    }

}
