package com.stopping.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Classname: HttpChannelHandlerInit
 * @Date: 2022/12/15 3:18 下午
 * @author: stopping
 */
public class HttpChannelHandlerInit extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //Netty提供处理Http的编辑器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        //自定义handler
        pipeline.addLast("ChannelHandler",new HttpChannelHandler());
    }
}


