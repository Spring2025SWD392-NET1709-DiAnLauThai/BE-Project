package com.be.back_end.service.ModificationRequestService;

import com.be.back_end.dto.request.ModificationRequest;
import com.be.back_end.dto.request.SetModificationRequest;
import com.be.back_end.enums.RequestStatusEnum;
import com.be.back_end.model.Bookingdetails;
import com.be.back_end.model.Bookings;
import com.be.back_end.model.ModificationForm;
import com.be.back_end.model.Task;
import com.be.back_end.repository.BookingDetailsRepository;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.ModificationFormRepository;
import com.be.back_end.repository.TaskRepository;
import com.be.back_end.service.EmailService.IEmailService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ModificationFormService implements IModificationFormService {
    private final ModificationFormRepository modificationFormRepository;
    private final BookingDetailsRepository bookingDetailsRepository;
    private final IEmailService emailService;
    private final BookingRepository bookingRepository;
    private final TaskRepository taskRepository;
    public ModificationFormService(ModificationFormRepository modificationFormRepository, BookingDetailsRepository bookingDetailsRepository, IEmailService emailService, BookingRepository bookingRepository, TaskRepository taskRepository) {
        this.modificationFormRepository = modificationFormRepository;
        this.bookingDetailsRepository = bookingDetailsRepository;
        this.emailService = emailService;
        this.bookingRepository = bookingRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public boolean AddModificationForm(ModificationRequest modificationRequest) {
        ModificationForm modificationForm = new ModificationForm();
        Bookingdetails bookingdetails=  bookingDetailsRepository.findById(modificationRequest.getBookingdetailId()).orElse(null);
        if(bookingdetails==null){
            return false;
        }else {
            modificationForm.setReviewedat(null);
            modificationForm.setRequeststatus(RequestStatusEnum.PENDING);
            modificationForm.setDenialReason(null);
            modificationForm.setModificationnote(modificationForm.getModificationnote());
            modificationForm.setBookingdetail(bookingdetails);
            modificationFormRepository.save(modificationForm);
            try{
                Bookings booking= bookingRepository.findById(bookingdetails.getBooking().getId()).orElse(null);
                Task task= taskRepository.findByBookingId(booking.getId()).orElse(null);

                String recipientEmail= task.getAccount().getEmail();
                String designerName= task.getAccount().getName();
                emailService.sendDesignerEmail(recipientEmail,designerName);
            }catch(MessagingException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }
    }

    @Override
    public boolean setModification(SetModificationRequest modificationRequest) {
        ModificationForm modificationForm = modificationFormRepository.findById(
                modificationRequest.getModificationId()).orElse(null);
        if (modificationForm == null ||
                modificationForm.getBookingdetail() == null ||
                modificationForm.getBookingdetail().getBooking() == null) {
            return false;
        }
        Bookingdetails bookingDetails = modificationForm.getBookingdetail();
        Bookings booking = bookingDetails.getBooking();
        if (modificationRequest.getModificationStatus().equals(RequestStatusEnum.APPROVED)) {
            bookingDetails.setDescription(modificationForm.getModificationnote());
            booking.setUpdateddate(LocalDateTime.now());
        } else if (modificationRequest.getModificationStatus().equals(RequestStatusEnum.REJECTED)) {
            modificationForm.setDenialReason(modificationRequest.getDenialReason());
        }
        modificationForm.setRequeststatus(modificationRequest.getModificationStatus());
        modificationForm.setReviewedat(LocalDateTime.now());
        modificationFormRepository.save(modificationForm);
        bookingDetailsRepository.save(bookingDetails);
        bookingRepository.save(booking);

        return true;
    }

}
