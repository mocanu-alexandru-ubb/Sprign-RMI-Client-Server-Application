package Exceptions;

public class RepoException extends RuntimeException{
    public RepoException() {
        super("bad repo");
    }
    public RepoException(String message) {
        super(message);
    }

    public RepoException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepoException(Throwable cause) {
        super(cause);
    }
}
