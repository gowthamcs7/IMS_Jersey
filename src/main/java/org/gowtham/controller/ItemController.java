package org.gowtham.controller;
import org.gowtham.model.Item;
import org.gowtham.security.RoleAllowed;
import org.gowtham.service.ItemService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemController {
    private final ItemService itemService;
    public ItemController(){itemService = new ItemService();}

    @GET
    @RoleAllowed({"Admin","Manger","Staff"})
    public List<Item> getAllItems(){
        return itemService.getAllItems();
    }

    @GET
    @Path("/{id}")
    @RoleAllowed({"Admin","Manger","Staff"})
    public Response getItemById(@PathParam("id") int id) {
        Item item = itemService.getItemById(id);
        if (item != null) {
            return Response.ok(item).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Item not found\"}")
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RoleAllowed({"Admin","Manger","Staff"})
    public Response createItem(Item item) {
        boolean success = itemService.createItem(item);

        if (success) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Item created successfully\"}")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid data or failed to insert item\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @RoleAllowed({"Admin","Manger","Staff"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItem(@PathParam("id") int id, Item item) {
        item.setId(id); // Ensure ID is set from the path parameter
        boolean updated = itemService.updateItem(item);

        if (updated) {
            return Response.ok("{\"message\": \"Item updated successfully\"}").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Item not found or update failed\"}").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RoleAllowed({"Admin","Manger","Staff"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItem(@PathParam("id") int id) {
        boolean deleted = itemService.deleteItem(id);

        if (deleted) {
            return Response.ok("{\"message\": \"Item deleted successfully\"}").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Item not found or delete failed\"}").build();
        }
    }




}
