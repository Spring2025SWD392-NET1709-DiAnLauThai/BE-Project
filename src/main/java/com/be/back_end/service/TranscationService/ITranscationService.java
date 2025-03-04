
package com.be.back_end.service.TranscationService;

import com.be.back_end.dto.TranscationDTO;

import java.util.List;

public interface ITranscationService {
    TranscationDTO create(TranscationDTO user);
    List<TranscationDTO> getAll();
    TranscationDTO getById(String id);
    List<TranscationDTO> getAllForCustomer(String id) throws Exception;
}

