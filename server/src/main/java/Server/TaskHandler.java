package Server;

import Networking.Message;
import Service.ClientService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

public class TaskHandler implements Runnable{
    private final Socket client;
    private final ClientService clientSrv;

    public TaskHandler(Socket client, ClientService clientSrv) {
        this.client = client;
        this.clientSrv = clientSrv;
    }

    @Override
    public void run() {
        try (InputStream inputStream = client.getInputStream();
             OutputStream outputstream = client.getOutputStream()) {
            Message message = Message.read(inputStream);
            Message response = ServiceHandler.handleMessage(message, clientSrv);
            Objects.requireNonNull(response, "error computing response");
            Message.write(response, outputstream);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
