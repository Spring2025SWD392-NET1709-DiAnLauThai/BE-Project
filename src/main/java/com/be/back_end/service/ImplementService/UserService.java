package com.be.back_end.service.ImplementService;

import com.be.back_end.model.User;
import com.be.back_end.repository.TshirtRepository;
import com.be.back_end.repository.UserRepository;
import com.be.back_end.service.InterfaceService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User getById(int id) {
        return null;
    }

    @Override
    public List<User> getByName(String name) {
        return List.of();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void removeById(int id) {

    }
}
