package com.be.back_end.service.TshirtsService;

import com.be.back_end.dto.response.TshirtsListAvailableResponse;
import com.be.back_end.dto.response.TshirtsListDesignerResponse;
import com.be.back_end.dto.request.TshirtCreateRequest;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.model.Tshirts;

import java.time.LocalDateTime;
import java.util.List;

public interface ITshirtsService {
    Tshirts saveTshirt(TshirtCreateRequest tshirtCreateRequest);
    PaginatedResponseDTO<TshirtsListDesignerResponse> getAllTshirtsDesigner(String keyword,
                                                                    int page,
                                                                    int size,
                                                                    LocalDateTime dateFrom,
                                                                    LocalDateTime dateTo,
                                                                    String sortDir,
                                                                    String sortBy);
    TshirtsListDesignerResponse getTshirtById(String id);
    boolean  updateTshirt(TshirtsListDesignerResponse tshirt);
    List<TshirtsListAvailableResponse> getAllTshirtsAvailable();
}
