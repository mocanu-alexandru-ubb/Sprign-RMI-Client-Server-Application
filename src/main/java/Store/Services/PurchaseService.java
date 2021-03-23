package Store.Services;

import Store.Domain.Candy;
import Store.Domain.Client;
import Store.Domain.Purchase;
import Store.Domain.Validator.RepoException;
import Store.Domain.Validator.ValidatorException;
import Store.Repository.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/*
 * @author Mocanu Alexandru.
 * */

public class PurchaseService {
    private final Repository<Long, Purchase> repository;

    public PurchaseService(Repository<Long, Purchase> repository) {
        this.repository = repository;
    }

    public void addPurchase(Purchase purchase) throws ValidatorException {
        repository.findOne(purchase.getPurchaseID())
                .ifPresent((element) -> {throw new RepoException("id already taken!");});
        repository.save(purchase);
    }

    public Set<Purchase> getAllPurchases() {
        Iterable<Purchase> purchases = repository.findAll();
        return StreamSupport.stream(purchases.spliterator(), false).collect(Collectors.toSet());
    }

    public void removePurchase(Long id) {
        repository.delete(id).orElseThrow(RepoException::new);
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
        Iterable<Purchase> purchases = repository.findAll();
        return StreamSupport
                .stream(purchases.spliterator(), false)
                .filter((c) -> c.getClientID().equals(id))
                .collect(Collectors.toSet());
    }

    public void removeByClientId(Long id) {
        var toDelete = this.getAllByClient(id);
        System.out.println(toDelete);
        toDelete.forEach((el) -> this.removePurchase(el.getPurchaseID()));
    }

    /**
     * Gets information about the purchases of a given candy.
     *
     * @param id
     *          id of the candy
     * @return a {@code Set} - a set containing all the purchases of a given candy.
     */
    public Set<Purchase> getAllByCandy(Long id) {
        Iterable<Purchase> purchases = repository.findAll();
        return StreamSupport
                .stream(purchases.spliterator(), false)
                .filter((c) -> c.getCandyID().equals(id))
                .collect(Collectors.toSet());
    }

    public void removeByCandyId(Long id) {
        var toDelete = getAllByCandy(id);
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
        return this.repository.findOne(id);
    }

    public String computePrice(Long purchaseId, Candy bought) {
        var purhcase = this.repository.findOne(purchaseId);
        return purhcase.map(p -> p.getPurchaseID() + ": " + p.getQuantity()*bought.getPrice()).orElse("");
    }
}
