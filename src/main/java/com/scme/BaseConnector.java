package com.scme;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class BaseConnector {

	protected Channel channel;

	protected Connection connection;

	protected String queueName;

	public BaseConnector(String queueName) throws IOException, TimeoutException {

		this.queueName = queueName;

		ConnectionFactory factory = new ConnectionFactory();

		factory.setHost("mx.atpiao.net");
		factory.setUsername("learner");
		factory.setPassword("yOdAtp5202Cs");

		connection = factory.newConnection(); // 创建连接

		channel = connection.createChannel(); // 创建频道
		// 声明创建队列并设置队列持久化（第二个参数为true，代表该队列持久化反之）
		channel.queueDeclare(queueName, true, false, false, null);// 声明队列

	}

	protected void close() { // 关闭频道及链接

		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
