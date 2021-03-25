package Server;

import Networking.Message;
import Service.CandyService;
import Service.ClientService;
import Service.PurchaseService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

public class TaskHandler implements Runnable{
    private final Socket client;
    private final ClientService clientSrv;
    private final CandyService candySrv;
    private final PurchaseService purchaseSrv;

    public TaskHandler(Socket client, ClientService clientSrv, CandyService candySrv, PurchaseService purchaseSrv) {
        this.client = client;
        this.clientSrv = clientSrv;
        this.candySrv = candySrv;
        this.purchaseSrv = purchaseSrv;
    }

    @Override
    public void run() {
        try (InputStream inputStream = client.getInputStream();
             OutputStream outputstream = client.getOutputStream()) {
            Message message = Message.read(inputStream);
            System.out.println(message);
            Message response = ServiceHandler.handleMessage(message, clientSrv, candySrv, purchaseSrv);
            System.out.println(response);
            Objects.requireNonNull(response, "error computing response");
            Message.write(response, outputstream);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
