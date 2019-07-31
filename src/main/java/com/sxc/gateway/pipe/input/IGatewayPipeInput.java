package com.sxc.gateway.pipe.input;

import com.sxc.gateway.domain.ApiDefineModule;
import com.sxc.gateway.domain.AppInfoModule;

public interface IGatewayPipeInput extends IPipeInput{

    /**
     * 调用来源
     * @return
     */
    String AppKey();

    /**
     * 获取调用api定义信息
     * @return
     */
    ApiDefineModule getApiDefine();

    /**
     * 获取调用者信息
     * @return
     */
    AppInfoModule getAppInfo();

}
