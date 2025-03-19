package com.be.back_end.service.TshirtsService;

import com.be.back_end.dto.response.*;
import com.be.back_end.dto.request.TshirtCreateRequest;
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
    TshirtDetailResponse getTshirtById(String id);
    boolean  updateTshirt(TshirtsListDesignerResponse tshirt);
    List<TshirtsListAvailableResponse> getAllTshirtsAvailable();
    PaginatedResponseDTO<TshirtsListsResponse> getAllTshirtCatalog(String keyword,
                                                                   int page,
                                                                   int size,
                                                                   LocalDateTime dateFrom,
                                                                   LocalDateTime dateTo,
                                                                   String sortDir,
                                                                   String sortBy);

}
