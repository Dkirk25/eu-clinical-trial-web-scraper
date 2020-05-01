package com.clincaloutcome.builder;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class EUBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(EUBuilder.class);

    @Autowired
    private Utils utility;

    public Map<String, List<String>> buildEUListFromWebResults(Elements eudraCTNumber, Map<String, List<String>> listOfResults) {
        String mainEudraCT = "";
        for (Element number : eudraCTNumber) {
            String eudraCT;

            if (number.text().contains("EudraCT Number:")) {
                eudraCT = utility.wordParser(number.text());
                mainEudraCT = eudraCT;
                listOfResults.get("eudraCT").add(eudraCT);
            }
            if (number.text().contains("Sponsor Protocol Number:")) {
                listOfResults.get("sponsorProtocols").add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Start Date*:")) {
                listOfResults.get("startDates").add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Sponsor Name:")) {
                listOfResults.get("sponsorNames").add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Full Title:")) {
                listOfResults.get("fullTitles").add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Medical condition:")) {
                listOfResults.get("medicalConditions").add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Disease:")) {
                listOfResults.get("diseases").add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Population Age:")) {
                listOfResults.get("populationAge").add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Gender:")) {
                listOfResults.get("genders").add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Trial protocol:")) {
                handleTrialProtocol(mainEudraCT, number, listOfResults);
            }
            if (number.text().contains("Trial results:")) {
                listOfResults.get("trialResults").add(utility.wordParser(number.text()));
            }
        }

        return listOfResults;
    }

    private void handleTrialProtocol(String eudraCT, Element number, Map<String, List<String>> listOfResults) {
        String protocol = utility.wordParser(number.text());
        listOfResults.get("trialProtocol").add(protocol);
        String firstProtocol = getProtocolType(protocol);
        // Go into link and save endpoints.
        connectAndGrabEndPoints(eudraCT, firstProtocol, listOfResults);
    }

    private String getProtocolType(String protocol) {
        String firstProtocol;
        if (protocol.contains("Outside EU/EEA")) {
            firstProtocol = "3rd";
        } else {
            firstProtocol = protocol.substring(0, 2);
        }
        return firstProtocol;
    }

    private void connectAndGrabEndPoints(String eudraCT, String firstProtocol, Map<String, List<String>> listOfResults) {
        Document doc2;
        String url = "https://www.clinicaltrialsregister.eu/ctr-search/trial/" + eudraCT + "/" + firstProtocol;

        try {
            doc2 = SSLHelper.getConnection(url).get();

            Elements endPoints = doc2.select("#section-e > tbody > tr");

            List<String> allRows = new ArrayList<>();

            endPoints.forEach(string -> allRows.add(string.text()));

            List<String> primary = new ArrayList<>();
            List<String> secondary = new ArrayList<>();

            allRows.forEach(word -> {
                if (word.contains("E.5.1 Primary end point")) {
                    primary.add(word);
                }
            });

            if (primary.isEmpty()) {
                primary.add("None");
            }

            String secondaryEndpoint = "E.5.2 Secondary end point";
            allRows.forEach(word -> {
                if (word.contains(secondaryEndpoint)) {
                    secondary.add(utility.trailParser(word, secondaryEndpoint));
                }
            });

            if (secondary.isEmpty()) {
                secondary.add("None");
            }

            listOfResults.get("primaryEndpoints").add(utility.trailParser(primary.get(0), "E.5.1 Primary end point"));
            listOfResults.get("secondaryEndPoints").add(utility.trailParser(secondary.get(0), secondaryEndpoint));
        } catch (IOException e) {
            LOGGER.error("Bad url for primary and secondary endpoints.");
        }
    }
}
