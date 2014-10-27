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
public class IDCardRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	public static final String COLLECTION_NAME = "User";

	// Query method to update idcards
	public void addIDCard(String userId, IDCard idcard) {
		if (!mongoTemplate.collectionExists(COLLECTION_NAME)) {
			mongoTemplate.createCollection(COLLECTION_NAME);
		}
		Query updateUserQuery = new Query();
		updateUserQuery.addCriteria(Criteria.where("_id").is(userId));
		Update update = new Update();
		update.push("IDcards", idcard);

		// update.pull(userId, idcard);

		mongoTemplate.updateFirst(updateUserQuery, update, IDCard.class,
				COLLECTION_NAME);
	}

	// Query method to list all idcards
	public List<IDCard> findIDCards(String userId) {
		Query findUserQuery = new Query();
		findUserQuery.addCriteria(Criteria.where("_id").is(userId));
		User rs1 = mongoTemplate.findOne(findUserQuery, User.class,
				COLLECTION_NAME);
		Iterator<IDCard> it = rs1.getIDcards().iterator();
		while (it.hasNext()) {
			IDCard card = it.next();
			System.out.println("IDCard: id=" + card.getCard_id() + ", name="
					+ card.getCard_name());
		}
		return rs1.getIDcards();
	}

	/*
	 * public void removeIDCard(String userId, String cardId) {
	 * 
	 * Query removeIdcardQuery = new Query();
	 * 
	 * removeIdcardQuery.addCriteria(Criteria.where("_id").is(userId)
	 * .and("card_id").is(cardId)); Update update = new Update();
	 * update.pull("IDcards", null);
	 * mongoTemplate.updateFirst(removeIdcardQuery, update, IDCard.class,
	 * COLLECTION_NAME);
	 */

	// mongoTemplate.remove(removeIdcardQuery, IDCard.class,
	// COLLECTION_NAME);

	// }
}
