package com.be.back_end.service.SizeService;

import com.be.back_end.dto.SizeDTO;
import com.be.back_end.model.Size;
import com.be.back_end.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeService implements ISizeService {


    private  SizeRepository sizeRepository;

    public SizeService(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }


    @Override
    public boolean createSize(SizeDTO size) {
        Size newsize=  new Size();

        newsize.setSizeName(size.getSizeName());
        sizeRepository.save(newsize);
        return true;
    }

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();

    }

    @Override
    public Size getSizeById(int id) {
       return sizeRepository.findById(id).orElse(null);
    }

    @Override
    public Size updateUser(int id, SizeDTO size) {
        Size foundedsize=sizeRepository.findById(id).orElse(null);
        if(foundedsize!=null)
        {
            foundedsize.setSizeName(size.getSizeName());
            sizeRepository.save(foundedsize);
        }
        return null;
    }

    @Override
    public boolean deleteUser(int id) {
        Size foundedSize=sizeRepository.findById(id).orElse(null);
        if(foundedSize!=null)
        {
            sizeRepository.delete(foundedSize);
            return true;
        }
        return false;
    }
}
