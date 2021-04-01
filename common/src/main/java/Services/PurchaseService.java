package Services;

import Domain.Purchase;
import Exceptions.ValidatorException;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

public interface PurchaseService {

    void addPurchase(Long purchaseID, Long clientID, Long candyID, Integer quantity) throws ValidatorException;

    Iterable<Purchase> getAllPurchases();

    void removePurchase(Long id);

    /**
     * Gets information about the purchases of a given client.
     *
     * @param id
     *          id of the client
     *
     * @return a {@code Set} - a set containing all purchases of a given client.
     */
    Iterable<Purchase> getAllByClient(Long id);

    void removeByClientId(Long id);

    /**
     * Gets information about the purchases of a given candy.
     *
     * @param id
     *          id of the candy
     * @return a {@code Set} - a set containing all the purchases of a given candy.
     */
    Iterable<Purchase> getAllByCandy(Long id);

    void removeByCandyId(Long id);

    /**
     * Find the entity with the given {@code id}.
     *
     * @param id
     *            must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException
     *             if the given id is null.
     */
    Optional<Purchase> getOne(Long id);

    String computePrice(Long purchaseId);
}
