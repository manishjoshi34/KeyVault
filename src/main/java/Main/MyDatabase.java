package Main;

import Models.Credential;
import Models.User;
import MongoDB.MongoDBUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.*;
import com.mongodb.client.model.Filters;
import java.util.logging.Logger;




public class MyDatabase {
    private String databasename = "KeyVaultDatabase";
    private String collectionName = "users";
    private MongoDBUtil mongoDB;
    private static final Logger logger = Logger.getLogger(MyDatabase.class.getName());

    public MyDatabase() {}

    public boolean initialize() {
        try {
            mongoDB = new MongoDBUtil(databasename, collectionName);
            logger.info("Mydatabase initialized");
        } catch (NullPointerException e) {
            logger.severe("Unable to connect mongoDB");
            return false;
        }
        return true;
    }

    //Read APIs
    public boolean isUserPresent(String userId){
        MongoCollection users = mongoDB.getCollection();
        Bson filter = Filters.eq(User.UserSchema.USERID,userId);
        FindIterable<Document> docs = users.find(filter);
        if (docs.iterator().hasNext()) {
            return true;
        }
        return false;
    }
    public User getUser(String userId){

        //check user in cache
        User user = Context.get_currentuser();
        if(user!= null) return user;

        //Search in database
        Document userDoc = getUserFromDatabase(userId);
        if(userDoc != null){
            user = User.getUserFromDocument(userDoc, true);
        }

        //cache user
        Context.set_currentuser(user);
        return user;
    }
    public Document getUserFromDatabase(String userId) {
        MongoCollection users = mongoDB.getCollection();
        Bson filter = Filters.eq(User.UserSchema.USERID,userId);
        FindIterable<Document> docs = users.find(filter);
        if (docs.iterator().hasNext()) {
            Document userDoc = docs.first();
            return userDoc;
        } else {
            return null;
        }
    }


    public Credential getCredential(String userId, String credTitle) {
        MongoCollection users = mongoDB.getCollection();
        Bson filter  = Filters.and(Filters.eq(User.UserSchema.USERID,userId),Filters.eq("credentials.Title",credTitle));
        FindIterable<Document> docs = users.find(filter);
        User user = User.getUserFromDocument(docs.first(),true);
        return user.getCredential(credTitle);
    }

    private boolean clearCollection() {
        MongoCollection users = mongoDB.getCollection();
        DeleteResult result = users.deleteMany(new Document());
        if(result.wasAcknowledged()) return true;
        return false;
    }

    public void disconnectToMongoDB() {
        MongoDBUtil.disconnectToMongoDB();
    }

    public boolean deleteuser(String userId) {
        User user = Context.get_currentuser();
        if(user == null || user.getEmailId() != userId) return false;
        Context.set_currentuser(null);
        deleteUserFromDatabase(userId);
        return true;
    }
    private boolean deleteUserFromDatabase(String id){
        MongoCollection users = mongoDB.getCollection();
        DeleteResult result = users.deleteOne(Filters.eq(User.UserSchema.USERID,id));
        if(result.wasAcknowledged() && result.getDeletedCount()==1){
            return true;
        }
        return false;
    }

    public boolean createUser(User user) {
        if (isUserPresent(user.getEmailId())) {
            logger.info("Main.Models.User already present with given email id");
            return false;
        } else {
            MongoCollection users = mongoDB.getCollection();
            users.insertOne(user.getDocument(true));
            return true;
        }
    }

    public boolean addCredential(Credential cred, String userId){
        // Update database
        if(isUserPresent(userId)){
            Document credDoc = cred.getDocument();
            Bson filter = Filters.eq(User.UserSchema.USERID,userId);
            Bson update = Updates.push("credentials",credDoc);
            UpdateResult result = mongoDB.getCollection().updateOne(filter,update);
            if(result.wasAcknowledged()){
                //Update cache
                User currentUser = Context.get_currentuser();
                currentUser.addCredential(cred);
                return true;
            }
        }
        return false;
    }

    public boolean removeCredential(String credKey, String UserId){
        Bson filter = Filters.eq(User.UserSchema.USERID, UserId);
        Bson update = Updates.pull("credentials", Filters.eq("Title", credKey));
        UpdateResult result = mongoDB.getCollection().updateOne(filter, update);
        if(result.wasAcknowledged()) {
            //update cache
            User user = Context.get_currentuser();
            user.removeCredential(credKey);
            return true;
        }
        return false;
    }

