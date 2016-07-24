package de.maltesermailo.connector;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import de.maltesermailo.connector.config.ConfigManager;
import de.maltesermailo.connector.connection.ConnectionHandler;
import de.maltesermailo.connector.connection.ConnectionInitializer;
import de.maltesermailo.connector.listener.ListenerPing;
import de.maltesermailo.connector.listener.ListenerPlayerConnect;
import de.maltesermailo.servercontroller.protocol.BootstrapClient;
import io.netty.handler.ssl.SslContext;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin extends Plugin {
	
	//The Connection Handler
	private ConnectionHandler handler;
	
	//Our Client to connect to the Controller
	private BootstrapClient client;
	private Thread clientThread;
	
	// Our config manager
	private ConfigManager configManager;
	
	private HashMap<String, Boolean> serverStatus = new HashMap<>();
	
	@Override
	public void onEnable() {
		this.configManager = new ConfigManager(new File(this.getDataFolder(), "config.yml"));
		
		this.handler = new ConnectionHandler();
		
		SslContext sslCtx = null;
		try {
			sslCtx = SslContext.newClientContext();
		} catch (SSLException e) {
			e.printStackTrace();
			
			return;
		}
		
		if(this.configManager.getHost() != null
				&& this.configManager.getPort() != 0 
				&& this.configManager.getToken() != null) {
			this.client = new BootstrapClient(new ConnectionInitializer(sslCtx, this.getHandler()), this.configManager.getHost(), this.configManager.getPort(), this.configManager.getToken());
			
			this.clientThread = new Thread(this.client);
			this.clientThread.start();
		}
		
		this.getProxy().getScheduler().schedule(this, new Runnable() {
			
			@Override
			public void run() {
				for(ServerInfo info : BungeePlugin.this.getProxy().getServers().values()) {
					info.ping(new Callback<ServerPing>() {
						
						@Override
						public void done(ServerPing ping, Throwable t) {
							if(t != null) {
								BungeePlugin.this.serverStatus.put(info.getName(), false);
							} else {
								BungeePlugin.this.serverStatus.put(info.getName(), true);
							}
						}
					});
				}
			}
		}, 0, 30, TimeUnit.SECONDS);
		
		this.getProxy().getPluginManager().registerListener(this, new ListenerPing(this));
		this.getProxy().getPluginManager().registerListener(this, new ListenerPlayerConnect(this));
	}
	
	@Override
	public void onDisable() {
		if(this.clientThread != null) {
			try {
				this.clientThread.join(500L);
				this.clientThread.stop();
				
				this.clientThread = null;
			} catch (InterruptedException e) {
			}
		}
	}
	
	public ConnectionHandler getHandler() {
		return handler;
	}
	
	public ConfigManager getConfigManager() {
		return configManager;
	}
	
	public boolean getServerStatus(String name) {
		if(this.serverStatus.containsKey(name))
			return this.serverStatus.get(name);
		
		return false;
	}
	

}
