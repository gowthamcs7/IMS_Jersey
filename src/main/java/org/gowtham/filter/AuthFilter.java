package org.gowtham.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.gowtham.helper.JwtUtil;

import javax.annotation.Priority;
import javax.annotation.security.PermitAll;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.inject.Inject;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    private ResourceInfo resourceInfo;  // Inject ResourceInfo to check @PermitAll

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Skip authentication for the login endpoint
        String requestPath = requestContext.getUriInfo().getPath();
        if (requestPath.startsWith("auth/login")) {
            System.out.println("🔹 [AuthFilter] Skipping authentication for /auth/login");
            return;
        }

        Method method = resourceInfo.getResourceMethod();

        // Skip authentication for @PermitAll methods
        if (method != null && method.isAnnotationPresent(PermitAll.class)) {
            System.out.println("🔹 [AuthFilter] Skipping authentication for @PermitAll method: " + method.getName());
            return;
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = null;

        // Check if Authorization header is present
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " prefix
        }

        // Check if token is in cookies (if applicable)
        if (token == null && requestContext.getCookies().containsKey("token")) {
            token = requestContext.getCookies().get("token").getValue();
        }

        System.out.println("🔹 Extracted Token: " + token);

        if (token == null) {
            System.out.println("⛔ Missing authentication token");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\":\"Missing authentication token\"}").build());
            return;
        }

        try {
            // Validate token
            System.out.println("🔹 [AuthFilter] Extracted Token: " + token);
            System.out.println("🔹 [AuthFilter] Validating Token...");
            Jws<Claims> claimsJws = JwtUtil.validateToken(token);
            Claims claims = claimsJws.getPayload();

            // Log extracted claims
            System.out.println("✅ [AuthFilter] Token Validated.");
            System.out.println("   - User ID: " + claims.getSubject());
            System.out.println("   - Role: " + claims.get("role"));
            System.out.println("   - Expiration: " + claims.getExpiration());

            // Ensure role is extracted correctly
            String userRole = claims.get("role", String.class);
            System.out.println("🔹 [AuthFilter] Extracted Role: " + userRole);

            // Store user details in request context
            if (userRole != null) {
                requestContext.setProperty("userRole", userRole);
                requestContext.setProperty("userId", Integer.parseInt(claims.getSubject()));
                System.out.println("🔹 [AuthFilter] Stored userRole in request context: " + userRole);
            } else {
                System.out.println("⛔ [AuthFilter] Role is null, rejecting request!");
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"message\":\"Invalid token - No role found\"}").build());
            }

        } catch (Exception e) {
            System.out.println("❌ Invalid Token: " + e.getMessage());
            e.printStackTrace();
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\":\"Invalid token\"}").build());
        }
    }
}
