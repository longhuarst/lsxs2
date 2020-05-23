package com.lsxs.ds.netty;

import com.lsxs.ds.zookeeper.MyZkSerializer;
import com.lsxs.ds.zookeeper.ZKPool;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Date;

public class NettyServerHandler extends SimpleChannelInboundHandler<String> {


    static Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    static ZKPool zkPool = new ZKPool();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        logger.info("address:" + ctx.channel().remoteAddress().toString());
        logger.info(msg);

        logger.info("收到数据" + new Date() + Thread.currentThread().getId());

        ctx.pipeline().remove(NettyServerHandler.class);//移除 处理
        BusinessThreadUtil.doBusiness(ctx, msg, new MyRunnable(ctx, msg) {
            @Override
            public void run() {
                ZkClient zkClient = null;
                try {
                    zkClient = zkPool.borrowObject();

//                            logger.info(msg.length());
//                            logger.info("d8ca560e-52b2-46b2-a976-c22187c23cc0".length());

                    logger.info("zk read start");
                    zkClient.setZkSerializer(new MyZkSerializer());
                    String auth = zkClient.readData("/auth/device/" + msg, true);
                    logger.info("zk read ok");
                    if (auth != null) {
                        logger.info(auth);
                        ctx.writeAndFlush(auth + "\r\n");
                        //加密数据流 可靠数据来源， 数据存储在可靠的数据库
                        //不加密的数据 不一定可靠， 可能会存在数据攻击， 不能将数据存储在ssl数据相同的数据库。
                        if (auth.equals("ssl")) {
                            //ssl 认证
                            KeyManagerFactory keyManagerFactory = null;
                            KeyStore keyStore = KeyStore.getInstance("JKS");
                            keyStore.load(new FileInputStream("DeviceServer.jks"), "lsxlsx".toCharArray());
                            keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
                            keyManagerFactory.init(keyStore, "lsxlsx".toCharArray());
                            SslContext sslContext = SslContextBuilder.forServer(keyManagerFactory).build();

                            SSLEngine engine = sslContext.newEngine(ctx.channel().alloc());
                            ctx.pipeline().addFirst("ssl", new SslHandler(engine));
                            ctx.pipeline().addLast(new SslNettyServerBusinessHandler());
                            logger.info("ssl channel init ok");
                        } else if (auth.equals("null")) {
                            logger.info("null ssl device ");
                            ctx.pipeline().addLast(new NettyServerBusinessHandler());
                        }
                    } else {
                        logger.info("device not found !");
                        ctx.writeAndFlush("device not found !\r\n");
                        ctx.close();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    zkPool.returnObject(zkClient);
                }
                logger.info("runnable ... ctx = " + ctx + " msg = " + super.msg);
                super.run();
            }
        });
    }
}
