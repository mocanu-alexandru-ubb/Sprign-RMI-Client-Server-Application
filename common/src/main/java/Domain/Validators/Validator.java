package Domain.Validators;

import Exceptions.ValidatorException;

public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}