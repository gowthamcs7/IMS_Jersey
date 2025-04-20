package org.gowtham.controller;
import org.gowtham.security.RoleAllowed;
import org.gowtham.service.UserService;
import org.gowtham.model.User;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Cookie;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Arrays;
import java.util.List;



import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    private final UserService userService = new UserService();

    @GET
    @RoleAllowed({"Admin"})
    public Response getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    @RoleAllowed({"Admin"})
    public Response getUserById(@PathParam("id") int id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        return Response.ok(user).build();
    }
    private boolean validateCsrfToken(HttpHeaders headers, Cookie csrfCookie) {
        String headerToken = headers.getHeaderString("X-CSRF-TOKEN");
        String cookieToken = csrfCookie != null ? csrfCookie.getValue().replace("=", "") : null;

        System.out.println("Header Token: " + headerToken);
        System.out.println("Cookie Token (trimmed): " + cookieToken);

        return headerToken != null && cookieToken != null && headerToken.equals(cookieToken);
    }





    @POST
    @RoleAllowed({"Admin"})
    public Response createUser(@Context HttpHeaders headers, @CookieParam("XSRF-TOKEN") Cookie csrfCookie, User user) {
        if (!validateCsrfToken(headers, csrfCookie)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"message\":\"CSRF token missing or invalid\"}")
                    .build();
        }

        boolean created = userService.addUser(user);
        if(created) {
            // Return a JSON response with status 201 and a success message
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"User created successfully\"}")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"Failed to create user\"}")
                .build();
    }

    @PUT
    @Path("/{id}")
    @RoleAllowed({"Admin"})
    public Response updateUser(@PathParam("id") long id, @Context HttpHeaders headers, @CookieParam("XSRF-TOKEN") Cookie csrfCookie, User user) {
        if (!validateCsrfToken(headers, csrfCookie)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"message\":\"CSRF token missing or invalid\"}")
                    .build();
        }

        boolean updated = userService.updateUser(id, user);
        if (updated) {
            return Response.status(Response.Status.OK)  // ✅ Ensure HTTP 200
                    .entity("{\"message\": \"User updated successfully\"}")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"Failed to update user\"}")
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RoleAllowed({"Admin"})
    public Response deleteUser(@PathParam("id") int id, @Context HttpHeaders headers, @CookieParam("XSRF-TOKEN") Cookie csrfCookie) {
        if (!validateCsrfToken(headers, csrfCookie)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"message\":\"CSRF token missing or invalid\"}")
                    .build();
        }

        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return Response.ok("User deleted successfully").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
    }


}
