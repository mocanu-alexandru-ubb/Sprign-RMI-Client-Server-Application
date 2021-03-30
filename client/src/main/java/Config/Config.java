package Config;

import Networking.ServerInformation;
import Services.CandyService;
import Services.ClientService;
import Services.PurchaseService;
import UI.Console;
import UI.ResponseBuffer;
import org.springframework.context.annotation.*;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan({"UI"})
public class Config {
    @Bean
    ExecutorService executorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Bean
    ResponseBuffer responseBuffer()
    {
        return new ResponseBuffer(1, true);
    }

    @Bean
    RmiProxyFactoryBean rmiClientProxyFactoryBean() throws UnknownHostException {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceInterface(ClientService.class);
        String hostAddress = Inet4Address.getLocalHost()
                .getHostAddress();
        String url = String.format("rmi://%s:%d/ClientService", hostAddress, ServerInformation.PORT);
        rmiProxyFactoryBean.setServiceUrl(url);
        return rmiProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiCandyProxyFactoryBean() throws UnknownHostException {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceInterface(CandyService.class);
        String hostAddress = Inet4Address.getLocalHost()
                .getHostAddress();
        String url = String.format("rmi://%s:%d/CandyService", hostAddress, ServerInformation.PORT);
        rmiProxyFactoryBean.setServiceUrl(url);
        return rmiProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiPurchaseProxyFactoryBean() throws UnknownHostException {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceInterface(PurchaseService.class);
        String hostAddress = Inet4Address.getLocalHost()
                .getHostAddress();
        String url = String.format("rmi://%s:%d/PurchaseService", hostAddress, ServerInformation.PORT);
        rmiProxyFactoryBean.setServiceUrl(url);
        return rmiProxyFactoryBean;
    }

    @Bean
    Console console () {
        return new Console();
    }
}
