package org.summer.net.handler.impl;

import org.summer.net.GameSession;
import org.summer.net.OpCode;
import org.summer.net.OperationCodes;
import org.summer.net.handler.PacketHandler;
import org.summer.net.packet.Packet;

@OpCode(code = OperationCodes.LOGIN)
public class LoginHandler implements PacketHandler {

    @Override
    public void handle(GameSession session, Packet packet) {

    }

}
