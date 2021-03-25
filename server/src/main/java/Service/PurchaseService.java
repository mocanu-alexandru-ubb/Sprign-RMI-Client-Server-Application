package Service;

import Domain.Candy;
import Domain.Purchase;
import Exceptions.RepoException;
import Exceptions.ValidatorException;
import Repository.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/*
 * @author Mocanu Alexandru.
 * */

public class PurchaseService {
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Repository<Long, Purchase> repository;
    private final Repository<Long, Candy> candyRepository;

    public PurchaseService(Repository<Long, Purchase> repository, Repository<Long, Candy> candyRepository) {
        this.repository = repository;
        this.candyRepository = candyRepository;
    }

    public void addPurchase(Long purchaseId, Long clientId, Long candyId, int quantity) throws ValidatorException {
        var purchase = new Purchase(purchaseId, clientId, candyId, quantity);
        lock.writeLock().lock();
        repository.findOne(purchase.getPurchaseID())
                .ifPresent((element) -> {lock.writeLock().unlock(); throw new RepoException("id already taken!");});
        repository.save(purchase);
        lock.writeLock().unlock();
    }

    public Set<Purchase> getAllPurchases() {
        lock.readLock().lock();
        Iterable<Purchase> purchases = repository.findAll();
        lock.readLock().unlock();
        return StreamSupport.stream(purchases.spliterator(), false).collect(Collectors.toSet());
    }

    public void removePurchase(Long id) {
        lock.writeLock().lock();
        repository.delete(id).orElseThrow(() -> {
            lock.writeLock().unlock();
            throw new RepoException("no element to remove");
        });
        lock.writeLock().unlock();
    }

    /**
     * Gets information about the purchases of a given client.
     *
     * @param id
     *          id of the client
     *
     * @return a {@code Set} - a set containing all purchases of a given client.
     */
    public Set<Purchase> getAllByClient(Long id) {
        lock.readLock().lock();
        Iterable<Purchase> purchases = repository.findAll();
        lock.writeLock().unlock();
        return StreamSupport
                .stream(purchases.spliterator(), false)
                .filter((c) -> c.getClientID().equals(id))
                .collect(Collectors.toSet());
    }

    public void removeByClientId(Long id) {
        lock.writeLock().lock();
        var toDelete = this.getAllByClient(id);
        System.out.println(toDelete);
        toDelete.forEach((el) -> this.removePurchase(el.getPurchaseID()));
        lock.writeLock().unlock();
    }

    /**
     * Gets information about the purchases of a given candy.
     *
     * @param id
     *          id of the candy
     * @return a {@code Set} - a set containing all the purchases of a given candy.
     */
    public Set<Purchase> getAllByCandy(Long id) {
        lock.readLock().lock();
        Iterable<Purchase> purchases = repository.findAll();
        lock.readLock().unlock();
        return StreamSupport
                .stream(purchases.spliterator(), false)
                .filter((c) -> c.getCandyID().equals(id))
                .collect(Collectors.toSet());
    }

    public void removeByCandyId(Long id) {
        lock.readLock().lock();
        var toDelete = getAllByCandy(id);
        lock.readLock().unlock();
        toDelete.forEach((el) -> removePurchase(el.getPurchaseID()));
    }

    /**
     * Find the entity with the given {@code id}.
     *
     * @param id
     *            must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException
     *             if the given id is null.
     */
    public Optional<Purchase> getOne(Long id) {
        lock.readLock().lock();
        var entity = repository.findOne(id);
        lock.readLock().unlock();
        return entity;
    }

    public String computePrice(Long purchaseId) {
        lock.writeLock().lock();
        var purchase = this.repository.findOne(purchaseId);
        if (purchase.isEmpty()) {
            lock.writeLock().unlock();
            return "no such purchase";
        }
        var bought = this.candyRepository.findOne(purchase.get().getCandyID()).get();
        lock.writeLock().unlock();
        return purchase.map(p -> p.getPurchaseID() + ": " + p.getQuantity() * bought.getPrice()).orElse("");
    }
}
