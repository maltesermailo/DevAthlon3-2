package de.maltesermailo.servercontroller.protocol.handler;

import de.maltesermailo.servercontroller.Server;
import de.maltesermailo.servercontroller.protocol.PacketDecoder;
import de.maltesermailo.servercontroller.protocol.PacketEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class BootstrapChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private SslContext sslCtx;
	
	public BootstrapChannelInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(this.sslCtx.newHandler(ch.alloc()));
		ch.pipeline().addLast(new PacketDecoder(), new PacketEncoder());
		ch.pipeline().addLast(new BootstrapChannelHandler());
	}

}
