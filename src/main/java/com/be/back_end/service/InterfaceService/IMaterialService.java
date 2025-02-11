package com.be.back_end.service.InterfaceService;

import com.be.back_end.model.Designs;
import com.be.back_end.model.Materials;

import java.util.List;

public interface IMaterialService {
    List<Materials> getAll();
    Materials getById(int id);
    List<Materials> getByName(String name);
    Materials save(Materials material);
    void removeById(int id);
}
