package com.scme.demo2;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RPCServer {

	private static final String RPC_QUEUE_NAME = "rpc_queue";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		// ����MabbitMQ��������ip����������
		factory.setHost("mx.atpiao.net");
		factory.setUsername("learner");
		factory.setPassword("yOdAtp5202Cs");
		// ����һ������
		Connection connection = factory.newConnection();
		// ����һ��Ƶ��
		Channel channel = connection.createChannel();

		// ��������
		channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

		// ���ƣ�ÿ������һ�������߷���1����Ϣ
		channel.basicQos(1);

		// Ϊrpc_queue���д��������ߣ����ڴ�������
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

		System.out.println(" [x] Awaiting RPC requests");

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();

			// ��ȡ�����е�correlationId����ֵ�����������õ������Ϣ��correlationId������
			BasicProperties props = delivery.getProperties();
			BasicProperties replyProps = new BasicProperties.Builder().correlationId(props.getCorrelationId()).build();
			// ��ȡ�ص���������
			String callQueueName = props.getReplyTo();

			String message = new String(delivery.getBody(), "UTF-8");

			System.out.println(" [.] fib(" + message + ")");

			// ��ȡ���
			String response = "" + fib(Integer.parseInt(message));
			// �ȷ��ͻص����
			channel.basicPublish("", callQueueName, replyProps, response.getBytes());
			// ���ֶ�������Ϣ����
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
	}

	/**
	 * ����쳲��������еĵ�n��
	 * 
	 * @param n
	 * @return
	 * @throws Exception
	 */
	private static int fib(int n) throws Exception {
		if (n < 0)
			throw new Exception("��������n������ڵ���0");
		if (n == 0)
			return 0;
		if (n == 1)
			return 1;
		return fib(n - 1) + fib(n - 2);
	}

}
