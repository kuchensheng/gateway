package com.sxc.gateway.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.mermaid.framework.core.mvc.APIResponse;
import com.sxc.demo.provider.MyJob;
import com.sxc.demo.provider.TestService;
import com.sxc.demo.provider.TypeEnum;
import com.sxc.gateway.pipe.DubboSyncPipe;
import com.sxc.gateway.pipe.IPipe;
import com.sxc.gateway.pipe.input.HttpGatewayPipeInput;
import com.sxc.gateway.pipe.input.IGatewayPipeInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * ClassName:ApiController
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  11:42
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 11:42   kuchensheng    1.0
 */
@RestController
public class ApiController {


    @Autowired
    private List<IPipe> pipeList;

    @Autowired
    private DubboSyncPipe dubboSyncPipe;

    @Reference(version = "1.0",group = "dubbo")
    private TestService testService;

    @RequestMapping(value = "/test5/{interface}/{method}",method = RequestMethod.GET)
    public APIResponse getExamp5(@PathVariable String interfaceName, @PathVariable String method) throws InterruptedException {
        IGatewayPipeInput input = new HttpGatewayPipeInput(
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(),
                interfaceName,method);
        APIResponse response = null;
        for (IPipe pipe :pipeList) {
            response = pipe.doPipe(input);
            break;
        }
        return new APIResponse("200",response);
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String getExamp() throws InterruptedException {
        GenericService instance = dubboSyncPipe.getInstance();
        Object response = instance.$invoke("sayHello",new String[]{"java.lang.String"},new Object[]{"xixi"});
        return String.valueOf(response);
    }

    @RequestMapping(value = "/test1",method = RequestMethod.GET)
    public String getExamp1() throws InterruptedException {
        GenericService instance = dubboSyncPipe.getInstance();
        Object response = instance.$invoke("sayHello",new String[]{"com.sxc.demo.provider.TypeEnum"},new Object[]{TypeEnum.TYPE_1});
        return String.valueOf(response);
    }

    @RequestMapping(value = "/test2",method = RequestMethod.GET)
    public String getExamp2() throws InterruptedException {
        GenericService instance = dubboSyncPipe.getInstance();
        MyJob myJob = new MyJob();
        myJob.setCheck(false);
        myJob.setCurrentTime(System.currentTimeMillis());
        myJob.setId(111);
        myJob.setMax(3.1415d);
        myJob.setName("淦你妹");
        myJob.setNow(new Date());
        myJob.setPrice(3.45f);
        Object response = instance.$invoke("sayHello",new String[]{"com.sxc.demo.provider.MyJob"},new Object[]{myJob});
        return String.valueOf(response);
    }

    @RequestMapping(value = "/test3",method = RequestMethod.GET)
    public String getExamp3() throws InterruptedException {
        GenericService instance = dubboSyncPipe.getInstance();
        MyJob myJob = new MyJob();
        myJob.setCheck(false);
        myJob.setCurrentTime(System.currentTimeMillis());
        myJob.setId(222);
        myJob.setMax(3.1415d);
        myJob.setName("淦你妹鸭");
        myJob.setNow(new Date());
        myJob.setPrice(3.45f);
        Object response = instance.$invoke("sayHello",
                new String[]{"java.lang.Integer","java.lang.String","com.sxc.demo.provider.TypeEnum",
                        "com.sxc.demo.provider.MyJob"},
                new Object[]{1,"库陈胜", TypeEnum.TYPE_2,myJob});
        return String.valueOf(response);
    }

}
