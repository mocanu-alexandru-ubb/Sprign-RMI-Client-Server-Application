package Networking;

public class ParserFloat implements Parser<Float>{
    @Override
    public String encode(Object entity) {
        return ((Float) entity).toString();
    }

    @Override
    public Float decode(String string) {
        return Float.parseFloat(string);
    }
}
