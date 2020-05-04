package com.clincaloutcome.builder;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Utils {

    public String trailParser(String word, String regex) {
        List<String> replacedWords = Arrays.asList(regex, "\\(s\\)");

        String newWord = word;
        for (String replacedWord : replacedWords) {
            newWord = newWord.replaceAll(replacedWord, "");
        }
        return newWord.trim();
    }

    public String wordParser(String word) {
        int colon = word.indexOf(':');
        String toReplace = word.substring(0, colon);
        word = word.replaceAll(toReplace, "");
        word = word.replace(":", "");
        word = word.replaceAll("\\*", "");
        return word.trim();
    }

    public boolean isValidFileFormat(String file, String fileType) {
        return !file.isEmpty() && file.endsWith(fileType);
    }
}
