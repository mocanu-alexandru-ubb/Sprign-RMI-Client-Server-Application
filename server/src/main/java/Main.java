import Config.Config;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(Config.class.getPackageName());
        System.out.println("Server started...");
    }
}
