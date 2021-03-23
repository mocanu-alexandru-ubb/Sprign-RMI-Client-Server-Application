package Store.Domain.Validator;

/**
 * @author Miron Mihnea.
 */

public class ValidatorException extends StoreException {
    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException() {super();}

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }
}
