package Utility;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KeyGenerator {

    public static StringBuilder charList = new StringBuilder();
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
            charList.append(c);
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

    public static String generateRandomNumber(int size,String excludedSpecial) {
        Random rand = new Random();
        StringBuilder result = new StringBuilder();
        StringBuilder specialChars = numberList;
        for (char c : excludedSpecial.toCharArray()) {
            specialChars.deleteCharAt(specialChars.indexOf(String.valueOf(c)));
        }
        for (int i = 0; i < size; i++) {
            result.append(specialChars.charAt(rand.nextInt(specialChars.length())));
        }
        return result.toString();
    }

    public static String generateRandomChars(int size,String excludedSpecial) {
        Random rand = new Random();
        StringBuilder result = new StringBuilder();
        StringBuilder specialChars = charList;
        for (char c : excludedSpecial.toCharArray()) {
            specialChars.deleteCharAt(specialChars.indexOf(String.valueOf(c)));
        }
        for (int i = 0; i < size; i++) {
            result.append(specialChars.charAt(rand.nextInt(specialChars.length())));
        }
        return result.toString();
    }

    public static String generateRandomSpecial(int size, String excludedSpecial){
        Random rand = new Random();
        StringBuilder result = new StringBuilder();
        StringBuilder specialChars = specials;
        for (char c : excludedSpecial.toCharArray()) {
            specialChars.deleteCharAt(specialChars.indexOf(String.valueOf(c)));
        }
        for (int i = 0; i < size; i++) {
            result.append(specialChars.charAt(rand.nextInt(specialChars.length())));
        }
        return result.toString();
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

    //generate 8 char long password including specials
    public static String generate() {
        return generate(DefaultSize);
    }
    public static  String generate(int size) {
        return generate(true,true, true,size,-1,-1,-1,"");
    }

    public static String generateWithoutSpecial() {
        return generateWithoutSpecial(DefaultSize);
    }
    public static String generateWithoutSpecial(int size) {
        return generate(true,true,false, size, -1,-1,-1,"");
    }
    public static String generate(boolean includeChars, boolean includeNums, boolean includeSpecials,
                                  int size, int charSize, int numSize, int specialSize, String excludeChars) {

        if (size == -1) size = DefaultSize;
        // Sample random characters
        StringBuilder result = new StringBuilder();
        Random rand = new Random();

        int bufferSize = size;
        if(charSize!=-1) bufferSize -=charSize;
        if(numSize!=-1)  bufferSize -=numSize;
        if(specialSize!=-1) bufferSize -= specialSize;

        if(bufferSize< 0) throw new RuntimeException("Invalid specified size");

        // Add characters to the list
        if (includeChars) {
            if (charSize != -1) {
                int newcharSize = generateNumberInRange(charSize,charSize+bufferSize);
                bufferSize -= (newcharSize - charSize);
                charSize = newcharSize;
            } else {
                charSize = generateNumberInRange(0,bufferSize);
                bufferSize -= charSize;
            }
        } else {
            charSize = 0;
        }
        // Add numbers to the list
        if (includeNums) {
            if(numSize != -1){
                int newnumSize = generateNumberInRange(numSize,numSize+bufferSize);
                bufferSize -= (newnumSize - numSize);
                numSize = newnumSize;
            } else {
                numSize = generateNumberInRange(numSize,bufferSize);
                bufferSize -= numSize;
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
                specialSize = generateNumberInRange(specialSize,bufferSize);
                bufferSize -= specialSize;
            }
        } else {
            specialSize = 0;
        }

        if(bufferSize!= 0) throw new RuntimeException("Something went wrong");

        result.append(generateRandomChars(charSize,excludeChars));
        result.append(generateRandomNumber(numSize,excludeChars));
        result.append(generateRandomSpecial(specialSize,excludeChars));

        return shuffle(result.toString());
    }
}
