package com.courseproject.cvbuilderbackendv2.service;

import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.repository.UserRepository;
import com.courseproject.cvbuilderbackendv2.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void givenNewUser_whenRegister_thenUserIsSaved() {
        String username = "new_user";
        String password = "password123";
        String hashedPassword = "$2a$10$hashed";

        when(userRepository.findByUserName(username)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn(hashedPassword);

        boolean result = userService.register(username, password);

        assertTrue(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void givenValidCredentials_whenAuthenticate_thenReturnTrue() {
        String username = "validUser";
        String password = "password123";
        String hashedPassword = "$2a$10$hashed";

        User mockUser = new User();
        mockUser.setUserName(username);
        mockUser.setUserPassword(hashedPassword);

        when(userRepository.findByUserName(username)).thenReturn(mockUser);
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(true);

        boolean result = userService.authenticate(username, password);

        assertTrue(result);
        verify(userRepository, times(1)).findByUserName(username);
        verify(passwordEncoder, times(1)).matches(password, hashedPassword);
    }

    @Test
    void givenInvalidCredentials_whenAuthenticate_thenReturnFalse() {
        String username = "validUser";
        String wrongPassword = "wrongPassword";
        String hashedPassword = "$2a$10$hashed";

        User mockUser = new User();
        mockUser.setUserName(username);
        mockUser.setUserPassword(hashedPassword);

        when(userRepository.findByUserName(username)).thenReturn(mockUser);
        when(passwordEncoder.matches(wrongPassword, hashedPassword)).thenReturn(false);

        boolean result = userService.authenticate(username, wrongPassword);

        assertFalse(result);
        verify(userRepository, times(1)).findByUserName(username);
        verify(passwordEncoder, times(1)).matches(wrongPassword, hashedPassword);
    }

    @Test
    void givenNonExistentUser_whenAuthenticate_thenReturnFalse() {
        String username = "nonExistentUser";
        when(userRepository.findByUserName(username)).thenReturn(null);

        boolean result = userService.authenticate(username, "anyPassword");

        assertFalse(result);
        verify(userRepository, times(1)).findByUserName(username);
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
    }

}
