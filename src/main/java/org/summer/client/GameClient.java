package org.summer.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.summer.net.OperationCodes;
import org.summer.net.dto.LoginRsp;
import org.summer.net.netty.PacketCodec;
import org.summer.net.packet.Packet;
import org.summer.util.JacksonUtil;

//实现一个简单Client，方便测试
@Slf4j
public class GameClient {
    private final ClientConfig config;
    private NioEventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;

    public GameClient(ClientConfig config) {
        this.config = config;
    }

    public void start() {
        Bootstrap bootstrap = new Bootstrap();
        channel = bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new LengthFieldBasedFrameDecoder(60000, 0, 4));
                        p.addLast(new PacketCodec());
                        p.addLast(new SimpleChannelInboundHandler<Packet>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
                                if (packet.getCode() == OperationCodes.LOGIN) {
                                    log.info("login payload: {}", JacksonUtil.toObject(packet.getPayload(), LoginRsp.class));
                                }
                            }
                        });
                    }
                })
                .connect(config.getHost(), config.getPort())
                .syncUninterruptibly()
                .channel();
        log.info("connected to server!");
    }

    //发送请求，等待响应
    public Packet send(Packet packet) {
        return null;
    }

    //发送请求，不等响应
    public void sendAsync(Packet packet) {
        channel.writeAndFlush(packet);
    }

}
