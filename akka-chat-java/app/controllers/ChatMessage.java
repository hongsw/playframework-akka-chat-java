package controllers;

import java.io.Serializable;

public class ChatMessage implements Serializable {

	public String msg;
	public ChatMessage(String msg2) {
		this.msg = msg2;
	}
}
