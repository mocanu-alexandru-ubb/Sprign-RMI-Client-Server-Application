package Store.Domain.Validator;

public class BadInputException extends StoreException{

    public BadInputException(String message) {
        super(message);
    }

    public BadInputException() {super();}

    public BadInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadInputException(Throwable cause) {
        super(cause);
    }
}
