package Store;

import Store.Domain.Candy;
import Store.Domain.Client;
import Store.Domain.Purchase;
import Store.Domain.Validator.CandyValidator;
import Store.Domain.Validator.ClientValidator;
import Store.Domain.Validator.PurchaseValidator;
import Store.Domain.Validator.Validator;
import Store.Repository.ClientRepo;
import Store.Repository.DBRepo.DBCandyRepo;
import Store.Repository.DBRepo.DBClientRepo;
import Store.Repository.DBRepo.DBPurchaseRepo;
import Store.Repository.GenericFileRepo;
import Store.Repository.GenericXmlRepo;
import Store.Repository.Repository;
import Store.Services.CandyService;
import Store.Services.ClientService;
import Store.Services.PurchaseService;
import Store.UI.Console;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {
    private static Repository<Long, Client> clientRepository;
    private static Repository<Long, Candy> candyRepository;
    private static Repository<Long, Purchase> purchaseRepository;
    private static final Validator<Client> clientValidator = new ClientValidator();
    private static final Validator<Candy> candyValidator = new CandyValidator();
    private static final Validator<Purchase> purchaseValidator = new PurchaseValidator();

    private static void innitInMemory(Scanner properties) {
        clientRepository = new ClientRepo<>(clientValidator);
        candyRepository = new ClientRepo<>(candyValidator);
        purchaseRepository = new ClientRepo<>(purchaseValidator);
    }

    private static void innitDB(Scanner properties) {
        String user = properties.nextLine();
        String pass = properties.nextLine();
        String url = properties.nextLine();
        try {
            clientRepository = new DBClientRepo(clientValidator, user, pass, url);
            candyRepository = new DBCandyRepo(candyValidator, user, pass, url);
            purchaseRepository = new DBPurchaseRepo(purchaseValidator, user, pass, url);
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("bad connection");
            System.exit(-1);
        }
        catch (NoSuchElementException e) {
            System.out.println("not enough arguments");
            System.exit(-1);
        }
    }

    private static void innitFile(Scanner properties) {
        String clientFile = properties.nextLine();
        String candyFile = properties.nextLine();
        String purchaseFile = properties.nextLine();
        clientRepository = new GenericFileRepo<>(clientValidator, clientFile, Client.class);
        candyRepository = new GenericFileRepo<>(candyValidator, candyFile, Candy.class);
        purchaseRepository = new GenericFileRepo<>(purchaseValidator, purchaseFile, Purchase.class);
    }

    private static void innitXML(Scanner properties) {
        String clientFile = properties.nextLine();
        String candyFile = properties.nextLine();
        String purchaseFile = properties.nextLine();
        clientRepository = new GenericXmlRepo<>(clientValidator, clientFile, Client.class);
        candyRepository = new GenericXmlRepo<>(candyValidator, candyFile, Candy.class);
        purchaseRepository = new GenericXmlRepo<>(purchaseValidator, purchaseFile, Purchase.class);
    }

    public static void main(String[] args) {
        try (Scanner properties = new Scanner(new FileReader("PROPERTIES.IN"))) {
            String repoType = properties.nextLine();
            HashMap<String, Consumer<Scanner>> types = new HashMap<>();
            types.put("InMemory", Main::innitInMemory);
            types.put("DB", Main::innitDB);
            types.put("XML", Main::innitXML);
            types.put("File", Main::innitFile);

            types.getOrDefault(repoType, Main::innitInMemory).accept(properties);

            ClientService studentService = new ClientService(clientRepository);
            CandyService candyService = new CandyService(candyRepository);
            PurchaseService purchaseService = new PurchaseService(purchaseRepository);

            Console console = new Console(studentService, candyService, purchaseService);
            console.runConsole();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}