package com.rain.testnetty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.nio.charset.Charset;

public class FirstClientHandler  extends ChannelInboundHandlerAdapter {
    private static  int count=0;
    private static final Logger logger = LoggerFactory.getLogger(FirstClientHandler.class);

    private static  int  readcount=0;
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        for (int i = 0; i < 1000; i++) {
            ByteBuf buffer = getByteBuf(ctx);
            ctx.channel().writeAndFlush(buffer);
            System.out.println("send complete..."+","+count);
//            ctx.channel().config().getWriteBufferWaterMark();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        super.channelRead(ctx, msg);

        ByteBuf in = (ByteBuf) msg;
        logger.info("msg type: " + (in.hasArray()?"堆内存":"直接内存"));

        System.out.println(" FirstClientHandler channelRead==:"+msg+",readcount="+readcount);
        readcount++;

    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        String content="count="+count+"你好，我的名字是1234567!";
        byte[] bytes =content.getBytes(Charset.forName("utf-8"));
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bytes);
        count++;
        return buffer;
    }
}
