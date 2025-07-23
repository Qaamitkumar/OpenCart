package utilities;

import java.io.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;  // for .xls
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  // for .xlsx

public class ExcelUtility {

    private final String path;
    private FileInputStream fi;
    private FileOutputStream fo;
    private Workbook workbook;

    public ExcelUtility(String path) {
        this.path = path;
    }

    public void openWorkbook() throws IOException {
        fi = new FileInputStream(path);
        if (path.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(fi);
        } else if (path.endsWith(".xls")) {
            workbook = new HSSFWorkbook(fi);
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + path);
        }
    }

    public void saveAndCloseWorkbook() throws IOException {
        if (workbook != null) {
            fo = new FileOutputStream(path);
            workbook.write(fo);
            workbook.close();
        }
        if (fi != null) fi.close();
        if (fo != null) fo.close();
    }

    public int getRowCount(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        return (sheet != null) ? sheet.getLastRowNum() + 1 : 0;
    }

    public int getCellCount(String sheetName, int rownum) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return 0;

        Row row = sheet.getRow(rownum);
        return (row != null) ? row.getLastCellNum() : 0;
    }

    public String getCellData(String sheetName, int rownum, int colnum) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return "";

        Row row = sheet.getRow(rownum);
        if (row == null) return "";

        Cell cell = row.getCell(colnum);
        if (cell == null) return "";

        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    public void setCellData(String sheetName, int rownum, int colnum, String data) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null)
            sheet = workbook.createSheet(sheetName);

        Row row = sheet.getRow(rownum);
        if (row == null)
            row = sheet.createRow(rownum);

        Cell cell = row.createCell(colnum);
        cell.setCellValue(data);
    }

    public void fillCellColor(String sheetName, int rownum, int colnum, IndexedColors color) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return;

        Row row = sheet.getRow(rownum);
        if (row == null) return;

        Cell cell = row.getCell(colnum);
        if (cell == null) return;

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);
    }

    public void fillGreenColor(String sheetName, int rownum, int colnum) {
        fillCellColor(sheetName, rownum, colnum, IndexedColors.GREEN);
    }

    public void fillRedColor(String sheetName, int rownum, int colnum) {
        fillCellColor(sheetName, rownum, colnum, IndexedColors.RED);
    }
}
