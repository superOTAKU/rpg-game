package org.summer.game.map;

import lombok.Getter;
import lombok.Setter;

/**
 * 单张地图配置
 */
@Setter
@Getter
public class MapConfig {
    private Integer id;
    //地图掩码，true代表能走，false代表不能走
    private Boolean[][] mask;
    private int maxX;
    private int maxY;
}
