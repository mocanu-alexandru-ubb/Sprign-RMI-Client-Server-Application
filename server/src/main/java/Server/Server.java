package Server;

import Networking.ServerInformation;
import Service.ClientService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class Server {
    private final ClientService clientService;
    private final ExecutorService executorService;
    private final Boolean running = true;

    public Server(ClientService clientService, ExecutorService executorService) {
        this.clientService = clientService;
        this.executorService = executorService;
    }

    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(ServerInformation.PORT)) {
            System.out.println("Server started...");
            while (running) {
                Socket socket = serverSocket.accept();
                executorService.submit(new TaskHandler(socket, clientService));
            }
            executorService.shutdown();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
