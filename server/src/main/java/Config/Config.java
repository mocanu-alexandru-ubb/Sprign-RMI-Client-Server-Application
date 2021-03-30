package Config;

import Domain.Candy;
import Domain.Client;
import Domain.Purchase;
import Networking.ServerInformation;
import Repository.*;
import Services.*;
import Validator.CandyValidator;
import Validator.ClientValidator;


import Validator.PurchaseValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

import java.sql.SQLException;

@Configuration
public class Config {

    @Bean
    ClientValidator clientValidator () {
        return new ClientValidator();
    }

    @Bean
    CandyValidator candyValidator () {
        return new CandyValidator();
    }

    @Bean
    PurchaseValidator purchaseValidator () {
        return new PurchaseValidator();
    }

    @Bean
    Repository<Long, Client> clientRepository(ClientValidator clientValidator) throws SQLException {
        return new DBClientRepo(clientValidator);
    }

    @Bean
    Repository<Long, Candy> candyRepository(CandyValidator candyValidator)
    {
        return new DBCandyRepo(candyValidator);
    }

    @Bean
    Repository<Long, Purchase> purchaseRepository(PurchaseValidator purchaseValidator)
    {
        return new DBPurchaseRepo(purchaseValidator);
    }

    @Bean
    ClientServiceSkeleton clientService(Repository<Long, Client> clientRepository)
    {
        return new ClientServiceSkeleton(clientRepository);
    }

    @Bean
    CandyServiceSkeleton candyService(Repository<Long, Candy> candyRepository)
    {
        return new CandyServiceSkeleton(candyRepository);
    }

    @Bean
    PurchaseServiceSkeleton purchaseService(Repository<Long, Candy> candyRepository, Repository<Long, Purchase> rentalRepository)
    {
        return new PurchaseServiceSkeleton(rentalRepository, candyRepository);
    }

    @Bean
    RmiServiceExporter clientExporter(ClientServiceSkeleton clientServiceSkeleton)
    {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("ClientService");
        rmiServiceExporter.setServiceInterface(ClientService.class);
        rmiServiceExporter.setService(clientServiceSkeleton);
        rmiServiceExporter.setRegistryPort(ServerInformation.PORT);
        return rmiServiceExporter;
    }

    @Bean
    RmiServiceExporter candyExporter(CandyServiceSkeleton candyServiceSkeleton)
    {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("CandyService");
        rmiServiceExporter.setServiceInterface(CandyService.class);
        rmiServiceExporter.setService(candyServiceSkeleton);
        rmiServiceExporter.setRegistryPort(ServerInformation.PORT);
        return rmiServiceExporter;
    }

    @Bean
    RmiServiceExporter purchaseExporter(PurchaseServiceSkeleton purchaseServiceSkeleton)
    {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("PurchaseService");
        rmiServiceExporter.setServiceInterface(PurchaseService.class);
        rmiServiceExporter.setService(purchaseServiceSkeleton);
        rmiServiceExporter.setRegistryPort(ServerInformation.PORT);
        return rmiServiceExporter;
    }
}
