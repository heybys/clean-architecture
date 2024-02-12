package com.example.cleanarchitecture.account.domain;

import com.example.cleanarchitecture.common.domain.Money;
import lombok.Value;

@Value
public class Account {

    AccountId id;

    Money baselineWindow;

    @Value
    public static class AccountId {
        Long value;
    }
}
