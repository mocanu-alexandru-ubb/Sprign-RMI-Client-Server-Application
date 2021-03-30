package Repository;

import Domain.Candy;
import Exceptions.ValidatorException;
import Validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.*;
import java.sql.*;

public class DBCandyRepo implements Repository<Long, Candy> {

    @Autowired
    private JdbcOperations jdbcOperations;

    private final Validator<Candy> val;
    private final RowMapper<Candy> candyRowMapper = (resultSet, rowNum) ->
            new Candy(
                    resultSet.getLong("CandyId"),
                    resultSet.getString("Name"),
                    resultSet.getFloat("Price")
            );

    public DBCandyRepo(Validator<Candy> candyValidator){
        this.val = candyValidator;
    }

    /**
     * Find the entity with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Candy> findOne(Long id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        String query = "SELECT * FROM \"Candies\" WHERE \"CandyId\" = ?";
        try {
            List<Candy> clientList = jdbcOperations.query(query, candyRowMapper);

            if (clientList.size() != 1) return Optional.empty();
            return Optional.of(clientList.get(0));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Candy> findAll() {
        try {
            String query = "SELECT * FROM \"Candies\"";
            return jdbcOperations.query(query, candyRowMapper);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Saves the given entity.
     *
     * @param entity must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Candy> save(Candy entity) throws ValidatorException, IllegalArgumentException {
        Optional.ofNullable(entity).orElseThrow(IllegalArgumentException::new);
        val.validate(entity);
        var fromDB = this.findOne(entity.getCandyID());
        if (fromDB.isPresent()) return fromDB;

        try {
            String query = "insert into \"Candies\" values(?,?,?)";
            jdbcOperations.update(query, entity.getCandyID(), entity.getName(), entity.getPrice());
            return Optional.empty();
        } catch (DataAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Removes the entity with the given id.
     *
     * @param id must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Candy> delete(Long id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        var fromDB = this.findOne(id);
        if (fromDB.isEmpty()) return fromDB;

        try {
            String query = "DELETE FROM \"Candies\" WHERE \"CandyId\" = ?";
            jdbcOperations.update(query, id);
            return fromDB;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Updates the given entity.
     *
     * @param entity must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     * entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Candy> update(Candy entity) throws ValidatorException {
        var res = this.delete(entity.getCandyID());
        return Optional.ofNullable(res.orElseGet(() -> this.save(entity).orElse(null)));
    }
}
