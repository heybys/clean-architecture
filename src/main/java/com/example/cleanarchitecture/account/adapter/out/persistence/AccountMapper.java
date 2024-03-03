package com.example.cleanarchitecture.account.adapter.out.persistence;

import com.example.cleanarchitecture.account.domain.Account;
import com.example.cleanarchitecture.account.domain.Account.AccountId;
import com.example.cleanarchitecture.account.domain.Activity;
import com.example.cleanarchitecture.account.domain.Activity.ActivityId;
import com.example.cleanarchitecture.account.domain.ActivityWindow;
import com.example.cleanarchitecture.account.domain.Money;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    Account mapToDomainEntity(
            AccountJpaEntity account, List<ActivityJpaEntity> activities, Long withdrawalBalance, Long depositBalance) {

        Money baselineBalance = Money.subtract(Money.of(depositBalance), Money.of(withdrawalBalance));

        return Account.withId(new AccountId(account.getId()), baselineBalance, mapToActivityWindow(activities));
    }

    ActivityWindow mapToActivityWindow(List<ActivityJpaEntity> activities) {
        List<Activity> mappedActivities = new ArrayList<>();

        for (ActivityJpaEntity activity : activities) {
            Activity mappedActivity = Activity.withId(
                    new ActivityId(activity.getId()),
                    new AccountId(activity.getOwnerAccountId()),
                    new AccountId(activity.getSourceAccountId()),
                    new AccountId(activity.getTargetAccountId()),
                    Money.of(activity.getAmount()),
                    activity.getTimestamp());

            mappedActivities.add(mappedActivity);
        }

        return new ActivityWindow(mappedActivities);
    }

    ActivityJpaEntity mapToJpaEntity(Activity activity) {
        return new ActivityJpaEntity(
                activity.getId() == null ? null : activity.getId().getValue(),
                activity.getOwnerAccountId().getValue(),
                activity.getSourceAccountId().getValue(),
                activity.getTargetAccountId().getValue(),
                activity.getMoney().getAmount().longValue(),
                activity.getTimestamp());
    }
}
