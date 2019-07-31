package com.sxc.gateway.domain;

import java.io.Serializable;

/**
 * ClassName:AppInfoModule
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  22:34
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 22:34   kuchensheng    1.0
 */
public class AppInfoModule implements Serializable {

    private String deviceUUID;

    private String clientIp;

    private String clientPort;

    private String clientVersion;

    private String clientSysName;

    private String clientSysVersion;

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientPort() {
        return clientPort;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getClientSysName() {
        return clientSysName;
    }

    public void setClientSysName(String clientSysName) {
        this.clientSysName = clientSysName;
    }

    public String getClientSysVersion() {
        return clientSysVersion;
    }

    public void setClientSysVersion(String clientSysVersion) {
        this.clientSysVersion = clientSysVersion;
    }
}
