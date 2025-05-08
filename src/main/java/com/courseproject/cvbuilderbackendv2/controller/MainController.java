package com.courseproject.cvbuilderbackendv2.controller;


import com.courseproject.cvbuilderbackendv2.command.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/{command}")
public class MainController {
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String handleCommand(@PathVariable String command, @RequestParam Map<String, String> params, Model model) {
        try {
            Command cmd = CommandType.define(command);
            return cmd.execute(params, model);
        } catch (IOException e) {
            model.addAttribute("err", "Ошибка ввода-вывода");
        } catch (UnsupportedOperationException e) {
            model.addAttribute("err", "Неизвестная команда: " + command);
        }
        return "index";
    }
}
