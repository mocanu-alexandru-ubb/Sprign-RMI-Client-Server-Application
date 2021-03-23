package Store.Repository;

import Store.Domain.BaseEntity;
import Store.Domain.Validator.Validator;
import Store.Domain.Validator.ValidatorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenericXmlRepo<T extends BaseEntity<Long>> extends ClientRepo<Long, T>{

    String fileName;
    private Class<T> typeOf;

    /**
     * Constructor
     * @param validator , validator for entities of type T
     * @param typeOf , {@code Class} of the entities that are gonna be stored in the repository
     */

    public GenericXmlRepo(Validator<T> validator,String fileName, Class<T> typeOf) {
        super(validator);
        this.typeOf = typeOf;
        this.fileName = fileName;

        if(Files.exists(Paths.get(fileName))) {
            loadEntitiesFromXml();
        } else {
            createXmlFile();
        }
    }

    /**
     * save a given entity to the XML file repository
     * @param entity, entity to be saved
     * @throws ParserConfigurationException, serious configuration error
     * @throws IOException, Signals that an I/O exception of some sort has occurred
     * @throws SAXException, signals an XML error
     * @throws TransformerException, exceptional condition that occurred during the transformation process
     */

    public void saveToXml(T entity) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(fileName);

        try {
            addEntityToDom(entity, document);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(
                new DOMSource(document),
                new StreamResult(new File(fileName))
        );
    }


    /**
     * saves all entities into the .xml file
     * @throws IOException, Signals that an I/O exception of some sort has occurred
     */
    public void saveAllToXml() throws IOException {
        deleteFile();
        createXmlFile();

        Iterable<T> entities = super.findAll();
        entities.forEach(entity -> {
            try {
                saveToXml(entity);
            }catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
                e.printStackTrace();
            }
        }
        );
    }


    /**
     * adds a given entity to a document
     * @param entity , entity that will be added to document
     * @param document , document in which the entity will be added
     * @throws NoSuchMethodException, if the method 'writeAsXML' is not found in the class of the entity
     * @throws IllegalAccessException , if the method does not have access to the definition of the specified class, field, method or constructor.
     */
    public void addEntityToDom(T entity, Document document) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Element root = document.getDocumentElement();
        Node entityNode = (Node) typeOf.getDeclaredMethod("writeAsXML", Document.class).invoke(entity,document);
        
        root.appendChild(entityNode);
    }


    /**
     * Loads all entities from an XML file
     * @return {@code List<T>} of all entities
     */
    public List<T> loadEntitiesFromXml() {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document root = db.parse(fileName);
            Element store = root.getDocumentElement();
            NodeList entityList = store.getChildNodes();
            List<T> entities = new ArrayList<>();

            for (int i = 0; i < entityList.getLength(); i++) {
                if(!(entityList.item(i) instanceof Element))
                    continue;
                T entity = typeOf.getDeclaredConstructor(Element.class).newInstance((Element) entityList.item(i));
                entities.add(entity);
            }
            return entities;
            
        } catch (ParserConfigurationException | SAXException | IOException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * function to create a new XML file
     */
    private void createXmlFile() {
        String xmlRootElement = "<root></root>";

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            writer.write(xmlRootElement);
        } catch (IOException exception) {
            throw new ValidatorException("XML file could not be created", exception);
        }
    }

    /**
     * function to delete a XML file
     * @throws IOException, Signals that an I/O exception of some sort has occurred
     */
    private void deleteFile() throws IOException {
        Files.deleteIfExists(Paths.get(fileName));
    }

    /**
     Saves a given entity
     * @param entity
     *            must not be null.
     *
     * @return Optional describing the specified value if entity is already saved,
     *         otherwise empty Optional
     * @throws ValidatorException, if the entity is not valid
     */
    @Override
    public Optional<T> save(T entity) throws ValidatorException {
        loadEntitiesFromXml();
        var res = super.save(entity);
        try {
            saveAllToXml();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
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
        loadEntitiesFromXml();
        var res = super.update(entity);
        try {
            saveAllToXml();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Removes the entity with the given id
     * @param aLong , id of the entity
     *            must not be null.
     * @return  an {@code Optional} empty if there is no entity with the given id,
     *                              otherwise the remove entity
     * @throws IllegalArgumentException
     * if the id is null
     */
    @Override
    public Optional<T> delete(Long aLong) {
        loadEntitiesFromXml();
        var res = super.delete(aLong);
        try {
            saveAllToXml();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}