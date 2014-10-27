package cmpe273;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {

	private String customer_name;

	public String getCustomer_name() {
		return customer_name = customer_name;
	}

	/*
	 * public void setCustomer_name(String customer_name) { this.customer_name =
	 * customer_name; }
	 */

}
