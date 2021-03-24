import Services.*;
import UI.Console;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ClientService clientService = new ClientServiceStub(executorService);
        CandyService candyService = new CandyServiceStub(executorService);
        PurchaseService purchaseService = new PurchaseServerStub(executorService);

        Console console = new Console(clientService, candyService, purchaseService);

        console.runConsole();
    }
}
