package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.exceptions.MyConstraintViolationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class MyConstraintViolationExceptionMapper implements ExceptionMapper<MyConstraintViolationException> {
    private static final Logger LOGGER = Logger.getLogger(MyConstraintViolationException.class.getCanonicalName());

    @Override
    public Response toResponse(MyConstraintViolationException e) {
        String errorMsg = e.getMessage();
        LOGGER.warning("ERROR: " + errorMsg);
        return Response.status(BAD_REQUEST).entity(errorMsg).build();
    }
}
