package Store.Repository.DBRepo;

import Store.Domain.Candy;
import Store.Domain.Validator.Validator;
import Store.Domain.Validator.ValidatorException;
import Store.Repository.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.sql.*;

public class DBCandyRepo implements Repository<Long, Candy> {
    private final Connection conn;
    private final Validator<Candy> val;

    public DBCandyRepo(Validator<Candy> clientValidator, String user, String pass, String url) throws SQLException {
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
    public Optional<Candy> findOne(Long id){
        try {
            var stmt = conn.prepareStatement("select * from \"Candies\" where \"CandyId\" = " + id + ";");
            var data = stmt.executeQuery();
            data.next();
            Candy toAdd = new Candy(data.getLong("CandyId"), data.getString("Name"), data.getFloat("Price"));
            return Optional.of(toAdd);
        } catch (SQLException throwables) {
            return Optional.empty();
        }
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Candy> findAll() {
        HashSet<Candy> res = new HashSet<>();
        try {
            var stmt = conn.prepareStatement("select * from \"Candies\"");
            var data = stmt.executeQuery();
            while (data.next()) {
                Candy toAdd = new Candy(data.getLong("CandyId"), data.getString("Name"), data.getFloat("Price"));
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
    public Optional<Candy> save(Candy entity) throws ValidatorException, IllegalArgumentException {
        Optional.ofNullable(entity).orElseThrow(IllegalArgumentException::new);
        val.validate(entity);
        try {
            var stmt = conn.prepareStatement("select * from \"Candies\" where \"CandyId\" = " + entity.getCandyID() + ";");
            var data = stmt.executeQuery();
            //Optional.of(data).filter(ResultSet::next).filter()
            if (data.next())
                return Optional.of(new Candy(data.getLong("CandyId"), data.getString("Name"), data.getFloat("Price")));

            stmt = conn.prepareStatement("insert into \"Candies\" values ("+entity.getCandyID()+",'"+entity.getName()+"'," +entity.getPrice()+");");
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
    public Optional<Candy> delete(Long id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        try {
            var stmt = conn.prepareStatement("select * from \"Candies\" where \"CandyId\" = " + id + ";");
            var data = stmt.executeQuery();
            if (data.next()) {
                Candy toReturn = new Candy(data.getLong("CandyId"), data.getString("Name"), data.getFloat("Price"));
                stmt = conn.prepareStatement("delete from \"Candies\" where \"CandyId\" = " + id +";");
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
    public Optional<Candy> update(Candy entity) throws ValidatorException {
        var res = this.delete(entity.getCandyID());
        return Optional.ofNullable(res.orElseGet(() -> this.save(entity).orElse(null)));
    }
}
