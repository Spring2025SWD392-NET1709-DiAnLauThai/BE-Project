package com.be.back_end.service.TshirtsService;

import com.be.back_end.dto.TshirtsDTO;

import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.model.Account;
import com.be.back_end.model.Tshirts;
import com.be.back_end.repository.AccountRepository;
import com.be.back_end.repository.TshirtsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TshirtsService implements  ITshirtsService{


    private final TshirtsRepository tshirtsRepository;
    private final AccountRepository accountRepository;
    public TshirtsService(TshirtsRepository tshirtsRepository, AccountRepository accountRepository) {
        this.tshirtsRepository = tshirtsRepository;
        this.accountRepository = accountRepository;
    }
    private TshirtsDTO mapToDTO(Tshirts tshirts) {
        TshirtsDTO dto = new TshirtsDTO();

        dto.setDescription(tshirts.getDescription());
        dto.setName(tshirts.getName());

        dto.setImageUrl(tshirts.getImage_url());

        return dto;
    }

    private Tshirts mapToEntity(TshirtsDTO dto) {
        Tshirts tshirt = new Tshirts();
        if (dto.getAccountId() != null) {
            Account account = accountRepository.findById(dto.getAccountId()).orElse(null);

        }
        tshirt.setName(dto.getName());
        tshirt.setDescription(dto.getDescription());

        tshirt.setImage_url(dto.getImageUrl());
        tshirt.setStatus(ActivationEnums.ACTIVE);

        return tshirt;
    }
    @Override
    public TshirtsDTO createTshirt(TshirtsDTO tshirt) {
        Tshirts newtshirt= mapToEntity(tshirt);
        tshirtsRepository.save(newtshirt);
        return tshirt;
    }
    @Override
    public List<TshirtsDTO> getAllTshirts() {
        List<Tshirts> tshirts= tshirtsRepository.findAll();
        List<TshirtsDTO> list= new ArrayList<>();
        for(Tshirts tshirt:tshirts)
        {
            list.add(mapToDTO(tshirt));
        }
        return list;
    }
    @Override
    public TshirtsDTO getTshirtById(String id) {
        Tshirts tshirt= tshirtsRepository.findById(id).orElse(null);
        return mapToDTO(tshirt);
    }
    @Override
    public boolean updateTshirt(String id,TshirtsDTO tshirt){
        Tshirts updateTshirt= tshirtsRepository.findById(id).orElse(null);
        if(updateTshirt==null){
            return false;
        }
        updateTshirt=mapToEntity(tshirt);
        tshirtsRepository.save(updateTshirt);
        return true;
    }
    @Override
    public boolean deleteTshirt(String id) {
        Tshirts existingTshirt = tshirtsRepository.getById(id);
        if (existingTshirt != null) {
            tshirtsRepository.delete(existingTshirt);
            return true;
        }
        return false;
    }
}
