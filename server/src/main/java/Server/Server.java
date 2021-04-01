package Server;

import Networking.ServerInformation;
import Services.CandyServiceSkeleton;
import Services.ClientServiceSkeleton;
import Services.PurchaseServiceSkeleton;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Server {
    private final ClientServiceSkeleton clientServiceSkeleton;
    private final CandyServiceSkeleton candySrv;
    private final PurchaseServiceSkeleton purchaseSrv;
    private final ExecutorService executorService;
    private final Boolean running = true;

    public Server(ClientServiceSkeleton clientServiceSkeleton, CandyServiceSkeleton candySrv, PurchaseServiceSkeleton purchaseSrv, ExecutorService executorService) {
        this.clientServiceSkeleton = clientServiceSkeleton;
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
                executorService.submit(new TaskHandler(socket, clientServiceSkeleton, candySrv, purchaseSrv));
            }
            executorService.shutdown();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
