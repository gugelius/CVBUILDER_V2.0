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
    public boolean authenticate(String userName, String userPassword){//TODO Validation + Encryption + JWT (Utils)
        User user = userRepository.findByUserName(userName);
        return (user == null) ? false : userPassword.equals(user.getUserPassword());
    }

    @Override
    public boolean register(String userName, String userPassword){ //TODO Validation + Encryption + JWT (Utils)
        if(userRepository.findByUserName(userName)!= null){
            return false;
        }else {
            userRepository.save(new User(userName, userPassword));
            return true;
        }
    }

    @Override
    public int findUserId(String userName){
        return userRepository.findByUserName(userName).getUserId();
    }
}
