package Networking;

public class ParserInteger implements Parser<Integer>{
    @Override
    public String encode(Object entity) {
        return ((Integer) entity).toString();
    }

    @Override
    public Integer decode(String string) {
        return Integer.parseInt(string);
    }
}
