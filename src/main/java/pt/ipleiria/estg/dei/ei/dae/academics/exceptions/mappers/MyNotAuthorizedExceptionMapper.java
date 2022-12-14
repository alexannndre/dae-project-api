package pt.ipleiria.estg.dei.ei.dae.academics.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyNotAuthorizedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

public class MyNotAuthorizedExceptionMapper implements ExceptionMapper<MyNotAuthorizedException> {
    public static final Logger LOGGER = Logger.getLogger(MyNotAuthorizedExceptionMapper.class.getCanonicalName());

    @Override
    public Response toResponse(MyNotAuthorizedException e) {
        String errorMsg = e.getMessage();
        LOGGER.warning("ERROR: " + errorMsg);
        return Response.status(UNAUTHORIZED).entity(errorMsg).build();
    }
}
