package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.CONFLICT;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    private static final Logger LOGGER = Logger.getLogger(IllegalArgumentException.class.getCanonicalName());

    @Override
    public Response toResponse(IllegalArgumentException e) {
        return getResponse(e);
    }

    protected static Response getResponse(IllegalArgumentException e) {
        LOGGER.warning("ERROR: " + e.getMessage());
        return Response.status(CONFLICT).entity(new ErrorDTO(e.getMessage())).build();
    }
}
