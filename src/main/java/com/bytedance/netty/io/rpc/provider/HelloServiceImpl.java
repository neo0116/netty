package com.bytedance.netty.io.rpc.provider;

import com.bytedance.netty.io.rpc.api.IHelloService;

public class HelloServiceImpl implements IHelloService {
    @Override
    public String say(String name) {
        return "hello " + name;
    }
}
