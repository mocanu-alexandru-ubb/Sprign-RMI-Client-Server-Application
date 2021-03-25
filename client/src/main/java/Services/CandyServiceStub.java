package Services;

import Domain.Candy;
import Exceptions.StoreException;
import Networking.Message;
import Networking.ParserCandy;
import Networking.TCPClient;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class CandyServiceStub implements CandyService{

    private final ExecutorService executorService;

    public CandyServiceStub(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public Future<Void> addCandy(Long id, String name, Float price) {
        Callable<Void> callable = () -> {
            Message message = new Message("CandyService:addCandy");
            message.addString(id.toString());
            message.addString(name);
            message.addString(price.toString());

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
    public Future<Optional<Candy>> getOne(Long ID) {
        Callable<Optional<Candy>> callable = () -> {
            Message message = new Message("CandyService:getOne");
            message.addString(ID.toString());

            Message res = TCPClient.sendAndReceive(message);

            if(res.getHeader().equals("success")) {
                var parser = new ParserCandy();
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
    public Future<Boolean> findCandy(Long ID) {
        return null;
    }

    @Override
    public Future<Set<Candy>> getAllCandies() {
        Callable<Set<Candy>> callable = () -> {
            Message message = new Message("CandyService:getAllCandies");
            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                var parser = new ParserCandy();
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
    public Future<Set<Candy>> filterByPrice(Float price) {
        Callable<Set<Candy>> callable = () -> {
            Message message = new Message("CandyService:filterByPrice");
            message.addString(price.toString());

            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                var parser = new ParserCandy();
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
    public Future<Void> removeCandy(Long id) {
        Callable<Void> callable = () -> {
            Message message = new Message("CandyService:removeCandy");
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
}
