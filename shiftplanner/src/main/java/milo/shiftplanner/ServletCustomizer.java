package milo.shiftplanner;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ServletCustomizer implements EmbeddedServletContainerCustomizer {
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		// redirect not found to index
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
		// add mime charset for static html files
		MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
		mappings.add("html", "text/html; charset=UTF-8");
		container.setMimeMappings(mappings);
	}
}