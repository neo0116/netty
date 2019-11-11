package com.bytedance.netty.io.NIO.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class ServerDemo {

    public static void main(String[] args) {
        try {
            //打开通道（银行开门营业）
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //通道连接地址（营业地址）
            serverSocketChannel.bind(new InetSocketAddress(8080));
            //设置非阻塞
            serverSocketChannel.configureBlocking(false);

            //选择器（银行大堂经理）
            Selector selector = Selector.open();
            //注册
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            //缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            while (true) {
                //银行大堂经理开始叫号
                selector.select();
                //银行大堂经理拿到所有的号
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    //准备就绪状态
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = ssc.accept();
                        //设置非阻塞
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    //可读状态
                    else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        byteBuffer.clear();
                        int read = socketChannel.read(byteBuffer);
                        if (read > 0) {
                            byteBuffer.flip();
                            System.out.println(new String(byteBuffer.array(), 0, read));
                            socketChannel.register(selector, SelectionKey.OP_WRITE);
                        }
                    }
                    //可写状态
                    else if (selectionKey.isWritable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        byteBuffer.clear();
                        ByteBuffer buffer = byteBuffer.put("收到了~~".getBytes());
                        buffer.flip();
                        socketChannel.write(buffer);
                        socketChannel.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
