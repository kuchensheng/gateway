package com.sxc.gateway.pipe.factory;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.sxc.gateway.pipe.IPipe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/7/31 22:27
 * version 1.0
 */
public class ClientFactory implements IClientFactory<GenericService> {

    private final Map<String,GenericService> cacheMap = new ConcurrentHashMap<>();

    private IPipe iPipe;

    public ClientFactory(IPipe pipe) {
        this.iPipe =  pipe;
    }

    @Override
    public GenericService getInstance() {

        return null;
    }
}
