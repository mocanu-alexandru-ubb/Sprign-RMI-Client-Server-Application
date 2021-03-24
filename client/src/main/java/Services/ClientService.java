package Services;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import Domain.Client;
import Exceptions.ValidatorException;

public interface ClientService {
    public Future<Void> addClient(Long id) throws ValidatorException;

    public Future<Optional<Client>> getOne(Long ID);

    public boolean findClient(Long ID);

    public Future<Set<Client>> getAllClients();


    /**
     * Prints information about the candies with a price lower than a given value.
     *
     * @param name
     *          String used to filter all the clients
     *
     * @return a {@code Set} - a set containing all entries that contain a given name.
     */

    public Future<Set<Client>> filterByName(String name);


    /**
     * Removes a client based on id
     *
     * @param id
     *          id of entity to be removed
     *
     */
    public void removeClient(Long id);
}
