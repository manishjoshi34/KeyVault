package MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.logging.Logger;
import java.util.Properties;

public class MongoDBUtil {
    private static MongoClient mongoClient;
    private static final Logger logger = Logger.getLogger(MongoDBUtil.class.getName());
    private static String connectionUri;
    private MongoDatabase _database;
    private MongoCollection<Document> _collection;
    public static boolean connectToMongoDB(){
        try {
            loadConfiguration();

            if (mongoClient == null) {
                mongoClient = MongoClients.create(connectionUri);
                logger.info("Connected");
            }
        } catch (Exception e){
            logger.severe("Connection fail "+ e.getMessage());
            return false;
        }
        return true;
    }

    private static void loadConfiguration() throws IOException {
        Properties config = new Properties();
        InputStream inputStream = MongoDBUtil.class.getClassLoader().getResourceAsStream("MongoDB.properties");
        config.load(inputStream);
        boolean useCloudstorage = Boolean.parseBoolean(config.getProperty("useCloudstorage"));
        if (useCloudstorage) {
            connectionUri = getCloudConnectionUri(config);
        } else {
            connectionUri = getLocalConnectionUri(config);
        }
    }

    private static String getCloudConnectionUri(Properties config) {

        String cloudMongoDB = config.getProperty("cloudMongoDB");
        String cloudeUserName = config.getProperty("cloudUserName");
        String cloudPassword =  config.getProperty("cloudKey");
        String cloudHost = config.getProperty("cloudHost");

        return cloudMongoDB.concat(cloudeUserName).concat(":").concat(cloudPassword).concat("@").concat(cloudHost);
    }

    private static String getLocalConnectionUri(Properties config) {
        String localMonogoDB = config.getProperty("localMonogoDB");
        String localHost = config.getProperty("localHost");
        String localPort = config.getProperty("localPort");

        String uri = localMonogoDB.concat(localHost.toString()).concat(":").concat(localPort);
        return uri;
    }

    public static boolean disconnectToMongoDB() {
        if (mongoClient != null){
            mongoClient.close();
        }
        return true;
    }

    public MongoDBUtil(String databaseName, String collectionName) throws NullPointerException, IllegalArgumentException{
        if (databaseName.isEmpty() || collectionName.isEmpty()) {
            throw new IllegalArgumentException("databaseName and collectionName cannot be empty");
        } else if (MongoDBUtil.mongoClient == null && !MongoDBUtil.connectToMongoDB()) {
            throw new NullPointerException("Not able to connect mongoDB client. Check connection string");
        } else {
            _database = MongoDBUtil.mongoClient.getDatabase(databaseName);
            _collection = _database.getCollection(collectionName);
        }
    }

    public MongoDatabase getDatabase() {return  _database; }
    public MongoCollection<Document> getCollection() { return _collection; }
    public boolean insertDocument(Document document){
        if (_collection != null) {
            _collection.insertOne(document);
            logger.info("Document inserted in collection");
            return true;
        }
        logger.info("Document insertion fail");
        return false;
    }
    public boolean saveObject(Object object) throws IllegalAccessException {
        Document doc = new Document();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            doc.append(field.getName(), field.get(object));
        }
        return insertDocument(doc);
    }
}
