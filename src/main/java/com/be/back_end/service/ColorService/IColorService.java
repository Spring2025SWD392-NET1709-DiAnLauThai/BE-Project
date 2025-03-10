package com.be.back_end.service.ColorService;

import com.be.back_end.dto.request.ColorDTO;
import com.be.back_end.model.Color;

import java.util.List;

public interface IColorService {
    boolean addColor(ColorDTO colorDTO);
    List<Color> getAllColors();
    Color getColorById(String id);
    Color updateColor(String id, ColorDTO colorDTO);
    boolean deleteColor(String id);
}
