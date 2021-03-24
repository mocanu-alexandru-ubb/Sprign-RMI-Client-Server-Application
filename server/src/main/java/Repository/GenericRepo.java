package Repository;

import Domain.BaseEntity;
import Exceptions.ValidatorException;
import Validator.Validator;

import java.util.*;

public class GenericRepo<ID, T extends BaseEntity<ID>> implements Repository<ID, T> {

    private final Map<ID, T> entities;
    private final Validator<T> validator;

    public GenericRepo(Validator<T> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    /**
     * Find entity with the given {@code id}.
     * @param id
     *            must be not null.
     * @return {@code Optional} of the entity with the given id,
     * @throws IllegalArgumentException
     *          if the given id is null
     */
    @Override
    public Optional<T> findOne(ID id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        return (Optional.ofNullable(entities.get(id)));
    }

    /**
     *
     * @return all entities.
     */
    @Override
    public Iterable<T> findAll() {
        return new HashSet<>(entities.values());
    }

    /**
     * Saves a given entity
     * @param entity
     *            must not be null.
     *
     * @return Optional describing the specified value if entity is already saved,
     *         otherwise empty Optional
     * @throws ValidatorException if the id is null
     */
    @Override
    public Optional<T> save(T entity) throws ValidatorException {
        Optional.ofNullable(entity).orElseThrow(IllegalArgumentException::new);
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }


    /**
     * Removes the entity with the given id
     * @param id
     *            must not be null.
     * @return  an {@code Optional} empty if there is no entity with the given id,
     *                              otherwise the remove entity
     * @throws IllegalArgumentException
     * if the id is null
     */
    @Override
    public Optional<T> delete(ID id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        return Optional.ofNullable(entities.remove(id));
    }

    /**
     * Updates the given entity
     *
     * @param entity
     *            must not be null.
     * @return an {@code Optional} empty if the entity was updated,
     *                             otherwise entity
     * @throws ValidatorException
     * if the entity is not valid
     * @throws IllegalArgumentException
     * if the entity is null
     */
    @Override
    public Optional<T> update(T entity) throws ValidatorException {
        Optional.ofNullable(entity).orElseThrow(IllegalArgumentException::new);
        validator.validate(entity);
        return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (k, v) -> entity));
    }
}