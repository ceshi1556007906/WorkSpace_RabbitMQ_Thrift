package com.scme.demo2;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RPCClient {

	private static final String RPC_QUEUE_NAME = "rpc_queue";

	private Connection connection;
	private Channel channel;
	private String replyQueueName;
	private QueueingConsumer consumer;

	public RPCClient() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		// ����MabbitMQ��������ip����������
		factory.setHost("mx.atpiao.net");
		factory.setUsername("learner");
		factory.setPassword("yOdAtp5202Cs");
		// ����һ������
		connection = factory.newConnection();
		// ����һ��Ƶ��
		channel = connection.createChannel();

		// ��������
		channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

		// Ϊÿһ���ͻ��˻�ȡһ������Ļص�����
		replyQueueName = channel.queueDeclare().getQueue();
		// Ϊÿһ���ͻ��˴���һ�������ߣ����ڼ����ص����У���ȡ�����
		consumer = new QueueingConsumer(channel);
		// ����������й���
		channel.basicConsume(replyQueueName, true, consumer);
	}

	/**
	 * ��ȡ쳲��������е�ֵ
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public String call(String message) throws Exception {
		String response = null;
		String corrId = java.util.UUID.randomUUID().toString();

		// ����replyTo��correlationId����ֵ
		BasicProperties props = new BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName).build();

		// ������Ϣ��rpc_queue����
		channel.basicPublish("", RPC_QUEUE_NAME, props, message.getBytes());

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			if (delivery.getProperties().getCorrelationId().equals(corrId)) {
				response = new String(delivery.getBody(), "UTF-8");
				break;
			}
		}

		return response;
	}

	public static void main(String[] args) throws Exception {
		RPCClient fibonacciRpc = new RPCClient();
		String result = fibonacciRpc.call("4");
		System.out.println("fib(4) is " + result);
	}

}
