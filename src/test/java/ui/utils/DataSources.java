package ui.utils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataSources {
    public static String[][] excel(final String sourceFile, final String selectedSheet) {
        String[][] resultsIntoList = null;
        try {

            File inputFile = new File(sourceFile);
            FileInputStream inputStream = new FileInputStream(inputFile);

            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = wb.getSheet(selectedSheet);

            int rowCount = sheet.getLastRowNum();
            int collCount = sheet.getRow(0).getLastCellNum();

            resultsIntoList = new String[rowCount][collCount];

            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < collCount; j++) {
                    switch (sheet.getRow(i + 1).getCell(j).getCellType()) {
                        /*case BLANK:
                            resultsIntoList[i][j] = "";
                            break;*/
                        case STRING:
                            resultsIntoList[i][j] = String.valueOf(
                                    sheet.getRow(i + 1).getCell(j).getRichStringCellValue());
                            break;
                        case NUMERIC:
                            resultsIntoList[i][j] = String
                                    .valueOf(sheet.getRow(i + 1).getCell(j).getNumericCellValue());
                            break;
                       /* case _NONE:
                            resultsIntoList[i][j] = "";
                            break;*/
                        case BOOLEAN:
                            resultsIntoList[i][j] = String
                                    .valueOf(sheet.getRow(i + 1).getCell(j).getBooleanCellValue());
                            break;
                        case ERROR:
                            resultsIntoList[i][j] = String.valueOf(sheet.getRow(i + 1).getCell(j).getErrorCellValue());
                            break;
                        case FORMULA:
                            resultsIntoList[i][j] =
                                    String.valueOf(sheet.getRow(i + 1).getCell(j).getCellFormula());
                            break;
                        default:
                            resultsIntoList[i][j] = "";
                            break;
                    }
                }
            }

            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultsIntoList;
    }

    public String[][] excel(final String sourceFile, final String selectedSheet, int fromRow, final int toRow) {
        String[][] resultsIntoList = null;
        try {

            File inputFile = new File(sourceFile);
            FileInputStream inputStream = new FileInputStream(inputFile);

            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = wb.getSheet(selectedSheet);

            int collCount = sheet.getRow(fromRow).getLastCellNum();
            int rowsTotal = toRow - fromRow + 1;

            resultsIntoList = new String[rowsTotal][collCount];

            for (int i = 0; i < rowsTotal; i++) {
                for (int j = 0; j < collCount; j++) {
                    switch (sheet.getRow(fromRow).getCell(j).getCellType()) {
                        /*case BLANK:
                            resultsIntoList[i][j] = "";
                            break;*/
                        case STRING:
                            resultsIntoList[i][j] = String.valueOf(
                                    sheet.getRow(fromRow).getCell(j).getRichStringCellValue());
                            break;
                        case NUMERIC:
                            resultsIntoList[i][j] = String
                                    .valueOf(sheet.getRow(fromRow).getCell(j).getNumericCellValue());
                            break;
                        /*case _NONE:
                            resultsIntoList[i][j] = "";
                            break;*/
                        case BOOLEAN:
                            resultsIntoList[i][j] = String
                                    .valueOf(sheet.getRow(fromRow).getCell(j).getBooleanCellValue());
                            break;
                        case ERROR:
                            resultsIntoList[i][j] = String.valueOf(sheet.getRow(fromRow).getCell(j).getErrorCellValue());
                            break;
                        case FORMULA:
                            resultsIntoList[i][j] =
                                    String.valueOf(sheet.getRow(fromRow).getCell(j).getCellFormula());
                            break;
                        default:
                            resultsIntoList[i][j] = "";
                            break;
                    }
                }
                fromRow++;
            }

            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultsIntoList;
    }

    public Iterator<Object[]> csv(final String fileName, final String delimiter) {
        BufferedReader input = null;
        File file = new File(fileName);
        ArrayList<Object[]> data = null;

        try {
            input = new BufferedReader(new FileReader(file));

            String line = null;
            data = new ArrayList<Object[]>();
            while ((line = input.readLine()) != null) {
                String in = line.trim();
                String[] temp = in.split(delimiter);
                List<Object> arrray = new ArrayList<Object>();
                for (String s : temp) {
                    arrray.add(s);
                }
                data.add(arrray.toArray());
            }

            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.iterator();
    }

    // Method to read from CSV file
    public static List<String[]> readCSV(String filePath) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> records = reader.readAll();
        reader.close();
        return records;
    }
    public static void writeCSV(String filePath, List<String[]> data) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(filePath));
        writer.writeAll(data);
        writer.close();
    }

    // Method to update a specific column for a specific row
    public static void updateCSVColumnValueBySpecificRow(String filePath, int rowIndex, int columnIndex, String newValue) throws IOException, CsvException {
        List<String[]> records = readCSV(filePath);  // Read the CSV file into records

        // Check if rowIndex is within the valid range
        if (rowIndex < 0 || rowIndex >= records.size()) {
            System.out.println("Invalid row index: " + rowIndex);
            return;
        }

        // Get the specific row and update the specified column
        String[] record = records.get(rowIndex);

        // Check if columnIndex is valid for the selected row
        if (columnIndex < 0 || columnIndex >= record.length) {
            System.out.println("Invalid column index: " + columnIndex);
            return;
        }

        // Update the column value
        record[columnIndex] = newValue;

        // Write the updated records back to the CSV
        writeCSV(filePath, records);
        System.out.println("CSV file updated successfully.");
    }
}
