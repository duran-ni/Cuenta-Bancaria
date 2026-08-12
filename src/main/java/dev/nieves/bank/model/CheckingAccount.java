package dev.nieves.bank.model;

import java.util.Locale;

/**
 * Representa una cuenta corriente.
 * Hereda el comportamiento base de Account y añade la lógica de sobregiro.
 */
public class CheckingAccount extends Account {

    private float overdraft = 0f;

    public CheckingAccount(float balance, float annualRate) {
        super(balance, annualRate);
    }

    @Override
    public void withdraw(float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El importe a retirar debe ser mayor que cero");
        }
        if (amount <= this.balance) {
            super.withdraw(amount);
        } else {
            float shortfall = amount - this.balance;
            this.overdraft += shortfall;
            this.balance = 0f;
            this.withdrawalsCount++;
        }
    }

    @Override
    public void deposit(float amount) {
        super.deposit(amount);
        if (this.overdraft > 0) {
            float payment = Math.min(this.overdraft, this.balance);
            this.overdraft -= payment;
            this.balance -= payment;
        }
    }

    @Override
    public void monthlyStatement() {
        super.monthlyStatement();
    }

    /**
     * Devuelve el saldo, la comisión mensual, el número de transacciones
     * y el valor del sobregiro de la cuenta corriente.
     */
    @Override
public String printAccount() {
    int transactionsCount = this.depositsCount + this.withdrawalsCount;
    return String.format(
        Locale.US,
        "Balance: %.2f | Monthly fee: %.2f | Transactions: %d | Overdraft: %.2f",
        balance, monthlyFee, transactionsCount, overdraft
    );
}

    public float getOverdraft() {
        return overdraft;
    }
}
