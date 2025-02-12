package com.be.back_end.service.TshirtsService;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.TshirtsDTO;

import java.util.List;
import java.util.UUID;

public interface ITshirtsService {
    TshirtsDTO createTshirt(TshirtsDTO tshirt);
    List<TshirtsDTO> getAllTshirts();
    TshirtsDTO getTshirtById(String id);
    boolean updateTshirt(String id,TshirtsDTO tshirt);
    boolean deleteTshirt(String id);
}
