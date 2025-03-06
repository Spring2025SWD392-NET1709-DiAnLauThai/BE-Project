package com.be.back_end.service.DesignService;


import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.model.Designs;

public interface IDesignService {
/*
    List<DesignDTO> getAll();
    DesignDTO getById(String id);
    List<DesignDTO> getByName(String name);
    DesignDTO create(DesignDTO design);
    boolean update(String id, DesignDTO design);
    boolean removeById(String id);
    CreateDesignResponse createDesign(CreateDesignRequest createDesignRequest);
*/
Designs createAndSaveDesign(BookingCreateRequest.BookingDetailCreateRequest detailRequest);
}
