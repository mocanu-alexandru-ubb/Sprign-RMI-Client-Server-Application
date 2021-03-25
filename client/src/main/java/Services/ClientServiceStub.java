package Services;

import Domain.Client;
import Exceptions.StoreException;
import Exceptions.ValidatorException;
import Networking.Message;
import Networking.ParserClient;
import Networking.TCPClient;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ClientServiceStub implements ClientService{

    private final ExecutorService executorService;

    public ClientServiceStub(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public Future<Void> addClient(Long id, String name) throws ValidatorException {
        Callable<Void> callable = () -> {
            Message message = new Message("ClientService:addClient");
            message.addString(id.toString());
            message.addString(name);

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
    public Future<Optional<Client>> getOne(Long ID) {
        Callable<Optional<Client>> callable = () -> {
            Message message = new Message("ClientService:getOne");
            message.addString(ID.toString());

            Message res = TCPClient.sendAndReceive(message);

            if(res.getHeader().equals("success")) {
                var parser = new ParserClient();
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
    public Future<Boolean> findClient(Long ID) {
        Callable<Boolean> callable = () -> {
            Message message = new Message("ClientService:findClient");
            message.addString(ID.toString());

            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                return true;
            }
            if (res.getHeader().equals("exception")) {
                throw new StoreException(res.getBody().get(0));
            }
            throw new RuntimeException("invalid response!");
        };
        return executorService.submit(callable);
    }

    @Override
    public Future<Set<Client>> getAllClients() {
        Callable<Set<Client>> callable = () -> {
            Message message = new Message("ClientService:getAllClients");
            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                var parser = new ParserClient();
                Thread.sleep(10000);
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

    /**
     * Prints information about the candies with a price lower than a given value.
     *
     * @param name String used to filter all the clients
     * @return a {@code Set} - a set containing all entries that contain a given name.
     */
    @Override
    public Future<Set<Client>> filterByName(String name) {
        Callable<Set<Client>> callable = () -> {
            Message message = new Message("ClientService:filterByName");
            message.addString(name);

            Message res = TCPClient.sendAndReceive(message);
            if(res.getHeader().equals("success")) {
                var parser = new ParserClient();
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

    /**
     * Removes a client based on id
     *
     * @param id id of entity to be removed
     */
    @Override
    public Future<Void> removeClient(Long id) {
        Callable<Void> callable = () -> {
            Message message = new Message("ClientService:removeClient");
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
