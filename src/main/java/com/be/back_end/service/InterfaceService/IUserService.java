package com.be.back_end.service.InterfaceService;

import com.be.back_end.model.Tshirts;
import com.be.back_end.model.User;

import java.util.List;

public interface IUserService {
    List<User> getAll();
    User getById(int id);
    List<User> getByName(String name);
    User save(User user);
    void removeById(int id);
}
