//package com.courseproject.cvbuilderbackendv2.command;
//
//import com.courseproject.cvbuilderbackendv2.command.impl.*;
//
//public enum CommandType {
//    COUNT(new CountCommand()),
//    LOGIN_COMMAND(new LoginCommand());
//    private final Command command;
//    CommandType(Command command){
//        this.command = command;
//    }
//    public Command getCommand(){
//        return command;
//    }
//    public static Command define(String commandStr){
//        try {
//            return CommandType.valueOf(commandStr.toUpperCase()).getCommand();
//        }catch (IllegalArgumentException e){
//            throw new UnsupportedOperationException("Unknown command" + commandStr);
//        }
//    }
//}
