package Store.Domain.Validator;

import Store.Domain.Candy;
import Store.Domain.Client;

import java.util.Optional;

public class CandyValidator implements Validator<Candy> {

    @Override
    public void validate(Candy entity) throws ValidatorException {
        Optional.of(entity)
                .filter(e -> e.getName().length() > 2)
                .filter(e -> e.getPrice() > 0)
                .orElseThrow(ValidatorException::new);
    }
}
