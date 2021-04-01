package Repository;

import Domain.Purchase;
import Exceptions.ValidatorException;
import Validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.*;
import java.sql.*;

public class DBPurchaseRepo implements Repository<Long, Purchase> {

    @Autowired
    private JdbcOperations jdbcOperations;

    private final Validator<Purchase> val;
    private final RowMapper<Purchase> purchaseRowMapper = (resultSet, rowNum) ->
            new Purchase(
                    resultSet.getLong("PurchaseId"),
                    resultSet.getLong("ClientId"),
                    resultSet.getLong("CandyId"),
                    resultSet.getInt("Quantity")
            );

    public DBPurchaseRepo(Validator<Purchase> purchaseValidator){
        this.val = purchaseValidator;
    }

    /**
     * Find the entity with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Purchase> findOne(Long id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        String query = "SELECT * FROM \"Purchases\" WHERE \"PurchaseId\" = ?";
        try {
            List<Purchase> clientList = jdbcOperations.query(query, purchaseRowMapper);

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
    public Iterable<Purchase> findAll() {
        try {
            String query = "SELECT * FROM \"Purchases\"";
            return jdbcOperations.query(query, purchaseRowMapper);
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
    public Optional<Purchase> save(Purchase entity) throws ValidatorException, IllegalArgumentException {
        Optional.ofNullable(entity).orElseThrow(IllegalArgumentException::new);
        val.validate(entity);
        var fromDB = this.findOne(entity.getPurchaseID());
        if (fromDB.isPresent()) return fromDB;

        try {
            String query = "insert into \"Purchases\" values(?,?,?,?)";
            jdbcOperations.update(query, entity.getCandyID(), entity.getClientID(), entity.getCandyID(), entity.getQuantity());
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
    public Optional<Purchase> delete(Long id) throws IllegalArgumentException{
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        var fromDB = this.findOne(id);
        if (fromDB.isEmpty()) return fromDB;

        try {
            String query = "DELETE FROM \"Purchases\" WHERE \"PurchaseId\" = ?";
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
    public Optional<Purchase> update(Purchase entity) throws ValidatorException, IllegalArgumentException {
        var res = this.delete(entity.getPurchaseID());
        return Optional.ofNullable(res.orElseGet(() -> this.save(entity).orElse(null)));
    }
}
