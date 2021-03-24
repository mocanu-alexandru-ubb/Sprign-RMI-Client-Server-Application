package Services;

import Domain.Candy;
import Domain.Client;
import Networking.Message;
import Networking.TCPClient;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CandyServiceStub implements CandyService{

    private ExecutorService executorService;

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
                return null;
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
                return null;
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
                return null;
            }
            throw new RuntimeException("invalid response!");
        };
        return executorService.submit(callable);
    }

    @Override
    public Future<Void> removeCandy(Long id) {
        return null;
    }
}
