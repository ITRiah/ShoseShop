package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.response.UserResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelExportService {
    private int currentRow;
    private Sheet currentSheet;
    private CellStyle definitionHeader;
    private CellStyle definitionData;

    @Getter
    @Builder
    private static class HeaderConfig {
        private String sheetName;
        private String[][] headers;
        private int minColumnWidth;
        @Builder.Default
        private boolean autoSizeColumns = true;
    }

    @FunctionalInterface
    private interface DataMapper<T> {
        Object[] map(T response, int rowNumber);
    }

    private void initDefaultStyle(Workbook workbook) {
        Font bold = workbook.createFont();
        bold.setBold(true);

        definitionHeader = workbook.createCellStyle();
        definitionHeader.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
        definitionHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        definitionHeader.setFont(bold);
        definitionHeader.setAlignment(HorizontalAlignment.CENTER);
        definitionHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        addBorders(definitionHeader);

        definitionData = workbook.createCellStyle();
        definitionData.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        definitionData.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        addBorders(definitionData);
    }

    private void addBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    }

    private void setRowValues(Row row, Object[] values, CellStyle style) {
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            if (values[i] != null) {
                if (values[i] instanceof Number) {
                    cell.setCellValue(((Number) values[i]).doubleValue());
                } else {
                    cell.setCellValue(values[i].toString());
                }
            }
            if (style != null) {
                cell.setCellStyle(style);
            }
        }
    }

    private void saveHeaderMultiRows(HeaderConfig config) {
        int[][] mergeRegions = generateMergeRegions(config.getHeaders());

        for (String[] headerRow : config.getHeaders()) {
            Row row = currentSheet.createRow(currentRow++);
            setRowValues(row, headerRow, definitionHeader);
        }

        mergeRegions(currentSheet, mergeRegions);

        if (config.isAutoSizeColumns()) {
            autoSizeWithMinWidth(currentSheet, config.getMinColumnWidth());
        }
    }

    private int[][] generateMergeRegions(String[][] headers) {
        List<int[]> regions = new ArrayList<>();
        int rowCount = headers.length;
        int colCount = headers[0].length;

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                if (headers[row][col] == null || headers[row][col].isEmpty()) continue;

                int lastRow = row;
                for (int i = row + 1; i < rowCount; i++) {
                    if (headers[i][col] != null && !headers[i][col].isEmpty()) break;
                    lastRow = i;
                }

                int lastCol = col;
                for (int j = col + 1; j < colCount; j++) {
                    if (headers[row][j] != null && !headers[row][j].isEmpty()) break;
                    lastCol = j;
                }

                if (lastRow > row || lastCol > col) {
                    boolean overlap = false;
                    for (int[] region : regions) {
                        if (region[0] <= lastRow && region[1] >= row && region[2] <= lastCol && region[3] >= col) {
                            overlap = true;
                            break;
                        }
                    }
                    if (!overlap) {
                        regions.add(new int[]{row, lastRow, col, lastCol});
                    }
                }
            }
        }
        return regions.toArray(new int[0][]);
    }

    private void mergeRegions(Sheet sheet, int[][] regions) {
        for (int[] region : regions) {
            sheet.addMergedRegion(new CellRangeAddress(region[0], region[1], region[2], region[3]));
        }
    }

    private void autoSizeWithMinWidth(Sheet sheet, int minWidth) {
        int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
        for (int col = 0; col < columnCount; col++) {
            sheet.autoSizeColumn(col);
            int currentWidth = sheet.getColumnWidth(col);
            if (currentWidth < minWidth * 128) {
                sheet.setColumnWidth(col, minWidth * 128);
            }
            for (Row row : sheet) {
                Cell cell = row.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellStyle(row.getRowNum() == 0 ? definitionHeader : definitionData);
            }
        }
    }

    private <T> void saveData(List<T> data, DataMapper<T> mapper) {
        int rowNumber = 1;
        for (T item : data) {
            Row row = currentSheet.createRow(currentRow++);
            Object[] cellValues = mapper.map(item, rowNumber++);
            setRowValues(row, cellValues, definitionData);
        }
    }

    private <T> void exportExcel(HeaderConfig config, List<T> data, DataMapper<T> mapper, ByteArrayOutputStream outStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            initDefaultStyle(workbook);
            currentSheet = workbook.createSheet(config.getSheetName());
            currentRow = 0;
            saveHeaderMultiRows(config);
            saveData(data, mapper);
            workbook.write(outStream);
        } catch (IOException e) {
            log.error("Error exporting Excel: {}", e.getMessage());
        }
    }

