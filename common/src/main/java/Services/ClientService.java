package Services;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import Domain.Client;
import Exceptions.ValidatorException;

public interface ClientService {
    void addClient(Long id, String name) throws ValidatorException;

    Optional<Client> getOne(Long ID);

    boolean findClient(Long ID);

    Iterable<Client> getAllClients();


    /**
     * Prints information about the candies with a price lower than a given value.
     *
     * @param name
     *          String used to filter all the clients
     *
     * @return a {@code Set} - a set containing all entries that contain a given name.
     */

    Iterable<Client> filterByName(String name);


    /**
     * Removes a client based on id
     *
     * @param id
     *          id of entity to be removed
     *
     */
    void removeClient(Long id);

    void updateClient(Long id, String name);
}
