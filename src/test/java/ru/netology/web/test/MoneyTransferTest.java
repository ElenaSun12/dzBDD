package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashBoardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.netology.web.data.DataHelper.*;

public class MoneyTransferTest {
    DashBoardPage dashBoardPage;
    CardInfo firstCardInfo;
    CardInfo secondCardInfo;
    int firstCartBalanse;
    int secondCardBalance;

    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashBoardPage = verificationPage.validVerify(verificationCode);
        firstCardInfo = DataHelper.getFirstCardInfo();
        secondCardInfo = DataHelper.getSecondCardInfo();
        firstCartBalanse = dashBoardPage.getCardBalance(firstCardInfo);
        secondCardBalance = dashBoardPage.getCardBalance(secondCardInfo);
    }

//    @Test
//    void ShoudTransferMoneyBetweenOwnCards() {
//        var info = getAuthInfo();
//        var verificationCode = DataHelper.getVerificationCodeFor(info);
//
//        open("http://localhost:9999");
//
//        var loginPage = new LoginPage();
//        var verificationPage = loginPage.validLogin(info);
//        var dashBoardPage = verificationPage.validVerify(verificationCode);
//    }

    @Test
    void shouldTransferFromFirstToSecond() {
        var amount = generateValidAmount(firstCartBalanse);
        var expectedBalanceFirstCard = firstCartBalanse - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashBoardPage.selectCardToTransfer(secondCardInfo);

        dashBoardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        dashBoardPage.reloadDashBoardPage();
        assertAll(() -> dashBoardPage.checkCardBalance(firstCardInfo, expectedBalanceFirstCard),
                () -> dashBoardPage.checkCardBalance(secondCardInfo, expectedBalanceSecondCard));
    }

    @Test
    void ShouldGetErrorMessageAmountMoreBalance() {
        var amount = generateInvalidAmount(secondCardBalance);
        var transferPage = dashBoardPage.selectCardToTransfer(firstCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);
        assertAll(() -> transferPage.findErrorMessage("Выполнена попытка перевода суммы, превышающей остатокна карте списания"),
                () -> dashBoardPage.reloadDashBoardPage(),
                () -> dashBoardPage.checkCardBalance(firstCardInfo, firstCartBalanse),
                () -> dashBoardPage.checkCardBalance(secondCardInfo, secondCardBalance)
        );
    }
}

