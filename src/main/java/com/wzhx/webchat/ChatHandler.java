package com.wzhx.webchat;

import java.time.LocalDateTime;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 自定义助手 TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 * 
 * @author 12192
 *
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

//	用于记录和管理所有客户端的channel
	private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

		// 获取客户端传过来的消息
		String content = msg.text();
		System.out.println("接收到的数据是：" + content);

//		for (Channel channel : clients) {
//			channel.writeAndFlush(new TextWebSocketFrame("服务器接收到消息[" + LocalDateTime.now() + "]:" + content));
//		}
		
		clients.writeAndFlush(new TextWebSocketFrame("服务器接收到消息[" + LocalDateTime.now() + "]:" + content));

	}

	/**
	 * 当客户端连接服务器后（打开连接） 获取客户端的channel，并且添加到ChannelGroup中管理
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		clients.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// 当触发handlerRemoved的时候，ChannelGroup会自动remove对应客户端的channel
//		clients.remove(ctx.channel());
		System.out.println("客户端断开连接，对应的长id为：" + ctx.channel().id().asLongText());
		System.out.println("客户端断开连接，对应的短id为：" + ctx.channel().id().asShortText());
	}

}
