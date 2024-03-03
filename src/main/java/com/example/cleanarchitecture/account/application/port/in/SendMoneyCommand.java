package com.example.cleanarchitecture.account.application.port.in;

import com.example.cleanarchitecture.account.domain.Account.AccountId;
import com.example.cleanarchitecture.account.domain.Money;
import lombok.NonNull;

public record SendMoneyCommand(
        @NonNull AccountId sourceAccountId, @NonNull AccountId targetAccountId, @NonNull Money money) {}
