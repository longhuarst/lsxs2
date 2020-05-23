package com.lsxs.ds.netty;

import com.lsxs.ds.rabbitmq.RMQPool;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SslNettyServerBusinessHandler extends SimpleChannelInboundHandler<String> {


    static Logger logger = LoggerFactory.getLogger(SslNettyServerBusinessHandler.class);

    private static ExecutorService excutor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(100000));

    static RMQPool rmqPool = new RMQPool();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        logger.info("address:" + ctx.channel().remoteAddress().toString());

        //可靠的ssl解码过的数据
        logger.info("SslNettyServerBusinessHandler");
        ctx.writeAndFlush(msg + "\r\n");


        if (msg.startsWith("pub")) {
            final ChannelHandlerContext fctx = ctx;
            final String fmsg = msg;
            excutor.submit(new Runnable() {
                public void run() {
                    Channel channel = null;
                    try {
                        channel = rmqPool.borrowObject();
                        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").build();

                        channel.basicPublish("rabbitmq.DeviceServer.ssl.tx", "pub", false, basicProperties, fmsg.getBytes());

                        fctx.write("ok\r\n");
                        logger.info("send data to amqp -> rabbitmq");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        rmqPool.returnObject(channel);
                    }
                }
            });
        }


    }
}
