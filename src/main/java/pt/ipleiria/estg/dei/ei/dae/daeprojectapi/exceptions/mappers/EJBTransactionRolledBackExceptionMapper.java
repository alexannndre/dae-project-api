package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.exceptions.mappers;


import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;

import javax.ejb.EJBTransactionRolledbackException;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Provider
public class EJBTransactionRolledBackExceptionMapper implements ExceptionMapper<EJBTransactionRolledbackException> {
    @Override
    public Response toResponse(EJBTransactionRolledbackException e) {
        var cause = e.getCause();

        while (cause.getCause() != null)
            cause = cause.getCause();

        if (!(cause instanceof ConstraintViolationException))
            return Response.status(INTERNAL_SERVER_ERROR).entity(new ErrorDTO(e.getMessage())).build();


        var constraintViolations = ((ConstraintViolationException) cause).getConstraintViolations();

        var errors = new ArrayList<>(constraintViolations.size());
        for (var constraintViolation : constraintViolations) {
            errors.add(new ErrorDTO(
                    constraintViolation.getPropertyPath().toString(),
                    constraintViolation.getInvalidValue() != null ? constraintViolation.getInvalidValue().toString() : null,
                    constraintViolation.getMessage()
            ));
        }

        return Response.status(BAD_REQUEST).entity(errors).build();
    }
}