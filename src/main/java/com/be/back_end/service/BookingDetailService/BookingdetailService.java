package com.be.back_end.service.BookingDetailService;

import com.be.back_end.dto.BookingdetailsDTO;

import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.response.BookingCreateResponse;
import com.be.back_end.dto.response.BookingDetailResponseDTO;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.model.*;
import com.be.back_end.repository.BookingDetailsRepository;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.DesignRepository;
import com.be.back_end.repository.TshirtDesignRepository;
import com.be.back_end.service.CloudinaryService.ICloudinaryService;
import com.be.back_end.service.DesignService.IDesignService;
import com.be.back_end.utils.AccountUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public PaginatedResponseDTO<BookingDetailResponseDTO> getAllBookingDetailsByBookingId(String bookingId, int page, int size) {
        Page<Bookingdetails> bookingDetailsPage = bookingDetailsRepository.findByBookingId(bookingId, PageRequest.of(page - 1, size));
        List<BookingDetailResponseDTO> bookingDetailDTOs = new ArrayList<>();
        for (Bookingdetails bookingDetail : bookingDetailsPage.getContent()) {
            Designs design = bookingDetail.getDesign();
            BookingDetailResponseDTO dto = new BookingDetailResponseDTO(
                    bookingDetail.getId(),
                    design.getDesignFile(),
                    bookingDetail.getDescription(),
                    bookingDetail.getUnit_price()
            );
            bookingDetailDTOs.add(dto);
        }
        PaginatedResponseDTO<BookingDetailResponseDTO> paginatedResponseDTO = new PaginatedResponseDTO<>();
        paginatedResponseDTO.setContent(bookingDetailDTOs);
        paginatedResponseDTO.setPageNumber(bookingDetailsPage.getNumber() + 1);
        paginatedResponseDTO.setPageSize(bookingDetailsPage.getSize());
        paginatedResponseDTO.setTotalElements(bookingDetailsPage.getTotalElements());
        paginatedResponseDTO.setTotalPages(bookingDetailsPage.getTotalPages());
        return paginatedResponseDTO;
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
