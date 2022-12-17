package com.stopping.netty.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 通过Netty实现HTTP协议
 * @Classname: HttpServerNetty
 * @Date: 2022/12/15 3:12 下午
 * @author: stopping
 */
public class HttpServerNetty {

    public static void main(String[] args) {
        new HttpServerNetty();
    }

    private static final String HOST = "127.0.0.1";

    private static final Integer PORT = 9898;

    public HttpServerNetty(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture cf;
        try {
            cf = bootstrap
                .group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpChannelHandlerInit())
                .bind(HOST,PORT)
                .sync();
            System.out.println("启动HTTP服务成功...");
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
