package ai.salesken.onboarding.misc;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import ai.salesken.onboarding.enums.Role;

 
@javax.ws.rs.NameBinding
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface JWTTokenNeeded {
	Role[] Permissions() default {Role.NoRights};
}
