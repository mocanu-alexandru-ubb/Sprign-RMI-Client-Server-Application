package Store.Services;

import Store.Domain.Client;
import Store.Domain.Validator.ClientValidator;
import Store.Domain.Validator.Validator;
import Store.Domain.Validator.ValidatorException;
import Store.Repository.ClientRepo;
import Store.Repository.Repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {
    private Repository<Long, Client> repo;
    private ClientService srv;
    Validator<Client> val = new ClientValidator();
    Client c0 = new Client(0L, "n@ nu m3rg3");
    @BeforeEach
    void setUp() {
        repo = new ClientRepo<>(val);
        srv = new ClientService(repo);
    }

    @Test
    void findClient() {
        Client c3 = new Client(3L, "Mocanu Radu");
        srv.addClient(c3);
        assert (srv.findClient(3L));
    }

    @Test
    void addClient() {
        Client c4 = new Client(4L, "Mircea Geoana");
        srv.addClient(c4);
        assert(repo.findOne(4L).get().equals(c4));
    }

    @Test
    void addClientThrow() {
        assertThrows(ValidatorException.class, () -> srv.addClient(c0));
    }

    @Test
    void getAllStudents() {
        Client c5=new Client(5L,"Vadim Lascarache");
        srv.addClient(c5);
        Client c6=new Client(6L,"Teodor Muresanu");
        srv.addClient(c6);
        assert(srv.getAllStudents().size()==2);
    }

    @Test
    void filterByName() {
        Client c7=new Client(7L,"Matei Ionascu");
        srv.addClient(c7);
        Client c8=new Client(8L,"Iulian Dohotaru");
        srv.addClient(c8);
        assert(srv.filterByName("Iulian Dohotaru").size()==1);
    }

    @Test
    void removeClient() {
        Client c9=new Client(9L,"Marian Muresan");
                srv.addClient(c9);
        srv.removeClient(9L);
        assert(!srv.findClient(9L));
    }

    @AfterEach
    void tearDown() {
    }
}
