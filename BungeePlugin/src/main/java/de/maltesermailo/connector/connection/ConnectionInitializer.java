package de.maltesermailo.connector.connection;

import de.maltesermailo.servercontroller.protocol.PacketDecoder;
import de.maltesermailo.servercontroller.protocol.PacketEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class ConnectionInitializer extends ChannelInitializer<SocketChannel> {

	private SslContext sslCtx;
	private ConnectionHandler handler;

	public ConnectionInitializer(SslContext sslCtx, ConnectionHandler handler) {
		this.sslCtx = sslCtx;
		
		this.handler = handler;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(this.sslCtx.newHandler(ch.alloc()));
		ch.pipeline().addLast(new PacketDecoder(), new PacketEncoder());
		ch.pipeline().addLast(this.handler);
	}

}
