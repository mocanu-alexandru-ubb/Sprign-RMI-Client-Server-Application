package Store.Repository.DBRepo;

import Store.Domain.Purchase;
import Store.Domain.Validator.Validator;
import Store.Domain.Validator.ValidatorException;
import Store.Repository.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.sql.*;

public class DBPurchaseRepo implements Repository<Long, Purchase> {
    private final Connection conn;
    private final Validator<Purchase> val;

    public DBPurchaseRepo(Validator<Purchase> clientValidator, String user, String pass, String url) throws SQLException {
        this.val = clientValidator;
        this.conn = DriverManager.getConnection(url, user, pass);
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
        try {
            var stmt = conn.prepareStatement("select * from \"Purchases\" where \"PurchaseId\" = " + id + ";");
            var data = stmt.executeQuery();
            data.next();
            Purchase toAdd = new Purchase(data.getLong(1), data.getLong(2), data.getLong(3), data.getInt(4));
            return Optional.of(toAdd);
        } catch (SQLException throwables) {
            return Optional.empty();
        }
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Purchase> findAll() {
        HashSet<Purchase> res = new HashSet<>();
        try {
            var stmt = conn.prepareStatement("select * from \"Purchases\"");
            var data = stmt.executeQuery();
            while (data.next()) {
                Purchase toAdd = new Purchase(data.getLong(1), data.getLong(2), data.getLong(3), data.getInt(4));
                res.add(toAdd);
            }
        } catch (SQLException throwables) {
            System.out.println("something went wrong with the db");
            throwables.printStackTrace();
            return Collections.emptySet();
        }

        return res;
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
        try {
            var stmt = conn.prepareStatement("select * from \"Purchases\" where \"PurchaseId\" = " + entity.getPurchaseID() + ";");
            var data = stmt.executeQuery();
            //Optional.of(data).filter(ResultSet::next).filter()
            if (data.next())
                return Optional.of( new Purchase(data.getLong(1), data.getLong(2), data.getLong(3), data.getInt(4)));

            stmt = conn.prepareStatement("insert into \"Purchases\" values ("+entity.getPurchaseID()+","+entity.getClientID()+","+entity.getCandyID()+","+entity.getQuantity()+");");
            stmt.execute();
            return Optional.empty();
        }
        catch (SQLException e) {
            System.out.println("something went wrong with the db");
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
    public Optional<Purchase> delete(Long id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        try {
            var stmt = conn.prepareStatement("select * from \"Purchases\" where \"PurchaseId\" = " + id + ";");
            var data = stmt.executeQuery();
            if (data.next()) {
                Purchase toReturn = new Purchase(data.getLong(1), data.getLong(2), data.getLong(3), data.getInt(4));
                stmt = conn.prepareStatement("delete from \"Purchases\" where \"PurchaseId\" = " + id +";");
                stmt.execute();
                return Optional.of(toReturn);
            }
            return Optional.empty();
        }
        catch (SQLException e) {
            System.out.println("something went wrong with the db");
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
    public Optional<Purchase> update(Purchase entity) throws ValidatorException {
        var res = this.delete(entity.getCandyID());
        return Optional.ofNullable(res.orElseGet(() -> this.save(entity).orElse(null)));
    }
}
