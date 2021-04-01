package Services;

import Domain.Candy;
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

public class CandyServiceSkeleton implements CandyService{
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Repository<Long, Candy> repository;

    public CandyServiceSkeleton(Repository<Long, Candy> repository) {
        this.repository = repository;
    }

    public void addCandy(Long id, String name, Float price) throws ValidatorException {
        Candy candy = new Candy(id, name, price);
        lock.writeLock().lock();
        repository.findOne(candy.getCandyID())
                .ifPresent((element) -> {lock.writeLock().unlock(); throw new RepoException("id already taken!");});
        repository.save(candy);
        lock.writeLock().unlock();
    }

    public Optional<Candy> getOne(Long ID) {
        lock.readLock().lock();
        var entity = repository.findOne(ID);
        lock.readLock().unlock();
        return entity;
    }

    public boolean findCandy(Long ID) {
        lock.readLock().lock();
        var entity = repository.findOne(ID);
        lock.readLock().unlock();
        return entity.isPresent();
    }

    public Iterable<Candy> getAllCandies() {
        lock.readLock().lock();
        Iterable<Candy> candies = repository.findAll();
        lock.readLock().unlock();
        System.out.println(candies);
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

    public Iterable<Candy> filterByPrice(Float price) {
        lock.readLock().lock();
        Iterable<Candy> candies = repository.findAll();
        lock.readLock().unlock();
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
        lock.writeLock().lock();
        repository.delete(id);
        lock.writeLock().unlock();
    }
}
