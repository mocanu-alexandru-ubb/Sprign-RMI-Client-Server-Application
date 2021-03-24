package Server;

import Domain.Client;
import Networking.Message;
import Repository.GenericRepo;
import Service.ClientService;
import Validator.ClientValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceHandlerTest {

    @Test
    void handleMessage() {
        Message addClient = new Message("ClientService:addClient");
        addClient.addString("1");
        addClient.addString("Mocanu Alexandru");

        var dbRepo = new GenericRepo<Long, Client>(new ClientValidator());
        var srv = new ClientService(dbRepo);

        var rsp = ServiceHandler.handleMessage(addClient, srv);
        System.out.println(rsp.getBody());
        assertEquals(rsp.getHeader(), "success");
        assert(dbRepo.findAll().iterator().hasNext());
    }

    @Test
    void handleMessageWithResponse() {
        Message addClient = new Message("ClientService:addClient");
        addClient.addString("1");
        addClient.addString("Mocanu Alexandru");
        var dbRepo = new GenericRepo<Long, Client>(new ClientValidator());
        var srv = new ClientService(dbRepo);

        var rsp = ServiceHandler.handleMessage(addClient, srv);
        assertEquals(rsp.getHeader(), "success");
        assert(dbRepo.findAll().iterator().hasNext());

        srv.addClient(2L, "Trisam");
        Message listClients = new Message("ClientService:getAll");
        rsp = ServiceHandler.handleMessage(listClients, srv);
        assertEquals(2, rsp.getBody().size());
    }
}