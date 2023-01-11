package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.Auth;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.TokenIssuer;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("auth")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
public class AuthService {
    @Inject
    private TokenIssuer issuer;

    @EJB
    private UserBean userBean;

    @Context
    private SecurityContext securityContext;

    @POST
    @Path("/login")
    public Response authenticate(@Valid Auth auth) {
        if (userBean.canLogin(auth.getVat(), auth.getPassword())) {
            String token = issuer.issue(auth.getVat());
            return Response.ok(token).build();
        }

        return Response.status(UNAUTHORIZED).build();
    }

    @GET
    @Authenticated
    @Path("/user")
    public Response getAuthenticatedUser() {
        var vat = securityContext.getUserPrincipal().getName();
        var user = userBean.findOrFail(vat);
        return Response.ok(UserDTO.from(user)).build();
    }
}
