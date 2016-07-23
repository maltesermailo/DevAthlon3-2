package de.maltesermailo.servercontroller.protocol;

import org.apache.logging.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class BootstrapServer {
	
	private static Logger logger;
	
	public static Logger getLogger() {
		return logger;
	}
	
	public BootstrapServer(Logger logger) {
		BootstrapServer.logger = logger;
	}

	public void start(ChannelInitializer<SocketChannel> initializer) {
		ServerBootstrap server = new ServerBootstrap();
		server.channel(NioServerSocketChannel.class);
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		server.group(bossGroup, workerGroup);
		server.childHandler(initializer);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				workerGroup.shutdownGracefully();
				bossGroup.shutdownGracefully();
				
				BootstrapServer.getLogger().info("Stopping...");
				
			} catch (Exception e) {
			}
		}));
		
		try {
			ChannelFuture chFuture = server.bind(9001).sync();
			
			chFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			//Can't get here.
		}
	}
	
}
