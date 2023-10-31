package com.stopping.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Objects;

/**
 * @Classname: NettyServer
 * @Description: 通过Netty搭建网络交互服务
 * @Date: 2022/12/14 9:55 上午
 * @author: stopping
 */
public class NettyServer {

    private static final String HOST = "127.0.0.1";

    private static final Integer PORT = 6667;

    public static void main(String[] args) throws InterruptedException {
        //创建线程组
        //noiEventLoopGroup 处理IO操作的多线程事件的Loop
        //therefore two NioEventLoopGroup will be used.
        // The first one, often called 'boss', accepts an incoming connection.
        // The second one, often called 'worker',handles the traffic of the accepted connection once the boss accepts the connection and registers the accepted connection to the worker.
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //服务器启动对象，用于配置netty
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture cf = serverBootstrap
                //设置group，服务单有两个group，客户端只有一个
                .group(boosGroup,workerGroup)
                //声明服务端通道的实现
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new NettyServerChannelInit())
                .bind(HOST,PORT)
                .addListener(future -> {
                    if (future.isDone() && future.isSuccess()){
                        System.out.println("绑定端口成功");
                    }
                    if (future.isDone() && Objects.nonNull(future.cause())){
                        System.out.println("绑定端口失败");
                    }
                })
                .sync().addListener(future -> {
                    if (future.isDone() && future.isSuccess()){
                        System.out.println("服务端已经启动");
                    }
                    if (future.isDone() && Objects.nonNull(future.cause())){
                        System.out.println("服务端启动失败");
                    }
                });
        //关闭通道进行监听
        cf.channel().closeFuture().sync();

    }
}
