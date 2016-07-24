package de.maltesermailo.servercontroller.protocol.packet;

public interface PacketListener {
	
	
	
	public void handleLogin(PacketLogin packet);
	
	public void handleStartServer(PacketStartServer packet);
	public void handleStopServer(PacketStopServer packet);

	public void handleCreateServer(PacketCreateServer packet);
	public void handleDeleteServer(PacketDeleteServer packet);

	public void handleStatus(PacketStatus packet);
}
