package dev.nieves.bank.model;

/**
 * Representa una cuenta de ahorros.
 * Hereda el comportamiento base de Account y añade la lógica
 * de actividad/inactividad según el saldo mínimo.
 */
public class SavingsAccount extends Account {

    private static final float MINIMUM_BALANCE = 10000f;
    private static final int FREE_WITHDRAWALS = 4;
    private static final float EXTRA_WITHDRAWAL_FEE = 1000f;

    private boolean active;

    public SavingsAccount(float balance, float annualRate) {
        super(balance, annualRate);
        this.active = balance >= MINIMUM_BALANCE;
    }

    @Override
    public void deposit(float amount) {
        if (!this.active) {
            throw new IllegalStateException("No se puede consignar: la cuenta de ahorros está inactiva");
        }
        super.deposit(amount);
    }

    @Override
    public void withdraw(float amount) {
        if (!this.active) {
            throw new IllegalStateException("No se puede retirar: la cuenta de ahorros está inactiva");
        }
        super.withdraw(amount);
    }

    @Override
    public void monthlyStatement() {
        if (this.withdrawalsCount > FREE_WITHDRAWALS) {
            int extraWithdrawals = this.withdrawalsCount - FREE_WITHDRAWALS;
            this.monthlyFee = extraWithdrawals * EXTRA_WITHDRAWAL_FEE;
        }
        super.monthlyStatement();
        this.active = this.balance >= MINIMUM_BALANCE;
    }

    /**
     * Devuelve el saldo, la comisión mensual y el número de transacciones
     * (consignaciones + retiros) de la cuenta de ahorros.
     */
    @Override
    public String printAccount() {
        int transactionsCount = this.depositsCount + this.withdrawalsCount;
        return String.format(
            "Balance: %.2f | Monthly fee: %.2f | Transactions: %d",
            balance, monthlyFee, transactionsCount
        );
    }

    public boolean isActive() {
        return active;
    }
}
