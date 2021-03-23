package Store.Services;

import Store.Domain.Purchase;
import Store.Domain.Validator.PurchaseValidator;
import Store.Repository.ClientRepo;
import Store.Repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseServiceTest {
    private Repository<Long, Purchase> Repo;
    private PurchaseService Srv;

    @BeforeEach
    void setUp() {
        Repo = new ClientRepo<>(new PurchaseValidator());
        Srv = new PurchaseService(Repo);
    }

    @Test
    void addPurchase() {
        Purchase p1=new Purchase(0L,1L,2L,100);
        Srv.addPurchase(p1);
        assert(Repo.findOne(0L).get().equals(p1));
    }

    @Test
    void getAllPurchases() {
        Purchase p2=new Purchase(1L,3L,2L,120);
        Srv.addPurchase(p2);
        Purchase p3=new Purchase(2L,2L,4L,110);
        Srv.addPurchase(p3);
        assert(Srv.getAllPurchases().size()==2);
    }

    @Test
    void removePurchase() {
        Purchase p4=new Purchase(3L,1L,2L,20);
        Srv.addPurchase(p4);
        Srv.removePurchase(3L);
        assert(Srv.getAllPurchases().size()==0);
    }

    @Test
    void getAllByClient() {
        Purchase p2=new Purchase(1L,3L,2L,120);
        Srv.addPurchase(p2);
        Purchase p3=new Purchase(2L,2L,4L,110);
        Srv.addPurchase(p3);
        assert(Srv.getAllByClient(2L).size()==1);
    }

    @Test
    void removeByClientId() {
        Purchase p2=new Purchase(1L,3L,2L,120);
        Srv.addPurchase(p2);
        Srv.removeByClientId(1L);
        assert(Srv.getAllByClient(1L).size()==0);
    }

    @Test
    void getAllByCandy() {
        Purchase p2=new Purchase(1L,3L,2L,120);
        Srv.addPurchase(p2);
        Purchase p3=new Purchase(2L,2L,4L,110);
        Srv.addPurchase(p3);
        assert(Srv.getAllByClient(2L).size()==1);
    }

    @Test
    void removeByCandyId() {
        {
            Purchase p2=new Purchase(1L,3L,2L,120);
            Srv.addPurchase(p2);
            Srv.removeByCandyId(3L);
            assert(Srv.getAllByCandy(3L).size()==0);
        }
    }
}