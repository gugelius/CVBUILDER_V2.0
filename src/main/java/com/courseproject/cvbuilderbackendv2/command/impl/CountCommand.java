//package com.courseproject.cvbuilderbackendv2.command.impl;
//
//import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
//import org.springframework.stereotype.Component;
//import org.springframework.ui.Model;
//
//import java.util.Map;
//
//@Component
//public class CountCommand extends BaseCommand {
//    public CountCommand(JwtUtil jwtUtil){
//        super(jwtUtil);
//    }
//    @Override
//    public String execute(Map<String, String> params, Model model){
//        try{
//            if(!isTokenValid(params, model)){
//                return "index";
//            }else {
//                int num = Integer.parseInt(params.get("input"));
//                int result = num * 2;
//                System.out.println(result);
//                model.addAttribute("result", result);
//            }
//        } catch (NumberFormatException e){
//            model.addAttribute("err", "Ошибка, нюхай бебру");
//        }
//        return "main";
//    }
//}
