package com.example.cleanarchitecture.account.adapter.out.persistence;

import com.example.cleanarchitecture.account.application.port.out.LoadAccountPort;
import com.example.cleanarchitecture.account.application.port.out.UpdateAccountStatePort;
import com.example.cleanarchitecture.account.domain.Account;
import com.example.cleanarchitecture.account.domain.Account.AccountId;
import com.example.cleanarchitecture.account.domain.Activity;
import com.example.cleanarchitecture.common.annotations.PersistenceAdapter;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {

    private final AccountMapper accountMapper;

    private final AccountJpaRepository accountJpaRepository;

    private final ActivityJpaRepository activityJpaRepository;

    @Override
    public Account createAccount() {
        AccountJpaEntity saved = accountJpaRepository.save(new AccountJpaEntity());

        return accountMapper.mapToDomainEntity(saved, new ArrayList<>(), 0L, 0L);
    }

    @Override
    public Account loadAccount(AccountId accountId, LocalDateTime baselineDate) {
        AccountJpaEntity account =
                accountJpaRepository.findById(accountId.getValue()).orElseThrow(EntityNotFoundException::new);

        List<ActivityJpaEntity> activities = activityJpaRepository.findByOwnerSince(accountId.getValue(), baselineDate);

        Long withdrawalBalance = Optional.ofNullable(
                        activityJpaRepository.getWithdrawalBalanceUntil(accountId.getValue(), baselineDate))
                .orElse(0L);

        Long depositBalance = Optional.ofNullable(
                        activityJpaRepository.getDepositBalanceUntil(accountId.getValue(), baselineDate))
                .orElse(0L);

        return accountMapper.mapToDomainEntity(account, activities, withdrawalBalance, depositBalance);
    }

    @Override
    public void updateActivities(Account account) {
        for (Activity activity : account.getActivityWindow().getActivities()) {
            if (activity.getId() == null) {
                activityJpaRepository.save(accountMapper.mapToJpaEntity(activity));
            }
        }
    }
}
