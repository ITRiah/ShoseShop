package com.shose.shoseshop.service;
import com.shose.shoseshop.controller.response.ProductStatisticResponse;
import com.shose.shoseshop.controller.response.StatisticResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class StatisticExportService {
    public byte[] exportStatisticsToExcel(List<StatisticResponse> statistics) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Thống kê doanh thu");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Tháng", "Năm", "Doanh thu"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("#,##0 \"VNĐ\""));

            int rowIdx = 1;
            for (StatisticResponse stat : statistics) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(stat.getMonth());
                row.createCell(1).setCellValue(stat.getYear());

                Cell revenueCell = row.createCell(2);
                revenueCell.setCellValue(stat.getTotalRevenue().doubleValue());
                revenueCell.setCellStyle(currencyStyle);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }


    public byte[] exportStatisticsProductToExcel(List<ProductStatisticResponse> statistics) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Thống kê doanh số sản phẩm");

            // Tạo tiêu đề cột
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Id", "Tên", "Số lượng bán"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            int rowIdx = 1;
            for (ProductStatisticResponse stat : statistics) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(stat.getProductId());
                row.createCell(1).setCellValue(stat.getProductName());
                row.createCell(2).setCellValue(stat.getTotalQuantitySold());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }
}
