package com.sxc.gateway.pipe;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.mermaid.framework.core.mvc.APIResponse;
import com.sxc.gateway.domain.ApiDefineModule;
import com.sxc.gateway.pipe.input.HttpGatewayPipeInput;
import com.sxc.gateway.pipe.input.IPipeInput;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
@Configuration
public class DubboSyncPipe implements SuccessSyncPipe {

    @Override
    public APIResponse doPipe(IPipeInput input) {
        HttpGatewayPipeInput parse = (HttpGatewayPipeInput) input;
        ApiDefineModule apiDefine = parse.getApiDefine();
        try {
            return getInstance().$invoke(apiDefine.getMethodName(),);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GenericService getInstance(String interfaceName) throws InterruptedException {
        ReferenceBean<GenericService> referenceBean = new ReferenceBean();
        referenceBean.setRegistry(new RegistryConfig("zookeeper://118.178.186.33:2181"));
        referenceBean.setInterface(interfaceName);
//        referenceBean.setVersion("1.0");
        referenceBean.setGeneric(true);
        referenceBean.setCheck(false);
        referenceBean.setConsumer(consumerConfig());
        referenceBean.setApplication(new ApplicationConfig("gateway-test"));
        referenceBean.setGroup("HSF");
        GenericService genericService = referenceBean.get();
        TimeUnit.SECONDS.sleep(2);
        genericService = referenceBean.get();
        return genericService;
    }

    public GenericService getInstance() throws InterruptedException {
        return getInstance("com.sxc.demo.provider.TestService");

    }

    public ConsumerConfig consumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setClient("suibian");
        consumerConfig.setGroup("HSF");
        return consumerConfig;
    }

}
