package dev.nieves.bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.nieves.bank.model.Account;
import dev.nieves.bank.model.CheckingAccount;
import dev.nieves.bank.model.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService();
    }

    @Test
    void depositShouldDelegateToAccount() {
        Account account = new Account(1000f, 12f);

        accountService.deposit(account, 500f);

        assertEquals(1500f, account.getBalance());
    }

    @Test
    void withdrawShouldDelegateToAccount() {
        Account account = new Account(1000f, 12f);

        accountService.withdraw(account, 300f);

        assertEquals(700f, account.getBalance());
    }

    @Test
    void generateMonthlyStatementShouldDelegateToAccount() {
        Account account = new Account(1000f, 12f);

        accountService.generateMonthlyStatement(account);

        float expectedInterest = 1000f * (12f / 12 / 100);
        assertEquals(1000f + expectedInterest, account.getBalance());
    }

    @Test
    void printAccountShouldDelegateToAccount() {
        Account account = new Account(1000f, 12f);

        String result = accountService.printAccount(account);

        assertEquals(account.printAccount(), result);
    }

    @Test
    void depositShouldRespectSavingsAccountPolymorphicBehavior() {
        SavingsAccount inactiveAccount = new SavingsAccount(5000f, 12f);

        assertThrows(IllegalStateException.class, () -> accountService.deposit(inactiveAccount, 100f));
    }

    @Test
    void withdrawShouldRespectCheckingAccountOverdraftBehavior() {
        CheckingAccount checkingAccount = new CheckingAccount(1000f, 12f);

        accountService.withdraw(checkingAccount, 1500f);

        assertEquals(0f, checkingAccount.getBalance());
        assertEquals(500f, checkingAccount.getOverdraft());
    }
}
