package Networking;

import Domain.Client;
import Domain.Purchase;
import Exceptions.ParsingException;

import java.util.Optional;

public class ParserPurchase implements Parser<Purchase> {
    @Override
    public String encode(Object entity) {
        Purchase purchase = (Purchase) entity;
        return String.format("%d,%d,%d,%d", purchase.getPurchaseID(), purchase.getClientID(), purchase.getCandyID(), purchase.getQuantity());
    }

    @Override
    public Purchase decode(String string) {
        String[] line = string.split(",");
        Optional.of(line)
                .filter(strings -> strings.length == 4)
                .orElseThrow(() -> new ParsingException("Purchase csv string cannot be parsed"));
        try
        {
            return new Purchase(Long.parseLong(line[0]), Long.parseLong(line[1]), Long.parseLong(line[2]), Integer.parseInt(line[3]));
        }
        catch (NumberFormatException e)
        {
            throw new ParsingException("Purchase csv string cannot be parsed");
        }
    }
}
