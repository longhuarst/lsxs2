package com.lsxs.ds.rabbitmq;


import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class RMQPool extends GenericObjectPool<Channel> {


    public RMQPool() {
        super(new RMQFactory(), new RMQPoolConfig());
    }


    public RMQPool(RMQPoolConfig rmqPoolConfig) {
        super(new RMQFactory(), rmqPoolConfig);
    }


}
