package org.summer.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.summer.net.netty.ConnectionManagementHandler;
import org.summer.net.netty.PacketCodec;
import org.summer.net.netty.PacketMessageHandler;

/**
 * 游戏服务器
 */
@Slf4j
public class GameServer {
    private final GameServerConfig config;
    private NioEventLoopGroup boss;
    private NioEventLoopGroup worker;

    public GameServer(GameServerConfig config) {
        this.config = config;
    }

    public void start() {
        boss = new NioEventLoopGroup(config.getBossCount());
        worker = new NioEventLoopGroup(config.getWorkerCount());
        ServerBootstrap b = new ServerBootstrap();
        ChannelFuture bindFuture = b.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, config.getBacklog())
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_SNDBUF, config.getSendBufSize())
                .childOption(ChannelOption.SO_RCVBUF, config.getRecvBufSize())
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel clientChannel) {
                        ChannelPipeline p = clientChannel.pipeline();
                        p.addLast(new LengthFieldBasedFrameDecoder(60000, 0, 4));
                        p.addLast(new PacketCodec());
                        p.addLast(new IdleStateHandler(0, 0, config.getIdleSeconds()));
                        p.addLast(new ConnectionManagementHandler());
                        p.addLast(new PacketMessageHandler());
                    }
                })
                .bind(config.getHost(), config.getPort())
                .syncUninterruptibly();
        if (bindFuture.isSuccess()) {
            log.info("GameServer started!");
        } else {
            log.error("Game Server start fail!", bindFuture.cause());
            throw new IllegalStateException("GameServer start fail, reason: " + bindFuture.cause().getMessage());
        }
    }

    public void shutdown() {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
        log.info("GameServer stopped!");
    }

}
