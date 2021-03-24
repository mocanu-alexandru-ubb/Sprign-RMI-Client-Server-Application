package UI;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

public class ResponseMapper<T> implements Function<Future<T>, String> {

    Function<T, String> resultMapper;

    public ResponseMapper(Function<T, String> resultMapper) {
        this.resultMapper = resultMapper;
    }

    @Override
    public String apply(Future<T> tFuture) {
        try {
            T result = tFuture.get();
            return resultMapper.apply(result);
        }
        catch (InterruptedException err) {
            throw new RuntimeException("operation interrupted");
        }
        catch (ExecutionException err){
            throw new RuntimeException(err);
        }
    }
}
