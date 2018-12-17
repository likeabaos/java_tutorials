package com.play.ground;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.commons.lang3.StringUtils;

@ManagedBean
@RequestScoped
public class BankingBean {
	private String customerId, password;
	private Customer customer;
	private static final CustomerLookupService LOOKUP_SERVICE = new CustomerSimpleMap();
	private static final String RESRC = "banking/";

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		if (StringUtils.isBlank(customerId)) {
			this.customerId = "(none entered)";
		} else {
			this.customerId = customerId;
		}
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Customer getCustomer() {
		return this.customer;
	}

	public String showBalance() {
		if (!"secret".equals(this.password)) {
			return RESRC + "wrong-password";
		}
		customer = LOOKUP_SERVICE.findCustomer(this.customerId);
		if (customer == null) {
			return RESRC + "unknown-customer";
		}
		else {
			return RESRC + "show-balance";
		}
	}
}
