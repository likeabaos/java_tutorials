package com.play.ground;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "helloWorld", eager = true)
@RequestScoped
public class HelloWorld {
	@ManagedProperty(value = "#{message}")
	private Message messageBean;
	@ManagedProperty(value = "#{param.pageId}")
	private String pageId;
	private String message;

	public HelloWorld() {
		System.out.println("HelloWorld Started");
	}

	public String getMessage() {
		if (messageBean != null) {
			message = messageBean.getMessage();
		}
		return message;
	}

	public void setMessageBean(Message bean) {
		this.messageBean = bean;
	}

	public String moveToPage1() {
		return "page1";
	}

	public String moveToPage2() {
		return "page2";
	}

	public String moveToHomePage() {
		return "home";
	}

	public String processPage1() {
		return "page";
	}

	public String processPage2() {
		return "page";
	}

	public String showPage() {
		if (pageId == null) {
			return "home";
		}

		if (pageId.equals("1")) {
			return "page1";
		} else if (pageId.equals("2")) {
			return "page2";
		} else {
			return "home";
		}
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
}
