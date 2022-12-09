package com.stopping.nio.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @Classname: NIOClient
 * @Description: NIO客户端
 * @Date: 2022/12/9 2:54 下午
 * @author: stopping
 */
public class NIOClient {

    static SocketChannel clientChannel;

    public static void main(String[] args) throws Exception {
        start();
        InputStream is = System.in;
        while (true){
            Scanner scanner = new Scanner(is);
            String sendStr = scanner.next();
            System.out.println("客户端输入:"+sendStr);
            send(sendStr);
        }
    }

    public static void start() throws Exception {
        clientChannel = SocketChannel.open();
        clientChannel.configureBlocking(false);
        if (!clientChannel.connect(new InetSocketAddress("127.0.0.1",9999))){
            while (!clientChannel.finishConnect()){
                System.out.println("linking...");
            }
        }
        System.out.println("客户端连接服务端成功");
        String first = "hello,server";
        clientChannel.write(ByteBuffer.wrap(first.getBytes(StandardCharsets.UTF_8)));
    }

    public static void send(String str) throws IOException {
        clientChannel.write(ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8)));
    }
}
