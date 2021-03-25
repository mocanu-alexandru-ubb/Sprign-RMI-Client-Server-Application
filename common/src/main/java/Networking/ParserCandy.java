package Networking;

import Domain.Candy;
import Exceptions.ParsingException;

import java.util.Optional;

public class ParserCandy implements Parser<Candy> {
    @Override
    public String encode(Object entity) {
        Candy candy = (Candy) entity;
        return String.format("%d,%s,%f", candy.getCandyID(), candy.getName(), candy.getPrice());
    }

    @Override
    public Candy decode(String string) {
        String[] line = string.split(",");
        Optional.of(line)
                .filter(strings -> strings.length == 3)
                .orElseThrow(() -> new ParsingException("Candy csv string cannot be parsed"));
        try {
            return new Candy(Long.parseLong(line[0]), line[1], Float.parseFloat(line[2]));
        } catch (NumberFormatException e) {
            throw new ParsingException("Candy csv cannot be parsed");
        }
    }
}
