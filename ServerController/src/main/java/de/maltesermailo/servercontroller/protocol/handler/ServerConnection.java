package de.maltesermailo.servercontroller.protocol.handler;

import de.maltesermailo.servercontroller.Server;
import de.maltesermailo.servercontroller.ServerController;
import de.maltesermailo.servercontroller.protocol.packet.PacketCreateServer;
import de.maltesermailo.servercontroller.protocol.packet.PacketDeleteServer;
import de.maltesermailo.servercontroller.protocol.packet.PacketListener;
import de.maltesermailo.servercontroller.protocol.packet.PacketLogin;
import de.maltesermailo.servercontroller.protocol.packet.PacketStartServer;
import de.maltesermailo.servercontroller.protocol.packet.PacketStatus;
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
		
		packet.setLoggedOn(this.loggedOn);
		this.ctx.writeAndFlush(packet);
	}

	@Override
	public void handleStartServer(PacketStartServer packet) {
		if(packet.getName() == null || packet.getName().isEmpty())
			return;
		
		if(this.loggedOn)
			ServerController.instance().startServer(packet.getName());
	}

	@Override
	public void handleStopServer(PacketStopServer packet) {
		if(packet.getName() == null || packet.getName().isEmpty())
			return;
		
		if(this.loggedOn)
			ServerController.instance().stopServer(packet.getName());
	}

	@Override
	public void handleCreateServer(PacketCreateServer packet) {
		if(packet.getName() == null || packet.getName().isEmpty())
			return;
		
		if(this.loggedOn)
			ServerController.instance().addServer(packet.getName(), packet.getBaseDir(), packet.getXmx(), packet.getXms(), packet.getPort());
	}

	@Override
	public void handleDeleteServer(PacketDeleteServer packet) {
		if(packet.getName() == null || packet.getName().isEmpty())
			return;
		
		if(this.loggedOn)
			ServerController.instance().removeServer(packet.getName(), packet.isDeleteFiles());
	}
	
	@Override
	public void handleStatus(PacketStatus packet) {
		if(packet.getName() == null || packet.getName().isEmpty())
			return;
		
		if(this.loggedOn) {
			if(ServerController.instance().getServer(packet.getName()) != null) {
				Server s = ServerController.instance().getServer(packet.getName());
				
				packet.setExisting(true);
				packet.setAlive(s.getProcess().isAlive() && s.getPidFile().exists());
				
				ctx.writeAndFlush(packet);
			} else {
				packet.setExisting(false);
				packet.setAlive(false);
				
				ctx.writeAndFlush(packet);
			}
		}
	}
	
}
