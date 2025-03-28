package com.be.back_end.service.ColorService;


import com.be.back_end.dto.request.ColorCreateRequest;
import com.be.back_end.dto.request.ColorDTO;
import com.be.back_end.model.Color;
import com.be.back_end.repository.ColorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColorService implements IColorService {
    private final ColorRepository colorRepository;

    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public boolean addColor(ColorCreateRequest colorDTO) {
        Color color = new Color();
        color.setColorName(colorDTO.getColorName());
        color.setColorCode(colorDTO.getColorCode());
        return colorRepository.save(color) != null;
    }

    @Override
    public List<ColorDTO> getAllColors() {
        List<Color> colors= colorRepository.findAll();
        List<ColorDTO> colorResponses= new ArrayList<>();
        for(Color color:colors)
        {
            ColorDTO colorResponse= new ColorDTO();
            colorResponse.setColorId(color.getId());
            colorResponse.setColorName(color.getColorName());
            colorResponse.setColorCode(color.getColorCode());
            colorResponses.add(colorResponse);
        }
        return colorResponses;

    }

    @Override
    public Color getColorById(String id) {
        return colorRepository.findById(id).orElse(null);
    }

    @Override
    public Color updateColor(String id, ColorDTO colorDTO) {
        Color color = colorRepository.findById(id).orElse(null);
        if (color != null) {
            color.setColorName(colorDTO.getColorName());
            color.setColorCode(colorDTO.getColorCode());
            return colorRepository.save(color);
        }
        return null;
    }

    @Override
    public boolean deleteColor(String id) {
        if (colorRepository.existsById(id)) {
            colorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
