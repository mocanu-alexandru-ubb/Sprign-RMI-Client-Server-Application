package Store.UI;

import Store.Domain.Candy;
import Store.Domain.Client;
import Store.Domain.Purchase;
import Store.Domain.Validator.RepoException;
import Store.Domain.Validator.StoreException;
import Store.Domain.Validator.ValidatorException;
import Store.Services.CandyService;
import Store.Services.ClientService;
import Store.Services.PurchaseService;

import java.util.*;
import java.util.stream.Stream;

public class Console {
    private final Scanner scanner = new Scanner(System.in);
    private final ClientService clientService;
    private final HashMap<String, Runnable> cmds = new HashMap<>();
    private final CandyService candyService;
    private final PurchaseService purchaseService;

    public Console(ClientService clientService, CandyService candyService, PurchaseService purchaseService) {
        this.clientService = clientService;
        this.candyService = candyService;
        this.purchaseService = purchaseService;
        cmds.put("printAllClients", this::printAllStudents);
        cmds.put("printAllCandies", this::printAllCandies);
        cmds.put("printAllPurchases", this::printAllPurchases);
        cmds.put("updateClient", this::updateClient);
        cmds.put("updateCandy", this::updateCandy);
        cmds.put("updatePurchase", this::updatePurchase);
        cmds.put("addClient", this::addClient);
        cmds.put("addCandy", this::addCandy);
        cmds.put("addPurchase", this::addPurchase);
        cmds.put("removePurchase", this::removePurchase);
        cmds.put("removeCandy", this::removeCandy);
        cmds.put("removeCandyCascade", this::removeCandyCascade);
        cmds.put("removeClient", this::removeClient);
        cmds.put("removeClientCascade", this::removeClientCascade);
        cmds.put("filterClientByName", this::filterClientsByName);
        cmds.put("filterByPrice", this::filterCandyByPrice);
        cmds.put("computeCost", this::cost);
        cmds.put("help", () -> System.out.println(cmds.keySet()));
    }

    public void runConsole() {
        Stream.generate(() -> {System.out.print(">>>"); return scanner.nextLine();})
                .takeWhile((str) -> !str.equals("exit"))
                .forEach((str) -> {
                    try { cmds.getOrDefault(str, this::noFunction).run();
                    } catch (StoreException | InputMismatchException e) {System.out.println(e);}
                });
    }

    private void cost() throws StoreException{
        System.out.println("Input purchaseId:");
        var purchaseId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());

