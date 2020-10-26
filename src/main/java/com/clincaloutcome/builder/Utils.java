package com.clincaloutcome.builder;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;


public class Utils {

    private Utils() {
    }

    public static String nullCheck(String value) {
        return StringUtils.isNotBlank(value) ? value : "N/A";
    }

    public static String trailParser(String word, String regex) {
        List<String> replacedWords = Arrays.asList(regex, "\\(s\\)");

        String newWord = word;
        for (String replacedWord : replacedWords) {
            newWord = newWord.replaceAll(replacedWord, "");
        }
        return newWord.trim();
    }

    public static String wordParser(String word) {
        int colon = word.indexOf(':');
        String toReplace = word.substring(0, colon);
        word = word.replaceAll(toReplace, "");
        word = word.replace(":", "");
        word = word.replaceAll("\\*", "");
        return word.trim();
    }

    public static boolean isValidFileFormat(String file, String fileType) {
        return StringUtils.isNotEmpty(file) && StringUtils.endsWith(file, fileType);
    }
}
