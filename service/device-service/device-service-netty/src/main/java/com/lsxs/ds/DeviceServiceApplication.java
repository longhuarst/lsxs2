package com.lsxs.ds;

import com.lsxs.ds.netty.NettyServer;

public class DeviceServiceApplication {

    public static void main(String[] args) throws Exception {

        NettyServer server = new NettyServer();
        server.start(22000);

    }
}
