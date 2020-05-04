package com.clincaloutcome.client;

import com.clincaloutcome.builder.Utils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class EUBuilder {
    @Autowired
    private Utils utility;

    @Autowired
    private ParseProtocol parseProtocol;

    public Map<String, List<String>> buildEUListFromWebResults(Elements eudraCTNumber, Map<String, List<String>> listOfResults) {
        List<String> listOfColumns = Arrays.asList("Sponsor Protocol Number:", "Start Date*:", "Sponsor Name:", "Full Title:",
                "Medical condition:", "Disease:", "Population Age:", "Gender:", "Trial results:");

        List<String> mapFields = Arrays.asList("sponsorProtocols", "startDates", "sponsorNames", "fullTitles",
                "medicalConditions", "diseases", "populationAge", "genders", "trialResults");

        String mainEudraCT = "";
        for (Element number : eudraCTNumber) {
            String eudraCT;

            if (number.text().contains("EudraCT Number:")) {
                eudraCT = utility.wordParser(number.text());
                mainEudraCT = eudraCT;
                listOfResults.get("eudraCT").add(eudraCT);
            } else if (number.text().contains("Trial protocol:")) {
                parseProtocol.handleTrialProtocol(mainEudraCT, number, listOfResults);
            } else {
                for (int i = 0; i < listOfColumns.size(); i++) {
                    if (number.text().contains(listOfColumns.get(i))) {
                        listOfResults.get(mapFields.get(i)).add(utility.wordParser(number.text()));
                    }
                }
            }
        }
        return listOfResults;
    }
}
