package dev.nieves.bank.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(1000f, 12f);
    }

    @Test
    void constructorShouldInitializeBalanceAndAnnualRate() {
        assertEquals(1000f, account.getBalance());
        assertEquals(12f, account.getAnnualRate());
        assertEquals(0, account.getDepositsCount());
        assertEquals(0, account.getWithdrawalsCount());
        assertEquals(0f, account.getMonthlyFee());
    }

    @Test
    void depositShouldIncreaseBalanceAndDepositsCount() {
        account.deposit(500f);

        assertEquals(1500f, account.getBalance());
        assertEquals(1, account.getDepositsCount());
    }

    @Test
    void depositWithNonPositiveAmountShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0f));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-100f));
    }

    @Test
    void withdrawShouldDecreaseBalanceAndWithdrawalsCount() {
        account.withdraw(300f);

        assertEquals(700f, account.getBalance());
        assertEquals(1, account.getWithdrawalsCount());
    }

    @Test
    void withdrawMoreThanBalanceShouldThrowException() {
        assertThrows(IllegalStateException.class, () -> account.withdraw(1500f));
    }

    @Test
    void withdrawWithNonPositiveAmountShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0f));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-50f));
    }

    @Test
    void calculateMonthlyInterestShouldIncreaseBalance() {
        account.calculateMonthlyInterest();

        float expectedInterest = 1000f * (12f / 12 / 100);
        assertEquals(1000f + expectedInterest, account.getBalance());
    }

    @Test
    void monthlyStatementShouldApplyInterestWhenNoFee() {
        account.monthlyStatement();

        float expectedInterest = 1000f * (12f / 12 / 100);
        assertEquals(1000f + expectedInterest, account.getBalance());
    }

    @Test
    void printAccountShouldReturnFormattedString() {
        String result = account.printAccount();

        assertEquals(
            "Balance: 1000.00 | Deposits: 0 | Withdrawals: 0 | Annual rate: 12.00% | Monthly fee: 0.00",
            result
        );
    }
}
