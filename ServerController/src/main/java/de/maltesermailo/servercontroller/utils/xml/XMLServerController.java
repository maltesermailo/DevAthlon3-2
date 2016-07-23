package de.maltesermailo.servercontroller.utils.xml;

import java.util.HashMap;

import de.maltesermailo.servercontroller.Server;

public class XMLServerController {
	
    private HashMap<String, Server> servers = new HashMap<>();
	
	//The current minecraft version for all servers
	private String mcVersion;
	//The Login Token
	private String token;
	
	public HashMap<String, Server> getServers() {
		return servers;
	}
	
	public String getMcVersion() {
		return mcVersion;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setServers(HashMap<String, Server> servers) {
		this.servers = servers;
	}
	
	public void setMcVersion(String mcVersion) {
		this.mcVersion = mcVersion;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

}
