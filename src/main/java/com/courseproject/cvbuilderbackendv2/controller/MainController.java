//package com.courseproject.cvbuilderbackendv2.controller;
//
//
//import com.courseproject.cvbuilderbackendv2.command.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/{command}")
//public class MainController {
//    private final CommandRegistry commandRegistry;
//    @Autowired
//    public MainController(CommandRegistry commandRegistry) { //new code
//        this.commandRegistry = commandRegistry;
//    }
//    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
//    public String handleCommand(@PathVariable String command, @RequestParam Map<String, String> params, Model model) {
//        try {
//            if(command.contains(".html")){ //TODO для перехода на дргуие страницы
//                return command.replaceAll(".html","");
//            }else {
//                Command cmd = commandRegistry.getCommand(command);
//                return cmd.execute(params, model);
//            }
//        } catch (IOException e) {
//            model.addAttribute("err", "Ошибка ввода-вывода");
//        } catch (UnsupportedOperationException e) {
//            model.addAttribute("err", "Неизвестная команда: " + command);
//        }
//        return "index";
//    }
//}
package com.courseproject.cvbuilderbackendv2.controller;


import com.courseproject.cvbuilderbackendv2.command.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
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
    public ResponseEntity<?> handleGetCommand(@RequestHeader String Authorization, @RequestParam Map<String, String> params) {
        try {
            //TODO Возможно убрать Authorization и всё-таки токен по-другому передавать, пора править секурити
            System.out.println("Command = " + params.get("command"));
            System.out.println("Parameters main controller = "+params);
            System.out.println("Authorization header = "+Authorization);
//            System.out.println("resumeId = "+resumeId);
            String command = params.get("command");
            if (command == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Команда не указана"));
            }

            Command cmd = commandRegistry.getCommand(command);
            Map<String,Object> response = cmd.execute(new HashMap<>(params) {{
                put("token", Authorization.substring(7));
            }});
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
            System.out.println(command);
            System.out.println(requestData);
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