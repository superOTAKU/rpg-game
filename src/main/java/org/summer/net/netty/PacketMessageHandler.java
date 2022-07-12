package org.summer.net.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.summer.net.GameSession;
import org.summer.net.GameSessionManager;
import org.summer.net.OperationCodes;
import org.summer.net.handler.PacketHandler;
import org.summer.net.handler.PacketHandlerRegistry;
import org.summer.net.packet.Packet;

@Slf4j
public class PacketMessageHandler extends SimpleChannelInboundHandler<Packet> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
        GameSession session = GameSessionManager.getInstance().getSession(ctx);
        PacketHandler handler = PacketHandlerRegistry.getInstance().getHandler(packet.getCode());
        if (handler == null) {
            //找不到处理器
            return;
        }
        //这里不是登录线程，不过session.state是volatile，所以不会有问题
        GameSession.SessionState state = session.getState();
        switch (state) {
            case PENDING_LOGIN:
                if (packet.getCode() != OperationCodes.LOGIN) {
                    return;
                } else {
                    break;
                }
            case LOADING_LOGIN_DATA:
                return;
            case ACTIVE:
                if (packet.getCode() == OperationCodes.LOGIN) {
                    return;
                } else {
                    break;
                }
        }
        handler.getEventExecutor(session, packet).execute(() -> {
            try {
                handler.handle(session, packet);
            } catch (Exception e) {
                log.error("channel {} handle code[{}] error", ctx.channel(), packet.getCode(), e);
            }
        });

    }

}
