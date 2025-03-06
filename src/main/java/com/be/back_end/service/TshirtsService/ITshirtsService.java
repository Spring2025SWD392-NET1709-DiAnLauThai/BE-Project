package com.be.back_end.service.TshirtsService;

import com.be.back_end.dto.TshirtsDTO;
import com.be.back_end.dto.request.TshirtCreateRequest;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.model.Tshirts;

import java.time.LocalDateTime;

public interface ITshirtsService {
    Tshirts saveTshirt(TshirtCreateRequest tshirtCreateRequest);
    PaginatedResponseDTO<TshirtsDTO> getAllTshirts(String keyword,
                                                   int page,
                                                   int size,
                                                   LocalDateTime dateFrom,
                                                   LocalDateTime dateTo,
                                                   String sortDir,
                                                   String sortBy);
    TshirtsDTO getTshirtById(String id);
    boolean updateTshirt(String id,TshirtsDTO tshirt);

}
