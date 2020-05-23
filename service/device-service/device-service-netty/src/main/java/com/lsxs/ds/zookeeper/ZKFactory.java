package com.lsxs.ds.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZKFactory extends BasePooledObjectFactory<ZkClient> {


    static Logger logger = LoggerFactory.getLogger(ZKFactory.class);


    public ZkClient create() throws Exception {
        logger.info("zkCLient create()");
        return new ZkClient("127.0.0.1:2181", 3000);
    }

    public PooledObject<ZkClient> wrap(ZkClient zkClient) {
        return new DefaultPooledObject<ZkClient>(zkClient);
    }
}
