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

		hashCode = Thread.currentThread().hashCode(); // 区分不同工作进程的输出
		try {
			// 消息最大转发数默认为1
			channel.basicQos(1);
			// 指定消费队列并开启应答模式（第二个参数为false，代表开启应答，否则反之）
			String op_result = channel.basicConsume(queueName, false, this); // 指定消费队列
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
		// 应答模式下每发完消息后手动发送应答
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
