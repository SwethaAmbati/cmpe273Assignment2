package cmpe273;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	public static final String COLLECTION_NAME = "User";

	public void addUser(String string, User user) {
		if (!mongoTemplate.collectionExists(COLLECTION_NAME)) {
			mongoTemplate.createCollection(COLLECTION_NAME);
		}

		mongoTemplate.insert(user, COLLECTION_NAME);
	}

	public void updateUser(String userId, User user) {
		Query updateUserQuery = new Query();
		updateUserQuery.addCriteria(Criteria.where("_id").is(userId));
		// mongoTemplate.save(user, COLLECTION_NAME);
		Update update = new Update();
		update.set("email", user.getEmail());
		update.set("password", user.getPassword());
		update.set("updated_at", user.getUpdated_at());
		mongoTemplate.updateFirst(updateUserQuery, update, User.class,
				COLLECTION_NAME);

	}

	public User getUser(String userId) {
		Query findUserQuery = new Query();
		findUserQuery.addCriteria(Criteria.where("_id").is(userId));
		return mongoTemplate
				.findOne(findUserQuery, User.class, COLLECTION_NAME);

	}

}
