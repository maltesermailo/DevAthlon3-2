package de.maltesermailo.servercontroller.protocol.utils;

import io.netty.buffer.ByteBuf;

public class PacketUtils {
	
	public static void writeString(ByteBuf buf, String s) {
		buf.writeInt(s.length());
		buf.writeBytes(s.getBytes());
	}
	
	public static String readString(ByteBuf buf) {
		int len = buf.readInt();
		
		if(len > 0)
			return new String(buf.readBytes(len).array());
		
		return null;
	}

}
