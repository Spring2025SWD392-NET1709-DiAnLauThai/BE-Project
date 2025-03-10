package com.be.back_end.service.TshirtColorService;

import com.be.back_end.dto.request.TshirtColorDTO;
import com.be.back_end.model.Color;
import com.be.back_end.model.TShirtColor;
import com.be.back_end.model.Tshirts;
import com.be.back_end.repository.ColorRepository;
import com.be.back_end.repository.TshirtColorRepository;
import com.be.back_end.repository.TshirtsRepository;
import org.springframework.stereotype.Service;

@Service
public class TshirtColorService implements ITshirtColorService{
    private final TshirtColorRepository tshirtColorRepository;

    private final ColorRepository colorRepository;

    private final TshirtsRepository tshirtsRepository;

    public TshirtColorService(TshirtColorRepository tshirtColorRepository, ColorRepository colorRepository, TshirtsRepository tshirtsRepository) {
        this.tshirtColorRepository = tshirtColorRepository;
        this.colorRepository = colorRepository;
        this.tshirtsRepository = tshirtsRepository;
    }
    @Override
    public boolean  createtshirtcolor(TshirtColorDTO tshirtColorDTO)
    {
        TShirtColor newtshirtcolor = new TShirtColor();
        newtshirtcolor.setTshirt(tshirtsRepository.findById(tshirtColorDTO.getTshirtid()).orElse(null));
        newtshirtcolor.setColor(colorRepository.findById(tshirtColorDTO.getColorid()).orElse(null));
        return tshirtColorRepository.save(newtshirtcolor)!=null;

    }
    @Override
    public void linkTshirtWithColor(Tshirts tshirt, String colorId) {
        Color color = colorRepository.findById(colorId)
                .orElseThrow(() -> new IllegalArgumentException("Color not found"));
        TShirtColor tshirtColor = new TShirtColor();
        tshirtColor.setTshirt(tshirt);
        tshirtColor.setColor(color);
        tshirtColorRepository.save(tshirtColor);
    }

}
