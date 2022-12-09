package com.stopping.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Classname: NIOServer
 * @Description: NIO服务demo
 * @Date: 2022/12/9 10:52 上午
 * @author: stopping
 */
public class NIOServer {

    public static void main(String[] args) throws Exception {
        start();
    }

    public static void start() throws Exception {
        //新建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        //配置非阻塞
        serverSocketChannel.configureBlocking(false);
        //select 多路复用器
        Selector selector = Selector.open();
        //将serverSocketChannel注册到selector中，订阅的事件为连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            if (selector.select(1000) == 0){
                continue;
            }
            //如果有事件发生则获取selectionKey
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //发生连接事件，将连接的channel注册到读取事件selector
                if (selectionKey.isAcceptable()){
                    //客户端的SocketChannel，注册到监听
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //发现读事件,通过selectionKey获取Channel实现数据读写
                if (selectionKey.isReadable()){
                    SocketChannel readSocketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer readBuffer = (ByteBuffer) selectionKey.attachment();
                    readSocketChannel.read(readBuffer);
                    System.out.println("读取数据:"+ new String(readBuffer.array()));
                    readBuffer.clear();
                }
                //移除SelectKey避免重复调用
                iterator.remove();
            }
        }
    }
}
