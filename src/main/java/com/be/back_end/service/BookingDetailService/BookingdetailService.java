package com.be.back_end.service.BookingDetailService;

import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.response.BookingCreateResponse;
import com.be.back_end.dto.response.BookingResponseNoLinkDTO;
import com.be.back_end.model.*;
import com.be.back_end.repository.BookingDetailsRepository;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.DesignRepository;
import com.be.back_end.repository.TshirtDesignRepository;
import com.be.back_end.service.CloudinaryService.ICloudinaryService;
import com.be.back_end.service.DesignService.IDesignService;
import com.be.back_end.utils.AccountUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class BookingdetailService implements IBookingdetailService {
    private final BookingDetailsRepository bookingDetailsRepository;
    private final DesignRepository designRepository;
    private final BookingRepository bookingRepository;
    private final ICloudinaryService cloudinaryService;
    private final AccountUtils accountUtils;
    private final TshirtDesignRepository tshirtDesignRepository;
    private final IDesignService designService;
    public BookingdetailService(BookingDetailsRepository bookingDetailsRepository, DesignRepository designRepository, BookingRepository bookingRepository, ICloudinaryService cloudinaryService, AccountUtils accountUtils, TshirtDesignRepository tshirtDesignRepository, IDesignService designService) {
        this.bookingDetailsRepository = bookingDetailsRepository;
        this.designRepository = designRepository;
        this.bookingRepository = bookingRepository;
        this.cloudinaryService = cloudinaryService;
        this.accountUtils = accountUtils;
        this.tshirtDesignRepository = tshirtDesignRepository;
        this.designService = designService;
    }


    @Override
    public List<BookingCreateResponse.BookingDetailResponse> processBookingDetails(BookingCreateRequest request, Bookings booking) {
        List<BookingCreateResponse.BookingDetailResponse> bookingDetailResponses = new ArrayList<>();

        // Initialize bookingDetails if it's null
        if (booking.getBookingDetails() == null) {
            booking.setBookingDetails(new HashSet<>());
        }

        for (BookingCreateRequest.BookingDetailCreateRequest detailRequest : request.getBookingdetails()) {
            // Create and save a new design
            Designs design = designService.createAndSaveDesign(detailRequest);

            // Create booking detail
            Bookingdetails detail = createBookingDetail(detailRequest, booking, design);
            bookingDetailsRepository.save(detail);

            // Now it's safe to add
            booking.getBookingDetails().add(detail);

            bookingDetailResponses.add(new BookingCreateResponse.BookingDetailResponse(
                    detail.getId(),
                    design.getId(),
                    design.getDesignFile(),
                    detail.getDescription(),
                    detail.getUnit_price()
            ));
        }

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
       List<Bookingdetails> bookingdetails= bookingDetailsRepository.findByBookingId(bookingId);
       List<BookingResponseNoLinkDTO.BookingDetailResponse> detailResponses= new ArrayList<>();
       BookingResponseNoLinkDTO bookingResponseNoLinkDTO= new BookingResponseNoLinkDTO();
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
           detailResponses.add(detailResponse);
       }
        bookingResponseNoLinkDTO.setBookingDetails(detailResponses);
       return bookingResponseNoLinkDTO;
    }


/*    @Override
    public Bookingdetails updatebookingdetail(String id, BookingdetailsDTO dto) {
        Bookingdetails bookingdetail = bookingDetailsRepository.findById(id).orElse(null);
        if (bookingdetail != null) {
            bookingdetail.setQuantity(dto.getQuantity());
            bookingdetail.setUnit_price(dto.getUnit_price());
            return bookingDetailsRepository.save(bookingdetail);
        }
        return null;
    }*/

    @Override
    public boolean deletebookingdetail(String id) {
        if (bookingDetailsRepository.existsById(id)) {
            bookingDetailsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
