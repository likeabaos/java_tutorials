package com.play.ground.login;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class NavigationBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String redirectToLogin() {
		return "/index.jsf?faces-redirect=true";
	}
	
	public String toLogin() {
		return "/index.xhtml";
	}
	
	public String redirectToMain() {
		return "/secured/main.jsf?faces-redirect=true";
	}
	
	public String toMain() {
		return "/main.jsf";
	}

}
