package com.supplyoffice.component;

import com.supplyoffice.entities.ExcelFile;
import com.supplyoffice.repositories.StoredRequestsRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

@Component
public class ExcelGenerator {

    @Autowired
    StoredRequestsRepository storedRequestsRepository;

    Logger LOG = LoggerFactory.getLogger(ExcelGenerator.class);

    public String generateExcel(Object[][] data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            LOG.debug("Started generating excel file.");
            Sheet sheet = workbook.createSheet("supply_requests");
            for (int rowNum = 0; rowNum < data.length; rowNum++) {
                Row row = sheet.createRow(rowNum);
                for (int colNum = 0; colNum < data[rowNum].length; colNum++) {
                    Cell cell = row.createCell(colNum);
                    setCellValue(cell, data[rowNum][colNum]);
                }
            }
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                byte[] result = outputStream.toByteArray();
                storeFile(result);
                return convertToFile(result);
            }
        }
    }

    private void setCellValue(Cell cell, Object value) {
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    private String convertToFile(byte[] data) throws IOException {
        LOG.debug("Converting byte array to a file...");
        Path path = Paths.get("src/main/resources/supply_request.xlsx");
        Files.write(path, data, StandardOpenOption.CREATE);
        String result = path.toFile().getAbsolutePath();
        LOG.debug("File path: {}", result);
        return result;
    }

    public void storeFile(byte[] file) {
        ExcelFile createdFile = ExcelFile.of("Supply_request", file, LocalDateTime.now());
        ExcelFile storedFile = storedRequestsRepository.save(createdFile);
        LOG.debug("Stored excel {} in DB", storedFile.toString());
    }
}
