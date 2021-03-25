package UI;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseBuffer {
    private List<FutureResponse<?>> responses = new LinkedList<>();
    // completable future
    public List<String> getResponses() {
        List<FutureResponse<?>> completedFutures = responses.stream()
                .filter(FutureResponse::available)
                .collect(Collectors.toList());

        List<String> result = completedFutures.stream()
                .map(FutureResponse::get)
                .collect(Collectors.toUnmodifiableList());

        responses.removeAll(completedFutures);
        return result;
    }

    public void add(FutureResponse<?> futureResponse) {
        responses.add(futureResponse);
    }
}
