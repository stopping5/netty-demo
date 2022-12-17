package com.stopping.nio.fileserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 通过NIO的方式实现文件传输
 * @Classname: FileNIOServer
 * @Date: 2022/12/12 11:37 上午
 * @author: stopping
 */
public class FileNIOServer {
    private ServerSocketChannel socketChannel;

    public static void main(String[] args) throws IOException {
        FileNIOServer fileNIOServer = new FileNIOServer();
    }

    public FileNIOServer() throws IOException {
        socketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(7003));
        FileOutputStream fos = new FileOutputStream(new File("nioTest.mp4"));
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while (true) {
            SocketChannel accept = socketChannel.accept();
            int read = 0;
            while (read !=-1 ) {
                read = accept.read(byteBuffer);
                fos.getChannel().write(byteBuffer);
                byteBuffer.rewind();
            }
        }
    }
}
