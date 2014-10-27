package cmpe273;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	IDCardRepository idCardRepository;
	@Autowired
	WebLoginRepository webLoginRepository;
	@Autowired
	BankAccountRepository bankAccountRepository;

	Random random = new Random();
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	Calendar calobj = Calendar.getInstance();
	Gson gson = new Gson();

	@RequestMapping(value = "api/v1/users", method = RequestMethod.POST)
	public ResponseEntity<String> createUser(@Valid @RequestBody User user,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(
				MongoConfig.class);
		UserRepository userRepository = context.getBean(UserRepository.class);

		user.setUserId("u-" + random.nextInt(1000));
		String currentDate = dateFormat.format(calobj.getTime());
		user.setCreated_at(currentDate);
		user.setUpdated_at(currentDate);
		userRepository.addUser(user.getUserId(), user);

		JsonObject jObject = new JsonObject();
		jObject.addProperty("user_id", user.getUserId());
		jObject.addProperty("email", user.getEmail());
		jObject.addProperty("password", user.getPassword());
		jObject.addProperty("created_at", user.getCreated_at());

		return new ResponseEntity<String>(gson.toJson(jObject),
				HttpStatus.CREATED);

	}

	// GET
	@Cacheable(value = "api/v1/users", key = "{user_id}")
	@RequestMapping(value = "api/v1/users/{user_id}", method = RequestMethod.GET)
	public ResponseEntity<String> viewUser(
			@PathVariable String user_id,
			@RequestHeader(value = "If-Modified-Since", defaultValue = "") String ifModifiedSince) {
		Date ifModifiedSinceDate = null;

		if (ifModifiedSince != null && !ifModifiedSince.isEmpty()) {
			try {
				ifModifiedSinceDate = dateFormat.parse(ifModifiedSince);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String updatedAt = userRepository.getUser(user_id).getUpdated_at();

		Date updatedAtDate = null;

		if (updatedAt != null && !updatedAt.isEmpty()) {
			try {
				updatedAtDate = dateFormat.parse(updatedAt);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		HttpHeaders headers = new HttpHeaders();

		headers.set("Last-Modified", userRepository.getUser(user_id)
				.getUpdated_at());

		if (ifModifiedSinceDate == null
				|| ifModifiedSinceDate.compareTo(updatedAtDate) < 0) {
			User user = userRepository.getUser(user_id);

			JsonObject jObject = new JsonObject();
			System.out.println("test");
			System.out.println(user);
			jObject.addProperty("user_id", user.getUserId());
			jObject.addProperty("email", user.getEmail());
			jObject.addProperty("password", user.getPassword());
			jObject.addProperty("created_at", user.getCreated_at());
			return new ResponseEntity<String>(gson.toJson(jObject), headers,
					HttpStatus.OK);

		} else {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_MODIFIED);
		}

	}

	// PUT
	@RequestMapping(value = "api/v1/users/{user_id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateUser(@PathVariable String user_id,
			@Valid @RequestBody User user, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		user.setUpdated_at(dateFormat.format(calobj.getTime()));
		// user.setUserId(user_id);
		// User userObj = repository.getUser(user_id);
		// user.setUpdated_at(dateFormat.format(calobj.getTime()));

		userRepository.updateUser(user_id, user);
		JsonObject jObject = new JsonObject();

		User userObj = userRepository.getUser(user_id);
		jObject.addProperty("user_id", userObj.getUserId());
		jObject.addProperty("email", userObj.getEmail());
		jObject.addProperty("password", userObj.getPassword());
		jObject.addProperty("updated_at", userObj.getUpdated_at());
		return new ResponseEntity<String>(gson.toJson(jObject),
				HttpStatus.CREATED);

	}

	// IDCard POST

	@RequestMapping(value = "api/v1/users/{user_id}/idcards", method = RequestMethod.POST)
	public ResponseEntity<IDCard> createIdCard(
			@Valid @RequestBody IDCard idcard, BindingResult result,
			@PathVariable String user_id) {

		if (result.hasErrors()) {
			return new ResponseEntity<IDCard>(HttpStatus.BAD_REQUEST);
		}

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(
				MongoConfig.class);
		IDCardRepository idCardRepository = context
				.getBean(IDCardRepository.class);

		idcard.setCard_id("c-" + random.nextInt(1000));

		idCardRepository.addIDCard(user_id, idcard);

		return new ResponseEntity<IDCard>(idcard, HttpStatus.CREATED);
	}

	// GET
	@RequestMapping(value = "api/v1/users/{user_id}/idcards", method = RequestMethod.GET)
	public ResponseEntity<List<IDCard>> listIdCard(@PathVariable String user_id) {

		List<IDCard> idcard = idCardRepository.findIDCards(user_id);
		System.out.println("IDCards:" + idcard.toString());
		return new ResponseEntity<List<IDCard>>(idcard, HttpStatus.OK);

	}

	// DELETE
	/*
	 * @RequestMapping(value = "api/v1/users/{user_id}/idcards/{card_id}",
	 * method = RequestMethod.DELETE) public ResponseEntity<IDCard>
	 * deleteIdCard(@PathVariable String card_id,
	 *
	 * @PathVariable String user_id) {
	 *
	 * // cardInfo.get(user_id).remove(card_id); return new
	 * idCardRepository.removeIDCard(user_id, card_id); return new
	 * ResponseEntity<IDCard>(HttpStatus.NO_CONTENT); }
	 */

	// WebLogin POST

	@RequestMapping(value = "api/v1/users/{user_id}/weblogins", method = RequestMethod.POST)
	public ResponseEntity<WebLogin> createWebLogin(
			@Valid @RequestBody WebLogin weblogin, BindingResult result,
			@PathVariable String user_id) {

		if (result.hasErrors()) {
			return new ResponseEntity<WebLogin>(HttpStatus.BAD_REQUEST);
		}

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(
				MongoConfig.class);
		WebLoginRepository webLoginRepository = context
				.getBean(WebLoginRepository.class);

		weblogin.setLogin_id("l-" + random.nextInt(1000));

		webLoginRepository.addWebLogins(user_id, weblogin);

		return new ResponseEntity<WebLogin>(weblogin, HttpStatus.CREATED);
	}

	// GET
	@RequestMapping(value = "api/v1/users/{user_id}/weblogins", method = RequestMethod.GET)
	public ResponseEntity<List<WebLogin>> listWebLogin(
			@PathVariable String user_id) {

		List<WebLogin> weblogin = webLoginRepository.findWebLogins(user_id);
		System.out.println("WebLogins:" + weblogin.toString());
		return new ResponseEntity<List<WebLogin>>(weblogin, HttpStatus.OK);

	}

	// BankAccount POST

	@RequestMapping(value = "api/v1/users/{user_id}/bankaccounts", method = RequestMethod.POST)
	public ResponseEntity<BankAccount> createBankAccount(
			@Valid @RequestBody BankAccount bankaccount, BindingResult result,
			@PathVariable String user_id) {

		if (result.hasErrors()) {
			return new ResponseEntity<BankAccount>(HttpStatus.BAD_REQUEST);
		}

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(
				MongoConfig.class);
		BankAccountRepository bankAccountRepository = context
				.getBean(BankAccountRepository.class);

		bankaccount.setBa_id("b-" + random.nextInt(1000));

		String url = "http://www.routingnumbers.info/api/data.json?rn="
				+ bankaccount.getRouting_number();

		URL obj;
		StringBuffer response = new StringBuffer();
		try {
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JsonParser jsonParser = new JsonParser();
		JsonObject jo = (JsonObject) jsonParser.parse(response.toString());
		String status = jo.get("code").getAsString();
		if (status.equalsIgnoreCase("200")) {
			bankaccount.setAccount_name(jo.get("customer_name").getAsString());
			bankAccountRepository.addBankAccount(user_id, bankaccount);
			return new ResponseEntity<BankAccount>(bankaccount,
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<BankAccount>(HttpStatus.NOT_FOUND);
		}
	}

	// GET
	@RequestMapping(value = "api/v1/users/{user_id}/bankaccounts", method = RequestMethod.GET)
	public ResponseEntity<List<BankAccount>> listBankaccount(
			@PathVariable String user_id) {

		List<BankAccount> bankaccount = bankAccountRepository
				.findBankAccounts(user_id);
		System.out.println("BankAccounts:" + bankaccount.toString());
		return new ResponseEntity<List<BankAccount>>(bankaccount, HttpStatus.OK);

	}

}