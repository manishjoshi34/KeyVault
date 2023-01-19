package Models;

import Main.Documentable;
import Utility.Encryption;
import org.bson.Document;

public class Credential implements Documentable {

    public static class CredentialSchema{
        public static String USERID = "userId";
        public static String PASSWORD = "data";
        public static String TITLE = "title";
    }
    private String _userName;
    private String _data;

    private String _title;

    public Credential(String title, String userName, String data){
        this._data = data;
        this._title = title;
        this._userName = userName;
    }

    @Override
    public String toString() {
        return "Credential{" +
                CredentialSchema.USERID + ": "+_userName + '\'' +
                CredentialSchema.PASSWORD + ": "+ _data + '\'' +
                CredentialSchema.TITLE + " : "+_title + '\'' +
                '}';
    }

    public static Credential getCredentialFromDocument(Document doc) {
        String title = doc.getString(CredentialSchema.TITLE);
        String userName = doc.getString(CredentialSchema.USERID);
        String password = doc.getString(CredentialSchema.PASSWORD);
        return new Credential(title,userName,password);
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public String getUserId() {
        return _userName;
    }
    public void setUserName(String _userName) {
        this._userName = _userName;
    }

    public String getData() {
        return _data;
    }

    public void setData(String _data) {
        this._data = _data;
    }


    @Override
    public Document getDocument() {
        Document document = new Document();
        document.append(CredentialSchema.TITLE,_title)
                .append(CredentialSchema.USERID, Encryption.EncryptKey(_userName))
                .append(CredentialSchema.PASSWORD,Encryption.EncryptKey(_data));
        return document;
    }
}

