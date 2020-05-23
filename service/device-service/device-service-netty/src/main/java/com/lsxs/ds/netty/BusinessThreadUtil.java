package com.lsxs.ds.netty;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BusinessThreadUtil {

    private static ExecutorService excutor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(100000));

    public static void doBusiness(ChannelHandlerContext ctx, String msg, Runnable runnable) {
        //异步线程处理
        excutor.submit(runnable);
    }
}
