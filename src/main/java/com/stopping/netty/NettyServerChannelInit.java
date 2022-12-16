package com.stopping.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @Classname: NettyServerChannelInit
 * @Date: 2022/12/14 10:17 上午
 * @author: stopping
 */
public class NettyServerChannelInit extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new NettyServerHandler());
    }
}
