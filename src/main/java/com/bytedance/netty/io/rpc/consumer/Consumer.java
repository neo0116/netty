package com.bytedance.netty.io.rpc.consumer;

import com.bytedance.netty.io.rpc.api.IHelloService;

public class Consumer {

    public static void main(String[] args) {
        IHelloService iHelloService = ConsumerProxy.create(IHelloService.class);
        String say = iHelloService.say("路人是妖怪");

        System.out.println(say);
    }

}
