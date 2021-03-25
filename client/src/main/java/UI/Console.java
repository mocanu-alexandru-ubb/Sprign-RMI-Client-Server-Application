package UI;

import Domain.Candy;
import Domain.Client;
import Domain.Purchase;
import Exceptions.RepoException;
import Exceptions.ValidatorException;
import Services.CandyService;
import Services.ClientService;
import Services.PurchaseService;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Console {
    private final Scanner scanner = new Scanner(System.in);
    private final ClientService clientService;
    private final HashMap<String, Runnable> cmds = new HashMap<>();
    private final CandyService candyService;
    private final PurchaseService purchaseService;
    public ResponseBuffer responseBuffer = new ResponseBuffer();
    public Timer timer = new Timer();

    public Console(ClientService clientService, CandyService candyService, PurchaseService purchaseService) {
        this.clientService = clientService;
        this.candyService = candyService;
        this.purchaseService = purchaseService;
        timer.scheduleAtFixedRate(new ResponseDaemon(responseBuffer),10 * 1000, 10 * 1000);
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
        cmds.put("help", () -> System.out.println(cmds.keySet()));
    }

    public void runConsole() {
        Stream.generate(() -> {System.out.print(">>>"); return scanner.nextLine();})
                .takeWhile((str) -> !str.equals("exit"))
                .forEach((str) -> {
                    try { cmds.getOrDefault(str, this::noFunction).run();
                    } catch (InputMismatchException e) {System.out.println(e.getMessage());}
                });
        timer.cancel();
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

        responseBuffer.add(new FutureResponse<>(clientService.removeClient(clientID),
                new ResponseMapper<>(response -> "deleted")));
        responseBuffer.add(new FutureResponse<>(clientService.addClient(clientID, clientName),
                new ResponseMapper<>(response -> "added")));

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
        responseBuffer.add(new FutureResponse<>(candyService.filterByPrice(price),
                new ResponseMapper<>(response -> String.format("Candies with price > %f", price))));
    }

    private void removeCandyCascade() {
        System.out.println("Input candy id:");
        var candyId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());

        responseBuffer.add(new FutureResponse<>(purchaseService.removeByCandyId(candyId),
                new ResponseMapper<>(response -> "removed purchase")));
        responseBuffer.add(new FutureResponse<>(candyService.removeCandy(candyId),
                new ResponseMapper<>(response -> "removed candy")));
    }

    private void removeCandy() {
        System.out.println("Input candy id:");
        var candyId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());

        responseBuffer.add(new FutureResponse<>(candyService.removeCandy(candyId),
                new ResponseMapper<>(response -> "removed candy")));
    }

    private void removeClientCascade() {
        System.out.println("Input clientId:");
        var clientId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());

        responseBuffer.add(new FutureResponse<>(purchaseService.removeByClientId(clientId),
                new ResponseMapper<>(response -> "removed client")));
        responseBuffer.add(new FutureResponse<>(clientService.removeClient(clientId),
                new ResponseMapper<>(response -> "removed client")));
    }

    private void removeClient() {
        System.out.println("Input clientId:");
        var clientId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());

        responseBuffer.add(new FutureResponse<>(clientService.removeClient(clientId),
                new ResponseMapper<>(response -> "removed client")));
    }

    private void removePurchase() {
        System.out.println("Input purchaseId:");
        var purchaseId = Long.valueOf(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine());

        responseBuffer.add(new FutureResponse<>(purchaseService.removePurchase(purchaseId),
                new ResponseMapper<>(response -> "removed purchase")));
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

        responseBuffer.add(new FutureResponse<>(candyService.addCandy(candyID,candyName,price),
                new ResponseMapper<>(response -> "added candy")));
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


        responseBuffer.add(new FutureResponse<>(candyService.removeCandy(candyID),
                new ResponseMapper<>(response -> "candy removed")));
        responseBuffer.add(new FutureResponse<>(candyService.addCandy(candyID,candyName,price),
                new ResponseMapper<>(response -> "candy added")));
    }

    private void printAllStudents() {
        Set<Client> students = null;
        try {
            students = clientService.getAllClients().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(students != null) {
            responseBuffer.add(
                    new FutureResponse<>(
                            clientService.getAllClients(),
                            new ResponseMapper<>(response -> {
                                System.out.println("started mapping");
                                if (!response.iterator().hasNext()) {
                                    return "No clients found!";
                                }
                                return "List of clients\n" +
                                        response.stream()
                                                .map(client -> String.format("%d %s", client.getId(), client.getName()))
                                                .collect(Collectors.joining("\n", "", "\n"));
                            })
                    )
            );
        }
    }

    private void printAllCandies() {
        Set<Candy> candies = null;
        try {
            candies = candyService.getAllCandies().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(candies != null) {
            candies.forEach(System.out::println);
        }
    }

    private void printAllPurchases() {
        Set<Purchase> purchases = null;
        try {
            purchases = purchaseService.getAllPurchases().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (purchases != null) {
            purchases.forEach(System.out::println);
        }
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
                .orElseThrow(RepoException::new);

        System.out.println("Input candyId:");
        var candyId = Optional.of(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine())
                .map(Long::valueOf)
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

        responseBuffer.add(new FutureResponse<>(purchaseService.addPurchase(purchaseId,clientID,candyId,quantity),
                new ResponseMapper<>(response -> "added purchase")));
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
                .orElseThrow(RepoException::new);

        System.out.println("Input new candyId:");
        var candyId = Optional.of(
                Optional.of(scanner)
                        .filter(Scanner::hasNextLong)
                        .orElseThrow(() -> {scanner.nextLine(); throw new InputMismatchException();})
                        .nextLine())
                .map(Long::valueOf)
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


        responseBuffer.add(new FutureResponse<>(purchaseService.removePurchase(purchaseId),
                new ResponseMapper<>(response -> "removed purchase")));
        responseBuffer.add(new FutureResponse<>(purchaseService.addPurchase(purchaseId,clientID,candyId,quantity),
                new ResponseMapper<>(response -> "added purchase")));
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

        responseBuffer.add(new FutureResponse<>(clientService.addClient(clientID, clientName),
                new ResponseMapper<>(response -> "added client")));
    }

    private void filterClientsByName() {
        System.out.println("Input name:");
        String name = scanner.nextLine();
        Set<Client> clients = null;
        try {
            clients = this.clientService.filterByName(name).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(clients != null) {
            clients.forEach(System.out::println);
        }
    }

}
