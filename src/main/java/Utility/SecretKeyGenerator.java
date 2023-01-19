package Utility;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Logger;

public class SecretKeyGenerator {

    private static final Logger logger = Logger.getLogger(SecretKeyGenerator.class.getName());
    private String _algorithm;
    private Integer _sizeOfKey = 128;

    SecretKeyGenerator(String algo, Integer size) {
        _algorithm = algo;
        if(size == 256) _sizeOfKey = size;
    }

    public String getSecretKey() throws NoSuchAlgorithmException {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(_algorithm);
            keyGenerator.init(_sizeOfKey); // specify key size
            SecretKey key = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
