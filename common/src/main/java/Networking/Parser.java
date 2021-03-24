package Networking;

public interface Parser<T> {
    String encode(Object entity);
    T decode(String string);
}
