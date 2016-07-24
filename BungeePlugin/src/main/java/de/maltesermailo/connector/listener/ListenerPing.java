package de.maltesermailo.connector.listener;

import de.maltesermailo.connector.BungeePlugin;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerPing implements Listener {
	
	private BungeePlugin plugin;
	
	public ListenerPing(BungeePlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPing(ProxyPingEvent e) {
		String[] hostSplit = e.getConnection().getVirtualHost().getHostName().split(".");
		
		if(hostSplit.length > 2) {
			String serverName = hostSplit[0];
			
			ServerPing ping = this.plugin.getConfigManager().constructPing(serverName);
			
			if(ping != null) {
				ping.setVersion(new Protocol(this.plugin.getServerStatus(serverName) ? e.getResponse().getVersion().getName() : "Offline",
						this.plugin.getServerStatus(serverName) ? e.getResponse().getVersion().getProtocol() : Integer.MAX_VALUE));
				
				e.setResponse(ping);
			}
		}
	}

}
