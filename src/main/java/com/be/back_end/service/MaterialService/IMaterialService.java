package com.be.back_end.service.MaterialService;

import com.be.back_end.dto.MaterialDTO;
import com.be.back_end.model.Material;

import java.util.List;

public interface IMaterialService {
    boolean createMaterial(MaterialDTO materialDTO);
    List<Material> getAllMaterials();
    Material getMaterialById(String id);
    Material updateMaterial(String id, MaterialDTO materialDTO);
    boolean deleteMaterial(String id);
}
