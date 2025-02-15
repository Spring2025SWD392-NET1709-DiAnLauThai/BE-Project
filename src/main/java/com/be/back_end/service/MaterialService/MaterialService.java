package com.be.back_end.service.MaterialService;

import com.be.back_end.dto.MaterialDTO;
import com.be.back_end.model.Material;
import com.be.back_end.repository.MaterialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService implements IMaterialService {
    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Override
    public boolean createMaterial(MaterialDTO materialDTO) {
        Material material = new Material();
        material.setName(materialDTO.getName());
        material.setPrice(materialDTO.getPrice());
        material.setQuantity(materialDTO.getQuantity());
        materialRepository.save(material);
        return true;
    }

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Override
    public Material getMaterialById(String id) {
        return materialRepository.findById(id).orElse(null);
    }

    @Override
    public Material updateMaterial(String id, MaterialDTO materialDTO) {
        Material material = materialRepository.findById(id).orElse(null);
        if (material != null) {
            material.setName(materialDTO.getName());
            material.setPrice(materialDTO.getPrice());
            material.setQuantity(materialDTO.getQuantity());
            return materialRepository.save(material);
        }
        return null;
    }

    @Override
    public boolean deleteMaterial(String id) {
        if (materialRepository.existsById(id)) {
            materialRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
