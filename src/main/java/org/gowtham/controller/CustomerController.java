package org.gowtham.controller;


import org.gowtham.model.Customer;

import org.gowtham.security.RoleAllowed;
import org.gowtham.service.CustomerService;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController() {
        this.customerService = new CustomerService();
    }

    @GET
    @RoleAllowed({"Admin","Manger","Staff"})
    public Response getAllCustomer() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            return buildResponse(Response.Status.NOT_FOUND, "No customers found");
        }
        return Response.ok(customers).build();
    }

    @GET
    @Path("/{id}")
    @RoleAllowed({"Admin","Manger","Staff"})
    public Response getCustomerById(@PathParam("id") long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            return Response.ok(customer).build();
        } else {
            return buildResponse(Response.Status.NOT_FOUND, "Customer not found");
        }
    }

    @POST
    @RoleAllowed({"Admin","Manger","Staff"})
    public Response addCustomer(Customer customer) {
        if (customerService.addCustomer(customer)) {
            return buildResponse(Response.Status.CREATED, "Customer added successfully");
        } else {
            return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, "Failed to add customer");
        }
    }

    @PUT
    @Path("/{id}")
    @RoleAllowed({"Admin","Manger","Staff"})
    public Response updateCustomer(@PathParam("id") int id, Customer customer) {
        customer.setId(id);
        if (customerService.updateCustomer(customer)) {
            return buildResponse(Response.Status.OK, "Customer updated successfully");
        } else {
            return buildResponse(Response.Status.BAD_REQUEST, "Failed to update customer");
        }
    }

    @DELETE
    @Path("/{id}")
    @RoleAllowed({"Admin","Manger","Staff"})
    public Response deleteCustomer(@PathParam("id") int id) {
        if (customerService.deleteCustomer(id)) {
            return buildResponse(Response.Status.OK, "Customer deleted successfully");
        } else {
            return buildResponse(Response.Status.NOT_FOUND, "Customer not found");
        }
    }

    private Response buildResponse(Response.Status status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.getStatusCode());
        response.put("message", message);
        return Response.status(status).entity(response).build();
    }
}
