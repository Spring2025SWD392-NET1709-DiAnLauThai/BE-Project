package com.be.back_end.service.InterfaceService;

import com.be.back_end.model.Designs;
import com.be.back_end.model.Tshirts;

import java.util.List;

public interface IDesignService {
    List<Designs> getAll();
    Designs getById(int id);
    List<Designs> getByName(String name);
    Designs save(Designs tshirts);
    void removeById(int id);
}
