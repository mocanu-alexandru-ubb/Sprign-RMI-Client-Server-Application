package Networking;

public class ParserString implements Parser<String>{
    @Override
    public String encode(Object entity) {
        return (String) entity;
    }

    @Override
    public String decode(String string) {
        return string;
    }
}
