import Domain.Client;
import Repository.Repository;
import Server.Server;
import Service.ClientService;
import Validator.ClientValidator;
import Repository.GenericRepo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ClientValidator clientValidator = new ClientValidator();
        Repository<Long, Client> clientRepo = new GenericRepo<Long, Client>(clientValidator);
        ClientService clientService = new ClientService(clientRepo);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Server server = new Server(clientService, executorService);
        server.run();
    }
}
