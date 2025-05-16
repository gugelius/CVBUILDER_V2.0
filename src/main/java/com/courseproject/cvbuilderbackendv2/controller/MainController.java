package com.courseproject.cvbuilderbackendv2.controller;


import com.courseproject.cvbuilderbackendv2.command.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/controller")
public class MainController {
    private final CommandRegistry commandRegistry;

    @Autowired
    public MainController(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @GetMapping
    public ResponseEntity<?> handleGetCommand(@RequestParam Map<String, String> params) {
        try {
            String command = params.get("command");
            if (command == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Команда не указана"));
            }

            Command cmd = commandRegistry.getCommand(command);
            Map<String,Object> response = cmd.execute(new HashMap<>(params));
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Ошибка ввода-вывода"));
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(400).body(Map.of("error", "Неизвестная команда: " + params.get("command")));
        }
    }

    @PostMapping
    public ResponseEntity<?> handlePostCommand(@RequestBody Map<String, Object> requestData) {
        try {
            String command = requestData.get("command").toString();
            if (command == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Команда не указана"));
            }

            Command cmd = commandRegistry.getCommand(command);
            Map<String, Object> response = cmd.execute(requestData);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Ошибка ввода-вывода"));
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(400).body(Map.of("error", "Неизвестная команда: " + requestData.get("command")));
        }
    }
}