package Store.Domain.Validator;

/**
 * Created by Miron Mihnea.
 */
public class StoreException extends RuntimeException{

    public StoreException(String message) {
        super(message);
    }

    public StoreException() {super();}

    public StoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoreException(Throwable cause) {
        super(cause);
    }
}
