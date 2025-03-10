package com.be.back_end.service.TshirtColorService;

import com.be.back_end.dto.request.TshirtColorDTO;
import com.be.back_end.model.Tshirts;

public interface ITshirtColorService {
    public boolean  createtshirtcolor(TshirtColorDTO tshirtColorDTO);
    void linkTshirtWithColor(Tshirts tshirt, String colorId);
}
