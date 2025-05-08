package com.courseproject.cvbuilderbackendv2.service.impl;

import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.repository.UserRepository;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class UserServiceImpl implements UserService {
//    private static UserServiceImpl instance = new UserServiceImpl();
//    public static UserServiceImpl getInstance() {
//        return instance;
//    }

//    private UserServiceImpl(){
//    }
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public boolean login(Map<String, String> params){
        return (params.get("username").equals("admin") && params.get("password").equals("admin"));
    }

    @Override
    public boolean register(Map<String,String> params){
        return false;
    }
}
