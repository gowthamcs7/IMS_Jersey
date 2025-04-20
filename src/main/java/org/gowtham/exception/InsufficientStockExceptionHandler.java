package org.gowtham.exception;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider

public class InsufficientStockExceptionHandler implements ExceptionMapper<InsufficientStockException> {
    @Override
    public Response toResponse(InsufficientStockException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"message\": \"" + exception.getMessage() + "\"}")
                .build();
    }
}
