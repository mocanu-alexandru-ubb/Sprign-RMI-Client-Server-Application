import Config.Config;
import UI.Console;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.ExecutorService;

public class Main {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class.getPackageName());

        context.getBean(Console.class).runConsole();
        context.getBean(ExecutorService.class).shutdown();
    }
}
