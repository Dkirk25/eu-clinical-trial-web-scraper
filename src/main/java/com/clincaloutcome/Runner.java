package com.clincaloutcome;

import com.clincaloutcome.model.EUClinical;
import com.clincaloutcome.model.USClinical;
import com.poiji.bind.Poiji;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Runner {

    private static List<String> listOfEudra = new ArrayList<>();
    private static List<String> listOfSponsorProtocol = new ArrayList<>();
    private static List<String> listOfStartDates = new ArrayList<>();
    private static List<String> listOfSponsorNames = new ArrayList<>();
    private static List<String> listOfFullTitles = new ArrayList<>();
    private static List<String> listOfMedicalConditions = new ArrayList<>();
    private static List<String> listOfDiseases = new ArrayList<>();
    private static List<String> listOfPopulationAge = new ArrayList<>();
    private static List<String> listOfGenders = new ArrayList<>();
    private static List<String> listOfTrailProtocols = new ArrayList<>();
    private static List<String> listOfTrailResults = new ArrayList<>();
    private static List<String> listOfPrimaryEndPoints = new ArrayList<>();
    private static List<String> listOfSecondaryEndPoints = new ArrayList<>();

    private static String eudraCT = null;

    private static List<String> fileLines = new ArrayList<>();


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Trying to cross reference two files? (y)es or (n)o:");
        String crossRef = sc.nextLine();

        if (crossRef.equalsIgnoreCase("y")) {
            System.out.println("Enter US Clinical Trial:");
            String usTrail = "USTrials.xlsx";

            System.out.println("Enter EU Clinical Trial:");
            String euTrail = "EUClinicalTrails.xlsx";

            List<EUClinical> newList = extractMatchesFromBothLists(usTrail, euTrail);

            // Create Excel Document with new EU list.

        }

        System.out.println("Upload a txt File? (y)es or (n)o: ");
        String f = sc.nextLine();

        if (f.equalsIgnoreCase("y")) {
            System.out.println("What is the file name: ");
            String bulk = sc.nextLine();

            // Process Bulk File.
            createUsingBulkFile(bulk);
        } else {
            System.out.println("Enter Url: ");
            String url = sc.nextLine();
            System.out.println("How Many Pages: ");
            String pages = sc.nextLine();

            iterateThroughUrlAndPage(url, pages);
        }
        printToExcel();
    }

    private static void createUsingBulkFile(String bulk) {
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

    private static void iterateThroughUrlAndPage(String url, String pages) {
        if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(pages)) {
            try {
                int i = 1;
                int pageCount = Integer.parseInt(pages);
                while (i <= pageCount) {
                    Document doc;
                    doc = Jsoup.connect(url + "&page=" + i).get();

                    if (doc != null) {
                        Elements eudraCTNumber = doc.select("div.results.grid_8plus > table > tbody > tr > td");
                        insertDataIntoLists(eudraCTNumber);
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

    private static void insertDataIntoLists(Elements eudraCTNumber) {
        for (Element number : eudraCTNumber) {

            if (number.text().contains("EudraCT Number:")) {
                eudraCT = wordParser(number.text());
                listOfEudra.add(eudraCT);
            }
            if (number.text().contains("Sponsor Protocol Number:")) {
                listOfSponsorProtocol.add(wordParser(number.text()));
            }
            if (number.text().contains("Start Date*:")) {
                listOfStartDates.add(wordParser(number.text()));
            }
            if (number.text().contains("Sponsor Name:")) {
                listOfSponsorNames.add(wordParser(number.text()));
            }
            if (number.text().contains("Full Title:")) {
                listOfFullTitles.add(wordParser(number.text()));
            }
            if (number.text().contains("Medical condition:")) {
                listOfMedicalConditions.add(wordParser(number.text()));
            }
            if (number.text().contains("Disease:")) {
                listOfDiseases.add(wordParser(number.text()));
            }
            if (number.text().contains("Population Age:")) {
                listOfPopulationAge.add(wordParser(number.text()));
            }
            if (number.text().contains("Gender:")) {
                listOfGenders.add(wordParser(number.text()));
            }
            if (number.text().contains("Trial protocol:")) {
                String protocol = wordParser(number.text());
                listOfTrailProtocols.add(protocol);

                String firstProtocol = getProtocolType(protocol);

                // Go into link and save endpoints.
                connectAndGrabEndPoints(firstProtocol);
            }
            if (number.text().contains("Trial results:")) {
                listOfTrailResults.add(wordParser(number.text()));
            }
        }
    }

    private static String getProtocolType(String protocol) {
        String firstProtocol;
        if (protocol.contains("Outside EU/EEA")) {
            firstProtocol = "3rd";
        } else {
            firstProtocol = protocol.substring(0, 2);
        }
        return firstProtocol;
    }

    private static void connectAndGrabEndPoints(String firstProtocol) {
        Document doc2;
        String url = "https://www.clinicaltrialsregister.eu/ctr-search/trial/" + eudraCT + "/" + firstProtocol;

        try {
            doc2 = Jsoup.connect(url).get();

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
                    secondary.add(trailParser(word, secondaryEndpoint));
                }
            });

            if (secondary.isEmpty()) {
                secondary.add("None");
            }

            listOfPrimaryEndPoints.add(trailParser(primary.get(0), "E.5.1 Primary end point"));
            listOfSecondaryEndPoints.add(trailParser(secondary.get(0), secondaryEndpoint));

        } catch (IOException e) {
            System.out.println("Bad url for primary and secondary endpoints.");
        }
    }

    private static String trailParser(String word, String regex) {
        // Dont forget Remove (s)
        word = word.replaceAll(regex, "");
        word = word.replaceAll("[(s)]", "");
        return word.trim();
    }

    private static String wordParser(String word) {
        int colon = word.indexOf(':');
        String toReplace = word.substring(0, colon);
        word = word.replaceAll(toReplace, "");
        word = word.replaceAll(":", "");
        word = word.replaceAll("\\*", "");
        return word.trim();
    }

    private static void printToExcel() {
        String[] columns = {"EudraCT Number", "Sponsor Protocol Number", "Start Date", "Sponsor Name", "Full Title", "Medical condition", "Disease", "Population Age", "Gender", "Trial Protocol", "Trial results", "Primary End Points", "Secondary End Points"};

        try (Workbook workbook = new XSSFWorkbook()) {

            // Create a Font for styling header cells
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            // Create a CellStyle with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create a Sheet
            Sheet sheet = workbook.createSheet("EUClinical");

            // Create a Row
            Row headerRow = sheet.createRow(0);

            // Create cells
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowNum = 1;

            for (int i = 0; i < listOfEudra.size(); i++) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(listOfEudra.get(i));
                row.createCell(1).setCellValue(listOfSponsorProtocol.get(i));
                row.createCell(2).setCellValue(listOfStartDates.get(i));
                row.createCell(3).setCellValue(listOfSponsorNames.get(i));
                row.createCell(4).setCellValue(listOfFullTitles.get(i));
                row.createCell(5).setCellValue(listOfMedicalConditions.get(i));
                row.createCell(6).setCellValue(listOfDiseases.get(i));
                row.createCell(7).setCellValue(listOfPopulationAge.get(i));
                row.createCell(8).setCellValue(listOfGenders.get(i));
                row.createCell(9).setCellValue(listOfTrailProtocols.get(i));
                row.createCell(10).setCellValue(listOfTrailResults.get(i));
                row.createCell(11).setCellValue(listOfPrimaryEndPoints.get(i));
                row.createCell(12).setCellValue(listOfSecondaryEndPoints.get(i));
            }

            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream("/Users/donald/Desktop/EUClinicalTrails.xlsx");
            workbook.write(fileOut);
            fileOut.close();

        } catch (IOException e) {
            System.out.println("Can't Parse File.");
        }
        try {
            Desktop.getDesktop().open(new File("../EUClinicalTrails.xlsx"));
        } catch (IOException e) {
            System.out.println("Can't AutoOpen Excel File.");
        }
    }

    private static <T> List<T> readExcelFile(String file, Class<T> requestClass) {
        return Poiji.fromExcel(new File(file), requestClass);
    }

    private static List<EUClinical> extractMatchesFromBothLists(String usFile, String euFile) {
        // Take list of US Clinical CSV file
        List<USClinical> usClinicalList = readExcelFile(usFile, USClinical.class);
        // Take list of EU Clinical excel file
        List<EUClinical> euClinicalList = readExcelFile(euFile, EUClinical.class);
        // Compare both, extract the ones that are same base on "other ids" (US) and protocol number (EU)

        // then create new list
        Set<String> getOtherIds = usClinicalList.stream()
                .map(USClinical::getOtherId)
                .collect(Collectors.toSet());

        // stream the list and use the set to filter it
        List<EUClinical> newList = euClinicalList.stream()
                .filter(e -> !getOtherIds.contains(e.getSponsorProtocolNumber()))
                .collect(Collectors.toList());

        return newList;
    }
}
