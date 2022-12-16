package com.stopping.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @Classname: NettyServerHandler
 * @Description: 通过业务逻辑实现
 * @Date: 2022/12/14 10:19 上午
 * @author: stopping
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 接收消息事件
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //block(ctx,msg);

//        synToEventLoop(ctx, msg);
        synToSchedule(ctx,msg);
    }

    /**
     * 通过加入eventLoop实现任务异步处理
     * @param ctx
     * @param msg
     */
    private void synToEventLoop(ChannelHandlerContext ctx, Object msg) {
        ctx.channel().eventLoop().execute(()->{
            try {
                block(ctx, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 提交任务到Schedule
     * @param ctx
     * @param msg
     */
    private void synToSchedule(ChannelHandlerContext ctx, Object msg) {
        ctx.channel().eventLoop().schedule(()->{
            try {
                block(ctx, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        },5, TimeUnit.SECONDS);
    }

    /**
     * 阻塞的方法，回导致handler主线程阻塞
     * @param ctx
     * @param msg
     * @throws Exception
     */
    private void block(ChannelHandlerContext ctx, Object msg)throws Exception{
        //线程休息十秒
        Thread.sleep(10 * 1000);
        System.out.println("服务端 ctx" + ctx);
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端"+ctx.channel().remoteAddress()+"消息:" + byteBuf.toString(StandardCharsets.UTF_8));

    }

    /**
     * 内容读取结束后
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端读取消息结束，返回客户端消息");
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello,客户端", CharsetUtil.UTF_8));
    }
}
