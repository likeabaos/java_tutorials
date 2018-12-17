package com.play.ground;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class LoginBean {
	private String userName, password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String validateUserLogin() {
		String navResult;
		if (this.userName.equals("admin") && password.equals("access123")) {
			navResult = "success";
		}
		else {
			navResult = "failure";
		}
		return navResult;
	}

}
