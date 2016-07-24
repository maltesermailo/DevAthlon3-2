package de.maltesermailo.servercontroller.protocol.packet;

import de.maltesermailo.servercontroller.protocol.utils.PacketUtils;
import io.netty.buffer.ByteBuf;

public class PacketStatus extends AbstractPacket {
	
	private String name;
	
	private boolean exists;
	private boolean isAlive;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isExisting() {
		return exists;
	}
	
	public void setExisting(boolean exists) {
		this.exists = exists;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
	@Override
	public void encode(ByteBuf buf) {
		buf.writeInt(5);
		
		buf.writeBoolean(this.exists);
		buf.writeBoolean(this.isAlive);
		
		PacketUtils.writeString(buf, this.name);
	}

	@Override
	public void decode(ByteBuf buf) {
		this.exists = buf.readBoolean();
		this.isAlive = buf.readBoolean();
		
		this.name = PacketUtils.readString(buf);
	}

	@Override
	public void handle(PacketListener listener) {
		listener.handleStatus(this);
	}

}
