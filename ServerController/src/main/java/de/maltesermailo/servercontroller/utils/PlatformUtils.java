package de.maltesermailo.servercontroller.utils;

import java.io.IOException;
import java.lang.reflect.Field;

import de.maltesermailo.servercontroller.utils.win32.Kernel32;

public class PlatformUtils {
	
	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().startsWith("windows");
	}
	
	public static boolean isLinux() {
		return System.getProperty("os.name").toLowerCase().startsWith("linux");
	}
	
	public static boolean isMacOS() {
		return System.getProperty("os.name").toLowerCase().startsWith("mac");
	}
	
	public static long getpid(Process process) {
		try {
			if(PlatformUtils.isWindows()) {
				return PlatformUtils.getpidwin(process);
			} else if(PlatformUtils.isLinux() || PlatformUtils.isMacOS()) {
				return PlatformUtils.getpidunix(process);
			}
		} catch(Exception e) {
			System.err.println("[ERR] Could not get PID for Process.");
		}
		
		return -1;
	}

	public static void terminate(int pid) {
		if(PlatformUtils.isWindows()) {
			try {
				Runtime.getRuntime().exec(String.format("taskkill /PID %d", pid));
			} catch (IOException e) {
				
			}
		} else if(PlatformUtils.isLinux() || PlatformUtils.isMacOS()) {
			try {
				Runtime.getRuntime().exec(String.format("kill -15 %d", pid));
			} catch (IOException e) {
				
			}
		}
	}
	
	public static void kill(int pid) {
		if(PlatformUtils.isWindows()) {
			try {
				Runtime.getRuntime().exec(String.format("taskkill /F /PID %d", pid));
			} catch (IOException e) {
				
			}
		} else if(PlatformUtils.isLinux() || PlatformUtils.isMacOS()) {
			try {
				Runtime.getRuntime().exec(String.format("kill -9 %d", pid));
			} catch (IOException e) {
				
			}
		}
	}
	
	private static long getpidwin(Process process) throws Exception {
		Field handleField = process.getClass().getDeclaredField("handle");
		handleField.setAccessible(true);
		
		long handle = handleField.getLong(process);
		
		long pid = Kernel32.INSTANCE.GetProcessId(handle);
		
		return pid;
	}
	
	private static long getpidunix(Process process) throws Exception {
		Field pidField = process.getClass().getDeclaredField("pid");
		pidField.setAccessible(true);
		
		return pidField.getLong(pidField);
	}

}
