package ui.utils;

import com.github.javafaker.Faker;
import java.nio.file.Paths;

public class StaticVariables {

    // Initialize Faker for generating random data
    private static final Faker faker = new Faker();

    // Page Titles
    public static final String CART_PAGE_TITLE = "SHOPPING-CART SUMMARY";
    public static final String CONTACT_US_PAGE_TITLE = "CUSTOMER SERVICE - CONTACT US";
    public static final String CREATE_AN_ACCOUNT_PAGE_TITLE = "AUTHENTICATION";
    public static final String HOME_PAGE_TITLE = "Automation Practice Website";

    // Dynamically generated fake data using Faker
    public static String fakeDomain = faker.internet().domainName();
    public static String fakeMessage = faker.letterify("Some message");
    public static String fakeFirstName = faker.name().firstName();
    public static String fakeLastName = faker.name().lastName();
    public static String fakeEmail = fakeFirstName + fakeLastName + "@" + fakeDomain;
    public static String fakeCompanyName = faker.company().name();
    public static String fakeAddress = faker.address().fullAddress();
    public static String fakeSecondaryAddress = faker.address().secondaryAddress();
    public static String fakeCity = faker.address().cityName();
    public static String fakePhoneNumber = faker.phoneNumber().cellPhone();
    public static String fakeAlias = faker.address().streetAddress() + "," + faker.address().streetAddressNumber();
    public static String fakeStreetAddress=faker.address().streetAddress();
    public static String fakeState=faker.address().state();
    public static String fakeCountry=faker.address().country();
    public static String fakePostalCode=faker.address().zipCode();


    // Page titles and messages
    public static final String ALREADY_REGISTERED_TITLE = "ALREADY REGISTERED?";
    public static final String MY_ACCOUNT_TITLE = "MY ACCOUNT";
    public static final String WELCOME_TO_ACCOUNT_TEXT = "Welcome to your account. Here you can manage all of your personal information and orders.";
    public static final String AUTHENTICATION_ERROR = "There is 1 error";

    // Home page navigation menu items
    public static final String[] HOME_PAGE_MENU_ITEMS = {"Women", "Dresses", "T-shirt"};
    public static final String[] HOME_SUBMENU_ITEMS = {"Tops", "Dresses", "Casual Dresses", "Evening Dresses", "Summer Dresses"};

    // File upload path and download directory
    public static final String PNG_FILE_TO_UPLOAD = Paths.get("src", "test", "resources", "autoitfiles", "uploadFile.exe").toString();
    public static final String DOWNLOAD_DIRECTORY_PATH = Paths.get(System.getProperty("user.home"), "Downloads").toString();

    // Error message for creating an account with an already registered email
    public static final String CREATE_AN_ACCOUNT_EMAIL_ERROR_MESSAGE = "An account using this email address has already been registered. Please enter a valid password or request a new one.";

    // Gender options for forms
    public static final String[] GENDER = {"Mr", "Mrs"};

    // Account page menu items
    public static final String[] MY_ACCOUNT_PAGE_ITEMS = {"Order History", "My credit", "My Addresses", "My PI", "My wishlist"};

    // Continue/Proceed buttons
    public static final String[] CONTINUE_PROCEED = {"Continue", "Proceed"};

    // Payment method options
    public static final String[] PAYMENT_METHOD = {"Bank", "Check"};

    // Size options for products
    public static final String[] SIZE_OF_PRODUCT = {"S", "M", "L"};

    // Contact subjects for the contact page form
    public static final String[] CONTACT_SUBJECTS = {"Customer service", "Webmaster", "Return", "Payments", "Warranty", "Status of my order"};

    // Example of generating dynamic fake data for the tests
    public static String generateDynamicEmail() {
        return faker.name().firstName() + faker.name().lastName() + faker.internet().domainName();
    }

    public static String generateDynamicMessage() {
        return faker.letterify("Message with random data: ???");
    }

    // Utility method to get a random size (S, M, L) for product selection
    public static String getRandomSize() {
        return SIZE_OF_PRODUCT[faker.random().nextInt(SIZE_OF_PRODUCT.length)];
    }
}
