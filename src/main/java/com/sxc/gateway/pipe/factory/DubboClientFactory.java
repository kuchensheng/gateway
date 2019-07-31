package com.sxc.gateway.pipe.factory;

import com.alibaba.cloud.dubbo.metadata.DubboRestServiceMetadata;
import com.alibaba.cloud.dubbo.metadata.RequestMetadata;
import com.alibaba.cloud.dubbo.metadata.RestMethodMetadata;
import com.alibaba.cloud.dubbo.metadata.repository.DubboServiceMetadataRepository;
import com.alibaba.cloud.dubbo.service.DubboGenericServiceExecutionContextFactory;
import com.alibaba.cloud.dubbo.service.DubboGenericServiceFactory;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.sxc.gateway.domain.ApiDefineModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UriComponents;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/7/31 22:34
 * version 1.0
 */
@Component
public class DubboClientFactory implements IClientFactory<GenericService> {

    @Autowired
    private RegistryConfig registryConfig;

    @Autowired
    private ApplicationConfig applicationConfig;

    private final DubboServiceMetadataRepository repository;

    private final DubboGenericServiceFactory serviceFactory;

    private final DubboGenericServiceExecutionContextFactory contextFactory;

    private final PathMatcher pathMatcher = new AntPathMatcher();

    private final Map<String, Object> dubboTranslatedAttributes = new HashMap<>();


    public DubboClientFactory(DubboServiceMetadataRepository repository,
                              DubboGenericServiceFactory serviceFactory,
                              DubboGenericServiceExecutionContextFactory contextFactory) {
        this.repository = repository;
        this.serviceFactory = serviceFactory;
        this.contextFactory = contextFactory;
        dubboTranslatedAttributes.put("protocol", "dubbo");
        dubboTranslatedAttributes.put("cluster", "failover");

    }

    @Override
    public GenericService getInstance(ApiDefineModule apiDefineModule) throws ServletException {
        repository.initialize(apiDefineModule.getInterfaceName());
        // 将 HttpServletRequest 转化为 RequestMetadata
        RequestMetadata clientMetadata = buildRequestMetadata(request, restPath);

        DubboRestServiceMetadata dubboRestServiceMetadata = repository.get(serviceName,
                clientMetadata);

        if (dubboRestServiceMetadata == null) {
            // if DubboServiceMetadata is not found, executes next
            throw new ServletException("DubboServiceMetadata can't be found!");
        }

        RestMethodMetadata dubboRestMethodMetadata = dubboRestServiceMetadata
                .getRestMethodMetadata();
    }

    private RequestMetadata buildRequestMetadata(HttpServletRequest request,
                                                 String restPath) {
        UriComponents uriComponents = fromUriString(request.getRequestURI()).build(true);
        RequestMetadata requestMetadata = new RequestMetadata();
        requestMetadata.setPath(restPath);
        requestMetadata.setMethod(request.getMethod());
        requestMetadata.setParams(getParams(request));
        requestMetadata.setHeaders(getHeaders(request));
        return requestMetadata;
    }

    private Map<String, List<String>> getHeaders(HttpServletRequest request) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headerValues = request.getHeaders(headerName);
            map.put(headerName, Collections.list(headerValues));
        }
        return map;
    }

    private Map<String, List<String>> getParams(HttpServletRequest request) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            map.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }
        return map;
    }
}
