package com.bytedance.netty.io.rpc.registry;

import com.bytedance.netty.io.rpc.protocol.MyProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> serviceMap = new ConcurrentHashMap<>(16);

    private List<String> classNames = new ArrayList<>(16);

    public RegistryHandler() {
        doScanner("com.bytedance.netty.io.rpc.provider");
        addContanier();
    }

    private void addContanier() {
        if (classNames.size() == 0) {
            return;
        }
        try {
            for (String  className: classNames) {
                Class<?> clazz = Class.forName(className);
                Class<?> clazzInterface = clazz.getInterfaces()[0];
                serviceMap.put(clazzInterface.getName(), clazz.newInstance());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void doScanner(String packageName) {
        URL resource = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File file = new File(resource.getFile());
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                doScanner(packageName + "." + f.getName());
            } else {
                classNames.add(packageName + "." + f.getName().replace(".class", ""));
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyProtocol myProtocol = (MyProtocol) msg;
        String className = myProtocol.getClassName();
        String methodName = myProtocol.getMethodName();
        Class<?>[] parameterTypes = myProtocol.getParameterTypes();
        Object[] args = myProtocol.getArgs();

        Object result = null;
        if (serviceMap.containsKey(className)) {
            Object o = serviceMap.get(className);
            Class<?> aClass = o.getClass();
            Method method = aClass.getMethod(methodName, parameterTypes);
             result = method.invoke(o, args);
        }
        ctx.writeAndFlush(result);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
