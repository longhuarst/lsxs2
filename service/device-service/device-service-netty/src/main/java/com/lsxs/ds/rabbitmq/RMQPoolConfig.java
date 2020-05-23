package com.lsxs.ds.rabbitmq;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class RMQPoolConfig extends GenericObjectPoolConfig {

    public RMQPoolConfig() {
        setMinIdle(5);
        setTestOnBorrow(true);

    }
}
