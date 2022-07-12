package org.summer.game.player;

import io.netty.util.concurrent.EventExecutor;
import lombok.Data;
import org.summer.database.entity.Player;
import org.summer.database.entity.PlayerState;
import org.summer.game.Executors;
import org.summer.net.GameSession;
import org.summer.net.dto.LoginRspPacket;

/**
 * 玩家在内存中的数据结构
 */
@Data
public class PlayerCache {
    private Long playerId;
    private String accountId;
    private volatile PlayerCacheState state;
    private Player player;
    //玩家的状态极大影响协议的处理，因此玩家状态作为volatile字段
    private volatile PlayerState playerState;
    private volatile GameSession session;

    public enum PlayerCacheState {
        INIT, LOADING, ACTIVE
    }

    public EventExecutor executor() {
        if (playerId == null) {
            return null;
        } else {
            return Executors.getInstance().getPlayerExecutor(playerId);
        }
    }

    public void startLoginLoading() {
        state = PlayerCacheState.LOADING;
        Executors.getInstance().getCommonGroup().submit(() -> {
            //loading necessary data for login and pending process
        }).addListener(f -> {
           if (f.isSuccess()) {
               //用户线程设置player状态
               executor().submit(() -> state = PlayerCacheState.ACTIVE).syncUninterruptibly();
               //登录线程设置session状态
               Executors.getInstance().getLoginExecutor().execute(() -> {
                   session.setState(GameSession.SessionState.ACTIVE);
                   session.sendPacket(new LoginRspPacket(this));
               });
           } else {
               //登录失败，clear everything...
                clearMe();
           }
        });
    }

    public void clearMe() {
        Executors.getInstance().getLoginExecutor().submit(() -> session.close());
    }

}
