package Store.Repository;

import Store.Domain.Client;
import Store.Domain.Validator.ClientValidator;
import Store.Domain.Validator.Validator;
import Store.Repository.GenericXmlRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class GenericXmlRepoTest {
    private Client client1;
    private Client client2;
    private Client client3;
    private Client client4;
    private Class<Client> typeOf;
    private GenericXmlRepo<Client> repo;
    private Validator<Client> val;

    @BeforeEach
    public void setUp() throws Exception {
        client1 = new Client(123L, "Mocanel");
        client2 = new Client(223L, "Mihnea");
        client3 = new Client(224L, "Miron");
        client4 = new Client(225L, "Nicolae");

        typeOf = (Class<Client>) client1.getClass();

        val = new ClientValidator();
        repo = new GenericXmlRepo<>(val,"test.xml", typeOf);

        repo.save(client1);
        repo.save(client2);
    }

    @AfterEach
    public void tearDown() {
        client1 = null;
        client2 = null;
        val = null;
        repo = null;
        typeOf = null;
    }

    @Test
    public void testLoadData() {
        List<Client> clients = repo.loadEntitiesFromXml();
        assert ((long) clients.size() == 2);
    }

    @Test public void testSaveToXml() throws IOException {
        repo.loadEntitiesFromXml();
        repo.save(client3);
        repo.save(client4);
        repo.saveAllToXml();
        List<Client> clients = repo.loadEntitiesFromXml();
        assert ((long) clients.size() == 4);
    }

}