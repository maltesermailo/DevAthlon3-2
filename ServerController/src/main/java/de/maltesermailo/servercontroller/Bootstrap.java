package de.maltesermailo.servercontroller;

import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.maltesermailo.servercontroller.protocol.BootstrapServer;
import de.maltesermailo.servercontroller.protocol.handler.BootstrapChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * Hello world!
 *
 */
public class Bootstrap {
	
	private static Logger logger;
	
	public static Logger getLogger() {
		return logger;
	}
	
	public static void main(String[] args) {
		Bootstrap.logger = LogManager.getLogger();
		
		Bootstrap.getLogger().info("Loading servers...");
		
		ServerController controller = new ServerController("1.10.2");
		controller.loadServers();
		
		Bootstrap.getLogger().info("Generating certificate...");
		
		SelfSignedCertificate ssc = null;
		try {
			ssc = new SelfSignedCertificate("localhost", new SecureRandom(), 2048);
		} catch (CertificateException e) {
			Bootstrap.getLogger().fatal("Could not generate certificate.", e);
			Bootstrap.getLogger().fatal("Shutting down...");
			
			System.exit(1);
		}
		
		SslContext sslCtx = null;
		try {
			sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
		} catch (SSLException e) {
			Bootstrap.getLogger().fatal("Could not prepare SSL Context.", e);
			Bootstrap.getLogger().fatal("Shutting down...");
			
			System.exit(1);
		}
		
		BootstrapServer server = new BootstrapServer(logger, new BootstrapChannelInitializer(sslCtx));
		
		Thread serverThread = new Thread(server, "Server thread");
		serverThread.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> controller.saveServers()));
	}
	
}
