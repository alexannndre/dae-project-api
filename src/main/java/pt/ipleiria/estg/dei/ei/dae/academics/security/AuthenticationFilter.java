package pt.ipleiria.estg.dei.ei.dae.academics.security;

import io.jsonwebtoken.Jwts;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.User;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;

import javax.annotation.Priority;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.security.Key;
import java.security.Principal;

import static pt.ipleiria.estg.dei.ei.dae.academics.security.TokenIssuer.SECRET_KEY;
import static pt.ipleiria.estg.dei.ei.dae.academics.security.TokenIssuer.algorithm;

@Provider
@Authenticated
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @EJB
    private UserBean userBean;

    @Context
    private UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        var header = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer "))
            throw new NotAuthorizedException("Authorization header must be provided");

        // Get token from the HTTP Authorization header
        String token = header.substring("Bearer".length()).trim();
        User user;
        try {
            user = userBean.find(getUsername(token));
        } catch (MyEntityNotFoundException e) {
            throw new NotAuthorizedException("Invalid token");
        }

        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return user::getUsername;
            }

            @Override
            public boolean isUserInRole(String role) {
                var userClass = Hibernate.getClass(user);
                return userClass.getSimpleName().equals(role);
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

    private String getUsername(String token) {
        Key key = new SecretKeySpec(SECRET_KEY, algorithm);

        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            throw new NotAuthorizedException("Invalid JWT"); // don't trust the JWT!
        }
    }
}
