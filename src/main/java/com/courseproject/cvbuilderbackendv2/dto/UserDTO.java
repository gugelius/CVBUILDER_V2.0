package com.courseproject.cvbuilderbackendv2.dto;

import com.courseproject.cvbuilderbackendv2.entity.Role;

public class UserDTO {
    private int userId;
    private String userName;
    private String userEmail;
    private Role role;
    private boolean twoFactorEnabled;

    public UserDTO() {}

    public UserDTO(int userId, String userName, String userEmail, Role role, boolean twoFactorEnabled) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.role = role;
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isTwoFactorEnabled() { return twoFactorEnabled; }
    public void setTwoFactorEnabled(boolean twoFactorEnabled) { this.twoFactorEnabled = twoFactorEnabled; }
}