package com.be.back_end.service.BookingDetailService;

import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.request.UpdateBookingDetailsRequest;
import com.be.back_end.dto.response.BookingCreateResponse;
import com.be.back_end.dto.response.BookingResponseNoLinkDTO;
import com.be.back_end.model.*;
import com.be.back_end.repository.*;
import com.be.back_end.service.CloudinaryService.ICloudinaryService;
import com.be.back_end.service.DesignService.IDesignService;
import com.be.back_end.service.EmailService.IEmailService;
import com.be.back_end.utils.AccountUtils;
import jakarta.mail.MessagingException;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingdetailService implements IBookingdetailService {
    private final BookingDetailsRepository bookingDetailsRepository;
    private final DesignRepository designRepository;
    private final BookingRepository bookingRepository;
    private final ICloudinaryService cloudinaryService;
    private final AccountUtils accountUtils;
    private final TshirtDesignRepository tshirtDesignRepository;
    private final IDesignService designService;
    private final IEmailService emailService;
    private final TaskRepository taskRepository;
    public BookingdetailService(BookingDetailsRepository bookingDetailsRepository, DesignRepository designRepository, BookingRepository bookingRepository, ICloudinaryService cloudinaryService, AccountUtils accountUtils, TshirtDesignRepository tshirtDesignRepository, IDesignService designService, IEmailService emailService, TaskRepository taskRepository) {
        this.bookingDetailsRepository = bookingDetailsRepository;
        this.designRepository = designRepository;
        this.bookingRepository = bookingRepository;
        this.cloudinaryService = cloudinaryService;
        this.accountUtils = accountUtils;
        this.tshirtDesignRepository = tshirtDesignRepository;
        this.designService = designService;
        this.emailService = emailService;
        this.taskRepository = taskRepository;
    }



    @Override
    @Transactional
    public List<BookingCreateResponse.BookingDetailResponse> processBookingDetails(
            BookingCreateRequest request, Bookings booking) {
        List<BookingCreateResponse.BookingDetailResponse> bookingDetailResponses = new ArrayList<>();
        if (booking.getBookingDetails() == null) {
            booking.setBookingDetails(new HashSet<>());
        }
        List<Bookingdetails> bookingDetails = new ArrayList<>();
        List<Designs> designs = new ArrayList<>();
        for (BookingCreateRequest.BookingDetailCreateRequest detailRequest : request.getBookingdetails()) {
            Designs design = designService.createAndSaveDesign(detailRequest);
            Bookingdetails detail = createBookingDetail(detailRequest, booking, design);
            bookingDetails.add(detail);   // Collect booking details for batch insert
            designs.add(design);         // Collect designs for reference
            booking.getBookingDetails().add(detail);
            bookingDetailResponses.add(new BookingCreateResponse.BookingDetailResponse(
                    detail.getId(),
                    design.getId(),
                    design.getDesignFile(),
                    detail.getDescription(),
                    detail.getUnit_price()
            ));
        }
        bookingDetailsRepository.saveAll(bookingDetails);

        return bookingDetailResponses;
    }






    @Override
    public Bookingdetails createBookingDetail(BookingCreateRequest.BookingDetailCreateRequest detailRequest, Bookings booking, Designs design) {
        Bookingdetails detail = new Bookingdetails();
        detail.setBooking(booking);
        detail.setUnit_price(detailRequest.getUnitprice());
        detail.setDesign(design);
        detail.setDescription(detailRequest.getDescription());
        return detail;
    }





    @Transactional(readOnly = true)
    @Override
    public BookingResponseNoLinkDTO getAllBookingDetailsByBookingId(String bookingId) {
       Bookings booking= bookingRepository.findById(bookingId).orElse(null);
        Task task = taskRepository.findByBookingId(booking.getId()).orElse(null);
       List<Bookingdetails> bookingdetails= bookingDetailsRepository.findByBookingId(bookingId);
       List<BookingResponseNoLinkDTO.BookingDetailResponse> detailResponses= new ArrayList<>();
       BookingResponseNoLinkDTO bookingResponseNoLinkDTO= new BookingResponseNoLinkDTO();
        if (task != null && task.getAccount() != null) {
            bookingResponseNoLinkDTO.setDesignerName(task.getAccount().getName());
        } else {
            bookingResponseNoLinkDTO.setDesignerName(null);
        }
       bookingResponseNoLinkDTO.setDepositAmount(booking.getDepositAmount());
       bookingResponseNoLinkDTO.setCode(booking.getCode());
       bookingResponseNoLinkDTO.setTitle(booking.getTitle());
       bookingResponseNoLinkDTO.setBookingStatus(booking.getStatus());
       bookingResponseNoLinkDTO.setEnddate(booking.getEnddate());
       bookingResponseNoLinkDTO.setTotalQuantity(booking.getTotal_quantity());
       bookingResponseNoLinkDTO.setTotalPrice(booking.getTotal_price());
       bookingResponseNoLinkDTO.setStartdate(booking.getStartdate());
       bookingResponseNoLinkDTO.setUpdateddate(booking.getUpdateddate());
       bookingResponseNoLinkDTO.setDatecreated(booking.getDatecreated());
       for(Bookingdetails detail: bookingdetails) {
           BookingResponseNoLinkDTO.BookingDetailResponse detailResponse = new BookingResponseNoLinkDTO.BookingDetailResponse();
           detailResponse.setBookingDetailId(detail.getId());
           detailResponse.setDesignFile(detail.getDesign().getDesignFile());
           detailResponse.setUnitPrice(detail.getUnit_price());
           detailResponse.setDescription(detail.getDescription());
           Tshirts tshirt = detail.getTshirt();
           if (tshirt != null){
               detailResponse.setImageFile(detail.getTshirt().getImagesfile());
           detailResponse.setImageUrl(detail.getTshirt().getImage_url());
           detailResponse.setTshirtDescription(detail.getTshirt().getDescription());
           detailResponse.setTshirtName(detail.getTshirt().getName());
               List<BookingResponseNoLinkDTO.ColorResponse> colors = tshirt.getTShirtColors().stream()
                       .map(tShirtColor -> {
                           Color color = tShirtColor.getColor();
                           return new BookingResponseNoLinkDTO.ColorResponse(color.getId(), color.getColorName(), color.getColorCode());
                       })
                       .collect(Collectors.toList());
               detailResponse.setColors(colors);
            }
           detailResponses.add(detailResponse);

       }
        bookingResponseNoLinkDTO.setBookingDetails(detailResponses);
       return bookingResponseNoLinkDTO;
    }


    @Override
    public boolean updatebookingdetail(UpdateBookingDetailsRequest dto) {
        Bookingdetails bookingdetail = bookingDetailsRepository.findById(dto.getId()).orElse(null);
        if (bookingdetail == null) {
            return false;
        }
        String oldDescription = bookingdetail.getDescription();
        String newDescription = dto.getDescription();
        Task task = taskRepository.findByBookingId(bookingdetail.getBooking().getId()).orElse(null);
        if (task != null && task.getAccount() != null) {
            String designerEmail = task.getAccount().getEmail();
            String designerName = task.getAccount().getName();
            String bookingCode = bookingdetail.getBooking().getCode();
            try {
                emailService.sendDesignerEmail(
                        designerEmail, designerName, bookingCode, oldDescription, newDescription
                );
            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }
        }
        bookingdetail.setDescription(newDescription);
        bookingDetailsRepository.save(bookingdetail);
        return true;
    }



}
