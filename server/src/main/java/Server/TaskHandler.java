package Server;

import Networking.Message;
import Services.CandyServiceSkeleton;
import Services.ClientServiceSkeleton;
import Services.PurchaseServiceSkeleton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

public class TaskHandler implements Runnable{
    private final Socket client;
    private final ClientServiceSkeleton clientSrv;
    private final CandyServiceSkeleton candySrv;
    private final PurchaseServiceSkeleton purchaseSrv;

    public TaskHandler(Socket client, ClientServiceSkeleton clientSrv, CandyServiceSkeleton candySrv, PurchaseServiceSkeleton purchaseSrv) {
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
