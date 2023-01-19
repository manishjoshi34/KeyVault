package Utility;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionTest {
    @Test
    void encryptKey() throws NoSuchAlgorithmException {
        String key = "";
        SecretKeyGenerator keyGenerator = new SecretKeyGenerator("AES",128);
        key = keyGenerator.getSecretKey();
        String value = "My name is Awesome";
        String encryptedValue = Encryption.EncryptKey(value, key);
        String decryptedValue = Encryption.DecryptKey(encryptedValue,key);
        assertEquals(value,decryptedValue);
    }

}