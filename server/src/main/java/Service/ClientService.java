package Service;

import Domain.Client;
import Exceptions.RepoException;
import Exceptions.ValidatorException;
import Repository.Repository;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/*
* @author Mocanu Alexandru.
* */

public class ClientService {
    private final Repository<Long, Client> repository;

    public ClientService(Repository<Long, Client> repository) {
        this.repository = repository;
    }

    public boolean findClient(Long ID) {
        return repository.findOne(ID).isPresent();
    }

    public void addClient(Long id, String name) throws ValidatorException {
        Client student = new Client(id, name);
        repository.findOne(student.getClientID())
                .ifPresent((element) -> {throw new RepoException("id already taken!");});
        repository.save(student);
    }

    public Iterable<Client> getAll() {
        Iterable<Client> clients = repository.findAll();
        return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Prints information about the clients with given name.
     *
     * @param name
     *          string to match in names
     *
     * @return a {@code Set} - a set containing all entries that have the given string in their name.
     */
    public Set<Client> filterByName(String name) {
        Iterable<Client> clients = repository.findAll();
        return StreamSupport
                .stream(clients.spliterator(), false)
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toSet());
    }

    public void removeClient(Long id) {
        repository.delete(id);
    }
}
