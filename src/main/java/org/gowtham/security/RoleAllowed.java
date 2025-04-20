package org.gowtham.security;

import javax.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NameBinding // Binds annotation to Jersey filters
@Retention(RetentionPolicy.RUNTIME) // Keep annotation at runtime
@Target({ElementType.TYPE, ElementType.METHOD}) // Use on classes/methods
public @interface RoleAllowed {
    String[] value(); // Accepts roles dynamically when used
}
