package ui.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static ui.utils.StaticVariables.CONTACT_SUBJECTS;

public class ContactPageTests extends BaseTest{
    @DataProvider(name = "contactFormDataProvider")
    public Object[][] contactFormDataProvider() {
        return new Object[][]{
                // Single-field errors
                {"", "Kostovska", "natasha_test@mailinator.com", "The uploaded file must be a valid .txt text file and must be empty (0 KB) to proceed.",CONTACT_SUBJECTS[4], "data/uploadFileSample.txt", new String[]{"First name is required"}},
                {"Natasha", "", "natasha_test@mailinator.com", "The uploaded file must be a valid .txt text file and must be empty (0 KB) to proceed.",CONTACT_SUBJECTS[3], "data/uploadFileSample.txt", new String[]{"Last name is required"}},
                {"Natasha", "Kostovska", "", "The uploaded file must be a .txt and 0 KB in size.", CONTACT_SUBJECTS[2], "data/uploadFileSample.txt", new String[]{"Email is required"}},
                {"Natasha", "Kostovska", "natasha_test@mailinator.com", "The uploaded file must be a valid .txt text file and must be empty (0 KB) to proceed.", "", "data/uploadFileSample.txt", new String[]{"Subject is required"}},
                {"Natasha", "Kostovska", "natasha_test@mailinator.com", "", CONTACT_SUBJECTS[1], "data/uploadFileSample.txt", new String[]{"Message is required"}},
                {"Natasha", "Kostovska", "natasha_test@mailinator.com", "The uploaded file must be a valid .txt text file and must be empty (0 KB) to proceed.", CONTACT_SUBJECTS[0], "data/uploadFileNotEmptySample.txt", new String[]{"File should be empty."}},
                {"Natasha", "Kostovska", "natasha_test@mailinator.com", "The uploaded file must be a valid .txt text file and must be empty (0 KB) to proceed.", CONTACT_SUBJECTS[5], "data/loginTestDataPW.xlsx", new String[]{"File should be empty."}},
                {"Natasha", "Kostovska", "natasha_test@mailinator.com", "The uploaded file must be a .txt and 0 KB in size", CONTACT_SUBJECTS[4], "data/uploadFileSample.txt", new String[]{"Message must be minimal 50 characters"}},
                {"Natasha", "Kostovska", "natasha_test@mailinator.", "The uploaded file must be a .txt and 0 KB in size.", CONTACT_SUBJECTS[0], "data/uploadFileSample.txt", new String[]{"Email format is invalid"}},
                {"Natasha", "Kostovska", "natasha_testmailinator.com", "The uploaded file must be a .txt and 0 KB in size.", CONTACT_SUBJECTS[1], "data/uploadFileSample.txt", new String[]{"Email format is invalid"}},
                {"Natasha", "Kostovska", "natasha_testmailinator.", "The uploaded file must be a .txt and 0 KB in size.", CONTACT_SUBJECTS[2], "data/uploadFileSample.txt", new String[]{"Email format is invalid"}},
                {"Natasha5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rt4", "Kostovska", "natasha_test@mailinator.com", "The uploaded file must be a valid .txt text file and must be empty (0 KB) to proceed.", CONTACT_SUBJECTS[3], "data/uploadFileSample.txt", new String[]{"The name field must not be greater than 120 characters."}},
                {"Natasha", "Kostovska5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet56456464", "natasha_test@mailinator.com", "The uploaded file must be a valid .txt text file and must be empty (0 KB) to proceed.", CONTACT_SUBJECTS[4], "data/uploadFileSample.txt", new String[]{"The name field must not be greater than 120 characters."}},
                {"Natasha", "Kostovska", "natasha_test@mailinator.com", "tetertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet", CONTACT_SUBJECTS[3], "data/uploadFileSample.txt", new String[]{"The message field must not be greater than 250 characters."}},
                {"Natasha", "Kostovska", "natePWmailinatoThehemessageieldmustnotbegreaterthan250characterhs45@mailinatoThemessageieldmustnotbegreaterthan250characters.com", "The uploaded file must be a .txt and 0 KB in size.", CONTACT_SUBJECTS[5], "data/uploadFileSample.txt", new String[]{"Email format is invalid"}},

                // All fields empty
                {"", "", "", "", "", null, new String[]{
                        "First name is required",
                        "Last name is required",
                        "Email is required",
                        "Subject is required",
                        "Message is required"
                }},
                {"Natasha5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rt4",
                        "Kostovska5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet56456464",
                        "natePWmailinatoThehemessageieldmustnotbegreaterthan250characters@mailinatoThemessageieldmustnotbegreaterthan250characters.com",
                        "tetertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet5645646rtretertertertertet",
                        CONTACT_SUBJECTS[4], "data/uploadFileSample.txt",
                        new String[]{"The name field must not be greater than 120 characters.'/\n' The message field must not be greater than 250 characters."}}
        };
    }

    @Test(dataProvider = "contactFormDataProvider",groups = "smoke")
    void contactFormTest(String firstName, String lastName, String email, String message,
                         String subject, String fileToUploadPath, String[] expectedMessages) throws Exception {
        pages.getContactPage().navigateToContact();
        pages.getContactPage().fillForm(firstName, lastName, email, message, subject);
        pages.getContactPage().uploadFile(fileToUploadPath);
        pages.getContactPage().clickSend();

        // Collect all visible error messages
        List<String> actualMessages = pages.getContactPage().getAllErrorMessages();

        // Assert all expected messages are present
        String expectedStr = String.join(", ", expectedMessages);
        String actualStr = actualMessages.stream().collect(Collectors.joining(", "));

        Assert.assertTrue(actualMessages.containsAll(List.of(expectedMessages)),
                "Expected error messages not visible: " + expectedStr + "\nActual visible messages: " + actualStr);
    }

}
