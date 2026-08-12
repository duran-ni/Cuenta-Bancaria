package dev.nieves.bank.service;

import dev.nieves.bank.model.Account;

/**
 * Orquesta las operaciones sobre una cuenta bancaria.
 * Actúa como intermediario entre el cliente y el modelo de dominio.
 */
public class AccountService {

    public void deposit(Account account, float amount) {
        account.deposit(amount);
    }

    public void withdraw(Account account, float amount) {
        account.withdraw(amount);
    }

    public void generateMonthlyStatement(Account account) {
        account.monthlyStatement();
    }

    public String printAccount(Account account) {
        return account.printAccount();
    }
}
