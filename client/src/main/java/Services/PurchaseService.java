package Services;

import Domain.Candy;
import Exceptions.RepoException;
import Exceptions.ValidatorException;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface PurchaseService {

    Future<Void> addPurchase(Long purchaseID, Long clientID, Long candyID, Integer quantity) throws ValidatorException;

    Future<Set<Domain.Purchase>> getAllPurchases();

    Future<Void> removePurchase(Long id);

    /**
     * Gets information about the purchases of a given client.
     *
     * @param id
     *          id of the client
     *
     * @return a {@code Set} - a set containing all purchases of a given client.
     */
    Future<Set<Domain.Purchase>> getAllByClient(Long id);

    Future<Void> removeByClientId(Long id);

    /**
     * Gets information about the purchases of a given candy.
     *
     * @param id
     *          id of the candy
     * @return a {@code Set} - a set containing all the purchases of a given candy.
     */
    Future<Set<Domain.Purchase>> getAllByCandy(Long id);

    Future<Void> removeByCandyId(Long id);

    /**
     * Find the entity with the given {@code id}.
     *
     * @param id
     *            must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException
     *             if the given id is null.
     */
    Future<Optional<Domain.Purchase>> getOne(Long id);

    Future<String> computePrice(Long purchaseId);
}
