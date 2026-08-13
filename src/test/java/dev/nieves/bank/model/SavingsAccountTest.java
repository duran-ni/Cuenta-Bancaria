package dev.nieves.bank.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SavingsAccountTest {

    private SavingsAccount activeAccount;

    @BeforeEach
    void setUp() {
        activeAccount = new SavingsAccount(15000f, 12f);
    }

    @ParameterizedTest(name = "balance {0} should result in active={1}")
    @MethodSource("balanceAndExpectedActiveState")
    void constructorShouldSetActiveBasedOnMinimumBalance(float balance, boolean expectedActive) {
        SavingsAccount account = new SavingsAccount(balance, 12f);

        assertEquals(expectedActive, account.isActive());
    }

    private static Stream<Arguments> balanceAndExpectedActiveState() {
        return Stream.of(
            Arguments.of(9999f, false),
            Arguments.of(10000f, true),
            Arguments.of(15000f, true)
        );
    }

    @Test
    void depositShouldIncreaseBalanceWhenActive() {
        activeAccount.deposit(500f);

        assertEquals(15500f, activeAccount.getBalance());
        assertEquals(1, activeAccount.getDepositsCount());
    }

    @Test
    void depositShouldThrowExceptionWhenInactive() {
        SavingsAccount inactiveAccount = new SavingsAccount(5000f, 12f);

        assertThrows(IllegalStateException.class, () -> inactiveAccount.deposit(100f));
    }

    @Test
    void withdrawShouldDecreaseBalanceWhenActive() {
        activeAccount.withdraw(1000f);

        assertEquals(14000f, activeAccount.getBalance());
        assertEquals(1, activeAccount.getWithdrawalsCount());
    }

    @Test
    void withdrawShouldThrowExceptionWhenInactive() {
        SavingsAccount inactiveAccount = new SavingsAccount(5000f, 12f);

        assertThrows(IllegalStateException.class, () -> inactiveAccount.withdraw(100f));
    }

    @Test
    void monthlyStatementShouldNotChargeFeeWithFourOrFewerWithdrawals() {
        for (int i = 0; i < 4; i++) {
            activeAccount.withdraw(100f);
        }

        activeAccount.monthlyStatement();

        assertEquals(0f, activeAccount.getMonthlyFee());
    }

    @ParameterizedTest(name = "{0} withdrawals should charge a fee of {1}")
    @MethodSource("withdrawalsAndExpectedFee")
    void monthlyStatementShouldChargeFeeForExtraWithdrawals(int withdrawalsCount, float expectedFee) {
        for (int i = 0; i < withdrawalsCount; i++) {
            activeAccount.withdraw(100f);
        }

        activeAccount.monthlyStatement();

        assertEquals(expectedFee, activeAccount.getMonthlyFee());
    }

    private static Stream<Arguments> withdrawalsAndExpectedFee() {
        return Stream.of(
            Arguments.of(5, 1000f),
            Arguments.of(6, 2000f),
            Arguments.of(7, 3000f)
        );
    }

    @Test
    void monthlyStatementShouldDeactivateAccountWhenBalanceDropsBelowMinimum() {
        SavingsAccount account = new SavingsAccount(10200f, 0f);

        for (int i = 0; i < 5; i++) {
            account.withdraw(100f);
        }
        account.monthlyStatement();

        assertFalse(account.isActive());
    }

    @Test
    void printAccountShouldReturnBalanceFeeAndTransactionsCount() {
        activeAccount.deposit(500f);
        activeAccount.withdraw(200f);

        String result = activeAccount.printAccount();

        assertEquals(
            "Balance: 15300.00 | Monthly fee: 0.00 | Transactions: 2",
            result
        );
    }
}
