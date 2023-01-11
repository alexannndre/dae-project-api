package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.exceptions;

import javax.validation.ConstraintViolation;
import java.util.stream.Collectors;

public class ConstraintViolationException extends Exception {
    public ConstraintViolationException(javax.validation.ConstraintViolationException e) {
        super(getConstraintViolationMessages(e));
    }

    private static String getConstraintViolationMessages(javax.validation.ConstraintViolationException e) {
        return e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
    }
}
