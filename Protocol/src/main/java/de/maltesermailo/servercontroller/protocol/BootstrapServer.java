package de.maltesermailo.servercontroller.protocol;

import org.apache.logging.log4j.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class BootstrapServer implements Runnable {
	
	private static Logger logger;
	
	public static Logger getLogger() {
		return logger;
	}

	private ChannelInitializer<SocketChannel> initializer;
	
	public BootstrapServer(Logger logger, ChannelInitializer<SocketChannel> initializer) {
		BootstrapServer.logger = logger;
		
		this.initializer = initializer;
	}

	public void run() {
		ServerBootstrap server = new ServerBootstrap();
		server.channel(NioServerSocketChannel.class);
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		server.group(bossGroup, workerGroup);
		server.childHandler(this.initializer);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				BootstrapServer.getLogger().info("Stopping...");
				
				workerGroup.shutdownGracefully();
				bossGroup.shutdownGracefully();
			} catch (Exception e) {
			}
		}));
		
		try {
			ChannelFuture chFuture = server.bind(9001).sync();
			
			BootstrapServer.getLogger().info("Ready! Syncing...");
			
			chFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			//Can't get here.
		}
	}
	
}
