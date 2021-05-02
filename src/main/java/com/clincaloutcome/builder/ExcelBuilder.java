package com.clincaloutcome.builder;

import com.clincaloutcome.model.EUClinical;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
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
public
class ExcelBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelBuilder.class);

    public void printFromEUTrialExcelFile(Map<Integer, List<List<String>>> listOfResults) {
        var columns = new String[]{"EudraCT Number", "Sponsor Protocol Number", "Start Date", "Sponsor Name", "Full Title", "Medical Condition", "Disease", "Population Age", "Gender", "Trial Protocol", "Trial Results", "Primary End Points", "Secondary End Points"};
        var outputFile = "./EUClinicalTrails.xlsx";

        try (Workbook workbook = new XSSFWorkbook()) {

            // Create a Font for styling header cells
            var headerCellStyle = createSheetStyle(workbook);

            // Create a Sheet
            var sheet = workbook.createSheet("EUClinical");

            // Create a Row
            createExcelHeaders(columns, headerCellStyle, sheet);

            var rowNum = 1;

            for (var i = 0; i < listOfResults.size(); i++) {
                List<List<String>> firstFile = listOfResults.get(0);
                for (List<String> trialData : firstFile) {
                    var columnCount = 0;

                    var row = sheet.createRow(rowNum++);

                    row.createCell(0).setCellValue(trialData.get(columnCount));
                    row.createCell(1).setCellValue(trialData.get(columnCount + 1));
                    row.createCell(2).setCellValue(trialData.get(columnCount + 2));
                    row.createCell(3).setCellValue(trialData.get(columnCount + 3));
                    row.createCell(4).setCellValue(trialData.get(columnCount + 4));
                    row.createCell(5).setCellValue(trialData.get(columnCount + 5));
                    row.createCell(6).setCellValue(trialData.get(columnCount + 6));
                    row.createCell(7).setCellValue(trialData.get(columnCount + 7));
                    row.createCell(8).setCellValue(trialData.get(columnCount + 8));
                    row.createCell(9).setCellValue(trialData.get(columnCount + 9));
                    row.createCell(10).setCellValue(trialData.get(columnCount + 10));
                    row.createCell(11).setCellValue(trialData.get(columnCount + 11));
                    row.createCell(12).setCellValue(trialData.get(columnCount + 12));
                }
            }

            // Resize all columns to fit the content size
            for (var i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a file
            var fileOut = new FileOutputStream("uploads/" + outputFile);
            workbook.write(fileOut);
            fileOut.close();
        } catch (
                IOException e) {
            LOGGER.error("Can't Parse File {}", e.getMessage());
        }

    }

    public void printEUListToExcel(List<EUClinical> euClinicalList) {
        var columns = new String[]{"EudraCT Number", "Sponsor Protocol Number", "Start Date", "Sponsor Name", "Full Title", "Medical Condition", "Disease", "Population Age", "Gender", "Trial Protocol", "Trial Results", "Primary End Points", "Secondary End Points"};

        var outputFile = "./MatchedEUClinicalTrails.xlsx";
        try (Workbook workbook = new XSSFWorkbook()) {
            var headerCellStyle = createSheetStyle(workbook);

            // Create a Sheet
            var sheet = workbook.createSheet("MatchedEUClinical");

            // Create a Row
            createExcelHeaders(columns, headerCellStyle, sheet);

            var rowNum = 1;

            for (EUClinical euClinical : euClinicalList) {
                var row = sheet.createRow(rowNum++);

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
            for (var i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a file
            var fileOut = new FileOutputStream("uploads/" + outputFile);
            workbook.write(fileOut);
            fileOut.close();

        } catch (IOException e) {
            LOGGER.error("Can't Parse File. {}", e.getMessage());
        }
    }

    private CellStyle createSheetStyle(Workbook workbook) {
        // Create a Font for styling header cells
        var headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        // Create a CellStyle with the font
        var headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        return headerCellStyle;
    }

    private void createExcelHeaders(String[] columns, CellStyle headerCellStyle, Sheet sheet) {
        var headerRow = sheet.createRow(0);

        // Create cells
        for (var i = 0; i < columns.length; i++) {
            var cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }
}
