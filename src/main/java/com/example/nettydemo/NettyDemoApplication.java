package com.example.nettydemo;

import com.example.nettydemo.server.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

@SpringBootApplication
public class NettyDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NettyDemoApplication.class, args);
    }

    @Value("${n.port}")
    private int port;

    @Value("${n.url}")
    private String url;

    private NettyServer socketServer;

    @Override
    public void run(String... args) throws Exception {

        InetSocketAddress address = new InetSocketAddress(url,port);

        ChannelFuture future = socketServer.run(address);

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
//                socketServer.destroy();
          }
        });
        future.channel().closeFuture().syncUninterruptibly();
    }
}
