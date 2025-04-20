package org.gowtham.controller;

import org.gowtham.model.User;
import org.gowtham.service.AuthService;
import java.security.SecureRandom;
import java.util.Base64;
import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/auth")
public class AuthController {
    private final AuthService authService = new AuthService();
    @GET
    @Path("/csrf")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCsrfToken() {
        String csrfToken = generateCsrfToken(); // Generate a CSRF token
        NewCookie csrfCookie = new NewCookie("XSRF-TOKEN", csrfToken, "/", null, "CSRF Token", 3600, false, false);

        return Response.ok("{ \"csrfToken\": \"" + csrfToken + "\" }")
                .cookie(csrfCookie)
                .build();
    }


    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response register(User user) {
        try {
            boolean isRegistered = authService.register(user.getName(), user.getEmail(), user.getPassword(), user.getRole());

            if (isRegistered) {
                return Response.status(Response.Status.CREATED).entity("{\"message\":\"User registered successfully\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"User registration failed\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"An error occurred\"}").build();
        }
    }

    @POST
    @Path("/login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response login(@Context HttpHeaders headers, @CookieParam("XSRF-TOKEN") Cookie csrfCookie, User loginRequest) {
        try {
            System.out.println("🟢 [DEBUG] Received Login Request: " + loginRequest.getEmail());

            // ✅ Validate CSRF Token before proceeding
            if (!validateCsrfToken(headers, csrfCookie)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"message\":\"CSRF token missing or invalid\"}")
                        .build();
            }

            // ✅ Authenticate User
            String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            if (token == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"message\":\"Invalid credentials\"}")
                        .build();
            }

            // ✅ Generate new CSRF token on login
            String newCsrfToken = generateCsrfToken();
            NewCookie csrfCookieNew = new NewCookie("XSRF-TOKEN", newCsrfToken, "/", null, "CSRF Token", 3600, false, false);
            NewCookie authCookie = new NewCookie("token", token, "/", null, "Auth Token", 3600, false, false);

            return Response.ok("{ \"message\": \"Login successful\", \"token\": \"" + token + "\" }")

                    .cookie(authCookie, csrfCookieNew)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Login failed\"}")
                    .build();
        }
    }


    // ✅ CSRF Token Validation Middleware (For Protected Routes)
    private boolean validateCsrfToken(HttpHeaders headers, Cookie csrfCookie) {
        String headerToken = headers.getHeaderString("X-CSRF-TOKEN");
        String cookieTokenNotModified = csrfCookie.getValue();
        String cookieToken = csrfCookie != null ? csrfCookie.getValue().replace("=", "") : null;

        System.out.println("Header Token: " + headerToken);
        System.out.println("Cookie Token (trimmed): " + cookieToken);

        return headerToken != null && cookieTokenNotModified != null && cookieToken != null && headerToken.equals(cookieToken) || headerToken.equals((cookieTokenNotModified));
    }


    // ✅ Example Protected API with CSRF Token Validation
    @POST
    @Path("/protected-action")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response protectedAction(@Context HttpHeaders headers, @CookieParam("XSRF-TOKEN") Cookie csrfCookie) {
        if (!validateCsrfToken(headers, csrfCookie)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"message\":\"CSRF validation failed\"}")
                    .build();
        }
        return Response.ok("{\"message\":\"Action completed successfully\"}").build();
    }

    // ✅ Helper method to generate CSRF Token
    private String generateCsrfToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
