package com.sxc.gateway.pipe.input;

public interface IPipeInput {

    /**
     * 获取请求的唯一ID
     * @return
     */
    String getTraceId();

    /**
     * 获取调用参数
     * @param key
     * @return
     */
    String getParameter(String key);
}
