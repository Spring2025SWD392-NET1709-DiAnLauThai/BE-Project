package com.be.back_end.service.DesignService;


import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.DesignDTO;
import com.be.back_end.model.Designs;

import java.util.List;

public interface IDesignService {
    List<DesignDTO> getAllDesigns();
    DesignDTO getDesignById(String id);
    DesignDTO createDesign(DesignDTO design);
    boolean updateDesign(String id, DesignDTO design);
    boolean deleteDesign(String id);
}
