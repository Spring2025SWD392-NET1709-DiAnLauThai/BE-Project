package com.be.back_end.service.SizeService;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.SizeDTO;
import com.be.back_end.model.Size;

import java.util.List;

public interface ISizeService {
    boolean createSize(SizeDTO size);
    List<Size> getAllSizes();
    Size getSizeById(int id);
    Size updateUser(int id,SizeDTO size);
    boolean deleteUser(int id);
}
