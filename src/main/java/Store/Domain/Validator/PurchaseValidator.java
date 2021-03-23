package Store.Domain.Validator;

import Store.Domain.Purchase;

import java.util.Optional;

public class PurchaseValidator implements Validator<Purchase> {

    @Override
    public void validate(Purchase entity) throws ValidatorException {
        Optional.of(entity)
                .filter(p -> p.getQuantity() > 0)
                .orElseThrow(ValidatorException::new);
    }
}
