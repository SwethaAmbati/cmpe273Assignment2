package cmpe273;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories
@ComponentScan
public class MongoConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "cmpe273db";
	}

	@Override
	public Mongo mongo() throws Exception {

		// Establishes connection to mongolab

		MongoClient mongoClient = new MongoClient("ds039880.mongolab.com:39880");
		DB database = mongoClient.getDB("cmpe273db");
		boolean auth = database.authenticate("admin", "admin".toCharArray());

		// Connect to mongodb-localhost
		// MongoClient mongoClient = new MongoClient("localhost");

		return mongoClient;
	}

}