package com.smona.tool.opendoor.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ReqInviteCode {
    private String authId;
    private String deviceId;
}
