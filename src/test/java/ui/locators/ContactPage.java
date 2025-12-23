package ui.locators;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import ui.utils.PlaywrightMethods;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


public class ContactPage extends PlaywrightMethods {

    public ContactPage(Page page) {
        super(page);
 }

    // Locators
    public Locator firstNameField() { return page.getByLabel("First name"); }
    public Locator lastNameField() { return page.getByLabel("Last name"); }
    public Locator emailField() { return page.getByLabel("Email"); }
    public Locator messageField() { return page.getByLabel("Message"); }
    public Locator subjectField() { return page.getByLabel("Subject"); }
    public Locator sendButton() { return page.getByText("Send"); }
    public Locator attachmentInput() { return page.locator("#attachment"); }
    public Locator contactLink() { return page.locator("[data-test='nav-contact']"); }
    private Locator errorLocator() { return page.locator("[role='alert'], .help-block"); }

    // Actions
    public void navigateToContact() {
        contactLink().click();
    }


    public void fillForm(String firstName, String lastName, String email, String message, String subject) {
        firstNameField().fill(firstName);
        lastNameField().fill(lastName);
        emailField().fill(email);
        messageField().fill(message);
        if (subject != null && !subject.isEmpty()) subjectField().selectOption(subject);
    }

    public void uploadFile(String filePath) throws Exception {
        if (filePath != null) {
            Path path = Paths.get(ClassLoader.getSystemResource(filePath).toURI());
            attachmentInput().setInputFiles(path);
        }
    }

    public void clickSend() {
        sendButton().click();
    }

    // ======= Error Handling =======
    public List<String> getAllErrorMessages() {
        return errorLocator().allTextContents().stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean isErrorMessageVisible(String msg) {
        // Partial match, case-insensitive
        return getAllErrorMessages().stream()
                .anyMatch(text -> text.toLowerCase().contains(msg.toLowerCase()));
    }
}
