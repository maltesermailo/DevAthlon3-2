package de.maltesermailo.servercontroller.protocol.packet;

import de.maltesermailo.servercontroller.protocol.utils.PacketUtils;
import io.netty.buffer.ByteBuf;

public class PacketCreateServer extends AbstractPacket {

	private String name;
	
	private String baseDir;
	
	private String xmx;
	private String xms;
	
	private int port;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBaseDir() {
		return this.baseDir;
	}
	
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
	
	public String getXmx() {
		return this.xmx;
	}
	
	public void setXmx(String xmx) {
		this.xmx = xmx;
	}
	
	public String getXms() {
		return this.xms;
	}
	
	public void setXms(String xms) {
		this.xms = xms;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public void encode(ByteBuf buf) {
		buf.writeInt(3);
		
		PacketUtils.writeString(buf, this.name);
		
		PacketUtils.writeString(buf, this.baseDir);
		
		PacketUtils.writeString(buf, this.xmx);
		PacketUtils.writeString(buf, this.xms);
		
		buf.writeInt(this.port);
	}

	@Override
	public void decode(ByteBuf buf) {
		this.name = PacketUtils.readString(buf);
		
		this.baseDir = PacketUtils.readString(buf);
		
		this.xmx = PacketUtils.readString(buf);
		this.xms = PacketUtils.readString(buf);
		
		this.port = buf.readInt();
	}

	@Override
	public void handle(PacketListener listener) {
		listener.handleCreateServer(this);
	}

}
