package Services;

import Domain.Purchase;
import Exceptions.StoreException;
import Exceptions.ValidatorException;
import Networking.Message;
import Networking.ParserPurchase;
import Networking.TCPClient;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class PurchaseServerStub implements PurchaseService{

    private final ExecutorService executorService;

    public PurchaseServerStub(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public Future<Void> addPurchase(Long purchaseID, Long clientID, Long candyID, Integer quantity) throws ValidatorException {
        Callable<Void> callable = () -> {
            Message message = new Message("PurchaseService:addPurchase");
            message.addString(purchaseID.toString());
            message.addString(clientID.toString());
            message.addString(candyID.toString());
            message.addString(quantity.toString());

            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                return null;
            }
            if (res.getHeader().equals("exception")) {
                throw new StoreException(res.getBody().get(0));
            }
            throw new RuntimeException("invalid response!");
        };
        return executorService.submit(callable);
    }

    @Override
    public Future<Set<Purchase>> getAllPurchases() {
        Callable<Set<Purchase>> callable = () -> {
            Message message = new Message("PurchaseService:getAllPurchases");
            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                var parser = new ParserPurchase();
                return res.getBody().stream()
                        .map(parser::decode)
                        .collect(Collectors.toUnmodifiableSet());
            }
            if (res.getHeader().equals("exception")) {
                throw new StoreException(res.getBody().get(0));
            }
            throw new RuntimeException("invalid response!");
        };
        return executorService.submit(callable);
    }

    @Override
    public Future<Void> removePurchase(Long id) {
        Callable<Void> callable = () -> {
            Message message = new Message("PurchaseService:removePurchase");
            message.addString(id.toString());

            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                return null;
            }
            if (res.getHeader().equals("exception")) {
                throw new StoreException(res.getBody().get(0));
            }
            throw new RuntimeException("invalid response!");
        };
        return executorService.submit(callable);
    }

    @Override
    public Future<Set<Purchase>> getAllByClient(Long id) {
        Callable<Set<Purchase>> callable = () -> {
            Message message = new Message("PurchaseService:getAllByClient");
            message.addString(id.toString());

            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                var parser = new ParserPurchase();
                return res.getBody().stream()
                        .map(parser::decode)
                        .collect(Collectors.toUnmodifiableSet());
            }
            if (res.getHeader().equals("exception")) {
                throw new StoreException(res.getBody().get(0));
            }
            throw new RuntimeException("invalid response!");
        };
        return executorService.submit(callable);
    }

    @Override
    public Future<Void> removeByClientId(Long id) {
        Callable<Void> callable = () -> {
            Message message = new Message("PurchaseService:removeByClientId");
            message.addString(id.toString());

            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                return null;
            }
            if (res.getHeader().equals("exception")) {
                throw new StoreException(res.getBody().get(0));
            }
            throw new RuntimeException("invalid response!");
        };
        return executorService.submit(callable);
    }

    @Override
    public Future<Set<Purchase>> getAllByCandy(Long id) {
        Callable<Set<Purchase>> callable = () -> {
            Message message = new Message("PurchaseService:getAllByCandy");
            message.addString(id.toString());

            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                var parser = new ParserPurchase();
                return res.getBody().stream()
                        .map(parser::decode)
                        .collect(Collectors.toUnmodifiableSet());
            }
            if (res.getHeader().equals("exception")) {
                throw new StoreException(res.getBody().get(0));
            }
            throw new RuntimeException("invalid response!");
        };
        return executorService.submit(callable);
    }

    @Override
    public Future<Void> removeByCandyId(Long id) {
        Callable<Void> callable = () -> {
            Message message = new Message("PurchaseService:removeByCandyId");
            message.addString(id.toString());
            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                return null;
            }
            if (res.getHeader().equals("exception")) {
                throw new StoreException(res.getBody().get(0));
            }
            throw new RuntimeException("invalid response!");
        };
        return executorService.submit(callable);
    }

    @Override
    public Future<Optional<Purchase>> getOne(Long id) {
        Callable<Optional<Purchase>> callable = () -> {
            Message message = new Message("PurchaseService:getOne");
            message.addString(id.toString());
            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                var parser = new ParserPurchase();
                return Optional.of(parser.decode(res.getBody().get(0)));
            }
            if (res.getHeader().equals("exception")) {
                throw new StoreException(res.getBody().get(0));
            }
            throw new RuntimeException("invalid response!");
        };
        return executorService.submit(callable);
    }

    @Override
    public Future<String> computePrice(Long purchaseId) {
        Callable<String> callable = () -> {
            Message message = new Message("PurchaseService:computePrice");
            message.addString(purchaseId.toString());

            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                return res.getBody().get(0);
            }
            if (res.getHeader().equals("exception")) {
                throw new StoreException(res.getBody().get(0));
            }
            throw new RuntimeException("invalid response!");
        };
        return executorService.submit(callable);
    }
}
