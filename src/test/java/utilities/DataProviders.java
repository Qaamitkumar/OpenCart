package utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider(name = "LoginData")
    public String[][] getData() throws IOException {
        String path = ".\\testData\\Opencart_LoginData.xlsx"; // or .xls — supported now

        ExcelUtility excel = new ExcelUtility(path);
        excel.openWorkbook();

        int totalRows = excel.getRowCount("Sheet1");
        int totalCols = excel.getCellCount("Sheet1", 1);

        String[][] loginData = new String[totalRows - 1][totalCols]; // Skip header row (row 0)

        for (int i = 1; i < totalRows; i++) { // Start from 1 to skip header
            for (int j = 0; j < totalCols; j++) {
                loginData[i - 1][j] = excel.getCellData("Sheet1", i, j);
            }
        }

        excel.saveAndCloseWorkbook(); // Close resources

        return loginData;
    }

    // DataProvider2
    // DataProvider3
}
