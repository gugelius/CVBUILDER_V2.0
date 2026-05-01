package com.courseproject.cvbuilderbackendv2.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = true)
    private String userPassword;

    @Column(unique = true, nullable = true)
    private String userEmail;

    @Column(nullable = true)
    private String twoFactorCode;

    @Column(nullable = true)
    private OffsetDateTime twoFactorCodeExpiry;

    @Column(nullable = false)
    private boolean twoFactorEnabled = false;

    public User() {}

    public User(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public User(String userName, String userPassword, String userEmail) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getTwoFactorCode() { return twoFactorCode; }
    public void setTwoFactorCode(String twoFactorCode) { this.twoFactorCode = twoFactorCode; }

    public OffsetDateTime getTwoFactorCodeExpiry() { return twoFactorCodeExpiry; }
    public void setTwoFactorCodeExpiry(OffsetDateTime twoFactorCodeExpiry) { this.twoFactorCodeExpiry = twoFactorCodeExpiry; }

    public boolean isTwoFactorEnabled() { return twoFactorEnabled; }
    public void setTwoFactorEnabled(boolean twoFactorEnabled) { this.twoFactorEnabled = twoFactorEnabled; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", twoFactorEnabled=" + twoFactorEnabled +
                '}';
    }
}
