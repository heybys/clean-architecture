package com.example.cleanarchitecture.account.domain;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {
    AccountId id;

    @NonNull
    Money baselineBalance;

    ActivityWindow activityWindow;

    public static Account withId(AccountId accountId, Money baselineBalance, ActivityWindow activityWindow) {
        return new Account(accountId, baselineBalance, activityWindow);
    }

    public Optional<AccountId> getId() {
        return Optional.ofNullable(this.id);
    }

    public Money calculateBalance() {
        return Money.add(this.baselineBalance, this.activityWindow.calculateBalance(this.id));
    }

    public boolean withdraw(Money money, AccountId targetAccountId) {

        if (!mayWithdraw(money)) {
            return false;
        }

        Activity withdraw = Activity.withoutId(this.id, this.id, targetAccountId, money, LocalDateTime.now());
        this.activityWindow.addActivity(withdraw);

        return true;
    }

    public boolean mayWithdraw(Money money) {
        return calculateBalance().isGreaterThanOrEqualTo(money);
    }

    public boolean deposit(Money money, AccountId sourceAccountId) {
        Activity deposit = Activity.withoutId(this.id, sourceAccountId, this.id, money, LocalDateTime.now());
        this.activityWindow.addActivity(deposit);

        return true;
    }

    @Value
    public static class AccountId {

        Long value;
    }
}
