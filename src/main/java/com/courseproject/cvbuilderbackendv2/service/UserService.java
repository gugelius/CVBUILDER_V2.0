package com.courseproject.cvbuilderbackendv2.service;

import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;

public interface UserService {
    boolean authenticate(String userName, String userPassword);
    boolean register(String userName, String userPassword);
    boolean register(String userName, String userPassword, String userEmail);
    boolean register(String userName, String userPassword, String userEmail, Role role);
    int findUserId(String userName);
    String extractUserName();
    User findUserByUsername(String userName);
    void saveUser(User user);
    User findByEmail(String email);
    Role getUserRole(String userName);
}