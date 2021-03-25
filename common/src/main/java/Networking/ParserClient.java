package Networking;

import Domain.Client;
import Exceptions.ParsingException;

import java.util.Optional;

public class ParserClient implements Parser<Client> {
    @Override
    public String encode(Object entity) {
        Client client = (Client) entity;
        return String.format("%d,%s", client.getClientID(), client.getName());
    }

    @Override
    public Client decode(String string) {
        String[] line = string.split(",");
        Optional.of(line)
                .filter(strings -> strings.length == 2)
                .orElseThrow(() -> new ParsingException("Client csv string cannot be parsed"));
        try
        {
            return new Client(Long.parseLong(line[0]), line[1]);
        }
        catch (NumberFormatException e)
        {
            throw new ParsingException("Client csv string cannot be parsed");
        }
    }
}
