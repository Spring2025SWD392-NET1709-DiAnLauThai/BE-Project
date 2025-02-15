package com.be.back_end.service.TShirtMaterialService;

import com.be.back_end.dto.TshirtMaterialDTO;
import com.be.back_end.model.TShirtMaterial;
import com.be.back_end.repository.MaterialRepository;
import com.be.back_end.repository.TshirtMaterialRepository;
import com.be.back_end.repository.TshirtsRepository;
import org.springframework.stereotype.Service;

@Service
public class TShirtMaterialService implements ITshirtMaterialService {
    private final TshirtMaterialRepository tshirtMaterialRepository;
    private final MaterialRepository materialRepository;
    private final TshirtsRepository tshirtsRepository;

    public TShirtMaterialService(TshirtMaterialRepository tshirtMaterialRepository, MaterialRepository materialRepository, TshirtsRepository tshirtsRepository) {
        this.tshirtMaterialRepository = tshirtMaterialRepository;
        this.materialRepository = materialRepository;
        this.tshirtsRepository = tshirtsRepository;
    }

    @Override
    public boolean createTShirtMaterial(TshirtMaterialDTO dto) {
        TShirtMaterial tshirtMaterial = new TShirtMaterial();
        tshirtMaterial.setTshirt(tshirtsRepository.findById(dto.getTshirtId()).orElse(null));
        tshirtMaterial.setMaterial(materialRepository.findById(dto.getMaterialId()).orElse(null));
        return tshirtMaterialRepository.save(tshirtMaterial) != null;
    }
}
