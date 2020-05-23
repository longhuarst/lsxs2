package com.lsxs.ds.zookeeper;


import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class ZKPool extends GenericObjectPool<ZkClient> {

    public ZKPool() {
        super(new ZKFactory(), new ZKPoolConfig());
    }


    public ZKPool(ZKPoolConfig zkPoolConfig) {
        super(new ZKFactory(), zkPoolConfig);
    }
}
