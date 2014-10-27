package cmpe273;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

	@Id
	private String user_id;

	private String name;

	@NotEmpty
	private String email;
	@NotEmpty
	private String password;

	private String created_at;
	private String updated_at;
	private List<IDCard> IDcards;
	private List<WebLogin> WebLogins;
	private List<BankAccount> BankAccounts;

	public List<IDCard> getIDcards() {
		return IDcards;
	}

	public void setIDcards(List<IDCard> iDcards) {
		IDcards = iDcards;
	}

	public List<WebLogin> getWebLogins() {
		return WebLogins;
	}

	public void setWebLogins(List<WebLogin> WebLogins) {
		WebLogins = WebLogins;
	}

	public List<BankAccount> getBankAccounts() {
		return BankAccounts;
	}

	public void setBankAccounts(List<BankAccount> BankAccounts) {
		BankAccounts = BankAccounts;
	}

	public User() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {

		return user_id;
	}

	public void setUserId(String user_id) {
		this.user_id = user_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

}
