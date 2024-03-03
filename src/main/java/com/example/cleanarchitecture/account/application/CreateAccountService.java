package com.example.cleanarchitecture.account.application;

import com.example.cleanarchitecture.account.application.port.in.CreateAccountUseCase;
import com.example.cleanarchitecture.account.application.port.out.LoadAccountPort;
import com.example.cleanarchitecture.account.application.port.out.UpdateAccountStatePort;
import com.example.cleanarchitecture.account.domain.Account;
import com.example.cleanarchitecture.account.domain.Account.AccountId;
import com.example.cleanarchitecture.account.domain.Money;
import com.example.cleanarchitecture.common.annotations.UseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {

    private final LoadAccountPort loadAccountPort;

    private final UpdateAccountStatePort updateAccountStatePort;

    @Override
    public boolean createAccount() {
        Account account = loadAccountPort.createAccount();

        if (!account.deposit(Money.of(1000L), new AccountId(0L))) {
            return false;
        }

        updateAccountStatePort.updateActivities(account);
        return true;
    }
}
