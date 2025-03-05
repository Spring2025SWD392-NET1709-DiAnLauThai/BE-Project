package com.be.back_end.service.TshirtDesignService;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.TshirtDesignDTO;
import com.be.back_end.model.Designs;
import com.be.back_end.model.Tshirts;

import java.util.List;

public interface ITshirtDesignService {
    TshirtDesignDTO createTshirtDesign(TshirtDesignDTO dto);
    List<TshirtDesignDTO> getAllTshirtDesigns();
    TshirtDesignDTO getTshirtDesignById(String id);
    boolean updateTshirtDesign(String id,TshirtDesignDTO dto);
    boolean deleteTshirtDesign(String id);

}
