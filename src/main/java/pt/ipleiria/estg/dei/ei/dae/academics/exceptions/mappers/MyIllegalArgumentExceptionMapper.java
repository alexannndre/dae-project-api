package pt.ipleiria.estg.dei.ei.dae.academics.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyIllegalArgumentException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Provider
public class MyIllegalArgumentExceptionMapper implements ExceptionMapper<MyIllegalArgumentException> {
    private static final Logger LOGGER = Logger.getLogger(MyIllegalArgumentException.class.getCanonicalName());

    @Override
    public Response toResponse(MyIllegalArgumentException e) {
        String errorMsg = e.getMessage();
        LOGGER.warning("ERROR: " + errorMsg);
        return Response.status(INTERNAL_SERVER_ERROR).entity(errorMsg).build();
    }
}
