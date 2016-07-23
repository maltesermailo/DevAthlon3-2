package de.maltesermailo.servercontroller.protocol.handler;

import java.net.InetSocketAddress;
import java.util.logging.Level;

import de.maltesermailo.servercontroller.Bootstrap;
import de.maltesermailo.servercontroller.protocol.packet.AbstractPacket;
import de.maltesermailo.servercontroller.protocol.packet.PacketListener;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class BootstrapChannelHandler extends SimpleChannelInboundHandler<AbstractPacket> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		
		Bootstrap.getLogger().info(String.format("Got connection from %s", ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress()));
		
		PacketListener listener = new ServerConnection(ctx);
		AbstractPacket.registerListener(ctx, listener);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		
		AbstractPacket.unregisterListener(ctx);
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, AbstractPacket packet) throws Exception {
		packet.handle(AbstractPacket.getListener(ctx));
	}

}
