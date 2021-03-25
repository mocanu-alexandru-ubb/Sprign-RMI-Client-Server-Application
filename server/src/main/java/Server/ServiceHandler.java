package Server;

import Domain.Candy;
import Domain.Client;
import Domain.Purchase;
import Networking.*;
import Service.CandyService;
import Service.ClientService;
import Service.PurchaseService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class ServiceHandler {
    private static final Map<Class<?>, Parser<?>> typeParsers = new HashMap<>();

    static {
        typeParsers.put(int.class, new ParserInteger());
        typeParsers.put(Integer.class, new ParserInteger());
        typeParsers.put(String.class, new ParserString());
        typeParsers.put(Long.class, new ParserLong());
        typeParsers.put(Client.class, new ParserClient());
        typeParsers.put(Candy.class, new ParserCandy());
        typeParsers.put(Purchase.class, new ParserPurchase());
        typeParsers.put(Float.class, new ParserFloat());
    }

    public static Message handleMessage(
            Message message,
            ClientService clientSrv,
            CandyService candySrv,
            PurchaseService purchaseSrv
    )
    {
        Map<String, Object> services = new HashMap<>();
        services.put("ClientService", clientSrv);
        services.put("CandyService", candySrv);
        services.put("PurchaseService", purchaseSrv);

        String serviceName = message.getHeader().split(":")[0];
        Object serviceObject = services.get(serviceName);

        if (serviceObject == null) {
            return new Message("exception", "Invalid service name");
        }
        return handle(message, serviceObject);
    }

    static Message handle(Message message, Object service) {
        String methodName = message.getHeader().split(":")[1];

        Class<?> serviceClass = service.getClass();

        try {
            Method method = Arrays
                    .stream(serviceClass.getDeclaredMethods())
                    .filter(m -> m.getName().equals(methodName))
                    .findAny()
                    .orElseThrow(NoSuchMethodException::new);

            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != message.getBody().size()){
                throw new IndexOutOfBoundsException();
            }
            Object[] parameters = new Object[parameterTypes.length];

            IntStream.range(0, parameters.length)
                    .forEach(
                            i -> parameters[i] = typeParsers
                                    .get(parameterTypes[i])
                                    .decode(message.getBody().get(i))
                    );

            Object returnedValue = method.invoke(service, parameters);

            if (method.getReturnType().equals(Iterable.class)){
                List<String> values = StreamSupport
                        .stream(((Iterable<?>) returnedValue).spliterator(), false)
                        // ask professor about T instead of Object problem
                        .map(p -> typeParsers.get(p.getClass()).encode(p))
                        .collect(Collectors.toList());
                Message response = new Message("success");
                values.forEach(response::addString);
                return response;
            }
            return new Message("success");

        } catch (NoSuchMethodException e) {
            Message response = new Message("exception");
            response.addString("No such method");
            return response;
        } catch (IllegalAccessException | InvocationTargetException e) {
            Message response = new Message("exception");
            response.addString(e.getCause().getMessage());
            return response;
        } catch (IndexOutOfBoundsException e) {
            Message response = new Message("exception");
            response.addString("Invalid parameters number");
            return response;
        }

    }
}
