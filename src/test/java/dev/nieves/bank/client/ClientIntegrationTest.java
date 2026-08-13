package dev.nieves.bank.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.nieves.bank.model.CheckingAccount;
import dev.nieves.bank.model.SavingsAccount;
import dev.nieves.bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientIntegrationTest {

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService();
    }

    @Test
    void clientRequestDepositShouldUpdateSavingsAccountBalance() {
        SavingsAccount account = new SavingsAccount(15000f, 12f);
        Client client = new Client("Ana", account, accountService);

        client.requestDeposit(500f);

        assertEquals(15500f, account.getBalance());
        assertEquals(1, account.getDepositsCount());
    }

    @Test
    void clientRequestWithdrawShouldUpdateCheckingAccountOverdraft() {
        CheckingAccount account = new CheckingAccount(1000f, 12f);
        Client client = new Client("Luis", account, accountService);

        client.requestWithdraw(1500f);

        assertEquals(0f, account.getBalance());
        assertEquals(500f, account.getOverdraft());
    }

    @Test
    void clientRequestDepositShouldFailWhenSavingsAccountIsInactive() {
        SavingsAccount inactiveAccount = new SavingsAccount(5000f, 12f);
        Client client = new Client("Marta", inactiveAccount, accountService);

        assertThrows(IllegalStateException.class, () -> client.requestDeposit(100f));
    }

    @Test
    void clientRequestMonthlyStatementShouldChargeExtraWithdrawalFeeOnSavingsAccount() {
        SavingsAccount account = new SavingsAccount(15000f, 12f);
        Client client = new Client("Pedro", account, accountService);

        for (int i = 0; i < 5; i++) {
            client.requestWithdraw(100f);
        }
        client.requestMonthlyStatement();

        assertEquals(1000f, account.getMonthlyFee());
    }

    @Test
    void clientRequestAccountSummaryShouldReturnAccountPrintedText() {
        SavingsAccount account = new SavingsAccount(15000f, 12f);
        Client client = new Client("Sofia", account, accountService);

        String summary = client.requestAccountSummary();

        assertEquals(account.printAccount(), summary);
    }

    @Test
    void clientShouldExposeItsNameAndAccount() {
        CheckingAccount account = new CheckingAccount(2000f, 10f);
        Client client = new Client("Diego", account, accountService);

        assertEquals("Diego", client.getName());
        assertEquals(account, client.getAccount());
    }
}
