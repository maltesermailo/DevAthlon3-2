package de.maltesermailo.servercontroller.protocol.packet;

public interface PacketListener {
	
	
	
	public void handleLogin(PacketLogin packet);
	
	public void handleStartServer(PacketStartServer packet);
	public void handleStopServer(PacketStopServer packet);

}
