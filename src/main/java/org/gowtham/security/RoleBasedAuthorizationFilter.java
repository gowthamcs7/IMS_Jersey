package org.gowtham.security;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

@Provider
@Priority(Priorities.AUTHORIZATION) // Ensures this runs after authentication
public class RoleBasedAuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;  // ✅ Use ResourceInfo to get the matched method

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the matched resource method
        Method method = resourceInfo.getResourceMethod();

        // Get the RoleAllowed annotation
        RoleAllowed roleAllowed = method.getAnnotation(RoleAllowed.class);

        if (roleAllowed != null) {
            String[] allowedRoles = roleAllowed.value(); // Get roles from annotation


        }
    }
}
