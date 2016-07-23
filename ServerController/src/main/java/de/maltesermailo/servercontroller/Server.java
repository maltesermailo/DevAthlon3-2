package de.maltesermailo.servercontroller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.maltesermailo.servercontroller.utils.PlatformUtils;

public class Server {
	
	private static final ScheduledExecutorService shutdownService = Executors.newScheduledThreadPool(1);
	
	private Process theProcess;
	
	private String xmx;
	private String xms;
	
	private List<String> args;
	
	private File baseDir;
	private File pidFile;
	
	private String name;
	
	public Server(String name, File baseDir, String xmx, String xms) {
		this.setName(name);
		
		this.setBaseDir(baseDir);
		
		this.setXmx(xmx);
		this.setXms(xms);
		
		this.args = new ArrayList<>();
	}
	
	public void start(File serverFile) {
		ProcessBuilder builder = new ProcessBuilder("java", "-jar");
		builder.directory(this.baseDir);
		
		List<String> command = builder.command();
		
		command.add(String.format("-Xmx%s", this.xmx));
		command.add(String.format("-Xms%s", this.xms));
		
		command.add(serverFile.getPath());
		
		command.addAll(this.args);
		
		try {
			this.theProcess = builder.start();
			
			this.pidFile.createNewFile();
			
			//Faster writing
			Files.write(this.pidFile.toPath(),
					String.valueOf(PlatformUtils.getpid(this.theProcess)).getBytes(),
					StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void stop() {
		if(this.theProcess != null) {
			try {
				this.theProcess.getOutputStream().write("\n".getBytes());
				this.theProcess.getOutputStream().write("stop\n".getBytes());
				
				Server.shutdownService.schedule(() -> this.theProcess.destroy(), 5, TimeUnit.SECONDS);
			} catch (IOException e) {
				System.err.println(String.format("[ERR] Can't write to OutputStream for Server '%s'", this.name));
			}
		} else if(this.pidFile != null && this.pidFile.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(this.pidFile));
				
				int pid = Integer.parseInt(reader.readLine());
				
				PlatformUtils.terminate(pid);
				
				Server.shutdownService.schedule(() -> PlatformUtils.kill(pid), 5, TimeUnit.SECONDS);
				
				reader.close();
				
				pidFile.delete();
			} catch (FileNotFoundException e) {
				//Not throwable
			} catch (NumberFormatException e) {
				System.err.println(String.format("[ERR] Server '%s' has a wrong pidfile.", this.name));
			} catch (IOException e) {
				System.err.println(String.format("[ERR] Can't read pidfile of Server '%s'", this.name));
			}
		}
	}
	
	public void clear() {
		this.args = null;
		
		this.pidFile = null;
		
		this.theProcess = null;
	}
	
	public void setArgs(List<String> args) {
		this.args = args;
	}
	
	public void setXmx(String xmx) {
		this.xmx = xmx;
	}
	
	public void setXms(String xms) {
		this.xms = xms;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPidFile(File pidFile) {
		this.pidFile = pidFile;
	}
	
	public void setBaseDir(File baseDir) {
		this.baseDir = baseDir;
	}
	
	public List<String> getArgs() {
		return this.args;
	}
	
	public String getXmx() {
		return this.xmx;
	}
	
	public String getXms() {
		return this.xms;
	}
	
	public File getPidFile() {
		return this.pidFile;
	}
	
	public File getBaseDir() {
		return this.baseDir;
	}
	
	public Process getProcess() {
		return this.theProcess;
	}
	
	
}
