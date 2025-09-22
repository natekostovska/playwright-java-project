package ui.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AddAndUpdatePropertiesFileParameters {
    public static void savePropertiesInFileWithoutErasingPreviousOnesForDataProviderTests(String param1Name, String param2Name,String param1, String param2,String param3, String nameOfPropertiesFile) {

        Properties properties = new Properties();

        // Load existing properties
        try (FileInputStream input = new FileInputStream(nameOfPropertiesFile)) {
            properties.load(input);
        } catch (IOException e) {
            // If the file doesn't exist, we can ignore the error and create a new one later
        }


        // Add or update the parameters
        properties.setProperty(param1Name+param2+param3,param1);
        //  properties.setProperty(param2Name+ param2, param2);


        // Save the properties back to the file
        try (FileOutputStream output = new FileOutputStream(nameOfPropertiesFile)) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Simulate test logic
        System.out.println("Executed Test with " + param1Name + param1 + " and "+ param2Name + "=" + param2+"Order counter="+param3);
    }

    public static String loadAndGetPropertyFromPropertiesFile(String returnPropertyFromFile, String propertyFilePath) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(propertyFilePath)) {
            properties.load(inputStream);
            return properties.getProperty(returnPropertyFromFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
