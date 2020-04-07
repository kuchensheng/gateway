package org.isycore.gateway.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.isycore.gateway.core.handlers.HttpFileHandler;
import org.isycore.gateway.core.support.HandlerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class GatewayServer implements CommandLineRunner {

    private static GatewayServer gatewayServer;
    @PostConstruct
    public void init() {
        gatewayServer = this;
    }

    @Value("${netty.port:18088}")
    private int port;

    @Autowired
    private List<AbstractGatewayHandler> list;

    private static class SigletonGatewayServer {
        static final GatewayServer instance = new GatewayServer();
    }

    public static GatewayServer getInstance() {
        return SigletonGatewayServer.instance;
    }

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;

    public GatewayServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                try {
                    socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    socketChannel.pipeline().addLast(new StringDecoder());
                    socketChannel.pipeline().addLast(new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingResolver(this.getClass().getClassLoader())));
                    socketChannel.pipeline().addLast(new ObjectEncoder());
                    addHttpReqHandler(socketChannel.pipeline());
                    doHanlder(list, HandlerConstants.EnumHandlerType.PRE_TYPE,socketChannel.pipeline());
                    doHanlder(list, HandlerConstants.EnumHandlerType.ROUTE_TYPE,socketChannel.pipeline());
                } catch (Exception e) {
                    doHanlder(list, HandlerConstants.EnumHandlerType.ERROR_TYPE,socketChannel.pipeline());
                } finally {
                    doHanlder(list, HandlerConstants.EnumHandlerType.POST_TYPE,socketChannel.pipeline());
                }
            }
        });
    }

    private void addHttpReqHandler(ChannelPipeline pipeline) {
        pipeline.addLast("http-decoder",new HttpRequestDecoder())
                .addLast("http-aggregator",new HttpObjectAggregator(65536))
                .addLast("http-encoder",new HttpResponseEncoder())
                .addLast("http-chunked",new ChunkedWriteHandler())
                .addLast("fileServerHandler",new HttpFileHandler());
    }

    public void start() throws InterruptedException {
        this.channelFuture = serverBootstrap.bind(port).sync();
        System.out.println("netty启动完毕,port = "+gatewayServer.port);
        this.channelFuture.channel().closeFuture().sync().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println(future.channel().toString() + "链路关闭");
                Runtime.getRuntime().addShutdownHook(new Thread(() ->{
                    System.out.println("Shutdown Hook executor start...");
                    System.out.println("Netty NioEventLoopGroup shutdownGracefully...");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("ShutdownHook execute end...");
                },""));
                TimeUnit.SECONDS.sleep(7);
                System.exit(0);

            }
        });
    }
//    public void runner(List<AbstractGatewayHandler> list) {
//        //创建Reactor线程组，boss用于服务端接收客户端请求，worker用于SocketChannel的网络读写
//                serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel socketChannel) throws Exception {
//                        try {
//                            doHanlder(list, HandlerConstants.EnumHandlerType.PRE_TYPE,socketChannel.pipeline());
//                            doHanlder(list, HandlerConstants.EnumHandlerType.ROUTE_TYPE,socketChannel.pipeline());
//                        } catch (Exception e) {
//                            doHanlder(list, HandlerConstants.EnumHandlerType.ERROR_TYPE,socketChannel.pipeline());
//                        } finally {
//                            doHanlder(list, HandlerConstants.EnumHandlerType.POST_TYPE,socketChannel.pipeline());
//                        }
//                    }
//                });
//    }

    private void doHanlder(List<AbstractGatewayHandler> handlerList, HandlerConstants.EnumHandlerType type, ChannelPipeline pipeline) throws Exception {
        List<AbstractGatewayHandler> result = new ArrayList<>();
        handlerList.forEach(handler ->{
            if (handler.handlerType() == type) {
                result.add(handler);
            }
        });
        result.sort(new Comparator<AbstractGatewayHandler>() {
            @Override
            public int compare(AbstractGatewayHandler o1, AbstractGatewayHandler o2) {
                return o1.handlerOrder() - o2.handlerOrder();
            }
        });
        result.forEach(handler ->{
            pipeline.addLast(handler);
        });
    }

    @Override
    public void run(String... args) throws Exception {
        this.start();
    }
}
