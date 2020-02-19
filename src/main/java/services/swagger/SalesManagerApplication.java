package services.swagger;

import org.glassfish.jersey.server.ResourceConfig;

import io.swagger.jaxrs.config.BeanConfig;

public class SalesManagerApplication extends ResourceConfig {
	public SalesManagerApplication() {

		register(io.swagger.jaxrs.listing.ApiListingResource.class);
		register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.6.0");
		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setHost("localhost:8080");
		beanConfig.setBasePath("/SalesManager/api");
		beanConfig.setResourcePackage("io.swagger.resources");
		beanConfig.setScan(true);
		packages("ai.salesken.salesmanager.service");
	}
}
