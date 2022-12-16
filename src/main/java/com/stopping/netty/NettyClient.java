package com.stopping.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Classname: NettyClient
 * @Description: Netty实现网络交互客户端
 * @Date: 2022/12/14 10:31 上午
 * @author: stopping
 */
public class NettyClient {
    private static final String HOST = "127.0.0.1";

    private static final Integer PORT = 6667;

    public static void main(String[] args){
        EventLoopGroup clientGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try{
            ChannelFuture cf = bootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    })
                    .connect(HOST,PORT).sync();
            System.out.println("客户端启动中...");
            cf.channel().closeFuture().sync();
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            System.out.println("clientGroup.shutdownGracefully()");
            clientGroup.shutdownGracefully();
        }
    }
}
