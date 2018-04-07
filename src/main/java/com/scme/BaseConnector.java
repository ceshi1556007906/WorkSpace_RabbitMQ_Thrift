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

		connection = factory.newConnection(); // ��������

		channel = connection.createChannel(); // ����Ƶ��
		// �����������в����ö��г־û����ڶ�������Ϊtrue������ö��г־û���֮��
		channel.queueDeclare(queueName, true, false, false, null);// ��������

	}

	protected void close() { // �ر�Ƶ��������

		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
