package Utility;

import MongoDB.MongoDBUtil;
import org.bson.Document;
import org.bson.types.Binary;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;


public class Encryption {
        private static final Logger logger = Logger.getLogger(Encryption.class.getName());
        private static final String algorithm = "AES";
        private static final String transformation = "AES/ECB/PKCS5Padding";
        private static String databasename = "KeyVaultDatabase";
        private static String secretKeyCollectionName = "magicKey";
        private static Integer keySize = 256;
        private static String secretKey;
        private static MongoDBUtil keyDB;
        private static boolean initialized = false;

        private String _key;
        private String _algorithm;
        private String _transformation;


        static {
                initialize();
        }

        private static  boolean isMagicKeyPresent() {
                if (keyDB.getCollection().countDocuments() == 1) { return true; }
                return false;
        }

        private static String getMagicKeyFromDB() {
                String key = null;
                if (isMagicKeyPresent()) {
                        Document doc = keyDB.getCollection().find().first();
                        if (doc != null) {
                                key = doc.getString(secretKeyCollectionName);
                        }
                }
                return key;
        }

        public static void initialize() {
                if(!initialized) {
                        try {
                                keyDB = new MongoDBUtil(databasename,secretKeyCollectionName);
                                String key = getMagicKeyFromDB();
                                if (key != null) {
                                        secretKey = key;
                                } else {
                                        SecretKeyGenerator generator = new SecretKeyGenerator(algorithm,keySize);
                                        secretKey = generator.getSecretKey();
                                        keyDB.insertDocument(new Document(secretKeyCollectionName, secretKey));
                                }

                        } catch (NoSuchAlgorithmException e) {
                                secretKey = "p8H+JLxO5aCg7VQvX8Wl0M5Q5D5G7I5R";
                                logger.info("Secret key generation fail");
                        }
                        logger.info("Encryption key initialized");
                        initialized = true;
                }
        }

        public static String EncryptKey(String value){
                return EncryptKey(value, secretKey);
        }
        public static String DecryptKey(String encyptedString){
                return DecryptKey(encyptedString, secretKey);
        }
        public static String EncryptKey(String value, String key) {
                return Encryption.EncryptKey(value,key,Optional.empty(),Optional.empty());
        }

        public static String DecryptKey(String value, String key) {
                return Encryption.DecryptKey(value,key,Optional.empty(),Optional.empty());
        }
        private static String EncryptKey(String value, String key,
                                        Optional<String> algo,
                                        Optional<String> transform)  {
                String encryptedString;
                String customAlgo = algo.orElse(algorithm);
                String customTransform = transform.orElse(transformation);
                try {
                        // Encrypt the text
                        byte[] keyBytes = Base64.getDecoder().decode(key);
                        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, customAlgo);
                        Cipher cipher = Cipher.getInstance(customTransform);
                        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                        byte[] encrypted = cipher.doFinal(value.getBytes());
                        encryptedString = Base64.getEncoder().encodeToString(encrypted);
                } catch (IllegalBlockSizeException | BadPaddingException
                        | NoSuchPaddingException | NoSuchAlgorithmException e){
                        logger.severe(LoggingMessage.EncryptionFail+": "+e.getMessage());
                        return value;
                } catch (InvalidKeyException e){
                        logger.warning(LoggingMessage.KeyIsInvalid+": "+e.getMessage());
                        logger.severe(LoggingMessage.EncryptionFail);
                        return value;
                }
                logger.info(LoggingMessage.EncryptionDone);
                return encryptedString;
        }


        private static String DecryptKey(String encryptedString, String key,
                                        Optional<String> algo,
                                        Optional<String> transform)  {
                String decryptedString;
                String customAlgo = algo.orElse(algorithm);
                String customTransform = transform.orElse(transformation);
                try{
                        byte[] keyBytes = Base64.getDecoder().decode(key);
                        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, customAlgo);
                        Cipher cipher = Cipher.getInstance(customTransform);
                        cipher.init(Cipher.DECRYPT_MODE, secretKey);
                        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedString));
                        decryptedString = new String(decrypted);
                } catch (IllegalBlockSizeException | BadPaddingException
                         | NoSuchPaddingException | NoSuchAlgorithmException e){
                        logger.severe(LoggingMessage.DecryptionFail+": "+e.getMessage());
                        return encryptedString;
                }catch (InvalidKeyException e){
                        logger.warning(LoggingMessage.KeyIsInvalid+": "+e.getMessage());
                        logger.severe(LoggingMessage.DecryptionFail);
                        return encryptedString;
                }
                logger.info(LoggingMessage.DecryptionDone);
                return decryptedString;
        }





        public Encryption(String key, Optional<String> algorithm, Optional<String> transform){
                this._key = key;
                this._algorithm = algorithm.orElse(Encryption.algorithm);
                this._transformation = transform.orElse(Encryption.transformation);
        }
        public Encryption(String key){
                this._key = key;
                this._algorithm = Encryption.algorithm;
                this._transformation = Encryption.transformation;
        }

        public String doEncryption(String  value){
                return Encryption.EncryptKey(value, this._key,
                                            Optional.of(this._algorithm),
                                            Optional.of(this._transformation));
        }

        public String doDecryption(String value) {
                return Encryption.DecryptKey(value, this._key,
                        Optional.ofNullable(this._algorithm),
                        Optional.ofNullable(this._transformation));
        }

}
