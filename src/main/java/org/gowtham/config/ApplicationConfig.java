package org.gowtham.config;

//import com.sun.org.apache.xml.internal.utils.res.XResources_sv;
import org.gowtham.filter.AuthFilter;
import org.gowtham.filter.RoleFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
import org.gowtham.controller.*;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(AuthFilter.class);
        resources.add(RoleFilter.class); // Register the JWT filter
        resources.add(org.gowtham.controller.UserController.class); // Add controllers
        resources.add(CustomerController.class);
        resources.add(PurchaseController.class);
        resources.add(VendorController.class);
        resources.add(ItemController.class);
        resources.add(SaleController.class);
        resources.add(AuthController.class);
        return resources;
    }
}
