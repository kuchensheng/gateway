package com.sxc.gateway.pipe;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.mermaid.framework.core.mvc.APIResponse;
import com.sxc.gateway.domain.ApiDefineModule;
import com.sxc.gateway.pipe.factory.IClientFactory;
import com.sxc.gateway.pipe.input.HttpGatewayPipeInput;
import com.sxc.gateway.pipe.input.IPipeInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:DubboSyncPipe
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  11:40
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 11:40   kuchensheng    1.0
 */
@Component
public class DubboSyncPipe implements SuccessSyncPipe {

    @Autowired
    private IClientFactory<GenericService> clientFactory;

    @Override
    public APIResponse doPipe(IPipeInput input) {
        HttpGatewayPipeInput parse = (HttpGatewayPipeInput) input;
        ApiDefineModule apiDefine = parse.getApiDefine();
        GenericService genericService = null;

        genericService = clientFactory.getInstance(apiDefine);


    }

}
