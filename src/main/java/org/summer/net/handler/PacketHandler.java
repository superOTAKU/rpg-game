package org.summer.net.handler;

import io.netty.util.concurrent.EventExecutor;
import org.summer.net.GameSession;
import org.summer.net.packet.Packet;

/**
 * 请求处理器
 */
public interface PacketHandler {

    /**
     * 获取请求处理线程
     */
    EventExecutor getEventExecutor(GameSession session, Packet packet);

    /**
     * @param session 客户端连接
     * @param packet 本次收到的数据包
     */
    void handle(GameSession session, Packet packet);

}
