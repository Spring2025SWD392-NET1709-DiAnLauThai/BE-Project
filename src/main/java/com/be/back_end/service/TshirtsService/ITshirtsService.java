package com.be.back_end.service.TshirtsService;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.TshirtsDTO;
import com.be.back_end.dto.request.CreateDesignRequest;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.model.Tshirts;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ITshirtsService {
    Tshirts saveTshirt(CreateDesignRequest createDesignRequest);
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
