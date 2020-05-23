package com.lsxs.ds.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.HashMap;

public class RMQFactory extends BasePooledObjectFactory<Channel> {
    public Channel create() throws Exception {
        System.out.println("Rabbitmq client create()");
        ConnectionFactory cf = new ConnectionFactory();

        cf.setHost("127.0.0.1");
        cf.setPort(18100);
        cf.setVirtualHost("/");
        cf.setUsername("admin");
        cf.setPassword("admin");
        cf.setAutomaticRecoveryEnabled(true);
        cf.setNetworkRecoveryInterval(10 * 1000);

        Channel channel = cf.newConnection("main").createChannel();

//        channel.exchangeDeclare("rabbitmq.DeviceServer.ssl.tx", BuiltinExchangeType.DIRECT, true, false, false, new HashMap<String, Object>());
//        channel.exchangeDeclare("rabbitmq.DeviceServer.null.tx", BuiltinExchangeType.DIRECT, true, false, false, new HashMap<String, Object>());
//


        // 声明队列 (队列名, 是否持久化, 是否排他, 是否自动删除, 队列属性);
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare("rabbitmq.DeviceServer.ssl.tx", true, false, false, new HashMap<String, Object>());
        AMQP.Queue.DeclareOk declareOk2 = channel.queueDeclare("rabbitmq.DeviceServer.null.tx", true, false, false, new HashMap<String, Object>());

        // 声明交换机 (交换机名, 交换机类型, 是否持久化, 是否自动删除, 是否是内部交换机, 交换机属性);
        channel.exchangeDeclare("rabbitmq.DeviceServer.ssl.tx", BuiltinExchangeType.DIRECT, true, false, false, new HashMap<String, Object>());
        channel.exchangeDeclare("rabbitmq.DeviceServer.null.tx", BuiltinExchangeType.DIRECT, true, false, false, new HashMap<String, Object>());

        // 将队列Binding到交换机上 (队列名, 交换机名, Routing key, 绑定属性);
        channel.queueBind(declareOk.getQueue(), "rabbitmq.DeviceServer.ssl.tx", "pub", new HashMap<String, Object>());
        channel.queueBind(declareOk2.getQueue(), "rabbitmq.DeviceServer.null.tx", "pub", new HashMap<String, Object>());


        return channel;
    }

    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<Channel>(channel);
    }
}
