//package com.courseproject.cvbuilderbackendv2.command;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class CommandRegistryTest {
//
//    private CommandRegistry commandRegistry;
//    private Command mockCommand;
//
//    @BeforeEach
//    void setUp() {
//        mockCommand = Mockito.mock(Command.class);
//        Mockito.when(mockCommand.getClass().getSimpleName()).thenReturn("TestCommand");
//
//        Set<Command> commands = Set.of(mockCommand);
//        commandRegistry = new CommandRegistry(commands);
//    }
//
//    @Test
//    void givenValidCommand_whenGetCommand_thenReturnCommand() {
//        Command command = commandRegistry.getCommand("TestCommand");
//
//        assertNotNull(command);
//        assertEquals(mockCommand, command);
//    }
//
//    @Test
//    void givenUnknownCommand_whenGetCommand_thenThrowException() {
//        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
//            commandRegistry.getCommand("UNKNOWN");
//        });
//
//        assertEquals("Unknown command: UNKNOWN", exception.getMessage());
//    }
//}
