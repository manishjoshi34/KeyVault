package Models;

import Main.Documentable;
import org.bson.Document;

import java.util.*;


public class User implements Documentable {
    public Map<String, Credential> getCredentials() {
        return  _credentials;
    }

    public static class UserSchema {
        public static String USERNAME = "name";
        public static String USERID = "emailId";
        public static String USERPASSWORD = "key";
        public static String USERCREDENTIALS = "credentials";
        public static String USERLOGIN = "userLogin";
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_emailId(String _emailId) {
        this._emailId = _emailId;
    }

    public void set_passWord(String _passWord) {
        this._passWord = _passWord;
    }

    private String _name;
    private String _emailId;
    private String _passWord;

    private boolean _isUserLogin;
    private HashMap<String, Credential> _credentials;

    public User(String name,
                String emailId,
                String password)
    {
        this._name = name;
        this._emailId = emailId;
        this._passWord = password;
        this._credentials = new HashMap<String, Credential>();
    }

    public static User getUserFromDocument(Document userDoc,boolean withCredential) {
        String name = userDoc.getString(UserSchema.USERNAME);
        String emailId = userDoc.getString(UserSchema.USERID);
        String password = userDoc.getString(UserSchema.USERPASSWORD);
        boolean userLogin = userDoc.getBoolean(UserSchema.USERLOGIN);
        User user = new User(name,emailId,password);
        user.setIsUserLogin(userLogin);
        if (withCredential) {
            List<Document> docs = userDoc.getList(UserSchema.USERCREDENTIALS,Document.class);
            for(Document doc :docs){
                Credential cred = Credential.getCredentialFromDocument(doc);
                user.addCredential(cred);
            }
        }
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                UserSchema.USERNAME + _name + '\'' +
                UserSchema.USERID+ _emailId + '\'' +
                UserSchema.USERPASSWORD + _passWord + '\'' +
                UserSchema.USERCREDENTIALS+ _credentials +
                '}';
    }

    public void addCredential(String title, String userName, String data){
        Credential cred = new Credential(title, userName, data);
        _credentials.put(title,cred);
    }
    public void addCredential(Credential cred){
        _credentials.put(cred.getTitle(),cred);
    }
    public void removeCredential(String title){
        _credentials.remove(title);
    }
    public String getName() { return _name;}
    public String getEmailId() { return _emailId;}

    public String getPassword() { return _passWord;}
    public boolean getIsUserLogin() {
        return _isUserLogin;
    }

    public void setIsUserLogin(boolean _isUserLogin) {
        this._isUserLogin = _isUserLogin;
    }

    @Override
    public Document getDocument() {
        return getDocument(false);
    }

    public Document getDocument(boolean includeCredential){
        Document document = new Document();
        document.append(UserSchema.USERNAME, getName())
                .append(UserSchema.USERID,getEmailId())
                .append(UserSchema.USERPASSWORD,getPassword())
                .append(UserSchema.USERCREDENTIALS,new ArrayList<Document>())
                .append(UserSchema.USERLOGIN,getIsUserLogin());
        List<Document> credDocuments = new ArrayList<Document>();
        if (!_credentials.isEmpty() && includeCredential) {
           List<Credential> creds = new ArrayList<Credential>(_credentials.values());
           for(Credential cred : creds){
               credDocuments.add(cred.getDocument());
           }
        }
        document.append(UserSchema.USERCREDENTIALS,credDocuments);
        return document;
    }

    public Credential getCredential(String credTitle) {
        if (_credentials.containsKey(credTitle)) {
            return _credentials.get(credTitle);
        }
        return null;
    }
}