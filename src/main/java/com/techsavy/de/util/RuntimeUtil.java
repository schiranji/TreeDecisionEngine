package com.techsavy.de.util;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.RuntimeMXBean;
import java.sql.Timestamp;
import java.util.List;

public final class RuntimeUtil {

	private static final String NEW_LINE = "\r\n"; 
	
	private RuntimeUtil() {
		super();
	}


	public static String getRuntimeEnvironment() {

		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		StringBuilder sb = new StringBuilder();
		sb.append("Operating System ProcessId : "+runtimeMxBean.getName().split("@")[0]).append(NEW_LINE)
		.append("BootClassPath : "+runtimeMxBean.getBootClassPath()).append(NEW_LINE)
		.append("ClassPath : "+runtimeMxBean.getClassPath()).append(NEW_LINE)
		.append("LibraryPath : "+runtimeMxBean.getLibraryPath()).append(NEW_LINE)
		.append("ManagementSpecVersion : "+runtimeMxBean.getManagementSpecVersion()).append(NEW_LINE)
		.append("Name : "+runtimeMxBean.getName()).append(NEW_LINE)
		.append("SpecName : "+runtimeMxBean.getSpecName()).append(NEW_LINE)
		.append("SpecVendor : "+runtimeMxBean.getSpecVendor()).append(NEW_LINE)
		.append("SpecVersion : "+runtimeMxBean.getSpecVersion()).append(NEW_LINE)
		.append("StartTime : "+new Timestamp(runtimeMxBean.getStartTime())).append(NEW_LINE)
		.append("Uptime : "+runtimeMxBean.getUptime()).append(NEW_LINE)
		.append("VmName : "+runtimeMxBean.getVmName()).append(NEW_LINE)
		.append("VmVendor : "+runtimeMxBean.getVmVendor()).append(NEW_LINE)
		.append("VmVersion : "+runtimeMxBean.getVmVersion()).append(NEW_LINE)
		.append("SystemProperties : "+runtimeMxBean.getSystemProperties()).append(NEW_LINE)
		.append("InputArguments : "+runtimeMxBean.getInputArguments()).append(NEW_LINE).append(NEW_LINE);
		return sb.toString();
	}
	
	
	public static String getGarbageCollectorStats() {
		List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
		StringBuilder sb = new StringBuilder();
		for(GarbageCollectorMXBean i : garbageCollectorMXBeans) {
			sb.append("Name:"+i.getName())
			  .append(", CollectionCount:"+i.getCollectionCount())
			  .append(", CollectionTime:"+i.getCollectionTime());
			String[] memoryPoolNames = i.getMemoryPoolNames();
			sb.append(", MemoryPools:"+memoryPoolNames.length +", (");
			for(String str : memoryPoolNames) {
				sb.append(str+", ");
			}
			sb.append(")").append(NEW_LINE);
		}
		return sb.toString();
	}
	
	public static String getClassLoadingStats() {
		StringBuilder sb = new StringBuilder();
		ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
		sb.append("Loaded Class Count:"+classLoadingMXBean.getLoadedClassCount()).append(NEW_LINE)
		  .append("Total Loaded Class Count:"+classLoadingMXBean.getTotalLoadedClassCount()).append(NEW_LINE)
		  .append("UnLoaded Class Count:"+classLoadingMXBean.getUnloadedClassCount()).append(NEW_LINE);
		return sb.toString();
	}

	public static String getMemoryStats() {
		StringBuilder sb = new StringBuilder();
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		sb.append("HeapMemoryUsage:"+memoryMXBean.getHeapMemoryUsage()).append(NEW_LINE)
		  .append("NonHeapMemoryUsage:"+memoryMXBean.getNonHeapMemoryUsage()).append(NEW_LINE)
		  .append("ObjectPendingFinalizationCount:"+memoryMXBean.getObjectPendingFinalizationCount());
		return sb.toString();
	}
	
	public static String getMemoryProfile() {
		StringBuilder sb = new StringBuilder();
		List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
		for (MemoryPoolMXBean p : pools) {
			sb.append(p.getName()+ ":" + p.getUsage()).append(NEW_LINE);
		}
		return sb.toString();
	}
}
