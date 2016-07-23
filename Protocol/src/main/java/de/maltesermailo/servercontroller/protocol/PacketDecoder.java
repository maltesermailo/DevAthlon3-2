package de.maltesermailo.servercontroller.protocol;

import java.util.List;

import de.maltesermailo.servercontroller.protocol.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		if(buf.readableBytes() < 4) {
			return;
		}
		
		int id = buf.readInt();
		
		AbstractPacket packet = AbstractPacket.getPacket(id);
		
		if(packet != null) {
			packet.decode(buf);
			
			out.add(packet);
		}
	}

}
