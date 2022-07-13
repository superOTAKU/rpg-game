package org.summer.net.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.summer.net.GameSession;

@NoArgsConstructor
@Builder
@Data
public class LoginRsp {
    private Long playerId;
    private GameSession.SessionState state;
    //登录时要下发的一堆数据...
    private Object playerData;
}
