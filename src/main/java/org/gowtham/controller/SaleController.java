package org.gowtham.controller;

import org.gowtham.model.Sale;
import org.gowtham.security.RoleAllowed;
import org.gowtham.service.SaleService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/sales")
public class SaleController {
    private final SaleService saleService = new SaleService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RoleAllowed({"Admin","Manger","Staff"})
    public Response createSale(Sale sale) {
        System.out.println("Im in sales post controller");
        if (sale == null || sale.getSaleItems() == null || sale.getSaleItems().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\": \"Invalid request data\"}").build();
        }

        boolean success = saleService.processSale(sale, sale.getSaleItems());
        if (success) {
            return Response.status(Response.Status.CREATED).entity("{\"message\": \"Sale recorded successfully\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\": \"Error processing sale\"}").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSales() {
        List<Sale> sales = saleService.getAllSales();
        if (sales != null) {
            return Response.ok(sales).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Failed to fetch sales data\"}").build();
        }
    }
}
