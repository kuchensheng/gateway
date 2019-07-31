package com.sxc.gateway.pipe.factory;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.sxc.gateway.domain.ApiDefineModule;

import javax.servlet.ServletException;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/7/31 22:28
 * version 1.0
 */
public interface IClientFactory<T> {

    T getInstance(ApiDefineModule apiDefineModule) throws ServletException;
}
