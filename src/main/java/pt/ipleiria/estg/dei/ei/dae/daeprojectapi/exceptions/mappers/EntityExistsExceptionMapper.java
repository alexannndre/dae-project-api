package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;

import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.CONFLICT;

@Provider
public class EntityExistsExceptionMapper implements ExceptionMapper<EntityExistsException> {
    private static final Logger LOGGER = Logger.getLogger(EntityExistsException.class.getCanonicalName());

    @Override
    public Response toResponse(EntityExistsException e) {
        return getResponse(e);
    }

    protected static Response getResponse(EntityExistsException e) {
        LOGGER.warning("ERROR: " + e.getMessage());
        return Response.status(CONFLICT).entity(new ErrorDTO(e.getMessage())).build();
    }
}
