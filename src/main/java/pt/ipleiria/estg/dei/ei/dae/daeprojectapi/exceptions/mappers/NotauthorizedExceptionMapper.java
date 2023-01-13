package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

public class NotauthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {
    public static final Logger LOGGER = Logger.getLogger(NotauthorizedExceptionMapper.class.getCanonicalName());

    @Override
    public Response toResponse(NotAuthorizedException e) {
        return getResponse(e);
    }

    protected static Response getResponse(NotAuthorizedException e) {
        LOGGER.warning("ERROR: " + e.getMessage());
        return Response.status(UNAUTHORIZED).entity(new ErrorDTO(e.getMessage())).build();
    }

}
