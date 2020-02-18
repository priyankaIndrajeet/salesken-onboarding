package services.global.impl;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import constants.Role;

@javax.ws.rs.NameBinding
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface JWTTokenNeeded {
	Role[] Permissions() default {Role.NoRights};
}
