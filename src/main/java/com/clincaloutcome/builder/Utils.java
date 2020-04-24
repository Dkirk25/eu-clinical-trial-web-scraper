package com.clincaloutcome.builder;

import org.springframework.stereotype.Component;

@Component
public class Utils {

   public String trailParser(String word, String regex) {
        // Dont forget Remove (s)
        word = word.replaceAll(regex, "");
        word = word.replaceAll("[(s)]", "");
        return word.trim();
    }

    public String wordParser(String word) {
        int colon = word.indexOf(':');
        String toReplace = word.substring(0, colon);
        word = word.replaceAll(toReplace, "");
        word = word.replaceAll(":", "");
        word = word.replaceAll("\\*", "");
        return word.trim();
    }

    public boolean isValidFileFormat(String file, String fileType) {
        return !file.isEmpty() && file.endsWith(fileType);
    }
}
