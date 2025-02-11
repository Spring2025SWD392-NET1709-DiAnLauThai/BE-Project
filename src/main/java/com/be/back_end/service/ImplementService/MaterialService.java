package com.be.back_end.service.ImplementService;

import com.be.back_end.model.Materials;
import com.be.back_end.repository.MaterialRepository;
import com.be.back_end.service.InterfaceService.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MaterialService implements IMaterialService {

    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Override
    public List<Materials> getAll() {
        return List.of();
    }

    @Override
    public Materials getById(int id) {
        return null;
    }

    @Override
    public List<Materials> getByName(String name) {
        return List.of();
    }

    @Override
    public Materials save(Materials material) {
        return null;
    }

    @Override
    public void removeById(int id) {

    }
}
