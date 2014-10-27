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
public class WebLoginRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	public static final String COLLECTION_NAME = "User";

	// Query method to update weblogin
	public void addWebLogins(String userId, WebLogin weblogin) {
		if (!mongoTemplate.collectionExists(COLLECTION_NAME)) {
			mongoTemplate.createCollection(COLLECTION_NAME);
		}
		Query updateUserQuery = new Query();
		updateUserQuery.addCriteria(Criteria.where("_id").is(userId));
		Update update = new Update();
		update.push("WebLogins", weblogin);

		mongoTemplate.updateFirst(updateUserQuery, update, WebLogin.class,
				COLLECTION_NAME);
	}

	// Query method to list all weblogins
	public List<WebLogin> findWebLogins(String userId) {
		Query findUserQuery = new Query();
		findUserQuery.addCriteria(Criteria.where("_id").is(userId));
		User rs1 = mongoTemplate.findOne(findUserQuery, User.class,
				COLLECTION_NAME);
		Iterator<WebLogin> it = rs1.getWebLogins().iterator();
		while (it.hasNext()) {
			WebLogin card = it.next();

		}
		return rs1.getWebLogins();
	}

}
