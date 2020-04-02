package org.isycore.gateway.core.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.isycore.gateway.core.AbstractGatewayHandler;
import org.isycore.gateway.core.support.HandlerConstants;

public class GatewayServerHandler extends AbstractGatewayHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8");
        logger.info("receive order : {}",body);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public HandlerConstants.EnumHandlerType handlerType() {
        return HandlerConstants.EnumHandlerType.PRE_TYPE;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public int handlerOrder() {
        return Integer.MIN_VALUE;
    }



}
