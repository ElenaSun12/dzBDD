package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeInputField = $("[data-test-id='code'] input");
    private final SelenideElement verifyButton = $("[data-test-id='action-verify']");

    public VerificationPage() {
        codeInputField.shouldBe(Condition.visible); //Input надо ли?
    }

    public DashBoardPage validVerify(DataHelper.VerificationCode verificationCode) {
        codeInputField.setValue(verificationCode.getCode());
        verifyButton.click();
        return new DashBoardPage();
    }
}
