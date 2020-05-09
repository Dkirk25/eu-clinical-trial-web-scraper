package com.clincaloutcome.client;

import com.clincaloutcome.builder.Utils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ParseProtocol {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseProtocol.class);

    public void handleTrialProtocol(String eudraCT, Element number, Map<String, List<String>> listOfResults) {
        String protocol = Utils.wordParser(number.text());
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

            listOfResults.get("primaryEndpoints").add(addProtocolToMap("E.5.1 Primary end point", allRows));
            listOfResults.get("secondaryEndPoints").add(addProtocolToMap("E.5.2 Secondary end point", allRows));
        } catch (IOException e) {
            LOGGER.error("Bad url for primary and secondary endpoints. {}", e.getMessage());
        }
    }

    protected String addProtocolToMap(String specificProtocol, List<String> listOfProtocols) {
        String tempProtocol = "";
        for (String word : listOfProtocols) {
            if (word.contains(specificProtocol)) {
                tempProtocol = word;
                return Utils.trailParser(tempProtocol, specificProtocol);
            }
        }
        if (StringUtils.isBlank(tempProtocol)) {
            tempProtocol = "None";
        }
        return Utils.trailParser(tempProtocol, specificProtocol);
    }
}
