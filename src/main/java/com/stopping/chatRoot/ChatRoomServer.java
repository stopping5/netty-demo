package com.stopping.chatRoot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @Classname: ChatRoomServer
 * @Description: 通过NIO实现群聊 - 服务端
 * @Date: 2022/12/9 4:42 下午
 * @author: stopping
 */

public class ChatRoomServer {
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

    public ChatRoomServer(){
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
                    Channel channel = selectionKey.channel();
                    if(channel instanceof SocketChannel){
                        SocketChannel socketChannel = (SocketChannel) channel;
                        InetSocketAddress inetSocketAddress = (InetSocketAddress)socketChannel.getLocalAddress();
                        distributeMsg(selectionKey,inetSocketAddress.getHostName()+"上线");
                    }
                }
                //客户端新消息
                if (selectionKey.isReadable()){
                    readMsg(selectionKey);

                }
                iterator.remove();
            }
        }
    }

    /**
     * 读取消息
     * @param selectionKey 当前selectionKey
     * @throws IOException
     */
    private void readMsg(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer messageBuffer = (ByteBuffer) selectionKey.attachment();
        try{
            channel.read(messageBuffer);
            String sendMsg = new String(messageBuffer.array());
            System.out.println(sendMsg);
            distributeMsg(selectionKey,sendMsg);
            messageBuffer.clear();
        }catch (IOException e){
            System.out.println(channel.getRemoteAddress() + "离线");
            selectionKey.cancel();
            channel.close();
        }
    }

    /**
     * 转发消息
     * @param self
     * @param msg
     */
    private void distributeMsg(SelectionKey self,String msg){
        Set<SelectionKey> allClient = selector.keys();
        Iterator<SelectionKey> iterator = allClient.iterator();
        while (iterator.hasNext()){
            SelectionKey receiveClient = iterator.next();
            Channel channel = receiveClient.channel();
            if (channel instanceof SocketChannel){
                SocketChannel dest = (SocketChannel) channel;
                if (channel == self.channel()){
                    send(dest,"发送成功");
                    continue;
                }
                send(dest,msg);
            }
        }
    }

    public void send(SocketChannel clientChannel,String msg){
        try{
            clientChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        }catch (IOException e){
            e.printStackTrace();
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


}
