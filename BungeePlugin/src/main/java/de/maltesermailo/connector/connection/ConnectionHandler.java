package de.maltesermailo.connector.connection;

import de.maltesermailo.servercontroller.protocol.packet.AbstractPacket;
import de.maltesermailo.servercontroller.protocol.packet.PacketLogin;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ConnectionHandler extends SimpleChannelInboundHandler<AbstractPacket> {

	private Channel ch;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ch = ctx.channel();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, AbstractPacket packet) throws Exception {
		if(packet instanceof PacketLogin) {
			PacketLogin loginPacket = (PacketLogin)packet;
			
			if(!loginPacket.isLoggedOn()) {
				throw new RuntimeException("Token is wrong.");
			}
		}
	}

	
	public Channel getChannel() {
		return ch;
	}
}
