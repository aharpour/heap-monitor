package net.sourceforge.heapmonitor.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MemoryData {

	@XmlElement(name = "memory-maximum")
	private final long memoryMaximum;
	@XmlElement(name = "memory-taken")
	private final long memoryTaken;
	@XmlElement(name = "memory-free")
	private final long memoryFree;

	public MemoryData(long memoryMaximum, long memoryTaken, long memoryFree) {
		this.memoryMaximum = memoryMaximum;
		this.memoryTaken = memoryTaken;
		this.memoryFree = memoryFree;
	}

	@XmlElement(name = "memory-in-use")
	public long getMemoryInUse() {
		return memoryTaken - memoryFree;
	}

	@XmlElement(name = "memory-total-free")
	public long getMemoryTotalFree() {
		return memoryMaximum - getMemoryInUse();
	}

	@XmlElement(name = "percentage-memory-used")
	public double getPercentageMemoryUsed() {
		return (((double) getMemoryInUse()) / getMemoryMaximum() * 100);
	}

	public long getMemoryMaximum() {
		return memoryMaximum;
	}

	public long getMemoryTaken() {
		return memoryTaken;
	}

	public long getMemoryFree() {
		return memoryFree;
	}

}
