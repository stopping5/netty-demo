package com.stopping.chatRoot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @Classname: ChatRootServer
 * @Description: 通过NIO实现群聊 - 服务端
 * @Date: 2022/12/9 4:42 下午
 * @author: stopping
 */

public class ChatRootServer {
    /**
     * 服务监听
     * */
    private ServerSocketChannel listenServerSocketChannel;
    /**
     * Socket选择器
     * */
    private Selector selector;

    public static final Integer SERVER_PORT = 9999;

    public static final String SERVER_HOST = "127.0.0.1";

    public static void main(String[] args) {
        ChatRootServer chatRootServer = new ChatRootServer();
        chatRootServer.start();
    }

    /**
     * 启动服务监听
     */
    public void start(){
        try {
            listenServerSocketChannel = ServerSocketChannel.open();
            listenServerSocketChannel.bind(new InetSocketAddress(SERVER_PORT));
            listenServerSocketChannel.configureBlocking(false);
            selector = Selector.open();
            listenServerSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() throws IOException {
        System.out.println("服务启动完成，开始监听..");
        while (true){
            //尚未有事件接入
            if (selector.select(1000) == 0){
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //新的客户端接入
                if (selectionKey.isAcceptable()){
                    createClientChannel();
                }
                //客户端新消息
                if (selectionKey.isReadable()){
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer messageBuffer = (ByteBuffer) selectionKey.attachment();
                    channel.read(messageBuffer);
                    String message = messageBuffer.toString();
                    System.out.println("服务端接收消息："+message);
                    //分发
                    Iterator<SelectionKey> selectionKeyIterator = selector.keys().iterator();
                    while (selectionKeyIterator.hasNext()){
                        SelectionKey client = selectionKeyIterator.next();
                        if (client == selectionKey){
                            continue;
                        }
                        SocketChannel clientSocketChannel = (SocketChannel) client.channel();
                        clientSocketChannel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
                    }
                }
                iterator.remove();
            }
        }
    }

    private void createClientChannel() {
        try{
            SocketChannel clientChannel = listenServerSocketChannel.accept();
            clientChannel.configureBlocking(false);
            clientChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //2. 监听消息并且转发到其他客户端

}
