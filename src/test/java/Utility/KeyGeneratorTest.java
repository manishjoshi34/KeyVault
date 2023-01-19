package Utility;

import org.junit.jupiter.api.Test;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorTest {



    public boolean isCharInSequence(String key, String sequence) {
        for (char c: key.toCharArray()) {
            if (sequence.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

    @Test
    void generateNumberInRange() {
        boolean result = true;
        int number = KeyGenerator.generateNumberInRange(0,100);
        if(number < 0 || number > 100){
            result = false;
        }
        result = isCharInSequence(Integer.toString(number), KeyGenerator.numberList.toString());
        assertEquals(true,result);
    }

    @Test
    void generateRandomNumber() {
        String number = "";
        number = KeyGenerator.generateRandomNumber(8,"5");
        boolean result = true;
        if (number.length()!= 8 ||
            number.contains("5") ){
            result = false;
        }
        result = isCharInSequence(number, KeyGenerator.numberList.toString());

        assertEquals(true,result);
    }

    @Test
    void generateRandomChars() {
        String chars = KeyGenerator.generateRandomLowerChars(8,"io");
        boolean result = true;

        if(chars.length() != 8 ||
           chars.contains("i") || chars.contains("o")) {
            result = false;
        }

       result = isCharInSequence(chars,KeyGenerator.charList.toString());

        assertEquals(true,result);
    }

    @Test
    void generateRandomSpecial() {
        String chars = KeyGenerator.generateRandomSpecial(8,"#$");
        boolean result = true;

        if(chars.length() != 8 ||
                chars.contains("$") || chars.contains("#")) {
            result = false;
        }

        result = isCharInSequence(chars,KeyGenerator.specials.toString());

        assertEquals(true,result);
    }

    @Test
    void shuffle() {
        boolean result = true;
        String name = "abcdefg";
        String sName = KeyGenerator.shuffle(name);
        if (name.compareTo(sName)==0) result = false;
        if (name.length() != sName.length()) result = false;
        assertEquals(true,result);
    }

    @Test
    void generate() {
        String key = KeyGenerator.generate();
        boolean result = true;
        if(key.length() != 8) result = false;
        key = KeyGenerator.generate(10);
        if(key.length() != 10) result = false;
        assertEquals(true,result);
    }

    @Test
    void testGenerate() {
    }

    @Test
    void generateWithoutSpecial() {
    }

    @Test
    void testGenerateWithoutSpecial() {
    }

    @Test
    void testGenerate1() {
    }
}