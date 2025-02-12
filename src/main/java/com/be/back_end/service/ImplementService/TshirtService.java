package com.be.back_end.service.ImplementService;


import com.be.back_end.repository.TshirtRepository;
import com.be.back_end.service.InterfaceService.ITshirtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TshirtService implements ITshirtService {

    private final TshirtRepository tshirtRepository;

    @Autowired
    public TshirtService(TshirtRepository tshirtRepository) {
        this.tshirtRepository = tshirtRepository;
    }

    /*@Override
    public List<Tshirts> getAll() {
        return List.of();
    }

    @Override
    public Tshirts getById(int id) {
        return null;
    }

    @Override
    public List<Tshirts> getByName(String name) {
        return List.of();
    }

    @Override
    public Tshirts save(Tshirts tshirts) {
        return null;
    }

    @Override
    public void removeById(int id) {

    }
    */

}
