package com.bytedance.netty.io.NIO.buffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.EnumSet;

public class BufferTest {

    public static void main(String[] args) throws IOException {
        long s = Instant.now().toEpochMilli();
        FileChannel rfc = FileChannel.open(
                Paths.get("C:\\Users\\64893\\Desktop\\XHX\\06.rar")
                , EnumSet.of(StandardOpenOption.READ));

        FileChannel wfc = FileChannel.open(
                Paths.get("C:\\Users\\64893\\Desktop\\1234567.rar")
                , EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE));


        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int len;
        while ((len = rfc.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            wfc.write(byteBuffer);
            byteBuffer.clear();
        }
        long e = Instant.now().toEpochMilli();
        System.out.println(e - s);
    }


}
