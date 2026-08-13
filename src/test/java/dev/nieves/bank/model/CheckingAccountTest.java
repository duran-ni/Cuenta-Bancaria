package dev.nieves.bank.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CheckingAccountTest {

    private CheckingAccount account;

    @BeforeEach
    void setUp() {
        account = new CheckingAccount(1000f, 12f);
    }

    @Test
    void withdrawShouldDecreaseBalanceWhenAmountFitsWithinBalance() {
        account.withdraw(600f);

        assertEquals(400f, account.getBalance());
        assertEquals(0f, account.getOverdraft());
        assertEquals(1, account.getWithdrawalsCount());
    }

    @Test
    void withdrawShouldCreateOverdraftWhenAmountExceedsBalance() {
        account.withdraw(1500f);

        assertEquals(0f, account.getBalance());
        assertEquals(500f, account.getOverdraft());
        assertEquals(1, account.getWithdrawalsCount());
    }

    @Test
    void withdrawWithNonPositiveAmountShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0f));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-50f));
    }

    @Test
    void depositShouldReduceOverdraftBeforeIncreasingBalance() {
        account.withdraw(1500f);
        account.deposit(300f);

        assertEquals(0f, account.getBalance());
        assertEquals(200f, account.getOverdraft());
    }

    @Test
    void depositShouldClearOverdraftAndIncreaseBalanceWhenAmountExceedsDebt() {
        account.withdraw(1500f);
        account.deposit(700f);

        assertEquals(200f, account.getBalance());
        assertEquals(0f, account.getOverdraft());
    }

    @Test
    void depositShouldIncreaseBalanceNormallyWhenNoOverdraft() {
        account.deposit(500f);

        assertEquals(1500f, account.getBalance());
        assertEquals(0f, account.getOverdraft());
    }

    @Test
    void monthlyStatementShouldApplyInterest() {
        account.monthlyStatement();

        float expectedInterest = 1000f * (12f / 12 / 100);
        assertEquals(1000f + expectedInterest, account.getBalance());
    }

    @Test
    void printAccountShouldReturnBalanceFeeTransactionsAndOverdraft() {
        account.withdraw(1500f);
        account.deposit(300f);

        String result = account.printAccount();

        assertEquals(
            "Balance: 0.00 | Monthly fee: 0.00 | Transactions: 2 | Overdraft: 200.00",
            result
        );
    }
}
