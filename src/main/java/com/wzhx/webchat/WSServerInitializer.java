package com.wzhx.webchat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
/**
 * WSServer的channel的初始化处理器
 * @author 12192
 *
 */
public class WSServerInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		// websocket基于http协议，所以需要有http编解码器
		pipeline.addLast(new HttpServerCodec());
		// 添加对写大数据流的支持
		pipeline.addLast(new ChunkedWriteHandler());
		// 对httpMessage进行聚合，聚合成FullHttpRequest或者FullHttpResponse
		// 几乎在netty中的编程，都会用到此handler
		pipeline.addLast(new HttpObjectAggregator(1024 * 64));
		
		// =============== 以上用于支持http协议 ==============================
		// ================= 以下是支持httpWebsocket ================
		/**
		 * websocket服务器处理的协议，用于指定给客户端连接访问的路由：/webchat
		 * 本handler会帮你处理一些复杂、繁重的事情
		 * 会帮你处理握手操作：handshaking(close\ping\pong) ping + pong = 心跳
		 * 对于websocket来讲都是进行frames传输的，不同的数据类型对应的frames也不同
		 */
		pipeline.addLast(new WebSocketServerProtocolHandler("/webchat"));
		
		// 自定义的handler
		pipeline.addLast(new ChatHandler());
	}

}
