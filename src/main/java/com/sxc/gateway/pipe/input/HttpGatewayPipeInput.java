package com.sxc.gateway.pipe.input;

import com.alibaba.fastjson.JSONObject;
import com.mermaid.framework.util.StringUtils;
import com.sxc.gateway.domain.ApiDefineModule;
import com.sxc.gateway.domain.AppInfoModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:HttpGatewayPipeInput
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  22:40
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 22:40   kuchensheng    1.0
 */
public class HttpGatewayPipeInput implements IGatewayPipeInput {

    private static final Logger logger = LoggerFactory.getLogger(HttpGatewayPipeInput.class);

    private HttpServletRequest httpRequest;

    private Map<String,String[]> httpRequestHeaderMap;

    private String interfaceName;

    private String method;

    private ApiDefineModule apiDefineModule;


    public HttpGatewayPipeInput(HttpServletRequest httpRequest,String interfaceName,String method) {
        this.httpRequest = httpRequest;
        this.httpRequestHeaderMap = new HttpServletRequestWrapper(httpRequest).getParameterMap();
        this.interfaceName = interfaceName;

        this.method = method;

    }

    @Override
    public String AppKey() {
        return getParameter("appKey");
    }

    @Override
    public ApiDefineModule getApiDefine() {
        if(null == apiDefineModule) {
            apiDefineModule = new ApiDefineModule();
            apiDefineModule.setAppName(getParameter("appName"));
            apiDefineModule.setInterfaceName(interfaceName);
            apiDefineModule.setMethodName(method);
            Map<String, String[]> parameterMap = httpRequest.getParameterMap();
            Map<String,Object> map = new HashMap<>();
            for (Map.Entry<String,String[]> entry : parameterMap.entrySet()) {
                map.put(entry.getKey(),entry.getValue()[0]);
            }
            apiDefineModule.setParamMap(map);
        }
        return apiDefineModule;
    }

    @Override
    public AppInfoModule getAppInfo() {
        return null;
    }

    @Override
    public String getTraceId() {
        //TODO 生成tradeId
        String tradeId = httpRequest.getHeader("traceId");
        if(StringUtils.isEmpty(tradeId)) {
            tradeId = genericTraceId();
            setHttpHeader("traceId",tradeId);
        }
        return tradeId;
    }

    private String genericTraceId() {
        //TODO 生成tarceId
        return null;
    }

    void setHttpHeader(String name,String value) {
        logger.info("设置请求头信息，name={},value={}",name, value);
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(httpRequest) {
            @Override
            public String getParameter(String name) {
                return super.getParameter(name);
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                HashMap<String, String[]> newMap = new HashMap<>();
                newMap.putAll(super.getParameterMap());
                newMap.put(name,new String[]{value}) ;
                return Collections.unmodifiableMap(newMap);
            }
        };

    }

    @Override
    public String getParameter(String key) {
        String value = httpRequest.getParameter(key);
        if(StringUtils.isEmpty(value)) {
            value = httpRequest.getHeader(key);
        }
        return value;
    }
}
