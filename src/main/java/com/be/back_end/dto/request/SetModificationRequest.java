package com.be.back_end.dto.request;

import com.be.back_end.enums.RequestStatusEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SetModificationRequest {
    String modificationId;
    RequestStatusEnum modificationStatus;
    String denialReason;
}
