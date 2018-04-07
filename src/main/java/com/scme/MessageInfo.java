package com.scme;

import java.io.Serializable;

public class MessageInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String channel; // ��Ϣ����

	private String content; // ��Ϣ����
	
	private int hashCode;  //�첽�̱߳�־

	private String consumerTag;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getConsumerTag() {
		return consumerTag;
	}

	public void setConsumerTag(String consumerTag) {
		this.consumerTag = consumerTag;
	}

	@Override
	public String toString() {
		return "MessageInfo [channel=" + channel + ", content=" + content + ", hashCode=" + hashCode + "]";
	}

}
