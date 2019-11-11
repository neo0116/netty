package com.bytedance.netty.io.rpc.consumer;

import com.bytedance.netty.io.rpc.protocol.MyProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ConsumerProxy {

    public static <T> T create(Class<T> clazz) {
        Invoke invoke = new Invoke();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces(), invoke);
    }

    static class Invoke implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass().equals(Object.class)) {
                return method.invoke(this, args);
            } else {
                MyProtocol myProtocol = new MyProtocol();
                myProtocol.setClassName(method.getDeclaringClass().getName());
                myProtocol.setMethodName(method.getName());
                myProtocol.setParameterTypes(method.getParameterTypes());
                myProtocol.setArgs(args);

                ConsumerHandler consumerHandler = new ConsumerHandler();
                EventLoopGroup loopGroup = new NioEventLoopGroup();
                try {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(loopGroup)
                            .channel(NioSocketChannel.class)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline pipeline = ch.pipeline();
                                    pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
                                            Integer.MAX_VALUE, 0,
                                            4, 0,
                                            4));
                                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                                    pipeline.addLast("encoder", new ObjectEncoder());
                                    pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                                    pipeline.addLast("handler", consumerHandler);
                                }
                            })
                            .option(ChannelOption.TCP_NODELAY, true);
                    ChannelFuture future = bootstrap.connect("localhost", 8080).sync();
                    future.channel().writeAndFlush(myProtocol).sync();
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    loopGroup.shutdownGracefully();
                }
                return consumerHandler.getResult();
            }
        }
    }



}
