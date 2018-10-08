package com.example.nettydemo.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author Jerry
 * @date 2018/9/28
 * 描述：
 */
public class NettyServer {

    private static Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    /**
     *
     * @param address
     * @return
     */
    public ChannelFuture run(InetSocketAddress address) {

        ChannelFuture future = null;
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            future = bootstrap.bind(address).syncUninterruptibly();
            channel = future.channel();
        }catch (Exception e){
            logger.info("Netty start error:",e);
        } finally {
            if (future != null && future.isSuccess()){
                logger.info("Netty server listening " + address.getHostName() + " on port " + address.getPort() + " and ready for connections...");
            }else{
                logger.error("Netty server start up Error!");
            }
        }

        return future;
    }

    public void destory(){

        logger.info("Shutdown Netty Server...");
        if(channel != null) { channel.close();}
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        logger.info("Shutdown Netty Server Success!");

    }
}
