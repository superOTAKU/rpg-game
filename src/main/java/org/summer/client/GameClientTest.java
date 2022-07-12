package org.summer.client;

import org.summer.net.OperationCodes;
import org.summer.net.dto.LoginReq;
import org.summer.net.packet.Packet;
import org.summer.util.JacksonUtil;

public class GameClientTest {

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        config.setHost("localhost");
        config.setPort(10005);
        GameClient client = new GameClient(config);
        client.start();
        Packet req = new Packet();
        req.setCode(OperationCodes.LOGIN);
        LoginReq reqData = new LoginReq();
        reqData.setToken("966eff0e-44cb-4890-9776-fbff528a1d3f");
        req.setPayload(JacksonUtil.toJsonBytes(reqData));
        req.setClientSequence(1);
        client.sendAsync(req);
    }

}
