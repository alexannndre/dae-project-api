package pt.ipleiria.estg.dei.ei.dae.academics.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
public class MyEntityNotFoundExceptionMapper implements ExceptionMapper<MyEntityNotFoundException> {
    private static final Logger LOGGER = Logger.getLogger(MyEntityNotFoundException.class.getCanonicalName());

    @Override
    public Response toResponse(MyEntityNotFoundException e) {
        String errorMsg = e.getMessage();
        LOGGER.warning("ERROR: " + errorMsg);
        return Response.status(NOT_FOUND).entity(errorMsg).build();
    }
}
