package Services;

import Domain.Candy;
import Exceptions.RepoException;
import Exceptions.ValidatorException;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface CandyService {

    Future<Void> addCandy(Long id, String name, Float price);

    Future<Optional<Candy>> getOne(Long ID);

    Future<Boolean> findCandy(Long ID);

    Future<Set<Candy>> getAllCandies();

    /**
     * Prints information about the candies with a price lower than a given value.
     *
     * @param price
     *          Long used to filter all the candies
     *
     * @return a {@code Set} - a set containing all entries that have a price lower than the given price.
     */
    Future<Set<Candy>> filterByPrice(Float price);

    /**
     * Prints information about the clients with given name.
     *
     * @param id
     *          id of entity to be removed
     *
     */
    Future<Void> removeCandy(Long id);
}
