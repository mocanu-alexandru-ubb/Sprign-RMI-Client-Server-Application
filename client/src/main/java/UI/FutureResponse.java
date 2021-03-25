package UI;

import java.util.concurrent.Future;
import java.util.function.Function;

public class FutureResponse<T> {

    Future<T> future;
    Function<Future<T>, String> mapper;

    public FutureResponse(Future<T> future, Function<Future<T>, String> mapper) {
        this.future = future;
        this.mapper = mapper;
    }

    public boolean available() {
        return future.isDone();
    }

    public String get() {
        return mapper.apply(future);
    }
}
