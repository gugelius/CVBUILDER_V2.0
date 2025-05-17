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
    private static final String ERROR = "error";
    private static final String COMMAND = "command";
    private static final String COMMAND_UNDEFINED = "Команда не указана";
    private static final String UNKNOWN_COMMAND = "Неизвестная команда: ";
    private static final String IO_ERROR = "Ошибка ввода-вывода";


    @Autowired
    public MainController(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> handleGetCommand(@RequestParam Map<String, String> params) {
        try {
            String command = params.get(COMMAND);
            if (command == null) {
                return ResponseEntity.badRequest().body(Map.of(ERROR, COMMAND_UNDEFINED));
            }

            Command cmd = commandRegistry.getCommand(command);
            Map<String,Object> response = cmd.execute(new HashMap<>(params));
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(ERROR, IO_ERROR));
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(400).body(Map.of(ERROR, UNKNOWN_COMMAND + params.get(COMMAND)));
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> handlePostCommand(@RequestBody Map<String, Object> requestData) {
        try {
            String command = requestData.get(COMMAND).toString();

            if (command == null) {
                return ResponseEntity.badRequest().body(Map.of(ERROR, COMMAND_UNDEFINED));
            }

            Command cmd = commandRegistry.getCommand(command);
            Map<String, Object> response = cmd.execute(requestData);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(ERROR, IO_ERROR));
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(400).body(Map.of(ERROR, UNKNOWN_COMMAND + requestData.get(COMMAND)));
        }
    }
}