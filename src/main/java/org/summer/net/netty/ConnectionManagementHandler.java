package org.summer.net.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.summer.game.Executors;
import org.summer.net.GameSession;
import org.summer.net.GameSessionManager;

@Slf4j
public class ConnectionManagementHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        //新增session直接IO线程操作
        GameSessionManager.getInstance().addSession(ctx.channel(), new GameSession(channel));
        log.info("channel {} connected", channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        //移除session在登录线程做
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
