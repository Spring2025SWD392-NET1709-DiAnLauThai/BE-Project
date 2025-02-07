package com.be.back_end.service.InterfaceService;

import com.be.back_end.model.Tshirts;

import java.util.List;

public interface ITshirtService {
    List<Tshirts> getAll();
    Tshirts getById(int id);
    List<Tshirts> getByName(String name);
    Tshirts save(Tshirts tshirts);
    void removeById(int id);
}
