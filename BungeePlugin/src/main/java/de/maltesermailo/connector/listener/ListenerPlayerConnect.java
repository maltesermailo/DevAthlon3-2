package de.maltesermailo.connector.listener;

import de.maltesermailo.connector.BungeePlugin;
import de.maltesermailo.servercontroller.protocol.packet.PacketStartServer;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerPlayerConnect implements Listener {
	
	private BungeePlugin plugin;
	
	public ListenerPlayerConnect(BungeePlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onConnect(PreLoginEvent e) {
		String serverName = e.getConnection().getVirtualHost().getHostName().split(".")[0];
		
		ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
		
		if(serverInfo != null) {
			if(!this.plugin.getServerStatus(serverName)) {
				e.setCancelled(true);
				e.setCancelReason("§cBitte warte noch bis der Versuch, den Server zu starten, erfolgreich war.");
			}
		}
	}
	
	@EventHandler
	public void onConnect(PostLoginEvent e) {
		String serverName = e.getPlayer().getPendingConnection().getVirtualHost().getHostName().split(".")[0];
		
		ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
		
		if(serverInfo != null) {
			if(!this.plugin.getServerStatus(serverName)) {
				e.getPlayer().disconnect(new TextComponent("§cBitte warte noch bis der Versuch, den Server zu starten, erfolgreich war."));
				
				PacketStartServer packet = new PacketStartServer();
				packet.setName(serverName);
				
				this.plugin.getHandler().getChannel().writeAndFlush(packet);
			} else {
				e.getPlayer().connect(serverInfo);
			}
		}
	}
	
}
