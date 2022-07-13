package org.summer.net.dto;

import lombok.Data;
import org.summer.database.entity.VocationType;

@Data
public class SelectVocationReq {
    private VocationType vocation;
}
