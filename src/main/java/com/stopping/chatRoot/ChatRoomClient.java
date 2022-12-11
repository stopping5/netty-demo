package com.stopping.chatRoot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 *
 */
public class ChatRoomClient {

    public static final Integer SERVER_PORT = 9999;

    public static final String SERVER_HOST = "127.0.0.1";

    private SocketChannel clientChannel;

    /**
     * 初始化
     */
    public ChatRoomClient(){
        try{
            clientChannel = SocketChannel.open();
            clientChannel.configureBlocking(false);
            if (!clientChannel.connect(new InetSocketAddress(SERVER_HOST,SERVER_PORT))){
                while (!clientChannel.finishConnect()){
                    System.out.println("连接服务端"+SERVER_HOST+":"+SERVER_PORT);
                }
            }

            new Thread(()->{
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (true){
                    try{
                        clientChannel.read(buffer);
                        if (!buffer.hasRemaining()){
                            System.out.println(new String(buffer.array()));
                        }
                        buffer.clear();
                        Thread.sleep(2000);
                    }catch (IOException | InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void send(String msg){
        try{
            InetSocketAddress holder =(InetSocketAddress) clientChannel.getLocalAddress();
            String append = holder.getHostString() + ":" + holder.getPort() + "说:\n" + msg;
            clientChannel.write(ByteBuffer.wrap(append.getBytes(StandardCharsets.UTF_8)));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
