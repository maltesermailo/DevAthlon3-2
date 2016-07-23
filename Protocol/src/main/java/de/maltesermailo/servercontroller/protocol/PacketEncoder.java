package de.maltesermailo.servercontroller.protocol;

import de.maltesermailo.servercontroller.protocol.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<AbstractPacket> {

	@Override
	protected void encode(ChannelHandlerContext ctx, AbstractPacket packet, ByteBuf buf) throws Exception {
		packet.encode(buf);
	}

}
