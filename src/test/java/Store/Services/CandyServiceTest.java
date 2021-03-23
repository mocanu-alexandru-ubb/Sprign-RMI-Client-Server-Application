package Store.Services;

import Store.Domain.Candy;
import Store.Domain.Client;
import Store.Domain.Validator.CandyValidator;
import Store.Repository.ClientRepo;
import Store.Repository.Repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CandyServiceTest {
    private Client Client;
    private Repository<Long, Candy> Repo;
    private CandyService Srv;
    @Test
    void addCandy() throws Exception{
        Candy Test=new Candy(1L,"Mocanu Gummy Bears",10.99F);
        Srv.addCandy(Test);
        assert(Repo.findOne(1L).get().equals(Test));
    }

    @Test
    void findCandy() throws Exception{
        Candy Test=new Candy(1L,"Mocanu Gummy Bears",10.99F);
        Srv.addCandy(Test);
        assert(Srv.findCandy(1L));
    }

    @Test
    void getAllCandies() throws Exception{
        Candy Test1=new Candy(2L,"Milka Mihnea Edition", 11.99F);
        Srv.addCandy(Test1);
        Candy Test2=new Candy(3L,"Razvan Biscuits", 9.99F);
        Srv.addCandy(Test2);
        Candy Test3=new Candy(4L,"Vlad Ionescu Truffels", 20.99F);
        Srv.addCandy(Test3);
        assert(Srv.getAllCandies().size()==3);
    }

    @Test
    void filterByPrice() throws Exception{
        Candy Test1=new Candy(2L,"Milka Mihnea Edition", 11.99F);
        Srv.addCandy(Test1);
        Candy Test2=new Candy(3L,"Razvan Biscuits", 9.99F);
        Srv.addCandy(Test2);
        assert(Srv.filterByPrice(10.0F).size()==1);
    }

    @Test
    void removeCandy() throws Exception{
        Candy Test1=new Candy(2L,"Milka Mihnea Edition", 11.99F);
        Srv.addCandy(Test1);
        Srv.removeCandy(2L);
        assert(!Srv.findCandy(2L));

    }

    @BeforeEach
    void setUp() throws Exception{
        Repo = new ClientRepo<>(new CandyValidator());
        Srv = new CandyService(Repo);
    }

    @AfterEach
    void tearDown() throws Exception{
    }

}