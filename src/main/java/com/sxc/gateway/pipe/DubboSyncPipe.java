package com.sxc.gateway.pipe;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.rpc.service.GenericService;
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
public class DubboSyncPipe {

    @Bean
    @ConditionalOnMissingBean
    public GenericService getInstance() throws InterruptedException {
        ReferenceBean<GenericService> referenceBean = new ReferenceBean();
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://118.178.186.33:2181");
        referenceBean.setRegistry(registryConfig);
        referenceBean.setInterface("com.sxc.demo.provider.TestService");
        referenceBean.setVersion("*");
        referenceBean.setGeneric(true);
        referenceBean.setCheck(false);
        referenceBean.setApplication(new ApplicationConfig("gateway-test"));
        referenceBean.setGroup("dubbo");
        GenericService genericService = referenceBean.get();
        return genericService;

    }

    public ConsumerConfig consumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setClient("suibian");
        consumerConfig.setGroup("HSF");
        return consumerConfig;
    }

}
