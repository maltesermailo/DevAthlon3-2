package de.maltesermailo.connector.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigManager {

	private Configuration cfg;
	
	private String host;
	private int port;
	
	private String token;
	
	
	public ConfigManager(File cfgFile) {
		if(!cfgFile.exists()) {
			try {
				cfgFile.createNewFile();
			} catch (IOException e) {
				ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Could not create default config.", e);
			}
		}
		
		try {
			this.cfg = YamlConfiguration.getProvider(YamlConfiguration.class).load(cfgFile);
		} catch (IOException e) {
			ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Could not create config object.", e);
		}
		
		if(this.cfg != null) {
			if(this.cfg.get("host") == null) {
				this.cfg.set("host", "localhost");
				this.cfg.set("port", 9001);
				this.cfg.set("token", "0000");
				
				this.cfg.set("servers.testServer.motd", "TestMotd");
				this.cfg.set("servers.testServer.players", 20);
			}
		}
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getToken() {
		return token;
	}
	
	public ServerPing constructPing(String name) {
		if(this.cfg != null) {
			String motd = this.cfg.getString(String.format("servers.%s.motd", name));
			
			int players = this.cfg.getInt(String.format("servers.%s.players", name), 0);
			
			if(motd != null && players > 0) {
				motd = motd.replace("&", "§");
				
				ServerPing ping = new ServerPing();
				
				ping.setDescriptionComponent(new TextComponent(motd));
				ping.setPlayers(new Players(players, 0, new PlayerInfo[0]));
				ping.setFavicon(ProxyServer.getInstance().getConfig().getFaviconObject());
				
				return ping;
			}
		}
		
		return null;
	}

}
