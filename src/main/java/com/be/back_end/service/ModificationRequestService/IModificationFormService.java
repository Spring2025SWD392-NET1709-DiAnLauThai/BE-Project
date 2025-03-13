package com.be.back_end.service.ModificationRequestService;

import com.be.back_end.dto.request.ModificationRequest;
import com.be.back_end.dto.request.SetModificationRequest;

public interface IModificationFormService {
    boolean AddModificationForm(ModificationRequest modificationRequest);
    boolean setModification(SetModificationRequest modificationRequest);
}
