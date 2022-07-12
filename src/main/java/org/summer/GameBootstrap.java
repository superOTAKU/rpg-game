package org.summer;

import lombok.extern.slf4j.Slf4j;
import org.summer.database.DatabaseConfig;
import org.summer.database.DatabaseManager;
import org.summer.game.player.NickNameManager;
import org.summer.login.LoginTokenManager;
import org.summer.net.GameServer;
import org.summer.net.GameServerConfig;
import org.summer.net.handler.PacketHandlerRegistry;

/**
 * bootstrap the game
 */
@Slf4j
public class GameBootstrap {

    public static void main(String[] args) {
        //TODO 从配置文件读取配置

        //初始化数据库
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setUrl("jdbc:mysql://localhost:3306/rpg_game?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8");
        dbConfig.setUsername("root");
        dbConfig.setPassword("root");
        dbConfig.setMaxConnCount(10);
        dbConfig.setIdleConnCount(5);
        DatabaseManager.getInstance().init(dbConfig);

        //加载所有的用户昵称到内存
        NickNameManager.getInstance().loadAll();

        //加载所有的PacketHandler
        PacketHandlerRegistry.getInstance().loadAllHandlers();

        String token = LoginTokenManager.getInstance().addAccountToken("test");
        log.info("test login token: {}", token);

        //启动服务器
        GameServerConfig serverConfig = new GameServerConfig();
        serverConfig.setHost("0.0.0.0");
        serverConfig.setPort(10005);
        serverConfig.setBacklog(1024);
        serverConfig.setBossCount(1);
        serverConfig.setWorkerCount(4);
        serverConfig.setSendBufSize(1000000);
        serverConfig.setRecvBufSize(1000000);
        serverConfig.setIdleSeconds(30);
        GameServer gameServer = new GameServer(serverConfig);
        gameServer.start();
    }

}
