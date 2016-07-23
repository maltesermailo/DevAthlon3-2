package de.maltesermailo.servercontroller.protocol.packet;

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;

public class PacketStartServer extends AbstractPacket {

	private String name;
	
	@Override
	public void encode(ByteBuf buf) {
		buf.writeInt(1);
		
		buf.writeBytes(this.name.getBytes());
	}

	@Override
	public void decode(ByteBuf buf) {
		ByteBuffer buffer = ByteBuffer.allocate(buf.readableBytes());
		buf.readBytes(buffer);
		
		this.name = new String(buffer.array());
	}

	@Override
	public void handle(PacketListener listener) {
		listener.handleStartServer(this);
	}

}
