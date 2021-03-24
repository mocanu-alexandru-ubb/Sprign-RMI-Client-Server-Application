package Networking;

public class ParserLong implements Parser<Long>{
    @Override
    public String encode(Object entity) {
        return ((Long) entity).toString();
    }

    @Override
    public Long decode(String string) {
        return Long.parseLong(string);
    }
}
