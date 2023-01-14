package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security;

import io.jsonwebtoken.Jwts;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.UserBean;

import javax.annotation.Priority;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.security.Principal;

import static javax.ws.rs.Priorities.AUTHENTICATION;
import static pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.TokenIssuer.ALGORITHM;
import static pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.TokenIssuer.SECRET_KEY;

@Provider
@Authenticated
@Priority(AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @EJB
    private UserBean userBean;

    @Context
    private UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String header = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Get token from the HTTP Authorization header
        String token = header.substring("Bearer".length()).trim();
        var user = userBean.findOrFail(getVat(token));

        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return user::getVat;
            }

            @Override
            public boolean isUserInRole(String s) {
                return org.hibernate.Hibernate.getClass(user).getSimpleName().equals(s);
            }

            @Override
            public boolean isSecure() {
                return uriInfo.getAbsolutePath().toString().startsWith("https");
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });
    }

    public String getVat(String token) {
        var key = new SecretKeySpec(SECRET_KEY, ALGORITHM);
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            throw new NotAuthorizedException("Invalid JWT");
        }
    }
}
