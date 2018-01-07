package milo.shiftplanner;

import milo.shiftplanner.agents.AgentsResource;
import milo.shiftplanner.shifts.ShiftsResource;
import milo.utils.RestExceptionMapper;
import milo.utils.rest.JacksonDefaultMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.EncodingFilter;

import javax.inject.Named;
import javax.ws.rs.ApplicationPath;

@Named
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		// prevent registering all providers at utils package
		registerResources();

		register(EntityFilteringFeature.class);
		//		register(JacksonWriteInterceptorImpl.class);
		register(JacksonDefaultMapper.class);
		register(JacksonFeature.class);
		register(RestExceptionMapper.class);
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		EncodingFilter.enableFor(this, GZipEncoder.class);
	}

	private void registerResources() {
		// this does not work for spring boot fat jars / only for war packaging
//		packages(true, this.getClass().getPackage().getName());
		// for jar needs to register resources separately
		register(BaseResource.class);
		register(AgentsResource.class);
		register(ShiftsResource.class);
	}

}
