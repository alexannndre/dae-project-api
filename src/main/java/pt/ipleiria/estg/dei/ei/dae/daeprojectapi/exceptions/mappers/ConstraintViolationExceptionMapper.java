package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.exceptions.mappers;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private static final Logger LOGGER = Logger.getLogger(ConstraintViolationException.class.getCanonicalName());

    @Override
    public Response toResponse(ConstraintViolationException e) {
        String errorMsg = e.getMessage();
        LOGGER.warning("ERROR: " + errorMsg);
        return Response.status(BAD_REQUEST).entity(errorMsg).build();
    }
}
