import Domain.Candy;
import Domain.Client;
import Domain.Purchase;
import Repository.Repository;
import Server.Server;
import Service.CandyService;
import Service.ClientService;
import Service.PurchaseService;
import Validator.CandyValidator;
import Validator.ClientValidator;
import Repository.GenericRepo;
import Validator.PurchaseValidator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ClientValidator clientValidator = new ClientValidator();
        CandyValidator candyValidator = new CandyValidator();
        PurchaseValidator purchaseValidator = new PurchaseValidator();
        Repository<Long, Client> clientRepo = new GenericRepo<Long, Client>(clientValidator);
        Repository<Long, Candy> candyRepository = new GenericRepo<Long, Candy>(candyValidator);
        Repository<Long, Purchase> purchaseRepository = new GenericRepo<Long, Purchase>(purchaseValidator);
        ClientService clientService = new ClientService(clientRepo);
        CandyService candySrv = new CandyService(candyRepository);
        PurchaseService purchaseSrv = new PurchaseService(purchaseRepository, candyRepository);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Server server = new Server(clientService, candySrv, purchaseSrv, executorService);
        server.run();
    }
}
