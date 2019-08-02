package com.clincaloutcome.builder;

import com.clincaloutcome.model.EUClinical;
import com.clincaloutcome.model.USClinical;
import com.poiji.bind.Poiji;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

class ExcelBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelBuilder.class);

    private Utils utility = new Utils();

    private List<String> listOfEudra = new ArrayList<>();
    private List<String> listOfSponsorProtocol = new ArrayList<>();
    private List<String> listOfStartDates = new ArrayList<>();
    private List<String> listOfSponsorNames = new ArrayList<>();
    private List<String> listOfFullTitles = new ArrayList<>();
    private List<String> listOfMedicalConditions = new ArrayList<>();
    private List<String> listOfDiseases = new ArrayList<>();
    private List<String> listOfPopulationAge = new ArrayList<>();
    private List<String> listOfGenders = new ArrayList<>();
    private List<String> listOfTrailProtocols = new ArrayList<>();
    private List<String> listOfTrailResults = new ArrayList<>();
    private List<String> listOfPrimaryEndPoints = new ArrayList<>();
    private List<String> listOfSecondaryEndPoints = new ArrayList<>();


    void buildEUListFromWebResults(Elements eudraCTNumber) {
        String eudraCT = "";
        for (Element number : eudraCTNumber) {

            if (number.text().contains("EudraCT Number:")) {
                eudraCT = utility.wordParser(number.text());
                listOfEudra.add(eudraCT);
            }
            if (number.text().contains("Sponsor Protocol Number:")) {
                listOfSponsorProtocol.add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Start Date*:")) {
                listOfStartDates.add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Sponsor Name:")) {
                listOfSponsorNames.add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Full Title:")) {
                listOfFullTitles.add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Medical condition:")) {
                listOfMedicalConditions.add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Disease:")) {
                listOfDiseases.add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Population Age:")) {
                listOfPopulationAge.add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Gender:")) {
                listOfGenders.add(utility.wordParser(number.text()));
            }
            if (number.text().contains("Trial protocol:")) {
                handleTrialProtocol(eudraCT, number);
            }
            if (number.text().contains("Trial results:")) {
                listOfTrailResults.add(utility.wordParser(number.text()));
            }
        }
    }

    private void handleTrialProtocol(String eudraCT, Element number) {
        String protocol = utility.wordParser(number.text());
        listOfTrailProtocols.add(protocol);
        String firstProtocol = getProtocolType(protocol);
        // Go into link and save endpoints.
        connectAndGrabEndPoints(eudraCT, firstProtocol);
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

    void printFromEUTrialExcelFile() {
        String[] columns = {"EudraCT Number", "Sponsor Protocol Number", "Start Date", "Sponsor Name", "Full Title", "Medical Condition", "Disease", "Population Age", "Gender", "Trial Protocol", "Trial Results", "Primary End Points", "Secondary End Points"};
        String outputFile = "./EUClinicalTrails.xlsx";

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
            createExcelHeaders(columns, headerCellStyle, sheet);

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
            FileOutputStream fileOut = new FileOutputStream(outputFile);
            workbook.write(fileOut);
            fileOut.close();

        } catch (IOException e) {
            LOGGER.error("Can't Parse File {}", e.getMessage());
        }
        autoOpenExcel(outputFile);
    }

    private void createExcelHeaders(String[] columns, CellStyle headerCellStyle, Sheet sheet) {
        Row headerRow = sheet.createRow(0);

        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }

    private void connectAndGrabEndPoints(String eudraCT, String firstProtocol) {
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
                    secondary.add(utility.trailParser(word, secondaryEndpoint));
                }
            });

            if (secondary.isEmpty()) {
                secondary.add("None");
            }

            listOfPrimaryEndPoints.add(utility.trailParser(primary.get(0), "E.5.1 Primary end point"));
            listOfSecondaryEndPoints.add(utility.trailParser(secondary.get(0), secondaryEndpoint));

        } catch (IOException e) {
            LOGGER.error("Bad url for primary and secondary endpoints.");
        }
    }


    private <T> List<T> readExcelFile(String file, Class<T> requestClass) {
        return Poiji.fromExcel(new File(file), requestClass);
    }

    List<EUClinical> extractMatchesFromBothLists(String usFile, String euFile) {
        // Take list of US Clinical CSV file
        List<USClinical> usClinicalList = readExcelFile(usFile, USClinical.class);

        List<USClinical> usListWithoutDuplicates = getListWithoutDuplicates(usClinicalList);

        // Take list of EU Clinical excel file
        List<EUClinical> euClinicalList = readExcelFile(euFile, EUClinical.class);

        List<EUClinical> distinctEUList1 = euClinicalList.stream()
                .collect(collectingAndThen(toCollection(() ->
                                new TreeSet<>(
                                        Comparator.comparing(EUClinical::getSponsorProtocolNumber,
                                                Comparator.nullsFirst(Comparator.naturalOrder())))),
                        ArrayList::new));

        return removeUSProtocolsFromEUList(distinctEUList1, usListWithoutDuplicates);
    }

    private ArrayList<USClinical> getListWithoutDuplicates(List<USClinical> usClinicalList) {
        return usClinicalList.stream()
                .collect(collectingAndThen(toCollection(() ->
                                new TreeSet<>(
                                        Comparator.comparing(USClinical::getOtherId,
                                                Comparator.nullsFirst(Comparator.naturalOrder())))),
                        ArrayList::new));
    }

    private List<EUClinical> removeUSProtocolsFromEUList(List<EUClinical> euList, List<USClinical> usList) {
        List<EUClinical> newEuList = new ArrayList<>(euList);

        for (EUClinical euClinical : euList) {
            for (USClinical otherId : usList) {
                if (otherId.getOtherId().contains("|")) {
                    String[] words = otherId.getOtherId().split("\\|");
                    List<String> wordArrayList = new ArrayList<>(Arrays.asList(words));
                    for (String word : wordArrayList) {
                        if (word.equalsIgnoreCase(euClinical.getSponsorProtocolNumber())) {
                            newEuList.remove(euClinical);
                        }
                    }
                } else if (otherId.getOtherId().equalsIgnoreCase(euClinical.getSponsorProtocolNumber())) {
                    newEuList.remove(euClinical);
                }
            }
        }
        return newEuList;
    }

    void printEUListToExcel(List<EUClinical> euClinicalList) {
        String[] columns = {"EudraCT Number", "Sponsor Protocol Number", "Start Date", "Sponsor Name", "Full Title", "Medical Condition", "Disease", "Population Age", "Gender", "Trial Protocol", "Trial Results", "Primary End Points", "Secondary End Points"};

        String outputFile = "./MatchedEUClinicalTrails.xlsx";
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
            Sheet sheet = workbook.createSheet("MatchedEUClinical");

            // Create a Row
            createExcelHeaders(columns, headerCellStyle, sheet);

            int rowNum = 1;

            for (EUClinical euClinical : euClinicalList) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(euClinical.getEudraNumber());
                row.createCell(1).setCellValue(euClinical.getSponsorProtocolNumber());
                row.createCell(2).setCellValue(euClinical.getStartDate());
                row.createCell(3).setCellValue(euClinical.getSponsorName());
                row.createCell(4).setCellValue(euClinical.getFullTitle());
                row.createCell(5).setCellValue(euClinical.getMedicalCondition());
                row.createCell(6).setCellValue(euClinical.getDisease());
                row.createCell(7).setCellValue(euClinical.getPopulationAge());
                row.createCell(8).setCellValue(euClinical.getGender());
                row.createCell(9).setCellValue(euClinical.getTrialProtocol());
                row.createCell(10).setCellValue(euClinical.getTrialResult());
                row.createCell(11).setCellValue(euClinical.getPrimaryEndPoint());
                row.createCell(12).setCellValue(euClinical.getSecondaryEndPoint());
            }

            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }


            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(outputFile);
            workbook.write(fileOut);
            fileOut.close();

        } catch (IOException e) {
            LOGGER.error("Can't Parse File.");
        }
        autoOpenExcel(outputFile);
    }

    private void autoOpenExcel(String outputFile) {
        try {
            Desktop.getDesktop().open(new File(outputFile));
        } catch (IOException e) {
            LOGGER.error("Can't AutoOpen Excel File.");
        }
    }
}
