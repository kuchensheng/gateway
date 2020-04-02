package org.isycore.gateway.core.handlers;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.tomcat.jni.Error;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class HttpFileHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        if (null == HttpMethod.resolve(request.method().name())) {
            sendError(ctx,HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        final String uri = request.uri();
        final String path = sanitizeUri(uri);
    }

    private String sanitizeUri(String uri) throws Exception {
        try {
            uri = URLDecoder.decode(uri,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri,"ISO-8859-1");
            } catch (UnsupportedEncodingException ex) {
                throw new Exception();
            }
        }

        if (!uri.startsWith("/")) {
            return null;
        }
        uri  = uri.replace('/', File.separatorChar);
        if (uri.contains(File.separator +'.') || uri.contains('.'+File.separator)
                || uri.startsWith(".") || uri.endsWith(".")) {
            return null;
        }
        return uri;
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status,Unpooled.copiedBuffer("Failure: "+status.toString()+"\r\n",CharsetUtil.UTF_8));
        response.headers().set(HttpHeaders.CONTENT_TYPE,"text-plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