    public boolean updateUserId(String credKey, String userId, String newUserId){
        String field = User.UserSchema.USERCREDENTIALS.concat(".$.").concat(Credential.CredentialSchema.USERID);
        Bson filter = Filters.and(Filters.eq(User.UserSchema.USERID, userId), Filters.eq("credentials.Title", credKey));
        Bson update = Updates.set(field, newUserId);
        UpdateResult result = mongoDB.getCollection().updateOne(filter,update);
        if(result.wasAcknowledged()) {
            Context.get_currentuser().getCredential(credKey).setUserName(newUserId);
            return true;
        }
        return false;
    }

    public boolean updateTitle(String credKey, String userId, String newCredKey){
        String titleFilter = User.UserSchema.USERCREDENTIALS.concat(".").concat(Credential.CredentialSchema.TITLE);
        String titleField =  User.UserSchema.USERCREDENTIALS.concat(".$.").concat(Credential.CredentialSchema.TITLE);
        Bson filter = Filters.and(Filters.eq(User.UserSchema.USERID, userId),
                                  Filters.eq(titleFilter, credKey));
        Bson update = Updates.set(titleField, newCredKey);
        UpdateResult result = mongoDB.getCollection().updateOne(filter,update);
        if(result.getMatchedCount()==1 && result.getModifiedCount()==1) {
            Context.get_currentuser().getCredential(credKey).setTitle(newCredKey);
            return true;
        }
        return false;
    }
    public boolean updateUserName(String userId, String name){
        MongoCollection users = mongoDB.getCollection();
        Bson filter = Filters.eq(User.UserSchema.USERID, userId);
        Bson update = Updates.set(User.UserSchema.USERNAME,name);
        UpdateResult result = users.updateOne(filter,update);
        if(result.wasAcknowledged()) {
            Context.get_currentuser().set_name(name);
            return true;
        }
        return false;
    }

    public boolean updateCredentialData(String credKey, String newPassword) {
        String userId = Context.get_currentuser().getEmailId();
        String field = User.UserSchema.USERCREDENTIALS.concat(".$.").concat(Credential.CredentialSchema.PASSWORD);
        Bson filter = Filters.and(Filters.eq(User.UserSchema.USERID, userId),
                      Filters.eq(User.UserSchema.USERCREDENTIALS.concat(".").concat(Credential.CredentialSchema.TITLE), credKey));
        Bson update = Updates.set(field, newPassword);
        UpdateResult result = mongoDB.getCollection().updateOne(filter,update);
        if(result.wasAcknowledged()) {
            Context.get_currentuser().getCredential(credKey).setData(newPassword);
            return true;
        }
        return false;
    }

    public boolean updateEmailId(String newEmailId) {
        if(isUserPresent(newEmailId)){
            logger.info("Email id associated with other user");
            return false;
        } else {
            MongoCollection users = mongoDB.getCollection();
            String userId = Context.get_currentuser().getEmailId();
            Bson filter = Filters.eq(User.UserSchema.USERID, userId);
            Bson update = Updates.set(User.UserSchema.USERID, newEmailId);
            UpdateResult result = users.updateOne(filter,update);
            if(result.wasAcknowledged()) {
                Context.get_currentuser().set_emailId(newEmailId);
                return true;
            }
        }
        return false;
    }

    public boolean logoutUser(String userId) {
        MongoCollection users = mongoDB.getCollection();
        Bson filter = Filters.eq(User.UserSchema.USERID, userId);
        Bson update = Updates.set(User.UserSchema.USERLOGIN, false);
        UpdateResult result = users.updateOne(filter,update);
        if(result.getModifiedCount()==1) {
            Context.get_currentuser().setIsUserLogin(false);
            return true;
        }
        return false;
    }
    public boolean loginUser(String userId){
        Bson filter = Filters.eq(User.UserSchema.USERID, userId);
        Bson update = Updates.set(User.UserSchema.USERLOGIN, true);
        UpdateResult result = mongoDB.getCollection().updateOne(filter,update);
        if(result.wasAcknowledged() && result.getModifiedCount()==1){
            Context.get_currentuser().setIsUserLogin(true);
            return true;
        }
        return false;
    }

    public boolean updateUserPassword(String userId, String newPassword) {
        Bson filter = Filters.eq(User.UserSchema.USERID, userId);
        Bson update = Updates.set(User.UserSchema.USERPASSWORD, newPassword);
        UpdateResult result = mongoDB.getCollection().updateOne(filter,update);
        if(result.getModifiedCount()==1){
            Context.get_currentuser().set_passWord(newPassword);
            return true;
        }
        return false;
    }
}
