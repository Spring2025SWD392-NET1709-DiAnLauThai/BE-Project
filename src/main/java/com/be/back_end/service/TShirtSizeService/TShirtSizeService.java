package com.be.back_end.service.TShirtSizeService;

import com.be.back_end.dto.TShirtSizeDTO;
import com.be.back_end.model.TShirtSize;
import com.be.back_end.repository.SizeRepository;
import com.be.back_end.repository.TshirtSizeRepository;
import com.be.back_end.repository.TshirtsRepository;
import org.springframework.stereotype.Service;

@Service
public class TShirtSizeService implements ITShirtSizeService {
    private final TshirtSizeRepository tshirtSizeRepository;
    private final SizeRepository sizeRepository;
    private final TshirtsRepository tshirtsRepository;

    public TShirtSizeService(TshirtSizeRepository tshirtSizeRepository, SizeRepository sizeRepository, TshirtsRepository tshirtsRepository) {
        this.tshirtSizeRepository = tshirtSizeRepository;
        this.sizeRepository = sizeRepository;
        this.tshirtsRepository = tshirtsRepository;
    }

    @Override
    public boolean createTShirtSize(TShirtSizeDTO dto) {
        TShirtSize tshirtSize = new TShirtSize();
        tshirtSize.setTshirt(tshirtsRepository.findById(dto.getTshirtId()).orElse(null));
        tshirtSize.setSize(sizeRepository.findById(dto.getSizeId()).orElse(null));
        return tshirtSizeRepository.save(tshirtSize) != null;
    }
}