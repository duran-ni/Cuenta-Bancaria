package dev.nieves.bank.client;

import dev.nieves.bank.model.Account;
import dev.nieves.bank.service.AccountService;

/**
 * Representa a un cliente del banco que solicita operaciones
 * sobre su cuenta a través del servicio.
 */
public class Client {
    private final String name;
    private final Account account;
    private final AccountService accountService;

    public Client(String name, Account account, AccountService accountService) {
        this.name = name;
        this.account = account;
        this.accountService = accountService;
    }

    public void requestDeposit(float amount) {
        accountService.deposit(this.account, amount);
    }

    public void requestWithdraw(float amount) {
        accountService.withdraw(this.account, amount);
    }

    public void requestMonthlyStatement() {
        accountService.generateMonthlyStatement(this.account);
    }

    public String requestAccountSummary() {
        return accountService.printAccount(this.account);
    }

    public String getName() {
        return name;
    }

    public Account getAccount() {
        return account;
    }
}
