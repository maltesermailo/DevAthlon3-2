package de.maltesermailo.servercontroller.protocol;

import de.maltesermailo.servercontroller.protocol.packet.PacketLogin;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

public class BootstrapClient implements Runnable {
	
	private String host;
	
	private int port;
	
	private String token;

	private ChannelInitializer<SocketChannel> initializer;

	public BootstrapClient(ChannelInitializer<SocketChannel> initializer, String host, int port, String token) {
		this.host = host;
		this.port = port;
		
		this.token = token;
		
		this.initializer = initializer;
	}

	@Override
	public void run() {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.channel(SocketChannel.class);
			
			bootstrap.group(group);
			bootstrap.handler(this.initializer);
			
			ChannelFuture future = bootstrap.connect(this.host, this.port).sync();
			
			PacketLogin packet = new PacketLogin();
			packet.setLoggedOn(false);
			packet.setToken(this.token);
			
			future.channel().writeAndFlush(packet);
			
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	

}
