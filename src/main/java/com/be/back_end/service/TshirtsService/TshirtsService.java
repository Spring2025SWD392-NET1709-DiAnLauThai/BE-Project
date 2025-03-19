package com.be.back_end.service.TshirtsService;

import com.be.back_end.dto.request.TshirtsUpdateRequest;
import com.be.back_end.dto.response.*;

import com.be.back_end.dto.request.TshirtCreateRequest;
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
            tshirts=tshirtsRepository.findByBookingdetails_Booking_IsPublicTrueAndNameContainingIgnoreCaseAndCreatedAtBetween(keyword,dateFrom,dateTo,pageable);
        }
        else if(dateFrom!=null&&dateTo!=null)
        {
            tshirts= tshirtsRepository.findByBookingdetails_Booking_IsPublicTrueAndCreatedAtBetween(dateFrom,dateTo,pageable);
        }

        else if(keyword!=null)
        {
            tshirts=tshirtsRepository.findByBookingdetails_Booking_IsPublicTrueAndNameContainingIgnoreCase(keyword,pageable);
        }
        else{
            tshirts=tshirtsRepository.findByBookingdetails_Booking_IsPublicTrue(pageable);}
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
        double totalRating = 0.0;
        int feedbackCount = 0;
        for (var tfb : tshirt.getTshirtFeedbacks()) {
            totalRating += tfb.getFeedback().getRating();
            feedbackCount++;
        }
        double averageRating = feedbackCount > 0 ? totalRating / feedbackCount : 0.0;
        response.setRating(String.valueOf(averageRating));
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public TshirtDetailResponse getTshirtById(String id) {
        Tshirts tshirt= tshirtsRepository.findById(id).orElse(null);
        if (tshirt == null) {
            throw new IllegalArgumentException("T-shirt not found with ID: " + id);
        }
        TshirtDetailResponse tshirtDetailResponse= new TshirtDetailResponse();
        tshirtDetailResponse.setDescription(tshirt.getDescription());
        tshirtDetailResponse.setTshirtName(tshirt.getName());
        tshirtDetailResponse.setCreatedAt(tshirt.getCreatedAt());
        tshirtDetailResponse.setImage_url(tshirt.getImage_url());
        tshirtDetailResponse.setImageFile(tshirt.getImagesfile());
        List<TshirtDetailResponse.ColorResponse> colorResponses= new ArrayList<>();
        for(TShirtColor tShirtColor: tshirt.getTShirtColors()){
            TshirtDetailResponse.ColorResponse colorRes= new TshirtDetailResponse.ColorResponse();
            colorRes.setColorId(tShirtColor.getColor().getId());
            colorRes.setColorCode(tShirtColor.getColor().getColorCode());
            colorRes.setColorName(tShirtColor.getColor().getColorName());
            colorResponses.add(colorRes);
        }
        tshirtDetailResponse.setColors(colorResponses);
        return tshirtDetailResponse;
    }
    @Transactional
    @Override
    public boolean updateTshirt(TshirtsUpdateRequest tshirt){
        Tshirts updateTshirt= tshirtsRepository.findById(tshirt.getTshirtId()).orElse(null);
        if(updateTshirt==null){
            return false;
        }
        boolean hasCompleteBooking = bookingDetailsRepository.existsByTshirtIdAndBooking_StatusNot(
                tshirt.getTshirtId(), BookingEnums.COMPLETED
        );
        Bookingdetails bookingDetails = bookingDetailsRepository.findByTshirtId(tshirt.getTshirtId());
        if (bookingDetails != null) {
            Bookings booking = bookingDetails.getBooking();
            bookingRepository.save(booking);
        }
        if (hasCompleteBooking) {
            return false;
        }
        tshirtColorRepository.deleteByTshirtId(tshirt.getTshirtId());
        updateTshirt.setName(tshirt.getName());
        updateTshirt.setDescription(tshirt.getDescription());
        updateTshirt.setImage_url(tshirt.getImageUrl());
        updateTshirt.setImagesfile(tshirt.getImageFile());
        List<TShirtColor> newColors = new ArrayList<>();
        if (tshirt.getColors() != null) {
            for (TshirtsUpdateRequest.TshirtColorUpdateRequest colorRequest : tshirt.getColors()) {
                TShirtColor newColor = new TShirtColor();
                newColor.setTshirt(updateTshirt);
                newColor.setColor(
                        colorRepository.findById(colorRequest.getColorId())
                                .orElse(null)
                );
                newColors.add(newColor);
            }
        }
        tshirtColorRepository.saveAll(newColors);
        tshirtsRepository.save(updateTshirt);
        return true;
    }

}
