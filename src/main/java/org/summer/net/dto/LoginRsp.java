package org.summer.net.dto;

import lombok.Data;
import org.summer.net.GameSession;

@Data
public class LoginRsp {
    private Long playerId;
    private GameSession.SessionState state;
    //登录时要下发的一堆数据...
    private Object playerData;
}
