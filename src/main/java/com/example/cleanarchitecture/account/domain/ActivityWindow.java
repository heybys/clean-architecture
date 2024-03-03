package com.example.cleanarchitecture.account.domain;

import com.example.cleanarchitecture.account.domain.Account.AccountId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.NonNull;
import lombok.Value;

@Value
public class ActivityWindow {

    @NonNull
    List<Activity> activities;

    public ActivityWindow(@NonNull List<Activity> activities) {
        this.activities = activities;
    }

    public ActivityWindow(@NonNull Activity... activities) {
        this.activities = new ArrayList<>(Arrays.asList(activities));
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(this.activities);
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    public LocalDateTime getStartTimestamp() {
        return activities.stream()
                .min(Comparator.comparing(Activity::getTimestamp))
                .orElseThrow(IllegalAccessError::new)
                .getTimestamp();
    }

    public LocalDateTime getEndTimestamp() {
        return activities.stream()
                .max(Comparator.comparing(Activity::getTimestamp))
                .orElseThrow(IllegalStateException::new)
                .getTimestamp();
    }

    public Money calculateBalance(AccountId accountId) {
        Money depositBalance = this.activities.stream()
                .filter(activity -> activity.getTargetAccountId().equals(accountId))
                .map(Activity::getMoney)
                .reduce(Money.ZERO, Money::add);

        Money withdrawalBalance = this.activities.stream()
                .filter(activity -> activity.getSourceAccountId().equals(accountId))
                .map(Activity::getMoney)
                .reduce(Money.ZERO, Money::add);

        return Money.add(depositBalance, withdrawalBalance.negate());
    }
}
