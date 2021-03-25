package Server;

import Networking.ServerInformation;
import Service.CandyService;
import Service.ClientService;
import Service.PurchaseService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Server {
    private final ClientService clientService;
    private final CandyService candySrv;
    private final PurchaseService purchaseSrv;
    private final ExecutorService executorService;
    private final Boolean running = true;

    public Server(ClientService clientService, CandyService candySrv, PurchaseService purchaseSrv, ExecutorService executorService) {
        this.clientService = clientService;
        this.candySrv = candySrv;
        this.purchaseSrv = purchaseSrv;
        this.executorService = executorService;
    }

    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(ServerInformation.PORT)) {
            System.out.println("Server started...");
            while (running) {
                Socket socket = serverSocket.accept();
                executorService.submit(new TaskHandler(socket, clientService, candySrv, purchaseSrv));
            }
            executorService.shutdown();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
