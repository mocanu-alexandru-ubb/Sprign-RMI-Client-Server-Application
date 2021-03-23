package Store.Repository;

import Store.Domain.BaseEntity;
import Store.Domain.Client;
import Store.Domain.Validator.Validator;
import Store.Domain.Validator.ValidatorException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Miron Mihnea
 */

public class GenericFileRepo<T extends BaseEntity<Long>> extends ClientRepo<Long, T>{

    private String fileName;
    private Class<T> typeOf;

    /**
     * Constructor
     * @param validator , Validator<Client> object
     * @param fileName , String representing the path of a given file
     */
    public GenericFileRepo(Validator<T> validator, String fileName, Class<T> typeOf) {
        super(validator);
        this.fileName = fileName;
        this.typeOf = typeOf;


        if(Files.exists(Paths.get(fileName))) {
            loadData();
        } else {
            try {
                Files.createFile(Paths.get(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads data from file into in-memory repo
     */
    public void loadData() {
        Path path = Paths.get(fileName);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));
                if(!items.get(0).equals("")) {
                    try {
                        T entity = typeOf.getDeclaredConstructor(List.class).newInstance(items);
                        super.save(entity);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the given entity.
     *
     * @param entity
     *            must not be null.
     * @return {@code Optional} - null if the entity was saved,
     *                            otherwise returns the entity(id already saved)
     * @throws ValidatorException if the entity is not valid
     */
    @Override
    public Optional<T> save(T entity) throws ValidatorException {
        Optional<T> optional = super.save(entity);
        optional.ifPresentOrElse(x -> {}, () -> saveToFile(entity));
        return optional;
    }

    /**
     * save all entities into the file
     */
    public void saveAll() {
        Path path = Paths.get(fileName);
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterable<T> entities = super.findAll();
        entities.forEach(this::save);
    }


    /**
     * Saves entity into File
     * @param entity , entity to be saved into file
     */
    public void saveToFile(T entity) {
        Path path = Paths.get(fileName);
        String string = entity.writeAsCSV();

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(string);
            bufferedWriter.newLine();

        } catch (IOException e){
            e.printStackTrace();
        }
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
        loadData();
        var res = super.update(entity);
        saveAll();
        return res;
    }

    /**
     * Removes the entity with the given id
     * @param aLong , id of the entity to be deleted
     *            must not be null.
     * @return  an {@code Optional} empty if there is no entity with the given id,
     *                              otherwise the remove entity
     * @throws IllegalArgumentException
     * if the id is null
     */
    @Override
    public Optional<T> delete(Long aLong) {
        loadData();
        var res = super.delete(aLong);
        saveAll();
        return res;
    }
}

