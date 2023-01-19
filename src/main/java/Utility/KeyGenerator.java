package Utility;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KeyGenerator {

    public static StringBuilder charList = new StringBuilder();
    public static StringBuilder upperCharList = new StringBuilder();
    public static StringBuilder numberList = new StringBuilder();
    public static StringBuilder specials = new StringBuilder();

    private static int DefaultSize = 8;

    static {
        initialize();
    }

    private static void initialize() {

        for (char c = 'a'; c <= 'z'; c++) {
            charList.append(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            upperCharList.append(c);
        }
        for (char c = '0'; c <= '9'; c++) {
            numberList.append(c);
        }
        for (char c : Constant.specialChars.toCharArray()) {
            specials.append(c);
        }

    }

    public static int generateNumberInRange(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
    public static String generateKeyWithAllowedChar(int size, String allowedChars) {
        StringBuilder result = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            result.append(allowedChars.charAt(rand.nextInt(allowedChars.length())));
        }
        return result.toString();
    }
    public static String generateRandomNumber(int size,String excludedSpecial) {
        StringBuilder allowedChars = numberList;
        for(char c : excludedSpecial.toCharArray()){
            allowedChars.deleteCharAt(allowedChars.indexOf(String.valueOf(c)));
        }
        return generateKeyWithAllowedChar(size,allowedChars.toString());
    }
    public static String generateRandomLowerChars(int size,String excludedSpecial) {
        StringBuilder allowedChars = charList;
        for(char c : excludedSpecial.toCharArray()){
            allowedChars.deleteCharAt(allowedChars.indexOf(String.valueOf(c)));
        }
        return generateKeyWithAllowedChar(size,allowedChars.toString());
    }
    public static String generateRandomUpperChars(int size,String excludedSpecial) {
        StringBuilder allowedchars = upperCharList;
        for(char c : excludedSpecial.toCharArray()){
            allowedchars.deleteCharAt(allowedchars.indexOf(String.valueOf(c)));
        }
        return generateKeyWithAllowedChar(size,allowedchars.toString());
    }
    public static String generateRandomSpecial(int size, String excludedSpecial){
        StringBuilder allowedchars = specials;
        for(char c : excludedSpecial.toCharArray()){
            allowedchars.deleteCharAt(allowedchars.indexOf(String.valueOf(c)));
        }
        return generateKeyWithAllowedChar(size,allowedchars.toString());
    }
    public static String shuffle(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        Collections.shuffle(characters);
        StringBuilder result = new StringBuilder();
        for (char c : characters) {
            result.append(c);
        }
        return result.toString();
    }



    public static String generate() {
        return generate(DefaultSize);
    }
    public static String generate(int size) {
        return generate(true,true, true,size,-1,-1,-1,"");
    }

    public static String generateWithoutSpecial() {
        return generateWithoutSpecial(DefaultSize);
    }
    public static String generateWithoutSpecial(int size) {
        return generate(true,true,false, size, -1,-1,-1,"");
    }
    public static String generate(boolean includeChars, boolean includeNums, boolean includeSpecials,
                                  int size, int upperCharSize, int numSize, int specialSize, String excludeChars) {

        if(size==0) throw  new RuntimeException("Size can not be zero");

        StringBuilder lowerChars = charList;
        StringBuilder upperChars = upperCharList;
        StringBuilder specialChars = specials;
        StringBuilder numberChars = numberList;

        if (size == -1) size = DefaultSize;
        int lowerSize = 1;

        int bufferSize = size - lowerSize;
        bufferSize -= (includeChars? (upperCharSize == -1 ? 1 : upperCharSize) : 0);
        bufferSize -= (includeNums? (numSize==-1? 1: numSize) : 0);
        bufferSize -= (includeSpecials? (specialSize==-1?1:specialSize) : 0 );

        if(bufferSize< 0 ) throw new RuntimeException("All required size are not correct");


        // Sample random characters
        StringBuilder result = new StringBuilder();
        Random rand = new Random();

        // Add characters to the list
        if (includeChars) {
            if (upperCharSize != -1) {
                int newcharSize = generateNumberInRange(upperCharSize,upperCharSize+bufferSize);
                bufferSize -= (newcharSize - upperCharSize);
                upperCharSize = newcharSize;
            } else {
                upperCharSize = generateNumberInRange(1,bufferSize+1);
                bufferSize -= (upperCharSize-1);
            }
        } else {
            upperCharSize = 0;
        }
        // Add numbers to the list
        if (includeNums) {
            if(numSize != -1){
                int newnumSize = generateNumberInRange(numSize,numSize+bufferSize);
                bufferSize -= (newnumSize - numSize);
                numSize = newnumSize;
            } else {
                numSize = generateNumberInRange(1,bufferSize+1);
                bufferSize -= (numSize-1);
            }
        } else {
            numSize = 0;
        }
        // Add special characters to the list
        if (includeSpecials) {
            if(specialSize != -1) {
                int newspecialSize = generateNumberInRange(specialSize,bufferSize+specialSize);
                bufferSize -= (newspecialSize - specialSize);
                specialSize = newspecialSize;
            } else {
                specialSize = generateNumberInRange(1,bufferSize+1);
                bufferSize -= (specialSize-1);
            }
        } else {
            specialSize = 0;
        }

        if(bufferSize < 0) throw new RuntimeException("Something went wrong");

        lowerSize += bufferSize;

        for(char c : excludeChars.toCharArray()) {
            if (c >= 'a' && c <'z') {
                lowerChars.deleteCharAt(lowerChars.indexOf(String.valueOf(c)));
            } else if (c >= 'A' && c <='Z') {
                upperChars.deleteCharAt(upperChars.indexOf(String.valueOf(c)));
            } else if (c >='0' && c<='9'){
                numberChars.deleteCharAt(numberChars.indexOf(String.valueOf(c)));
            } else {
                specialChars.deleteCharAt(specialChars.indexOf(String.valueOf(c)));
            }
        }

        result.append(generateKeyWithAllowedChar(lowerSize,lowerChars.toString()));
        result.append(generateKeyWithAllowedChar(upperCharSize,upperChars.toString()));
        result.append(generateKeyWithAllowedChar(numSize,numberChars.toString()));
        result.append(generateKeyWithAllowedChar(specialSize,specialChars.toString()));

        return shuffle(result.toString());
    }
}
