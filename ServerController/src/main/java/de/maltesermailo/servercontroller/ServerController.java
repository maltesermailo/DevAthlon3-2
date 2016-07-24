package de.maltesermailo.servercontroller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.bind.JAXB;

import de.maltesermailo.servercontroller.utils.xml.XMLServerController;

public class ServerController {
	
	private static ServerController instance;
	
	public static ServerController instance() {
		return ServerController.instance;
	}
	
	private HashMap<String, Server> servers = new HashMap<>();
	
	//The current minecraft version for all servers
	private String mcVersion;
	// The Login token
	private String token = "0000";
	
	private File serverFile;
	
	public ServerController(String mcVersion) {
		File f = new File(String.format("./jars/spigot-v%s.jar", mcVersion));
		
		if(!f.exists()) {
			System.err.println(
					String.format("[ERR] Could not find Spigot Server for Version '%s' (%s)",
							mcVersion, f.getPath()));
			
			System.exit(-1);
		}
		
		ServerController.instance = this;
		
		this.mcVersion = mcVersion;
		this.serverFile = f;
	}
	
	public String getToken() {
		return token;
	}

	public boolean addServer(String name, String baseDirName, String xmx, String xms, int port) {
		File baseDir = new File(String.format("./%s/", baseDirName));
		if(!baseDir.exists()) {
			baseDir.mkdirs();
		}
		
		Server server = new Server(name, baseDir, xmx, xms, port);
		
		this.servers.put(name, server);
		
		return this.servers.containsKey(name);
	}
	
	public void removeServer(String name, boolean deleteFiles) {
		Server server = this.servers.remove(name);
		
		if(server != null) {
			server.stop();
			
			if(deleteFiles) {
				try {
					Files.walkFileTree(server.getBaseDir().toPath(), new FileVisitor<Path>() {

						@Override
						public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
								throws IOException {
							return FileVisitResult.CONTINUE;
						}

						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
							Files.deleteIfExists(file);
							
							return FileVisitResult.CONTINUE;
						}

						@Override
						public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
							return FileVisitResult.TERMINATE;
						}

						@Override
						public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
							Files.deleteIfExists(dir);
							
							return FileVisitResult.CONTINUE;
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean startServer(String name) {
		if(this.servers.containsKey(name)) {
			Server server = this.servers.get(name);
			
			if(server != null) {
				server.start(this.serverFile);
				
				return (server.getProcess() != null && server.getProcess().isAlive());
			}
		}
		
		return false;
	}
	
	public void stopServer(String name) {
		if(this.servers.containsKey(name)) {
			Server server = this.servers.get(name);
			
			if(server != null) {
				server.stop();
			}
		}
	}
	
	public Server getServer(String name) {
		if(this.servers.containsKey(name)) {
			Server server = this.servers.get(name);
			
			return server;
		}
		
		return null;
	}
	
	public void loadServers() {
		File serversFile = new File("servers.xml");
		
		if(!serversFile.exists()) {
			return;
		}
		
		XMLServerController xmlObject = JAXB.unmarshal(serversFile, XMLServerController.class);
		
		this.servers = xmlObject.getServers();
		
		if(!this.mcVersion.equals(xmlObject.getMcVersion())) {
			File f = new File("./jars/spigot-v" + xmlObject.getMcVersion());
			
			if(!f.exists()) {
				System.err.println(
						String.format("[ERR] Could not find Spigot Server for Version '%s' (%s)",
								this.mcVersion, f.getName()));
				
				System.exit(-1);
			}
			
			this.serverFile = f;
		}
		
		this.mcVersion = xmlObject.getMcVersion();
		this.token = xmlObject.getToken();
	}
	
	public void saveServers() {
		XMLServerController xmlObject = new XMLServerController();
		xmlObject.setMcVersion(this.mcVersion);
		xmlObject.setServers(new HashMap<>(this.servers));
		xmlObject.setToken(this.token);
		
		JAXB.marshal(xmlObject, new File("servers.xml"));
	}
	
	
}
