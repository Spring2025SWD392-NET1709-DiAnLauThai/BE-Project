package com.be.back_end.service.TshirtColorService;

import com.be.back_end.dto.TshirtColorDTO;
import com.be.back_end.model.TShirtColor;
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

}
