package org.isycore.gateway.core.handlers;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyHttpServerHandler.class);

    private static final String CONTENT_TYPE = "Content-Type";
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        FullHttpResponse response = null;
        Map<String,Object> paramMap = getParamsFromChannel(request,HttpMethod.valueOf(request.method().name()));
        logger.info("method is {},param is {}",request.method().name(),JSONObject.toJSONString(paramMap));
        switch (HttpMethod.valueOf(request.method().name())) {
            case GET:
                ByteBuf buf = Unpooled.copiedBuffer("get method over",CharsetUtil.UTF_8);
                response = responseOK(HttpResponseStatus.OK,buf);
                break;
            default:
                break;
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private FullHttpResponse responseOK(HttpResponseStatus status, ByteBuf content) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        if (null != content) {
            response.headers().set(HttpHeaders.CONTENT_TYPE,"text/plain;charset=UTF-8");
            response.headers().set(HttpHeaders.CONTENT_LENGTH,response.content().readableBytes());
        }
        return response;

    }

    private Map<String,Object> getParamsFromChannel(FullHttpRequest request ,HttpMethod method) {
        Map<String,Object> params = new HashMap<>();
        switch (method) {
            case GET:
                QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
                Map<String, List<String>> paramList = decoder.parameters();
                for (Map.Entry<String,List<String>> entry : paramList.entrySet()) {
                    params.put(entry.getKey(),entry.getValue().get(0));
                }
                break;
            default:
                String strContentType = request.headers().get(HttpHeaders.CONTENT_TYPE).trim();

                switch (strContentType) {
                    case MediaType.APPLICATION_FORM_URLENCODED_VALUE :
                        params = getFromParams(request);
                        break;
                    case MediaType.APPLICATION_JSON_VALUE :
                        params = getJsonFromParams(request);
                        break;
                }
                break;
        }
        return params;
    }

    private Map<String, Object> getJsonFromParams(FullHttpRequest request) {
        Map<String, Object> result = new HashMap<>();
        ByteBuf content = request.content();
        byte[] ctx = new byte[content.readableBytes()];
        content.readBytes(ctx);
        String strContent = new String(ctx, CharsetUtil.UTF_8);

        JSONObject jsonObject = JSONObject.parseObject(strContent);
        result.putAll(jsonObject);
        return result;
    }

    private Map<String, Object> getFromParams(FullHttpRequest request) {
        Map<String, Object> result = new HashMap<>();
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false),request);
        List<InterfaceHttpData> post = decoder.getBodyHttpDatas();
        post.forEach(data ->{
            MemoryAttribute attribute = (MemoryAttribute) data;
            result.put(attribute.getName(),attribute.getValue());
        });
        return result;
    }
}
