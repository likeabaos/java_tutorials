package com.play.ground.login;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Map<String, String> USERS;
	static {
		USERS = new HashMap<String, String>();
		USERS.put("bao", "admin123");
		USERS.put("john", "test123");
	}

	private String username, password;
	private boolean loggedIn;
	
	@ManagedProperty(value="#{navigationBean}")
	private NavigationBean navigationBean;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public void setNavigationBean(NavigationBean navigationBean) {
		this.navigationBean = navigationBean;
	}

	public String login() {
		String username;
		username = this.username.toLowerCase();
		if (USERS.containsKey(username) && this.password.equals(USERS.get(this.username))) {
			this.loggedIn = true;
			return this.navigationBean.redirectToMain();
		}
		this.loggedIn = false;
		
		FacesMessage msg = new FacesMessage("Incorrect username or password");
		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return this.navigationBean.toLogin();
	}
	
	public String logout() {
		this.loggedIn = false;
		FacesMessage msg = new FacesMessage("Logout success!");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);
		return this.navigationBean.toLogin();
	}

}
