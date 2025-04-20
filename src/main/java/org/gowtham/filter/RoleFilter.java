package org.gowtham.filter;

import javax.annotation.Priority;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Method;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class RoleFilter implements ContainerRequestFilter {

    @Inject
    private ResourceInfo resourceInfo;  // ✅ Inject ResourceInfo to check @PermitAll

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();

        // ✅ Skip role-based checks for @PermitAll methods
        if (method != null && method.isAnnotationPresent(PermitAll.class)) {
            System.out.println("🔹 [RoleFilter] Skipping role check for @PermitAll method: " + method.getName());
            return;
        }

        // ✅ Extract user role from request context
        String userRole = (String) requestContext.getProperty("userRole");

        System.out.println("🔹 [RoleFilter] Extracted User Role = " + userRole);

        if (userRole == null) {
            System.out.println("⛔ [RoleFilter] User role is null, rejecting request!");
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"message\":\"Access denied: No role assigned\"}").build());
        }

        // ✅ Log successful role extraction
        System.out.println("✅ [RoleFilter] User role is valid: " + userRole);
    }
}
