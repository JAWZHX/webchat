package com.wzhx.webchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 这是一个websocket服务器
 * 
 * @author 12192
 *
 */
public class WSServer {

	public static void main(String[] args) throws Exception {
//		1、创建主从线程池组
		EventLoopGroup mainGroup = new NioEventLoopGroup();
		EventLoopGroup subGroup = new NioEventLoopGroup();

//		2、创建服务器启动类
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(mainGroup, subGroup).channel(NioServerSocketChannel.class).childHandler(new WSServerInitializer());
			ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
			channelFuture.channel().closeFuture();
		} finally {
			mainGroup.shutdownGracefully();
			subGroup.shutdownGracefully();
		}
	}

}
