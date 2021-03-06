package de.maltesermailo.servercontroller.protocol.packet;

import de.maltesermailo.servercontroller.protocol.utils.PacketUtils;
import io.netty.buffer.ByteBuf;

public class PacketStopServer extends AbstractPacket {

	private String name;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void encode(ByteBuf buf) {
		buf.writeInt(2);
		
		PacketUtils.writeString(buf, this.name);
	}

	@Override
	public void decode(ByteBuf buf) {
		this.name = PacketUtils.readString(buf);
	}

	@Override
	public void handle(PacketListener listener) {
		listener.handleStopServer(this);
	}

	

}
