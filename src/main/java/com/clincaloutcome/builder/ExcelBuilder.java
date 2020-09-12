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
import java.io.PipedOutputStream;
import java.util.List;
import java.util.Map;

@Component
public
class ExcelBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelBuilder.class);

    public PipedOutputStream printFromEUTrialExcelFile(Map<Integer, List<List<String>>> listOfResults) {
        String[] columns = {"EudraCT Number", "Sponsor Protocol Number", "Start Date", "Sponsor Name", "Full Title", "Medical Condition", "Disease", "Population Age", "Gender", "Trial Protocol", "Trial Results", "Primary End Points", "Secondary End Points"};

        try (Workbook workbook = new XSSFWorkbook()) {

            // Create a Font for styling header cells
            CellStyle headerCellStyle = createSheetStyle(workbook);

            // Create a Sheet
            Sheet sheet = workbook.createSheet("EUClinical");

            // Create a Row
            createExcelHeaders(columns, headerCellStyle, sheet);

            int rowNum = 1;

            for (int i = 0; i < listOfResults.size(); i++) {
                List<List<String>> firstFile = listOfResults.get(0);
                for (List<String> trialData : firstFile) {
                    int columnCount = 0;

                    Row row = sheet.createRow(rowNum++);

                    createRowOfData(row, trialData.get(columnCount), trialData.get(columnCount + 1), trialData.get(columnCount + 2),
                            trialData.get(columnCount + 3), trialData.get(columnCount + 4), trialData.get(columnCount + 5), trialData.get(columnCount + 6),
                            trialData.get(columnCount + 7), trialData.get(columnCount + 8), trialData.get(columnCount + 9), trialData.get(columnCount + 10),
                            trialData.get(columnCount + 11), trialData.get(columnCount + 12));
                }
            }

            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a file
            PipedOutputStream stream = new PipedOutputStream();
            workbook.write(stream);
            stream.close();
            return stream;
        } catch (IOException e) {
            LOGGER.error("Can't Parse File {}", e.getMessage());
        }
        return null;
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

                createRowOfData(row, euClinical.getEudraNumber(), euClinical.getSponsorProtocolNumber(), euClinical.getStartDate(),
                        euClinical.getSponsorName(), euClinical.getFullTitle(), euClinical.getMedicalCondition(), euClinical.getDisease(),
                        euClinical.getPopulationAge(), euClinical.getGender(), euClinical.getTrialProtocol(), euClinical.getTrialResult(),
                        euClinical.getPrimaryEndPoint(), euClinical.getSecondaryEndPoint());
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

    private void createRowOfData(Row row, String... euClinicalValues) {
        for (int i = 0; i < euClinicalValues.length; i++) {
            row.createCell(i).setCellValue(euClinicalValues[i]);
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
