package com.example.nettydemo.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author Jerry
 * @date 2018/9/30
 * 描述：
 * @description
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        // 解码编码
        socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
        socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));

        socketChannel.pipeline().addLast(new ServerHandler());
    }
}
