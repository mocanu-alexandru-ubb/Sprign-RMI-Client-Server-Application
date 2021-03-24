package Domain.Validators;

import Domain.Client;
import Exceptions.ValidatorException;

import java.util.Optional;

/**
 * @author Mocanu Alexandru
 */

public class ClientValidator implements Validator<Client> {

    @Override
    public void validate(Client entity) throws ValidatorException {
        Optional.ofNullable(entity)
                .filter(e -> e.getName().matches("^([A-Z]|'|\\s|[a-z]){3,}$"))
                .orElseThrow(ValidatorException::new);
    }
}
