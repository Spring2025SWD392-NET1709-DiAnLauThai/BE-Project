package com.be.back_end.service.ImplementService;

import com.be.back_end.model.Designs;
import com.be.back_end.repository.DesignRepository;
import com.be.back_end.service.InterfaceService.IDesignService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DesignService implements IDesignService {

    private final DesignRepository designsRepository;

    @Autowired
    public DesignService(DesignRepository designsRepository) {
        this.designsRepository = designsRepository;
    }
    
    @Override
    public List<Designs> getAll() {
        return List.of();
    }

    @Override
    public Designs getById(int id) {
        return null;
    }

    @Override
    public List<Designs> getByName(String name) {
        return List.of();
    }

    @Override
    public Designs save(Designs tshirts) {
        return null;
    }

    @Override
    public void removeById(int id) {

    }
}
