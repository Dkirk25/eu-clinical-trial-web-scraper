package com.clincaloutcome.builder;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ClinicalPageHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClinicalPageHelper.class);

    @Autowired
    private ExcelBuilder excelBuilder;

    public Map<String, List<String>> iterateThroughUrlAndPage(String url, String pages) {
        if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(pages)) {
            try {
                int i = 1;
                int pageCount = Integer.parseInt(pages);
                while (i <= pageCount) {
                    Document doc;
                    doc = SSLHelper.getConnection(url + "&page=" + i).ignoreHttpErrors(true).get();

                    if (doc != null) {
                        Elements eudraCTNumber = doc.select("div.results.grid_8plus > table > tbody > tr > td");
                        return excelBuilder.buildEUListFromWebResults(eudraCTNumber);
                    }
                    i++;
                }
            } catch (IOException e) {
                LOGGER.error("Cannot Parse Website!");
            }
        } else {
            LOGGER.error("Url or Page Number is empty!");
        }
        return new HashMap<>();
    }

    public void createUsingBulkFile(File bulk) {
        try (BufferedReader br = new BufferedReader(new FileReader(bulk))) {
            String input;
            while ((input = br.readLine()) != null) {
                String[] line = input.split(" ");
                String bulkUrl = line[0];
                String bulkPage = line[1];
                iterateThroughUrlAndPage(bulkUrl, bulkPage);
            }
        } catch (IOException e) {
            LOGGER.error("Can't Read File.");
        }
    }
}
