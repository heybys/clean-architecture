package com.example.cleanarchitecture.account.domain;

import com.example.cleanarchitecture.account.domain.Account.AccountId;
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
    Money money;

    @NonNull
    LocalDateTime timestamp;

    public static Activity withId(
            @NonNull ActivityId activityId,
            @NonNull AccountId ownerAccountId,
            @NonNull AccountId sourceAccountId,
            @NonNull AccountId targetAccountId,
            @NonNull Money money,
            @NonNull LocalDateTime timestamp) {
        return new Activity(activityId, ownerAccountId, sourceAccountId, targetAccountId, money, timestamp);
    }

    public static Activity withoutId(
            @NonNull AccountId ownerAccountId,
            @NonNull AccountId sourceAccountId,
            @NonNull AccountId targetAccountId,
            @NonNull Money money,
            @NonNull LocalDateTime timestamp) {
        return new Activity(null, ownerAccountId, sourceAccountId, targetAccountId, money, timestamp);
    }

    @Value
    public static class ActivityId {
        Long value;
    }
}
