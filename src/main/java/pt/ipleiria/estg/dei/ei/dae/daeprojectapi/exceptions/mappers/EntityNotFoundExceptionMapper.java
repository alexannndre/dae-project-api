package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {
    private static final Logger LOGGER = Logger.getLogger(EntityNotFoundException.class.getCanonicalName());

    @Override
    public Response toResponse(EntityNotFoundException e) {
        return getResponse(e);
    }

    protected static Response getResponse(EntityNotFoundException e) {
        var cause = e.getMessage();
        var entity = cause.substring(cause.lastIndexOf('.') + 1, cause.indexOf(" with id"));
        var msg = entity + " not found: " + cause.substring(cause.lastIndexOf(' ') + 1);
        LOGGER.warning("ERROR: " + msg);

        return Response.status(NOT_FOUND).entity(new ErrorDTO(msg)).build();
    }
}
