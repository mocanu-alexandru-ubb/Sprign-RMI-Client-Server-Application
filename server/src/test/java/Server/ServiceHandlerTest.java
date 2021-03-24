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
        assertEquals(1, srv.getAllStudents().size());
    }
}