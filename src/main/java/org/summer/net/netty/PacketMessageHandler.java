package org.summer.net.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.summer.net.GameSession;
import org.summer.net.GameSessionManager;
import org.summer.net.handler.PacketHandler;
import org.summer.net.handler.PacketHandlerRegistry;
import org.summer.net.packet.Packet;

public class PacketMessageHandler extends SimpleChannelInboundHandler<Packet> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
        GameSession session = GameSessionManager.getInstance().getSession(ctx);
        PacketHandler handler = PacketHandlerRegistry.getInstance().getHandler(packet.getCode());
        //TODO 判断消息是否可处理
        GameSession.SessionState state = session.getState();
        handler.getEventExecutor(session, packet).execute(() -> handler.handle(session, packet));
    }

}
