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
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

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
            String euTrail = "EUList.xlsx";

            List<EUClinical> newList = extractMatchesFromBothLists(usTrail, euTrail);

            // Create Excel Document with new EU list.
            printEUListToExcel(newList);
            System.exit(0);
        }

        System.out.println("Upload a txt File? (y)es or (n)o: ");
        String f = sc.nextLine();

        if (f.equalsIgnoreCase("y")) {
            System.out.println("What is the file name: ");
            String bulk = sc.nextLine();

            // Process Bulk File.
            createUsingBulkFile(bulk);
        } else {
            System.out.println("Use Single Search? (y)es or (n)o: ");
            String single = sc.nextLine();

            if (single.equalsIgnoreCase("y")) {

                System.out.println("Enter Url: ");
                String url = sc.nextLine();
                System.out.println("How Many Pages: ");
                String pages = sc.nextLine();

                iterateThroughUrlAndPage(url, pages);
            } else {
                System.exit(0);
            }
        }
        printFromFileToExcel();
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

    private static void printFromFileToExcel() {
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
            FileOutputStream fileOut = new FileOutputStream("../EUClinicalTrails.xlsx");
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

    private static void printEUListToExcel(List<EUClinical> euClinicalList) {
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
            Sheet sheet = workbook.createSheet("MatchedEUClinical");

            // Create a Row
            Row headerRow = sheet.createRow(0);

            // Create cells
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

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
            FileOutputStream fileOut = new FileOutputStream("../MatchedEUClinicalTrails.xlsx");
            workbook.write(fileOut);
            fileOut.close();

        } catch (IOException e) {
            System.out.println("Can't Parse File.");
        }
        try {
            Desktop.getDesktop().open(new File("../MatchedEUClinicalTrails.xlsx"));
        } catch (IOException e) {
            System.out.println("Can't AutoOpen Excel File.");
        }
    }

    private static <T> List<T> readExcelFile(String file, Class<T> requestClass) {
        return Poiji.fromExcel(new File(file), requestClass);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private static List<EUClinical> extractMatchesFromBothLists(String usFile, String euFile) {
        // Take list of US Clinical CSV file
        List<USClinical> usClinicalList = readExcelFile(usFile, USClinical.class);

        List<USClinical> usListWithoutDuplicates = usClinicalList.stream()
                .collect(collectingAndThen(toCollection(() ->
                                new TreeSet<>(
                                        Comparator.comparing(USClinical::getOtherId,
                                                Comparator.nullsFirst(Comparator.naturalOrder())))),
                        ArrayList::new));

        // This needs to handle null
//        List<USClinical> distinctUSList = usClinicalList.stream()
//                .filter(distinctByKey(USClinical::getOtherId))
//                .collect(Collectors.toList());


        // Take list of EU Clinical excel file
        List<EUClinical> euClinicalList = readExcelFile(euFile, EUClinical.class);

//        List<EUClinical> distinctEUList = euClinicalList.stream()
//                .filter(distinctByKey(EUClinical::getSponsorProtocolNumber))
//                .collect(Collectors.toList());

        List<EUClinical> distinctEUList1 = euClinicalList.stream()
                .collect(collectingAndThen(toCollection(() ->
                                new TreeSet<>(
                                        Comparator.comparing(EUClinical::getSponsorProtocolNumber,
                                                Comparator.nullsFirst(Comparator.naturalOrder())))),
                        ArrayList::new));

        // Compare both, extract the ones that are same base on "other ids" (US) and protocol number (EU)

        // then create new list
        Set<String> getOtherIds = usListWithoutDuplicates.stream()
                .map(USClinical::getOtherId)
                .collect(Collectors.toSet());


        // stream the list and use the set to filter it

        // List<EUClinical> filteredList = standardSort(distinctEUList1, usListWithoutDuplicates);

        return distinctEUList1.stream()
                .filter(eu -> getOtherIds.stream()
                        .noneMatch(us ->
                                eu.getSponsorProtocolNumber().contains(us)))
                .collect(Collectors.toList());
    }

    private static List<EUClinical> standardSort(List<EUClinical> obj1, List<USClinical> obj2) {
        List<EUClinical> returnList = new ArrayList<>();
        for (EUClinical euClinical : obj1) {
            boolean found = false;
            for (USClinical usClinical : obj2) {
                if (!StringUtils.isEmpty(euClinical.getSponsorProtocolNumber())) {
                    if (euClinical.getSponsorProtocolNumber().contains(usClinical.getOtherId())) {
                        found = true;
                    }
                }
            }
            if (!found) {
                returnList.add(euClinical);
            }
        }

        return returnList;
    }
}
