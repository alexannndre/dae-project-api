package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.CONFLICT;

@Provider
public class ProcessingExceptionMapper implements ExceptionMapper<ProcessingException> {
    private static final Logger LOGGER = Logger.getLogger(ProcessingException.class.getCanonicalName());

    @Override
    public Response toResponse(ProcessingException e) {
        return getResponse(e);
    }

    protected static Response getResponse(ProcessingException e) {
        var value = e.getMessage().substring(e.getMessage().lastIndexOf('.') + 1);
        var enumName = e.getMessage().substring(e.getMessage().lastIndexOf('.', e.getMessage().lastIndexOf('.') - 1) + 1, e.getMessage().lastIndexOf('.'));
        var msg = "The value '" + value + "' is not a valid " + enumName.toLowerCase();
        LOGGER.warning("ERROR: " + msg);

        return Response.status(CONFLICT).entity(new ErrorDTO(msg)).build();
    }
}
