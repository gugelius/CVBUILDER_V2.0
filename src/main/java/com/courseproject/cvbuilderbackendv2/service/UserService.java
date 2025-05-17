package com.courseproject.cvbuilderbackendv2.service;

public interface UserService {
    boolean authenticate(String userName, String userPassword);
    boolean register(String userName, String userPassword);
    int findUserId(String userName);
    String extractUserName();
}
