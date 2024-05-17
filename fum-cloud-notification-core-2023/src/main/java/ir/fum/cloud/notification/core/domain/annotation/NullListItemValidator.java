package ir.fum.cloud.notification.core.domain.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Objects;

public class NullListItemValidator implements ConstraintValidator<RemoveNullItems, Collection<?>> {

    @Override
    public void initialize(RemoveNullItems removeNullItems) {
    }


    @Override
    public boolean isValid(Collection<?> collection, ConstraintValidatorContext constraintValidatorContext) {

        if (collection != null) {
            collection.removeIf(Objects::isNull);
        }

        return true;
    }

}
