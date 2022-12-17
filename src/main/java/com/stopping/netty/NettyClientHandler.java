package com.stopping.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Classname: NettyClientHandler
 * @Description: 客户端通道业务处理Handler
 * @Date: 2022/12/14 10:36 上午
 * @author: stopping
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端已连接.." + ctx.toString());
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,服务端..", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.printf("客户端接收服务端消息:" + ctx.toString());
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
    }
}
