package org.summer.net.dto;

import lombok.Data;
import org.summer.net.packet.Packet;

@Data
public class LoginReq {
    private String token;

    public static LoginReq parse(Packet packet) {
        return null;
    }

}