//    public void exportReportByMonth(ByteArrayOutputStream outStream, Page<FinanProjectIncomeReportResponse> reports) {
//        HeaderConfig config = HeaderConfig.builder()
//                .sheetName("Financial Report")
//                .headers(new String[][]{
//                        {"No", "Department", "Project name", "OB", "Project Manager", "Customer", "Status", "Kcomplex",
//                                "Project Budget", "Project Cost", "Project Cost", "Net income", "ROI (%)", "Net income to Completed"},
//                        {"", "", "", "", "", "", "", "", "Month", "Plan", "Actual", "Month", "Month", ""},
//                        {"", "", "", "", "", "", "", "", "Accumulate", "Month", "Accumulate", "Accumulate", "Accumulate", ""}
//                })
//                .minColumnWidth(15)
//                .build();
//
//        DataMapper<FinanProjectIncomeReportResponse> mapper = (db, rowNumber) -> new Object[]{
//                rowNumber,
//                db.getDepartment(),
//                db.getProjectName(),
//                db.getOb(),
//                db.getProjectManager(),
//                db.getCustomer(),
//                db.getStatus(),
//                db.getKcomplex() != null ? db.getKcomplex().doubleValue() : "N/A",
//                db.getBudget() != null ? db.getBudget().doubleValue() : "N/A",
//                db.getBudgetAccumulated() != null ? db.getBudgetAccumulated().doubleValue() : "N/A",
//                db.getPlanCost() != null ? db.getPlanCost().doubleValue() : "N/A",
//                db.getPlanCostAccumulated() != null ? db.getPlanCostAccumulated().doubleValue() : "N/A",
//                db.getActualBudget() != null ? db.getActualBudget().doubleValue() : "N/A",
//                db.getActualBudgetAccumulated() != null ? db.getActualBudgetAccumulated().doubleValue() : "N/A",
//                db.getNetIncome() != null ? db.getNetIncome().doubleValue() : "N/A",
//                db.getNetIncomeAccumulated() != null ? db.getNetIncomeAccumulated().doubleValue() : "N/A",
//                db.getRoi() != null ? db.getRoi().doubleValue() : "N/A",
//                db.getRoiAccumulated() != null ? db.getRoiAccumulated().doubleValue() : "N/A",
//                db.getNetIncomeToCompleted() != null ? db.getNetIncomeToCompleted().doubleValue() : "N/A"
//        };
//
//        exportExcel(config, reports.getContent(), mapper, outStream);
//    }

//    public void exportOtInsourceTimesheet(ByteArrayOutputStream outStream, TimeSheetListResponse response) {
//        HeaderConfig config = HeaderConfig.builder()
//                .sheetName("OT Insource Timesheet")
//                .headers(new String[][]{{
//                        TimesheetConstant.TimesheetHeader.TYPE,
//                        TimesheetConstant.TimesheetHeader.MONTH,
//                        TimesheetConstant.TimesheetHeader.DATE,
//                        TimesheetConstant.TimesheetHeader.DAY,
//                        TimesheetConstant.TimesheetHeader.PROJECT,
//                        TimesheetConstant.TimesheetHeader.REQUEST_NAME,
//                        TimesheetConstant.TimesheetHeader.OT_TYPE,
//                        TimesheetConstant.TimesheetHeader.EMPLOYEE,
//                        TimesheetConstant.TimesheetHeader.START_HOUR,
//                        TimesheetConstant.TimesheetHeader.END_HOUR,
//                        TimesheetConstant.TimesheetHeader.BREAK_TIME,
//                        TimesheetConstant.TimesheetHeader.MEAL_ALLOWANCE,
//                        TimesheetConstant.TimesheetHeader.ACTUAL_HOURS,
//                        TimesheetConstant.TimesheetHeader.SALARY_HOURS,
//                        TimesheetConstant.TimesheetHeader.STATUS
//                }})
//                .minColumnWidth(15)
//                .build();
//
//        DataMapper<TimesheetDTO> mapper = (timesheet, rowNumber) -> new Object[]{
//                timesheet.getType(),
//                timesheet.getMonth(),
//                timesheet.getDate(),
//                timesheet.getDay(),
//                timesheet.getProject(),
//                timesheet.getRequestName(),
//                timesheet.getOtType(),
//                timesheet.getEmployee(),
//                timesheet.getStartHour(),
//                timesheet.getEndHour(),
//                timesheet.getBreakTime(),
//                timesheet.getMealAllowance(),
//                timesheet.getActualHours(),
//                timesheet.getSalaryHours(),
//                timesheet.getStatus()
//        };
//
//        exportExcel(config, response.getTimesheets().getContent(), mapper, outStream);
//    }

    public void exportUser(ByteArrayOutputStream outStream, List<UserResponse> reports) {
        HeaderConfig config = HeaderConfig.builder()
                .sheetName("Financial Report")
                .headers(new String[][]{
                        {"No", "Name", "Email"}
                })
                .minColumnWidth(50)
                .build();

        DataMapper<UserResponse> mapper = (db, rowNumber) -> new Object[]{
                rowNumber,
                db.getFirstName() + " " + db.getLastName(),
                db.getEmail(),
        };

        exportExcel(config, reports, mapper, outStream);
    }
}