        var purchase = purchaseService.getOne(purchaseId).orElseThrow(StoreException::new);
        var candy = candyService.getOne(purchase.getCandyID()).orElseThrow(StoreException::new);
        System.out.println(purchaseService.computePrice(purchaseId, candy));
    }

    private void updateClient() throws ValidatorException, InputMismatchException{
        System.out.println("Input old clientId:");
        var clientID = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());

        System.out.println("Input new client name:");
        String clientName = scanner.nextLine();
        Client client = new Client(clientID, clientName);

        clientService.removeClient(clientID);
        clientService.addClient(client);

    }

    private void noFunction() {
        System.out.println("Command not found!");
        System.out.println(cmds.keySet());
    }

    private void filterCandyByPrice() {
        System.out.println("Input price:");
        var price = Float.parseFloat(
                Optional.of(scanner)
                        .filter(Scanner::hasNextFloat)
                        .orElseThrow(() -> {
                            scanner.nextLine();
                            throw new InputMismatchException();
                        })
                        .nextLine());
        candyService.filterByPrice(price).forEach(System.out::println);
    }

    private void removeCandyCascade() {
        System.out.println("Input candy id:");
        var candyId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());
        purchaseService.removeByCandyId(candyId);
        candyService.removeCandy(candyId);
    }

    private void removeCandy() {
        System.out.println("Input candy id:");
        var candyId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());
        candyService.removeCandy(candyId);
    }

    private void removeClientCascade() {
        System.out.println("Input clientId:");
        var clientId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());
        purchaseService.removeByClientId(clientId);
        clientService.removeClient(clientId);
    }

    private void removeClient() {
        System.out.println("Input clientId:");
        var clientId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());
        clientService.removeClient(clientId);
    }

    private void removePurchase() {
        System.out.println("Input purchaseId:");
        var purchaseId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());
        purchaseService.removePurchase(purchaseId);
    }

    /**
     * Saves the given entity.
     *
     * @throws ValidatorException
     *             if the given name is not valid.
     * @throws InputMismatchException
     *             if the given id is not valid.
     */
    private void addCandy() throws ValidatorException, InputMismatchException {
        System.out.println("Input candyID:");
        var candyID = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {
                            scanner.nextLine();
                            throw new InputMismatchException();
                        })
                        .nextLine());
        System.out.println("Input price:");
        var price = Float.parseFloat(
                Optional.of(scanner)
                .filter(Scanner::hasNextFloat)
                .orElseThrow(() -> {
                    scanner.nextLine();
                    throw new InputMismatchException();
                })
                .nextLine());

        System.out.println("Input candy name:");
        String candyName = scanner.nextLine();
        Candy candy = new Candy(candyID, candyName, price);

        candyService.addCandy(candy);
    }

    private void updateCandy() throws InputMismatchException, ValidatorException{
        System.out.println("Input old candyID:");
        var candyID = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {
                            scanner.nextLine();
                            throw new InputMismatchException();
                        })
                        .nextLine());
        System.out.println("Input new price:");
        var price = Float.parseFloat(
                Optional.of(scanner)
                        .filter(Scanner::hasNextFloat)
                        .orElseThrow(() -> {
                            scanner.nextLine();
                            throw new InputMismatchException();
                        })
                        .nextLine());

        System.out.println("Input new candy name:");
        String candyName = scanner.nextLine();
        Candy candy = new Candy(candyID, candyName, price);

        candyService.removeCandy(candyID);
        candyService.addCandy(candy);
    }

    private void printAllStudents() {
        Set<Client> students = clientService.getAllStudents();
        students.forEach(System.out::println);
    }

    private void printAllCandies() {
        Set<Candy> candies = candyService.getAllCandies();
        candies.forEach(System.out::println);
    }

    private void printAllPurchases() {
        Set<Purchase> purchases = purchaseService.getAllPurchases();
        purchases.forEach(System.out::println);
    }

    private void addPurchase() throws ValidatorException, InputMismatchException{
        System.out.println("Input purchaseId:");
        var purchaseId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());

        System.out.println("Input clientId:");
        var clientID = Optional.of(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine())
                .map(Long::valueOf)
                .filter(clientService::findClient)
                .orElseThrow(RepoException::new);

        System.out.println("Input candyId:");
        var candyId = Optional.of(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine())
                .map(Long::valueOf)
                .filter(candyService::findCandy)
                .orElseThrow(RepoException::new);

        System.out.println("Input quantity:");
        var quantity = Integer.parseInt(
                Optional.of(scanner)
                        .filter(Scanner::hasNextFloat)
                        .orElseThrow(() -> {
                            scanner.nextLine();
                            throw new InputMismatchException();
                        })
                        .nextLine());
        Purchase purchase = new Purchase(purchaseId, clientID, candyId, quantity);

        purchaseService.addPurchase(purchase);
    }

    private void updatePurchase() throws ValidatorException, InputMismatchException{
        System.out.println("Input old purchaseId:");
        var purchaseId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());

        System.out.println("Input new clientId:");
        var clientID = Optional.of(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine())
                .map(Long::valueOf)
                .filter(clientService::findClient)
                .orElseThrow(RepoException::new);

        System.out.println("Input new candyId:");
        var candyId = Optional.of(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine())
                .map(Long::valueOf)
                .filter(candyService::findCandy)
                .orElseThrow(RepoException::new);

        System.out.println("Input new quantity:");
        var quantity = Integer.parseInt(
                Optional.of(scanner)
                        .filter(Scanner::hasNextFloat)
                        .orElseThrow(() -> {
                            scanner.nextLine();
                            throw new InputMismatchException();
                        })
                        .nextLine());
        Purchase purchase = new Purchase(purchaseId, clientID, candyId, quantity);

        purchaseService.removePurchase(purchaseId);
        purchaseService.addPurchase(purchase);
    }

    /**
     * Saves the given entity.
     *
     * @throws ValidatorException
     *             if the given name is not valid.
     * @throws InputMismatchException
     *             if the given id is not valid.
     */
    private void addClient() throws ValidatorException, InputMismatchException{
        System.out.println("Input clientId:");
        var clientID = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());

        System.out.println("Input client name:");
        String clientName = scanner.nextLine();
        Client client = new Client(clientID, clientName);

        clientService.addClient(client);
    }

    private void filterClientsByName() {
        System.out.println("Input name:");
        String name = scanner.nextLine();
        var clients = this.clientService.filterByName(name);
        clients.forEach(System.out::println);
    }

}
