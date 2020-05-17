package com.clincaloutcome.client;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ClinicalPageHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClinicalPageHelper.class);

    @Autowired
    private WebScraper webScraper;

    public List<List<String>> iterateThroughUrlAndPage(String url, String pages) {
        if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(pages)) {
            try {
                int i = 1;
                int pageCount = Integer.parseInt(pages);

                List<List<String>> mapOfValues = new ArrayList<>();

                while (i <= pageCount) {
                    Document doc;
                    doc = SSLHelper.getConnection(url + "&page=" + i).ignoreHttpErrors(true).get();

                    if (doc != null) {
                        // Get list of tables
                        Elements tableResults = doc.select("table.result > tbody");

                        tableResults.parallelStream().forEach(trialBody -> mapOfValues.add(webScraper.iterateRowInTable(trialBody)));
                    }
                    i++;
                }
                return mapOfValues;
            } catch (IOException e) {
                LOGGER.error("Cannot Parse Website!");
            }
        } else {
            LOGGER.error("Url or Page Number is empty!");
        }
        return new ArrayList<>();
    }

    public Map<Integer, List<List<String>>> createUsingBulkFile(File bulk) {
        try (BufferedReader br = new BufferedReader(new FileReader(bulk))) {
            String input;
            Map<Integer, List<List<String>>> totalResults = new HashMap<>();
            Integer count = 0;
            while ((input = br.readLine()) != null) {
                String[] line = input.split(" ");
                String bulkUrl = line[0];
                String bulkPage = line[1];
                totalResults.put(count, iterateThroughUrlAndPage(bulkUrl, bulkPage));
                count++;
            }
            return totalResults;
        } catch (IOException e) {
            LOGGER.error("Can't Read File.");
        }
        return new HashMap<>();
    }
}
