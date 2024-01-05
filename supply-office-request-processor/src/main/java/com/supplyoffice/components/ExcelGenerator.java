package com.supplyoffice.components;

import com.supplyoffice.entities.ExcelFile;
import com.supplyoffice.repository.StoredRequestsRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${requests.file.path}")
    String filePath;

    Logger LOG = LoggerFactory.getLogger(ExcelGenerator.class);

    public String generateExcel(Object[][] data, String departmentName) throws IOException {
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
                storeFile(result, departmentName);
                return convertToFile(result, departmentName);
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

    private String convertToFile(byte[] data, String departmentName) throws IOException {
        LOG.debug("Converting byte array to a file...");
        Path path = Paths.get(departmentName + ".xlsx");
        Files.write(path, data, StandardOpenOption.CREATE);
        String result = path.toFile().getAbsolutePath();
        LOG.debug("File path: {}", result);
        return result;
    }

    public void storeFile(byte[] file, String departmentName) {
        ExcelFile createdFile = ExcelFile.of("Supply_request_" + departmentName, file, LocalDateTime.now());
        ExcelFile storedFile = storedRequestsRepository.save(createdFile);
        LOG.debug("Stored excel {} in DB", storedFile.toString());
    }
}
