package com.rain.testnetty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class NettyServer {
    private int port;
    public NettyServer(int port) {
        this.port = port;
    }
    public void start() {
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();


        ChannelFuture future = null;
        try {
            ServerBootstrap bootstrap = new ServerBootstrap().group(boosGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast(new ServerHandler());
                            ch.pipeline().addLast(new ServerHandler2());
                        }
                    });
            future = bootstrap.bind(port).sync();
            System.out.println("Server start listen at " + port);
//            阻止方法执行到finall中的关闭代码
//            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new NettyServer(port).start();
    }

}
