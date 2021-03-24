package Services;

import Domain.Client;
import Exceptions.ValidatorException;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

public class ClientServiceStub implements ClientService{
    @Override
    public Future<Void> addClient(Long id) throws ValidatorException {
        return null;
    }

    @Override
    public Future<Optional<Client>> getOne(Long ID) {
        return null;
    }

    @Override
    public boolean findClient(Long ID) {
        return false;
    }

    @Override
    public Future<Set<Client>> getAllClients() {
        return null;
    }

    /**
     * Prints information about the candies with a price lower than a given value.
     *
     * @param name String used to filter all the clients
     * @return a {@code Set} - a set containing all entries that contain a given name.
     */
    @Override
    public Future<Set<Client>> filterByName(String name) {
        return null;
    }

    /**
     * Removes a client based on id
     *
     * @param id id of entity to be removed
     */
    @Override
    public void removeClient(Long id) {

    }
}
