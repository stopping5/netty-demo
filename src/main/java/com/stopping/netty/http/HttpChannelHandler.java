package com.stopping.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * @Classname: HttpChannelHandler
 * @Description: TODO
 * @Date: 2022/12/15 3:34 下午
 * @author: stopping
 */
public class HttpChannelHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断是否HTTP请求
        if(msg instanceof HttpRequest){
            System.out.println("msg类型" + msg.getClass() + ",客户端地址：" + ctx.channel().remoteAddress());
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_8);
            httpResponse(ctx,byteBuf);
        }
    }

    private void httpResponse(ChannelHandlerContext ctx, ByteBuf byteBuf){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,byteBuf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.readableBytes());
        ctx.writeAndFlush(response);
    }
}
