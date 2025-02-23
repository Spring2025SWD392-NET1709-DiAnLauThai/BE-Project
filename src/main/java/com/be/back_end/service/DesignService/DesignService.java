package com.be.back_end.service.DesignService;


import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.DesignDTO;
import com.be.back_end.enums.AccountEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.model.Account;
import com.be.back_end.model.Designs;
import com.be.back_end.repository.DesignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DesignService implements IDesignService {


    private final DesignRepository designsRepository;

    @Autowired
    public DesignService(DesignRepository designsRepository) {
        this.designsRepository = designsRepository;
    }


    @Override
    public List<DesignDTO> getAllDesigns() {
        List<Designs> designsList= designsRepository.findAll();
        List<DesignDTO> list= new ArrayList<>();
        for(Designs design:designsList)
        {
            list.add(mapToDTO(design));
            System.out.println(design.getId());
        }
        return list;
    }

    @Override
    public DesignDTO getDesignById(String id) {
        Designs design= designsRepository.findById(id).orElse(null);
        return mapToDTO(design);
    }

    @Override
    public DesignDTO createDesign(DesignDTO dto) {
        Designs design= mapToEntity(dto);
        designsRepository.save(design);
        return dto;
    }

    @Override
    public boolean updateDesign(String id, DesignDTO dto) {
        Designs updatedDesign= designsRepository.findById(id).orElse(null);
        if(updatedDesign==null){
            return false;
        }
        updatedDesign=mapToEntity(dto);
        designsRepository.save(updatedDesign);
        return true;
    }

    @Override
    public boolean deleteDesign(String id) {
        Designs existingDesign = designsRepository.getById(id);
        if (existingDesign != null) {
            designsRepository.delete(existingDesign);
            return true;
        }
        return false;
    }

    private DesignDTO mapToDTO(Designs design) {
        DesignDTO dto = new DesignDTO();


        dto.setDesignName(design.getDesignName());
        dto.setDesignFile(design.getDesignFile());
        dto.setPrice(design.getPrice());


        return dto;
    }

    private Designs mapToEntity(DesignDTO dto) {
        Designs design = new Designs();

        design.setDesignName(dto.getDesignName());
        design.setDesignFile(dto.getDesignFile());
        design.setPrice(dto.getPrice());

        return design;
    }
}
