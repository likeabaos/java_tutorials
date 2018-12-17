package com.play.ground.banking;

import java.util.HashMap;
import java.util.Map;

public class CustomerSimpleMap implements CustomerLookupService {
	private Map<String, Customer> customers;

	public CustomerSimpleMap() {
		this.customers = new HashMap<String, Customer>();
		this.addCustomer(new Customer("id001", "Harry", "Hacker", -3456.78));
		this.addCustomer(new Customer("id002", "Codie", "Coder", 1234.56));
		this.addCustomer(new Customer("id003", "Polly", "Programmer", 987654.32));
	}

	private void addCustomer(Customer customer) {
		this.customers.put(customer.getId(), customer);
	}

	@Override
	public Customer findCustomer(String id) {
		if (id != null) {
			return this.customers.get(id.toLowerCase());
		}
		return null;
	}

}
