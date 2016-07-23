package de.maltesermailo.servercontroller.protocol.handler;

import de.maltesermailo.servercontroller.ServerController;
import de.maltesermailo.servercontroller.protocol.packet.PacketListener;
import de.maltesermailo.servercontroller.protocol.packet.PacketLogin;
import de.maltesermailo.servercontroller.protocol.packet.PacketStartServer;
import de.maltesermailo.servercontroller.protocol.packet.PacketStopServer;
import io.netty.channel.ChannelHandlerContext;

public class ServerConnection implements PacketListener {

	private ChannelHandlerContext ctx;
	
	private boolean loggedOn = false;;
	
	public ServerConnection(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
	public ChannelHandlerContext getChannelHandlerContext() {
		return ctx;
	}
	
	@Override
	public void handleLogin(PacketLogin packet) {
		if(packet.getToken().equals(ServerController.instance().getToken())) {
			this.loggedOn = true;
		}
	}

	@Override
	public void handleStartServer(PacketStartServer packet) {
		
	}

	@Override
	public void handleStopServer(PacketStopServer packet) {
		
	}
	
}
