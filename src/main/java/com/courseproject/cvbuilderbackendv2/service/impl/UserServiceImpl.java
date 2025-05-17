package com.courseproject.cvbuilderbackendv2.service.impl;

import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.repository.UserRepository;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private static final String ERROR = "error";
    private static final String ANON = "anonymousUser";
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public boolean authenticate(String userName, String userPassword){
        User user = userRepository.findByUserName(userName);
        return (user == null) ? false : passwordEncoder.matches(userPassword, user.getUserPassword());
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean register(String userName, String userPassword){
        if(userRepository.findByUserName(userName)!= null){
            return false;
        }else {
            String hashedPassword = passwordEncoder.encode(userPassword);
            userRepository.save(new User(userName, hashedPassword));
            return true;
        }
    }

    @Override
    public int findUserId(String userName){
        return userRepository.findByUserName(userName).getUserId();
    }

    @Override
    public String extractUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals(ANON)) {
            return ERROR;
        }

        return authentication.getName();
    }
}
