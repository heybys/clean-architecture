package com.example.cleanarchitecture.account.domain;

import com.example.cleanarchitecture.account.domain.Account.AccountId;
import com.example.cleanarchitecture.common.domain.Money;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Activity {

    ActivityId id;

    @NonNull
    AccountId ownerAccountId;

    @NonNull
    AccountId sourceAccountId;

    @NonNull
    AccountId targetAccountId;

    @NonNull
    LocalDateTime timestamp;

    @NonNull
    Money money;

    public static Activity withoutId(
            @NonNull AccountId ownerAccountId,
            @NonNull AccountId sourceAccountId,
            @NonNull AccountId targetAccountId,
            @NonNull LocalDateTime timestamp,
            @NonNull Money money) {
        return new Activity(null, ownerAccountId, sourceAccountId, targetAccountId, timestamp, money);
    }

    @Value
    public static class ActivityId {

        Long value;
    }
}
