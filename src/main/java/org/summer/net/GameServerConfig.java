package org.summer.net;

import lombok.Data;

/**
 * 服务器参数配置
 */
@Data
public class GameServerConfig {
    private Integer bossCount;
    private Integer workerCount;
    private Integer backlog;
    private Integer sendBufSize;
    private Integer recvBufSize;
    private String host;
    private Integer port;
    private Integer idleSeconds;
}
