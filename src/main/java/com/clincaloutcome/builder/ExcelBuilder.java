package com.clincaloutcome.builder;

import com.clincaloutcome.model.EUClinical;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
class ExcelBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelBuilder.class);

    void printFromEUTrialExcelFile(Map<String, List<String>> listOfResults) {
        String[] columns = {"EudraCT Number", "Sponsor Protocol Number", "Start Date", "Sponsor Name", "Full Title", "Medical Condition", "Disease", "Population Age", "Gender", "Trial Protocol", "Primary End Points", "Secondary End Points", "Trial Results"};
        String outputFile = "./EUClinicalTrails.xlsx";

        try (Workbook workbook = new XSSFWorkbook()) {

            // Create a Font for styling header cells
            CellStyle headerCellStyle = createSheetStyle(workbook);

            // Create a Sheet
            Sheet sheet = workbook.createSheet("EUClinical");

            // Create a Row
            createExcelHeaders(columns, headerCellStyle, sheet);

            int rowNum = 1;

            for (int i = 0; i < listOfResults.get("eudraCT").size(); i++) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(nullStringReplacement(listOfResults.get("eudraCT"), i));
                row.createCell(1).setCellValue(nullStringReplacement(listOfResults.get("sponsorProtocols"), i));
                row.createCell(2).setCellValue(nullStringReplacement(listOfResults.get("startDates"), i));
                row.createCell(3).setCellValue(nullStringReplacement(listOfResults.get("sponsorNames"), i));
                row.createCell(4).setCellValue(nullStringReplacement(listOfResults.get("fullTitles"), i));
                row.createCell(5).setCellValue(nullStringReplacement(listOfResults.get("medicalConditions"), i));
                row.createCell(6).setCellValue(nullStringReplacement(listOfResults.get("diseases"), i));
                row.createCell(7).setCellValue(nullStringReplacement(listOfResults.get("populationAge"), i));
                row.createCell(8).setCellValue(nullStringReplacement(listOfResults.get("genders"), i));
                row.createCell(9).setCellValue(nullStringReplacement(listOfResults.get("trialProtocol"), i));
                row.createCell(10).setCellValue(nullStringReplacement(listOfResults.get("primaryEndpoints"), i));
                row.createCell(11).setCellValue(nullStringReplacement(listOfResults.get("secondaryEndPoints"), i));
                row.createCell(12).setCellValue(nullStringReplacement(listOfResults.get("trialResults"), i));
            }

            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream("uploads/" + outputFile);
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            LOGGER.error("Can't Parse File {}", e.getMessage());
        }
    }

    public void printEUListToExcel(List<EUClinical> euClinicalList) {
        String[] columns = {"EudraCT Number", "Sponsor Protocol Number", "Start Date", "Sponsor Name", "Full Title", "Medical Condition", "Disease", "Population Age", "Gender", "Trial Protocol", "Trial Results", "Primary End Points", "Secondary End Points"};

        String outputFile = "./MatchedEUClinicalTrails.xlsx";
        try (Workbook workbook = new XSSFWorkbook()) {
            CellStyle headerCellStyle = createSheetStyle(workbook);

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
            FileOutputStream fileOut = new FileOutputStream("uploads/" + outputFile);
            workbook.write(fileOut);
            fileOut.close();

        } catch (IOException e) {
            LOGGER.error("Can't Parse File. {}", e.getMessage());
        }
    }

    private CellStyle createSheetStyle(Workbook workbook) {
        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        return headerCellStyle;
    }

    private String nullStringReplacement(List<String> value, int i) {
        if (value.isEmpty()) {
            return "none";
        } else {
            return value.get(i);
        }
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
}
