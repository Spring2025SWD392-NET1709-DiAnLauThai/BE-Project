package com.be.back_end.service.TshirtDesignService;

import com.be.back_end.dto.TshirtDesignDTO;
import com.be.back_end.dto.TshirtDesignDTO;

import com.be.back_end.enums.RoleEnums;
import com.be.back_end.model.TshirtDesign;
import com.be.back_end.repository.DesignRepository;
import com.be.back_end.repository.TshirtDesignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TshirtDesignService implements ITshirtDesignService{

    private final TshirtDesignRepository tshirtDesignRepository;

    @Autowired
    public TshirtDesignService(TshirtDesignRepository tshirtDesignRepository) {
        this.tshirtDesignRepository = tshirtDesignRepository;
    }


    @Override
    public TshirtDesignDTO createTshirtDesign(TshirtDesignDTO dto) {
        TshirtDesign newTshirtDesign= mapToEntity(dto);
        tshirtDesignRepository.save(newTshirtDesign);
        return dto;
    }

    @Override
    public List<TshirtDesignDTO> getAllTshirtDesigns() {
        List<TshirtDesign> TshirtDesigns= tshirtDesignRepository.findAll();
        List<TshirtDesignDTO> list= new ArrayList<>();
        for(TshirtDesign acc:TshirtDesigns)
        {
            list.add(mapToDTO(acc));
            System.out.println(acc.getId());
        }
        return list;
    }

    @Override
    public TshirtDesignDTO getTshirtDesignById(String id) {
        TshirtDesign TshirtDesign= tshirtDesignRepository.findById(id).orElse(null);
        return mapToDTO(TshirtDesign);
    }

    @Override
    public boolean updateTshirtDesign(String id, TshirtDesignDTO dto) {
        TshirtDesign updatedTshirtDesign= tshirtDesignRepository.findById(id).orElse(null);
        if(updatedTshirtDesign==null){
            return false;
        }
        updatedTshirtDesign=mapToEntity(dto);
        tshirtDesignRepository.save(updatedTshirtDesign);
        return true;
    }

    @Override
    public boolean deleteTshirtDesign(String id) {
        TshirtDesign existingTshirtDesign = tshirtDesignRepository.getById(id);
        if (existingTshirtDesign != null) {
            tshirtDesignRepository.delete(existingTshirtDesign);
            return true;
        }
        return false;
    }

    private TshirtDesignDTO mapToDTO(TshirtDesign TshirtDesign) {
        TshirtDesignDTO dto = new TshirtDesignDTO();

        dto.setDesign(TshirtDesign.getDesign());
        dto.setTshirt(TshirtDesign.getTshirt());
        return dto;
    }

    private TshirtDesign mapToEntity(TshirtDesignDTO dto) {
        TshirtDesign TshirtDesign = new TshirtDesign();

        TshirtDesign.setDesign(dto.getDesign());
        TshirtDesign.setTshirt(dto.getTshirt());
        return TshirtDesign;
    }
}
