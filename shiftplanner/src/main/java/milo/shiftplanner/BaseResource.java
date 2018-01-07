package milo.shiftplanner;

import milo.utils.reflection.ReflectionFacade;
import milo.utils.rest.ReflectionRestApi;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Path("/")
public class BaseResource implements ReflectionRestApi {

	public static Exception lastShiftDeploymentException = null;
	public static Exception emailSentException = null;

	@GET
	@Path("healthcheck")
	public Response healthcheck() {
		if (lastShiftDeploymentException == null && emailSentException == null) {
			return Response.ok("OK").build();
		} else {

			StringWriter stringWriter = new StringWriter();
			if (lastShiftDeploymentException != null) {
				stringWriter.append(lastShiftDeploymentException.getMessage() + '\n');
			}
			if (emailSentException != null) {
				stringWriter.append(emailSentException.getMessage() + '\n');
			}
			PrintWriter printWriter = new PrintWriter(stringWriter);
			lastShiftDeploymentException.printStackTrace(printWriter);
			return Response.serverError().entity(stringWriter.toString()).build();
		}
	}

	@Override
	public List<Field> types(@NotNull String fullClassName) throws ClassNotFoundException {
		return ReflectionFacade.getFieldsTypes(Class.forName(fullClassName));
	}

	@Override
	public Object[] resolveEnum(@NotNull String fullClassName) {
		return ReflectionFacade.getEnumResolver().getEnumValues(fullClassName); 
	}

	@Override
	public Map<String, Object> isEnum(@NotNull String fullClassName) {
		return ReflectionFacade.getEnumResolver().isEnumAsEntry(fullClassName); 
	}

}
