package Service;

import Domain.Candy;
import Exceptions.RepoException;
import Exceptions.ValidatorException;
import Repository.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/*
 * @author Mocanu Alexandru.
 * */

public class CandyService {
    private final Repository<Long, Candy> repository;

    public CandyService(Repository<Long, Candy> repository) {
        this.repository = repository;
    }

    public void addCandy(Candy candy) throws ValidatorException {
        repository.findOne(candy.getCandyID())
                .ifPresent((element) -> {throw new RepoException("id already taken!");});
        repository.save(candy);
    }

    public Optional<Candy> getOne(Long ID) {
        return repository.findOne(ID);
    }

    public boolean findCandy(Long ID) {
        return repository.findOne(ID).isPresent();
    }

    public Set<Candy> getAllCandies() {
        Iterable<Candy> candies = repository.findAll();
        return StreamSupport.stream(candies.spliterator(), false).collect(Collectors.toSet());
    }


    /**
     * Prints information about the candies with a price lower than a given value.
     *
     * @param price
     *          Long used to filter all the candies
     *
     * @return a {@code Set} - a set containing all entries that have a price lower than the given price.
     */

    public Set<Candy> filterByPrice(float price) {
        Iterable<Candy> candies = repository.findAll();
        return StreamSupport
                .stream(candies.spliterator(), false)
                .filter(c -> c.getPrice() < price)
                .collect(Collectors.toSet());
    }


    /**
     * Prints information about the clients with given name.
     *
     * @param id
     *          id of entity to be removed
     *
     */
    public void removeCandy(Long id) {
        repository.delete(id);
    }
}
