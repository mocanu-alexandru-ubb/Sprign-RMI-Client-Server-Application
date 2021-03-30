package Services;

import Domain.Client;
import Exceptions.RepoException;
import Exceptions.ValidatorException;
import Repository.Repository;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/*
* @author Mocanu Alexandru.
* */

public class ClientServiceSkeleton implements ClientService{
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Repository<Long, Client> repository;

    public ClientServiceSkeleton(Repository<Long, Client> repository) {
        this.repository = repository;
    }

    public boolean findClient(Long ID) {
        lock.readLock().lock();
        var entity = repository.findOne(ID);
        lock.readLock().unlock();
        return entity.isPresent();
    }

    public void addClient(Long id, String name) throws ValidatorException {
        Client student = new Client(id, name);
        lock.writeLock().lock();
        repository.findOne(student.getClientID())
                .ifPresent((element) -> {lock.writeLock().unlock(); throw new RepoException("id already taken!");});
        repository.save(student);
        lock.writeLock().unlock();
    }

    @Override
    public Optional<Client> getOne(Long ID) {
        lock.readLock().lock();
        var entity = repository.findOne(ID);
        lock.readLock().unlock();
        return entity;
    }

    public Iterable<Client> getAllClients() {
        lock.readLock().lock();
        Iterable<Client> clients = repository.findAll();
        lock.readLock().unlock();
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
    public Iterable<Client> filterByName(String name) {
        lock.readLock().lock();
        Iterable<Client> clients = repository.findAll();
        lock.readLock().unlock();
        return StreamSupport
                .stream(clients.spliterator(), false)
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toSet());
    }

    public void removeClient(Long id) {
        lock.writeLock().lock();
        repository.delete(id);
        lock.writeLock().unlock();
    }

    @Override
    public void updateClient(Long id, String name) {
        this.removeClient(id);
        this.addClient(id, name);
    }
}
