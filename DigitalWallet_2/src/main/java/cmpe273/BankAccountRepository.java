package cmpe273;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class BankAccountRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	public static final String COLLECTION_NAME = "User";

	// Query method to update bankaccount
	public void addBankAccount(String userId, BankAccount bankaccount) {
		if (!mongoTemplate.collectionExists(COLLECTION_NAME)) {
			mongoTemplate.createCollection(COLLECTION_NAME);
		}
		Query updateUserQuery = new Query();
		updateUserQuery.addCriteria(Criteria.where("_id").is(userId));
		Update update = new Update();
		update.push("BankAccounts", bankaccount);

		// update.pull(userId, idcard);

		mongoTemplate.updateFirst(updateUserQuery, update, BankAccount.class,
				COLLECTION_NAME);
	}

	// Query method to list all bankaccounts
	public List<BankAccount> findBankAccounts(String userId) {
		Query findUserQuery = new Query();
		findUserQuery.addCriteria(Criteria.where("_id").is(userId));
		User rs1 = mongoTemplate.findOne(findUserQuery, User.class,
				COLLECTION_NAME);
		Iterator<BankAccount> it = rs1.getBankAccounts().iterator();
		while (it.hasNext()) {
			BankAccount card = it.next();
			// System.out.println("IDCard: id=" + card.getCard_id() + ", name="
			// + card.getCard_name());
		}
		return rs1.getBankAccounts();
	}

	/*
	 * public void removeIDCard(String userId, String cardId) {
	 * 
	 * Query removeIdcardQuery = new Query();
	 * 
	 * 
	 * removeIdcardQuery.addCriteria(Criteria.where("_id").is(userId).and("card_id"
	 * ).is(cardId));
	 * 
	 * mongoTemplate. return mongoTemplate.find(removeIdcardQuery, IDCard.class,
	 * COLLECTION_NAME);
	 * 
	 * }
	 */

}
