package com.scme;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.SerializationUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class ReceiverHandler extends BaseConnector implements Runnable, Consumer {

	private MessageInfo messageInfo = new MessageInfo();

	private int hashCode;

	private volatile Thread thread;

	public ReceiverHandler(String queueName) throws IOException, TimeoutException {
		super(queueName);
	}

	public void receiveMessage() {

		hashCode = Thread.currentThread().hashCode(); // ���ֲ�ͬ�������̵����
		try {
			// ��Ϣ���ת����Ĭ��Ϊ1
			channel.basicQos(1);
			// ָ�����Ѷ��в�����Ӧ��ģʽ���ڶ�������Ϊfalse��������Ӧ�𣬷���֮��
			String op_result = channel.basicConsume(queueName, false, this); // ָ�����Ѷ���
			if ("".equals(op_result)) {
				System.out.println("BasicConsumeConfig Consumer Queue Error!");
			}
		} catch (IOException e) {
			System.out.println("Consumer Delivery Error,Msg info:" + e.getMessage());
		} catch (Exception e) {
			System.out.println("ErrorIs Opening,Msg info:" + e.getMessage());
		}

	}

	public void handleCancel(String arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	public void handleCancelOk(String arg0) {
		// TODO Auto-generated method stub

	}

	public void handleConsumeOk(String arg0) {
		// TODO Auto-generated method stub

	}

	public void handleDelivery(String consumerTag, Envelope env, BasicProperties props, byte[] body)
			throws IOException {
		// Ӧ��ģʽ��ÿ������Ϣ���ֶ�����Ӧ��
		channel.basicAck(env.getDeliveryTag(), false);
		messageInfo = (MessageInfo) SerializationUtils.deserialize(body);
		messageInfo.setHashCode(hashCode);
		System.out.println(messageInfo.getHashCode() + " [REC] Received '" + messageInfo.getContent() + "'");
	}

	public void handleRecoverOk(String arg0) {
		// TODO Auto-generated method stub

	}

	public void handleShutdownSignal(String arg0, ShutdownSignalException arg1) {
		// TODO Auto-generated method stub

	}

	public void run() {
		// TODO Auto-generated method stub
		receiveMessage();
	}

	@Override
	public void handleRecoverOk() {
		// TODO Auto-generated method stub

	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

}
