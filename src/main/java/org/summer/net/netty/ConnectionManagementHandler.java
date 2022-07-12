package org.summer.net.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.summer.game.Executors;
import org.summer.net.GameSession;
import org.summer.net.GameSessionManager;

public class ConnectionManagementHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        Executors.getInstance().getLoginExecutor().execute(() -> GameSessionManager.getInstance().addSession(ctx.channel(), new GameSession(channel)));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        Executors.getInstance().getLoginExecutor().execute(() -> GameSessionManager.getInstance().removeSession(channel));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
