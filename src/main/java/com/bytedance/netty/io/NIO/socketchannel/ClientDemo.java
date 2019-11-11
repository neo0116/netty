package com.bytedance.netty.io.NIO.socketchannel;


import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class ClientDemo {

    public static void main(String[] args) throws InterruptedException {
        try {
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 8080));

            socketChannel.configureBlocking(false);
//            socketChannel.register(open, SelectionKey.OP_WRITE);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            new Thread(() -> {
                boolean flag = true;
                while (flag) {
                    byteBuffer.clear();
                    int read = 0;
                    try {
                        read = socketChannel.read(byteBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (read > 0) {
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(),0, read));
                    }
                }
            }).start();

            ByteBuffer bf = ByteBuffer.allocate(1024);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String s = bufferedReader.readLine();
                if (null != s) {
                    bf.clear();
                    bf.put(s.getBytes());
                    bf.flip();
                    socketChannel.write(bf);
                }
            }




        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
