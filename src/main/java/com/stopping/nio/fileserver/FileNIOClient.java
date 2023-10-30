package com.stopping.nio.fileserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @Classname: FileNIOClient
 * @Description: TODO
 * @Date: 2022/12/12 1:43 下午
 * @author: stopping
 */
public class FileNIOClient {

    private SocketChannel socketChannel;

    public static void main(String[] args) throws IOException {
        FileNIOClient fileNIOClient = new FileNIOClient();
    }

    /**
     * 文件NIO上传
     *
     *  读取源文件 - FileChannel - SocketChannel - 上传
     *
     * @throws IOException
     */
    public FileNIOClient() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",7003));

        FileChannel fileChannel = new FileInputStream("test.mp4").getChannel();

        long transferCount = 0;
        long start = System.currentTimeMillis();
        if (socketChannel.finishConnect()){
            //transferTo 将channel的数据转移到目标channel上
            transferCount = fileChannel.transferTo(0,fileChannel.size(),socketChannel);
        }
        System.out.println("NIO发送大小:"+transferCount+"，消耗时间:"+ (System.currentTimeMillis() - start));
        fileChannel.close();
        socketChannel.close();
    }
}
