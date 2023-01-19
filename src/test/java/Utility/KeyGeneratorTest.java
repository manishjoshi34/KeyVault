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
        String chars = KeyGenerator.generateRandomChars(8,"io");
        boolean result = true;

        if(chars.length() != 8 ||
           chars.contains("i") || chars.contains("io")) {
            result = false;
        }

       result = isCharInSequence(chars,KeyGenerator.charList.toString());

        assertEquals(true,result);
    }

    @Test
    void generateRandomSpecial() {
    }

    @Test
    void shuffle() {
    }

    @Test
    void generate() {
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