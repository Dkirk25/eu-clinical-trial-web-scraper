package com.clincaloutcome.builder;

import com.clincaloutcome.model.EUClinical;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static sun.net.www.protocol.http.HttpURLConnection.userAgent;

public class WebBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebBuilder.class);

    private ExcelBuilder excelBuilder = new ExcelBuilder();

    public void singleBuilder(final String url, final String pages) {
        iterateThroughUrlAndPage(url, pages);
        excelBuilder.printFromEUTrialExcelFile();
    }

    public void bulkBuilder(final String file) {
        createUsingBulkFile(file);
        excelBuilder.printFromEUTrialExcelFile();
    }

    public void crossBuilder(final String usTrialFile, final String euTrialFile) {
        List<EUClinical> euList = excelBuilder.extractMatchesFromBothLists(usTrialFile, euTrialFile);
        excelBuilder.printEUListToExcel(euList);
    }

    private void iterateThroughUrlAndPage(String url, String pages) {
        if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(pages)) {
            try {
                int i = 1;
                int pageCount = Integer.parseInt(pages);
                while (i <= pageCount) {
                    Document doc;
                    doc = Jsoup.connect(url + "&page=" + i).userAgent(userAgent).ignoreHttpErrors(true).get();

                    if (doc != null) {
                        Elements eudraCTNumber = doc.select("div.results.grid_8plus > table > tbody > tr > td");
                        excelBuilder.buildEUListFromWebResults(eudraCTNumber);
                    }
                    i++;
                }
            } catch (IOException e) {
                LOGGER.error("Cannot Parse Website!");
            }
        } else {
            LOGGER.error("Url or Page Number is empty!");
        }
    }

    private void createUsingBulkFile(String bulk) {
        List<String> fileLines = new ArrayList<>();

        try (Stream<String> files = Files.lines(Paths.get(bulk))) {
            files.forEach(fileLines::add);

            AtomicInteger i = new AtomicInteger(0);
            fileLines.forEach(fileLine -> {
                String[] line = fileLine.split(" ");
                String bulkUrl = line[0];
                String bulkPage = line[1];
                progressPercentage(i.get(), fileLines.size());
                iterateThroughUrlAndPage(bulkUrl, bulkPage);
                i.set(i.get() + 1);

            });
        } catch (IOException e) {
            LOGGER.error("Can't Read File.");
        }
    }

    private void progressPercentage(int remain, int total) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }
        int maxBareSize = 10; // 10unit for 100%
        int remainPercent = ((100 * remain) / total) / maxBareSize;
        char defaultChar = '-';
        String icon = "*";
        String bare = new String(new char[maxBareSize]).replace('\0', defaultChar) + "]";
        StringBuilder bareDone = new StringBuilder();
        bareDone.append("[");
        for (int i = 0; i < remainPercent; i++) {
            bareDone.append(icon);
        }
        String bareRemain = bare.substring(remainPercent);
        System.out.print("\r" + bareDone + bareRemain + " " + remainPercent * 10 + "%");
        if (remain == total) {
            System.out.print("\n");
        }
    }
}
