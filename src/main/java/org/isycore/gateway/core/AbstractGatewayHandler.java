package org.isycore.gateway.core;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.isycore.gateway.core.support.HandlerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGatewayHandler extends ChannelInboundHandlerAdapter implements GatewayHandler {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractGatewayHandler.class);

    public abstract HandlerConstants.EnumHandlerType handlerType();

    public abstract int handlerOrder();

}
