package Utilities;

import org.apache.poi.ss.usermodel.*;



import java.io.FileInputStream;


public class ExcelUtility {

    public static String findFromExcel(String text) {

        String returnData = "";

        String path = "src/main/resources/LoginData.xlsx";

        Sheet sheet = null;

        try {
            FileInputStream inputStream = new FileInputStream(path);
            Workbook workbook = WorkbookFactory.create(inputStream);
            sheet = workbook.getSheetAt(0);
        } catch (Exception e) {

        }

        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {

            if (sheet.getRow(i).getCell(0).toString().equalsIgnoreCase(text)) {
                for (int j = 1; j < sheet.getRow(i).getPhysicalNumberOfCells(); j++) {
                    returnData += sheet.getRow(i).getCell(j);

                }
            }
        }

        return returnData;
    }



}
