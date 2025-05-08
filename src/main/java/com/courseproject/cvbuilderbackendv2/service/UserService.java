package com.courseproject.cvbuilderbackendv2.service;

import java.util.Map;

public interface UserService {
    boolean login(Map<String, String> params);
    boolean register(Map<String, String> params);
}
