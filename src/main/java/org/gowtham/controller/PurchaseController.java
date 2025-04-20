package org.gowtham.controller;

import org.gowtham.dao.PurchaseDAO;
import org.gowtham.model.Purchase;
import org.gowtham.security.RoleAllowed;
import org.gowtham.service.PurchaseService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.List;

@Path("/purchases")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PurchaseController {
    private final PurchaseService purchaseService = new PurchaseService();

    @POST
    @RoleAllowed({"Admin", "Manager"})
    @Consumes(MediaType.APPLICATION_JSON)  // ✅ Ensures the API expects JSON
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPurchase(Purchase purchase) {
        boolean success = purchaseService.createPurchase(purchase);
        if (success) {
            return Response.status(Response.Status.CREATED).entity("{\"message\":\"Purchase recorded successfully\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"Purchase failed\"}").build();
        }
    }

    @GET
    @RoleAllowed({"Admin", "Manager"})
    public Response getAllPurchases() {
        System.out.println("✅ Inside PurchasesController - GET /purchases");

        PurchaseDAO purchaseDAO = new PurchaseDAO();
        List<Purchase> purchases = purchaseDAO.getAllPurchases();

        return Response.ok(purchases).build();
    }

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testJson() {
        Purchase p = new Purchase();
        p.setId(1);
        p.setCreatedAt(Timestamp.valueOf("2025-02-12 12:00:00"));
        return Response.ok(p).build();
    }







}
