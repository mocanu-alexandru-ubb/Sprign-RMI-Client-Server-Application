package Services;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import Domain.Client;
import Exceptions.ValidatorException;

public interface ClientService {
    Future<Void> addClient(Long id, String name) throws ValidatorException;

    Future<Optional<Client>> getOne(Long ID);

    Future<Boolean> findClient(Long ID);
    CompletableFuture<Set<Client>> getAllClients();


    /**
     * Prints information about the candies with a price lower than a given value.
     *
     * @param name
     *          String used to filter all the clients
     *
     * @return a {@code Set} - a set containing all entries that contain a given name.
     */

    Future<Set<Client>> filterByName(String name);


    /**
     * Removes a client based on id
     *
     * @param id
     *          id of entity to be removed
     *
     */
    Future<Void> removeClient(Long id);
}
