package net.sourceforge.heapmonitor;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class MemoryMonitorTest {
	
	private Client client;
	private WebResource service;
	
	public MemoryMonitorTest() {
		client = Client.create();
		client.setConnectTimeout(2000);
		service = client.resource("http://localhost:8080/heap-monitor");
	}
	
	
	
	@Test
	public void smokeTest() throws JSONException {
		WebResource path = service.path("/");
		JSONObject memoryData = path.accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
		System.out.println(memoryData);
		int memoryMaximum = Integer.parseInt(memoryData.get("memory-maximum").toString());
		int memoryTaken = Integer.parseInt(memoryData.get("memory-taken").toString());
		int memoryFree = Integer.parseInt(memoryData.get("memory-free").toString());
		double percentageMemoryUsed = Double.parseDouble(memoryData.get("percentage-memory-used").toString());
		int memoryInUse = Integer.parseInt(memoryData.get("memory-in-use").toString());
		int memoryTotalFree = Integer.parseInt(memoryData.get("memory-total-free").toString());
		
		Assert.assertEquals(memoryMaximum, memoryInUse + memoryTotalFree);
		Assert.assertEquals(memoryInUse, memoryTaken - memoryFree);
		Assert.assertEquals((((double) memoryInUse) / memoryMaximum ) * 100, percentageMemoryUsed, 0.0);
		
	}

}
