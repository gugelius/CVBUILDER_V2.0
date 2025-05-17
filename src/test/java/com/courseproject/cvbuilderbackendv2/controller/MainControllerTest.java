package com.courseproject.cvbuilderbackendv2.controller;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.CommandRegistry;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CommandRegistry commandRegistry;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private MainController mainController;

    @Test
    public void handleGetCommand_WhenCommandMissing_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/controller"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Команда не указана"));
    }

    @Test
    public void handleGetCommand_WhenUnknownCommand_ShouldReturnBadRequest() throws Exception {
        when(commandRegistry.getCommand(anyString())).thenThrow(new UnsupportedOperationException());

        mockMvc.perform(get("/api/controller?command=unknown"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Неизвестная команда: unknown"));
    }

    @Test
    public void handlePostCommand_WhenUnknownCommand_ShouldReturnBadRequest() throws Exception {
        when(commandRegistry.getCommand(anyString())).thenThrow(new UnsupportedOperationException());

        mockMvc.perform(post("/api/controller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"command\":\"unknown\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Неизвестная команда: unknown"));
    }

    @Test
    public void handlePostCommand_WhenRegisterCommandFails_ShouldReturnError() throws Exception {
        when(commandRegistry.getCommand("register")).thenReturn(params -> {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Incorrect login or password");
            return response;
        });

        when(userService.register(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/api/controller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"command\":\"register\",\"username\":\"test\",\"pass\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Incorrect login or password"));
    }

}