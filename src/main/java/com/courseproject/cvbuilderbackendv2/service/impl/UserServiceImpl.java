package com.courseproject.cvbuilderbackendv2.service.impl;

import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.repository.UserRepository;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public boolean authenticate(String userName, String userPassword){
        User user = userRepository.findByUserName(userName);
        if (user == null){
            return false;
        }
        return userPassword.equals(user.getUserPassword());
    }

    @Override
    public boolean register(String userName, String userPassword){
        User newUser = new User(userName, userPassword);
        userRepository.save(newUser);
        return true;
    }
}
