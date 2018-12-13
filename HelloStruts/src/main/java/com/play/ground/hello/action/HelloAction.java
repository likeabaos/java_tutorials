package com.play.ground.hello.action;

import com.opensymphony.xwork2.ActionSupport;
import com.play.ground.hello.model.MessageStore;

public class HelloAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private MessageStore messageStore;
	
	public String execute() {
		messageStore = new MessageStore();
		return SUCCESS;
	}
	
	public MessageStore getMessageStore() {
		return messageStore;
	}
}
