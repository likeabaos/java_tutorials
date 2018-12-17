package com.play.ground.banking;

public class Customer {
	private String id, firstName, lastName;
	double balance, balanceNoSign;

	public Customer(String id, String firstName, String lastName, double balance) {
		this.setId(id);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setBalance(balance);
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public double getBalance() {
		return this.balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
		this.balanceNoSign = Math.abs(balance);
	}

	public double getBalanceNoSign() {
		return this.balanceNoSign;
	}

	public void setBalanceNoSign(double balance) {
		this.setBalance(balance);
	}
}
