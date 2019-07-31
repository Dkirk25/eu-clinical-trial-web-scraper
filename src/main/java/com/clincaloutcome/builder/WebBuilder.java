package com.clincaloutcome.builder;

import com.clincaloutcome.model.EUClinical;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WebBuilder {

    private ExcelBuilder excelBuilder;

    public void singleBuilder(final String url, final String pages) {
        iterateThroughUrlAndPage(url, pages);
        excelBuilder.printFromEUTrialExcelFile();
    }

    public void bulkBuilder(final String file) {
        excelBuilder = new ExcelBuilder();

        createUsingBulkFile(file);
        excelBuilder.printFromEUTrialExcelFile();
    }

    public void crossBuilder(final String usTrialFile, final String euTrialFile) {
        excelBuilder = new ExcelBuilder();

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
                    doc = Jsoup.connect(url + "&page=" + i).get();

                    if (doc != null) {
                        Elements eudraCTNumber = doc.select("div.results.grid_8plus > table > tbody > tr > td");
                        excelBuilder.buildEUListFromWebResults(eudraCTNumber);
                    }
                    i++;
                }

            } catch (IOException e) {
                System.out.println("Cannot Parse Website!");
            }
        } else {
            System.out.println("Url or Page Number is empty!");
        }
    }

    private void createUsingBulkFile(String bulk) {
        List<String> fileLines = new ArrayList<>();

        if (bulk.length() > 0 && bulk.endsWith(".txt")) {

            try (Stream<String> files = Files.lines(Paths.get(bulk))) {
                files.forEach(fileLines::add);

                fileLines.forEach(fileLine -> {
                    String[] line = fileLine.split(" ");
                    String bulkUrl = line[0];
                    String bulkPage = line[1];

                    iterateThroughUrlAndPage(bulkUrl, bulkPage);
                });
            } catch (IOException e) {
                System.out.println("Can't Read File.");
            }
        } else {
            System.out.println("File type must end with .txt");
            System.exit(0);
        }
    }
}
