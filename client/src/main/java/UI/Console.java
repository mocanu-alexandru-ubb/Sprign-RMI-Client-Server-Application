package UI;

import Services.ClientService;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Stream;

public class Console {
    private final Scanner scanner = new Scanner(System.in);
    private final HashMap<String, Runnable> cmds = new HashMap<>();
    private final ClientService clientService;

    public Console(ClientService clientService) {
        this.clientService = clientService;
        cmds.put("help", () -> System.out.println(cmds.keySet()));
    }

    public void runConsole() {
        Stream.generate(() -> {System.out.print(">>>"); return scanner.nextLine();})
                .takeWhile((str) -> !str.equals("exit"))
                .forEach((str) -> {
                    try { cmds.getOrDefault(str, this::noFunction).run();
                    } catch (InputMismatchException e) {System.out.println(e);}
                });
    }

    private void noFunction() {
        System.out.println("Command not found!");
        System.out.println(cmds.keySet());
    }
}
