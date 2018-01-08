package milo.shiftplanner;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.zip.GZIPOutputStream;

@WebServlet("/*")
public class StaticServlet extends HttpServlet {

	private static String staticFilesPath = "static/dist/";
	static {
		ClassLoader classLoader = StaticServlet.class.getClassLoader();
		try {
			Enumeration<URL> resources = classLoader.getResources("/");
			while (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				if (url.toString().contains("/WEB-INF/classes/")) {
//					System.out.println("StaticServlet.static initializer ============" + url.getPath() + " :: " + url.getFile());
					staticFilesPath = url.getPath() + staticFilesPath;
					break;
				}
			}
		} catch (IOException exception) {
			System.out.println("ImageServlet.instance initializer fuuuuck");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String filename = request.getPathInfo().substring(1);
		if (filename.isEmpty()) {
			filename = "index.html";
		}
//		System.out.println("StaticServlet.doGet -=-=-=- " + staticFilesPath + " :: " + filename);
		File file = new File(staticFilesPath, filename);
		if (!file.exists()) {
			if (request.getHeader("Accept").contains("text/html")) {
				file = new File(staticFilesPath, "index.html");
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		response.setContentType(getServletContext().getMimeType(filename));
//		response.setContentLength(Long.valueOf(file.length()).intValue());
		response.setHeader("Content-Encoding", "gzip");
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(response.getOutputStream());
		Files.copy(file.toPath(), gzipOutputStream);
		gzipOutputStream.close();
	}

}