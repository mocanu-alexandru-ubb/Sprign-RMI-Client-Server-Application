import Domain.Candy;
import Domain.Client;
import Domain.Purchase;
import Networking.ServerInformation;
import Repository.Repository;
import Server.Server;
import Service.CandyService;
import Service.ClientService;
import Service.PurchaseService;
import Validator.CandyValidator;
import Validator.ClientValidator;
import Repository.GenericRepo;
import Validator.PurchaseValidator;
import Repository.*;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ClientValidator clientValidator = new ClientValidator();
        CandyValidator candyValidator = new CandyValidator();
        PurchaseValidator purchaseValidator = new PurchaseValidator();

        try {
            var user = ServerInformation.databaseUser;
            var pass = ServerInformation.databasePassword;
            var conn = ServerInformation.databaseConnectionString;
            Repository<Long, Client> clientRepo = new DBClientRepo(clientValidator, user, pass, conn);
            Repository<Long, Candy> candyRepository = new DBCandyRepo(candyValidator, user, pass, conn);
            Repository<Long, Purchase> purchaseRepository = new DBPurchaseRepo(purchaseValidator, user, pass, conn);

            ClientService clientService = new ClientService(clientRepo);
            CandyService candySrv = new CandyService(candyRepository);
            PurchaseService purchaseSrv = new PurchaseService(purchaseRepository, candyRepository);

            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            Server server = new Server(clientService, candySrv, purchaseSrv, executorService);
            server.run();
            executorService.shutdown();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
