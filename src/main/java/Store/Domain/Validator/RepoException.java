package Store.Domain.Validator;

public class RepoException extends StoreException{

    public RepoException(String message) {
        super(message);
    }

    public RepoException() {super();}

    public RepoException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepoException(Throwable cause) {
        super(cause);
    }
}
