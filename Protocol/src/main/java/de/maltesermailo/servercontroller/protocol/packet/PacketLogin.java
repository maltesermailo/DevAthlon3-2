package de.maltesermailo.servercontroller.protocol.packet;

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;

public class PacketLogin extends AbstractPacket {

	private String token;
	
	private boolean loggedOn;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public void setLoggedOn(boolean loggedOn) {
		this.loggedOn = loggedOn;
	}
	
	public boolean isLoggedOn() {
		return loggedOn;
	}
	
	@Override
	public void encode(ByteBuf buf) {
		//Packet Id
		buf.writeInt(0);
		
		buf.writeBoolean(this.loggedOn);
		buf.writeBytes(this.token.getBytes());
	}

	@Override
	public void decode(ByteBuf buf) {
		this.loggedOn = buf.readBoolean();
		
		ByteBuffer buffer = ByteBuffer.allocate(buf.readableBytes());
		buf.readBytes(buffer);
		
		this.token = new String(buffer.array());
	}

	@Override
	public void handle(PacketListener listener) {
		listener.handleLogin(this);
	}

}
