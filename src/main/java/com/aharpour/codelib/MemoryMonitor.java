package com.aharpour.codelib;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aharpour.codelib.model.Result;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class MemoryMonitor extends HttpServlet {

	private ObjectMapper objectMapper = new ObjectMapper();

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
		objectMapper.setAnnotationIntrospector(introspector);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Runtime runtime = Runtime.getRuntime();
			resp.setContentType("application/json");
			Result result = new Result(runtime.maxMemory(), runtime.totalMemory(), runtime.freeMemory());
			if (result.getPercentageMemoryUsed() > 95.0) {
				runtime.gc();
				Thread.sleep(500);
				result = new Result(runtime.maxMemory(), runtime.totalMemory(), runtime.freeMemory());
			}
			
			String path = normalize(req.getPathInfo());
			if ("percentage-memory-used".equalsIgnoreCase(path)) {
				objectMapper.writeValue(resp.getWriter(), result.getPercentageMemoryUsed());
			} else if ("memory-free".equalsIgnoreCase(path)) {
				objectMapper.writeValue(resp.getWriter(), result.getMemoryFree());
			} else if ("memory-maximum".equalsIgnoreCase(path)) {
				objectMapper.writeValue(resp.getWriter(), result.getMemoryMaximum());
			} else if ("memory-taken".equalsIgnoreCase(path)) {
				objectMapper.writeValue(resp.getWriter(), result.getMemoryTaken());
			} else if ("memory-in-use".equalsIgnoreCase(path)) {
				objectMapper.writeValue(resp.getWriter(), result.getMemoryInUse());
			} else if ("memory-total-free".equalsIgnoreCase(path)) {
				objectMapper.writeValue(resp.getWriter(), result.getMemoryTotalFree());
			} else {
				objectMapper.writeValue(resp.getWriter(), result);
			}
		} catch (InterruptedException e) {
			throw new ServletException(e.getMessage(), e);
		}
	}

	private String normalize(String pathInfo) {
		String result = pathInfo;
		if (result != null && result.startsWith("/")) {
			result = result.substring(1);
		}
		if (result != null && result.endsWith("/")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}
}
