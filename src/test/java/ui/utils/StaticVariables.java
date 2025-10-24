package ui.utils;

import com.github.javafaker.Faker;

public class StaticVariables {
    private static final Faker faker = new Faker();

    public static final String CART_PAGE_TITLE = "SHOPPING-CART SUMMARY";
    public static final String CONTACT_US_PAGE_TITLE = "CUSTOMER SERVICE - CONTACT US";
    public static final String CREATE_AN_ACCOUNT_PAGE_TITLE = "AUTHENTICATION";
    public static final String HOME_PAGE_TITLE = "Automation Practice Website";
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
    public static final String EMAIL_FOR_REGISTERED_USER = "nate@test.com";
    public static final String PASSWORD_FOR_REGISTERED_USER = "123456789";
    public static final String ALREADY_REGISTERED_TITLE = "ALREADY REGISTERED?";
    public static final String MY_ACCOUNT_TITLE = "MY ACCOUNT";
    public static final String WELCOME_TO_ACCOUNT_TEXT = "Welcome to your account. Here you can manage all of your personal information and orders.";
    public static final String AUTHENTICATION_ERROR = "There is 1 error";
    public static final String[] HOME_PAGE_MENU_ITEMS = {"Women", "Dresses", "T-shirt"};
    public static final String PNG_FILE_TO_UPLOAD = "src\\test\\resources\\autoitfiles\\uploadFile.exe";
    public static final String DOWNLOAD_DIRECTORY_PATH = "C:\\Users\\Natasha.Kostovska\\Downloads";
    public static final String CREATE_AN_ACCOUNT_EMAIL_ERROR_MESSAGE = "An account using this email address has already been registered. Please enter a valid password or request a new one.";
    public static final String[] GENDER = {"Mr", "Mrs"};
    public static final String[] MY_ACCOUNT_PAGE_ITEMS = {"Order History", "My credit", "My Addresses", "My PI", "My wishlist"};
    public static final String[] HOME_SUBMENU_ITEMS = {"Tops", "Dresses", "Casual Dresses", "Evening Dresses", "Summer Dresses"};
    public static final String[] CONTINUE_PROCEED = {"Continue", "Proceed"};
    public static final String[] PAYMENT_METHOD = {"Bank", "Check"};
    public static final String[] SIZE_OF_PRODUCT={"S","M","L"};
}
