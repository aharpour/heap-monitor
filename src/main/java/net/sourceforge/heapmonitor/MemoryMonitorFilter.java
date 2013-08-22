package net.sourceforge.heapmonitor;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlElement;

import net.sourceforge.heapmonitor.model.MemoryData;
import net.sourceforge.heapmonitor.utils.AnnotationUtils;


public class MemoryMonitorFilter implements Filter {

	private static final String JSON_MIME_TYPE = "application/json";
	private Map<String, Method> urlReadMethodMap = new HashMap<String, Method>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		populateDescriptorMap();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		try {

			if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
				HttpServletRequest req = (HttpServletRequest) request;
				HttpServletResponse resp = (HttpServletResponse) response;
				PrintWriter writer = resp.getWriter();
				
				String pathInfo = getPathInfo(req);
				String path = normalize(pathInfo);

				if (path == null) {
					resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} else if ("/admin/".equals(pathInfo)) {
					chain.doFilter(request, response);
				} else {
					resp.setContentType(JSON_MIME_TYPE);
					MemoryData memoryData = getMemoryData();
					if (urlReadMethodMap.containsKey(path)) {
						Method readMethod = urlReadMethodMap.get(path);
						writer.print(readMethod.invoke(memoryData));
					} else {
						writer.format("{\"memory-maximum\":%s,\"memory-taken\":%s,\"memory-free\":%s,\"percentage-memory-used\":%s,\"memory-in-use\":%s,\"memory-total-free\":%s}",
								memoryData.getMemoryMaximum(), memoryData.getMemoryTaken(),memoryData.getMemoryFree(), memoryData.getPercentageMemoryUsed(), memoryData.getMemoryInUse(), memoryData.getMemoryTotalFree());
					}
				}

			}
		} catch (IllegalAccessException e) {

		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	private void populateDescriptorMap() throws ServletException {
		try {
			PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(MemoryData.class)
					.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				XmlElement xmlElement = AnnotationUtils.getPropertyAnnotation(propertyDescriptor, MemoryData.class,
						XmlElement.class);
				if (xmlElement != null && xmlElement.name() != null && propertyDescriptor.getReadMethod() != null) {
					urlReadMethodMap.put(xmlElement.name(), propertyDescriptor.getReadMethod());
				}
			}
		} catch (IntrospectionException e) {
			throw new ServletException(e);
		}
	}

	private MemoryData getMemoryData() {
		Runtime runtime = Runtime.getRuntime();
		MemoryData result = new MemoryData(runtime.maxMemory(), runtime.totalMemory(), runtime.freeMemory());
		if (result.getPercentageMemoryUsed() > 95.0) {
			runtime.gc();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// Ignore
			}
			result = new MemoryData(runtime.maxMemory(), runtime.totalMemory(), runtime.freeMemory());
		}
		return result;
	}

	private String getPathInfo(HttpServletRequest req) {
		String result = null;
		if (req != null && req.getRequestURI() != null && req.getContextPath() != null
				&& req.getRequestURI().length() >= req.getContextPath().length()) {
			result = req.getRequestURI().substring(req.getContextPath().length());
		}
		return result;
	}

	private String normalize(String pathInfo) {
		String result = pathInfo;
		if (result != null) {
			if (result != null && result.startsWith("/")) {
				result = result.substring(1);
			}
			if (result != null && result.endsWith("/")) {
				result = result.substring(0, result.length() - 1);
			}
		}
		return result;
	}

	@Override
	public void destroy() {

	}
}
