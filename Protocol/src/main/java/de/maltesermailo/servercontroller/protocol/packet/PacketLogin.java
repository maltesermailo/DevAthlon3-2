package de.maltesermailo.servercontroller.protocol.packet;

import de.maltesermailo.servercontroller.protocol.utils.PacketUtils;

import io.netty.buffer.ByteBuf;

public class PacketLogin extends AbstractPacket {

	private String token;
	
	private boolean loggedOn;
	
	public String getToken() {
		return this.token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public boolean isLoggedOn() {
		return this.loggedOn;
	}
	
	public void setLoggedOn(boolean loggedOn) {
		this.loggedOn = loggedOn;
	}
	
	@Override
	public void encode(ByteBuf buf) {
		//Packet Id
		buf.writeInt(0);
		
		buf.writeBoolean(this.loggedOn);
		
		PacketUtils.writeString(buf, this.token);
	}

	@Override
	public void decode(ByteBuf buf) {
		this.loggedOn = buf.readBoolean();
		
		this.token = PacketUtils.readString(buf);
	}

	@Override
	public void handle(PacketListener listener) {
		listener.handleLogin(this);
	}

}
