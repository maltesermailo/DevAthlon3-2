package de.maltesermailo.servercontroller.protocol.packet;

import java.nio.ByteBuffer;

import de.maltesermailo.servercontroller.protocol.utils.PacketUtils;
import io.netty.buffer.ByteBuf;

public class PacketDeleteServer extends AbstractPacket {

	private String name;
	
	private boolean deleteFiles;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isDeleteFiles() {
		return deleteFiles;
	}
	
	public void setDeleteFiles(boolean deleteFiles) {
		this.deleteFiles = deleteFiles;
	}
	
	@Override
	public void encode(ByteBuf buf) {
		buf.writeInt(4);
		
		buf.writeBoolean(this.deleteFiles);
		
		PacketUtils.writeString(buf, this.name);
	}

	@Override
	public void decode(ByteBuf buf) {
		this.deleteFiles = buf.readBoolean();
		
		this.name = PacketUtils.readString(buf);
	}

	@Override
	public void handle(PacketListener listener) {
		listener.handleDeleteServer(this);
	}

}
