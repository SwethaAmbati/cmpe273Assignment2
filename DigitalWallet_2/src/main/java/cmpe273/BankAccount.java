package cmpe273;

import org.hibernate.validator.constraints.NotEmpty;

public class BankAccount {

	private String account_name;
	@NotEmpty
	private String account_number;
	@NotEmpty
	private String routing_number;

	private String ba_id;

	public BankAccount() {

	}

	public String getBa_id() {
		return ba_id;
	}

	public void setBa_id(String ba_id) {
		this.ba_id = ba_id;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getRouting_number() {
		return routing_number;
	}

	public void setRouting_number(String routing_number) {
		this.routing_number = routing_number;
	}

	public String getAccount_number() {
		return account_number;
	}

	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}

}
