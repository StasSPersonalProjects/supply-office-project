package com.supplyoffice.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Table(name = "stored_requests")
public class ExcelFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "file_content")
    private byte[] fileContent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ExcelFile(String fileName, byte[] fileContent, LocalDateTime createdAt) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.createdAt = createdAt;
    }

    public ExcelFile() {
    }

    public static ExcelFile of(String fileName, byte[] data, LocalDateTime createdAt) {
        return new ExcelFile(fileName, data, createdAt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ExcelFile{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileContent=" + Arrays.toString(fileContent) +
                ", createdAt=" + createdAt +
                '}';
    }
}
