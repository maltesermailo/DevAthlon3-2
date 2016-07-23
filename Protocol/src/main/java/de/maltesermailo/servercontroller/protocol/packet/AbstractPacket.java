package de.maltesermailo.servercontroller.protocol.packet;

import java.util.HashMap;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractPacket {
	
	private static final HashMap<Integer, Class<? extends AbstractPacket>> REGISTRY = new HashMap<>();
	
	private static final HashMap<ChannelHandlerContext, PacketListener> LISTENERS = new HashMap<>();
	
	public static void registerPacket(int id, Class<? extends AbstractPacket> packet) {
		AbstractPacket.REGISTRY.put(id, packet);
	}
	
	public static AbstractPacket getPacket(int id) {
		if(AbstractPacket.REGISTRY.containsKey(id)) {
			try {
				return AbstractPacket.REGISTRY.get(id).newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				System.err.println(String.format("[ERR] Could not resolve Packet type for id '%d'", id));
			}
		}
		
		return null;
	}
	
	public static void registerListener(ChannelHandlerContext ctx, PacketListener listener) {
		AbstractPacket.LISTENERS.put(ctx, listener);
	}
	
	public static void unregisterListener(ChannelHandlerContext ctx) {
		AbstractPacket.LISTENERS.remove(ctx);
	}
	
	public static PacketListener getListener(ChannelHandlerContext ctx) {
		return AbstractPacket.LISTENERS.get(ctx);
	}
	
	public abstract void encode(ByteBuf buf);
	public abstract void decode(ByteBuf buf);
	
	public abstract void handle(PacketListener listener);
	
}
