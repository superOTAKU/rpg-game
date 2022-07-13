package org.summer.net.dto;

import lombok.Data;

@Data
public class Result {
    private boolean status;
    private Integer errorCode;
    private Object data;

}
