package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainPage extends BasePage {

    @FindBy(css = "input#name")
    private WebElement nameInput;

    @FindBy(css = "textarea#description")
    private WebElement descriptionInput;

    @FindBy(css = "input#maxParticipants")
    private WebElement maxParticipantsInput;

    @FindBy(css = "select#privacyType")
    private WebElement privacySelect;

    @FindBy(css = "input#location")
    private WebElement locationInput;

    @FindBy(css = "input#eventDate")
    private WebElement eventDateInput;

    @FindBy(css = "select#eventTypeId")
    private WebElement eventTypeSelect;

    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/main");
    }

    public void submit() {
        submitButton.click();
    }

    public void fillEventForm(String name, String desc, String max, String privacy, String location, String date, String typeValue) {
        nameInput.sendKeys(name);
        descriptionInput.sendKeys(desc);
        maxParticipantsInput.sendKeys(max);
        locationInput.sendKeys(location);
        //select optiosn from dropdown!
    }


}
