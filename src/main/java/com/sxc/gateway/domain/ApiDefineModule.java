package com.sxc.gateway.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * ClassName:ApiDefineModule
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  22:27
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 22:27   kuchensheng    1.0
 */
public class ApiDefineModule implements Serializable {

    private String appName;

    private String interfaceName;

    private String version;

    private String methodName;

    private Map<String,Object> paramMap;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}
