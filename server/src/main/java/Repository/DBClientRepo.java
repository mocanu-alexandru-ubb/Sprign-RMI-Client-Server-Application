package Repository;

import Domain.Client;
import Exceptions.ValidatorException;
import Validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.*;
import java.sql.*;

public class DBClientRepo implements Repository<Long, Client> {

    @Autowired
    private JdbcOperations jdbcOperations;

    private final Validator<Client> val;
    private final RowMapper<Client> clientRowMapper = (resultSet, rowNum) ->
            new Client(
                    resultSet.getLong("ClientId"),
                    resultSet.getString("Name")
            );

    public DBClientRepo(Validator<Client> clientValidator) {
        this.val = clientValidator;
    }

    /**
     * Find the entity with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Client> findOne(Long id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        String query = "SELECT * FROM \"Clients\" WHERE \"ClientId\" = ?";
        try {
            List<Client> clientList = jdbcOperations.query(query, clientRowMapper);

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
    public Iterable<Client> findAll() {
        try {
            String query = "SELECT * FROM \"Clients\"";
            return jdbcOperations.query(query, clientRowMapper);
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
    public Optional<Client> save(Client entity) throws ValidatorException, IllegalArgumentException {
        Optional.ofNullable(entity).orElseThrow(IllegalArgumentException::new);
        val.validate(entity);
        var fromDB = this.findOne(entity.getClientID());
        if (fromDB.isPresent()) return fromDB;

        try {
            String query = "insert into \"Clients\" values(?,?)";
            jdbcOperations.update(query, entity.getId(), entity.getName());
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
    public Optional<Client> delete(Long id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        var fromDB = this.findOne(id);
        if (fromDB.isEmpty()) return fromDB;

        try {
            String query = "DELETE FROM \"Clients\" WHERE \"ClientId\" = ?";
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
    public Optional<Client> update(Client entity) throws ValidatorException {
        var res = this.delete(entity.getClientID());
        return Optional.ofNullable(res.orElseGet(() -> this.save(entity).orElse(null)));
    }
}
