package com.lsxs.ds.netty;

import io.netty.channel.ChannelHandlerContext;

public class MyRunnable implements Runnable {

    protected ChannelHandlerContext ctx;
    protected String msg;

    MyRunnable(ChannelHandlerContext ctx, String msg) {
        this.ctx = ctx;
        this.msg = msg;
    }

    public void run() {

    }
}
