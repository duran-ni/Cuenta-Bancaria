package dev.nieves.bank.model;

import java.util.Locale;

/**
 * Representa una cuenta bancaria genérica.
 * Sirve como superclase para SavingsAccount y CheckingAccount.
 */
public class Account {

    protected float balance;
    protected int depositsCount = 0;
    protected int withdrawalsCount = 0;
    protected float annualRate;
    protected float monthlyFee = 0f;

    public Account(float balance, float annualRate) {
        this.balance = balance;
        this.annualRate = annualRate;
    }

    public void deposit(float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El importe a consignar debe ser mayor que cero");
        }
        this.balance += amount;
        this.depositsCount++;
    }

    public void withdraw(float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El importe a retirar debe ser mayor que cero");
        }
        if (amount > this.balance) {
            throw new IllegalStateException("El importe a retirar supera el saldo disponible");
        }
        this.balance -= amount;
        this.withdrawalsCount++;
    }

    public void calculateMonthlyInterest() {
        float monthlyRate = this.annualRate / 12 / 100;
        float interest = this.balance * monthlyRate;
        this.balance += interest;
    }

    public void monthlyStatement() {
        this.balance -= this.monthlyFee;
        calculateMonthlyInterest();
    }

    public String printAccount() {
    return String.format(
        Locale.US,
        "Balance: %.2f | Deposits: %d | Withdrawals: %d | Annual rate: %.2f%% | Monthly fee: %.2f",
        balance, depositsCount, withdrawalsCount, annualRate, monthlyFee
    );
}

    public float getBalance() {
        return balance;
    }

    public int getDepositsCount() {
        return depositsCount;
    }

    public int getWithdrawalsCount() {
        return withdrawalsCount;
    }

    public float getAnnualRate() {
        return annualRate;
    }

    public float getMonthlyFee() {
        return monthlyFee;
    }
}
