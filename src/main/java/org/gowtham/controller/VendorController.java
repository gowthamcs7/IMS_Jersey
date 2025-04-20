package org.gowtham.controller;

import org.gowtham.security.RoleAllowed;
import org.gowtham.service.VendorService;
import org.gowtham.model.Vendor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/vendors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VendorController {
    private final VendorService vendorService;

    public VendorController() {
        this.vendorService = new VendorService();
    }

    @GET
    @RoleAllowed({"Admin","Manager"})
    public Response getAllVendors() {
        List<Vendor> vendors = vendorService.getAllVendors();
        if (vendors.isEmpty()) {
            return buildResponse(Response.Status.NOT_FOUND, "No vendors found");
        }
        return Response.ok(vendors).build();
    }

    @GET
    @Path("/{id}")
    @RoleAllowed({"Admin","Manager"})
    public Response getVendorById(@PathParam("id") long id) {
        Vendor vendor = vendorService.getVendorById(id);
        if (vendor != null) {
            return Response.ok(vendor).build();
        } else {
            return buildResponse(Response.Status.NOT_FOUND, "Vendor not found");
        }
    }

    @POST
    @RoleAllowed({"Admin","Manager"})
    public Response addVendor(Vendor vendor) {
        if (vendorService.addVendor(vendor)) {
            return buildResponse(Response.Status.CREATED, "Vendor added successfully");
        } else {
            return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, "Failed to add vendor");
        }
    }

    @PUT
    @Path("/{id}")
    @RoleAllowed({"Admin","Manager"})
    public Response updateVendor(@PathParam("id") int id, Vendor vendor) {
        vendor.setId(id);
        if (vendorService.updateVendor(vendor)) {
            return buildResponse(Response.Status.OK, "Vendor updated successfully");
        } else {
            return buildResponse(Response.Status.BAD_REQUEST, "Failed to update vendor");
        }
    }

    @DELETE
    @Path("/{id}")
    @RoleAllowed({"Admin","Manager"})
    public Response deleteVendor(@PathParam("id") int id) {
        if (vendorService.deleteVendor(id)) {
            return buildResponse(Response.Status.OK, "Vendor deleted successfully");
        } else {
            return buildResponse(Response.Status.NOT_FOUND, "Vendor not found");
        }
    }

    private Response buildResponse(Response.Status status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.getStatusCode());
        response.put("message", message);
        return Response.status(status).entity(response).build();
    }
}
