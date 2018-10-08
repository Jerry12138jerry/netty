package com.example.nettydemo.client;

import com.example.nettydemo.server.ServerChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author Jerry
 * @date 2018/9/30
 * 描述：
 * @description
 */
public class NettyClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private static Bootstrap bootstrap;

    private static ChannelFuture future;

    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static void init(){

        try {

            logger.info("init...");

            bootstrap = new Bootstrap();

            bootstrap.group(workerGroup);

            bootstrap.channel(NioSocketChannel.class);

            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    // 解码编码
                    socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                    socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                    socketChannel.pipeline().addLast(new ClientHandler());
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Object startAndWrite(InetSocketAddress address, Object send) throws InterruptedException {
        init();
        future = bootstrap.connect(address).sync();
        //传数据给服务端
        future.channel().writeAndFlush(send);
        future.channel().closeFuture().sync();
        return future.channel().attr(AttributeKey.valueOf("Attribute_key")).get();
    }

    public static void main(){

        InetSocketAddress address = new InetSocketAddress("127.0.0.1",7000);

    }
}
