package com.scme;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.SerializationUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.MessageProperties;

public class PublisherHandler extends BaseConnector {

	public PublisherHandler(String queueName) throws IOException, TimeoutException {
		super(queueName);
	}

	public void sendMessage(MessageInfo messageInfo) {
		try {
			// 消息发送及声明消息持久化bp
			BasicProperties bp = MessageProperties.PERSISTENT_TEXT_PLAIN;
			channel.basicPublish("", queueName, bp, SerializationUtils.serialize(messageInfo));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭频道及链接
	 */
	public void close() {
		super.close();
	}

	public static void main(String[] args) {
		PublisherHandler publisher = null;

		ReceiverHandler receiver = null;

		ReceiverHandler receiver2 = null;

		ReceiverHandler receiver3 = null;

		String queueName = "mq_demo1";

		try {

			receiver = new ReceiverHandler(queueName); // 接收者1

			receiver.start();

			receiver2 = new ReceiverHandler(queueName); // 接收者2

			receiver2.start();

			receiver3 = new ReceiverHandler(queueName); // 接收者3

			receiver3.start();

			publisher = new PublisherHandler(queueName); // 发送者

			for (int i = 0; i < 10; i++) {

				String message = "消息【" + (i + 1) + "】";

				MessageInfo msgInfo = new MessageInfo();

				msgInfo.setConsumerTag("demo_tag");

				msgInfo.setChannel("demo");

				msgInfo.setContent(message);

				publisher.sendMessage(msgInfo);

			}

		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		} finally {
			publisher.close();
		}
	}

}
