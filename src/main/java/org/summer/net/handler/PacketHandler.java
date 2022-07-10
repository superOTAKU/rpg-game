package org.summer.net.handler;

import org.summer.net.GameSession;
import org.summer.net.packet.Packet;

/**
 * 请求处理
 */
public interface PacketHandler {

    /**
     * @param session 客户端连接
     * @param packet 本次收到的数据包
     */
    void handle(GameSession session, Packet packet);

}
