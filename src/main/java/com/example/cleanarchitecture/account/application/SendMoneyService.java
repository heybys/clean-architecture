package com.example.cleanarchitecture.account.application;

import com.example.cleanarchitecture.account.application.port.in.SendMoneyCommand;
import com.example.cleanarchitecture.account.application.port.in.SendMoneyUseCase;
import com.example.cleanarchitecture.account.application.port.out.LoadAccountPort;
import com.example.cleanarchitecture.account.application.port.out.UpdateAccountStatePort;
import com.example.cleanarchitecture.account.domain.Account;
import com.example.cleanarchitecture.account.domain.Account.AccountId;
import com.example.cleanarchitecture.common.annotations.UseCase;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class SendMoneyService implements SendMoneyUseCase {

    private final LoadAccountPort loadAccountPort;

    private final UpdateAccountStatePort updateAccountStatePort;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {

        LocalDateTime baselineDate = LocalDateTime.now().minusDays(7L);

        Account sourceAccount = loadAccountPort.loadAccount(command.sourceAccountId(), baselineDate);
        Account targetAccount = loadAccountPort.loadAccount(command.targetAccountId(), baselineDate);

        AccountId sourceAccountId = sourceAccount.getId().orElseThrow(IllegalStateException::new);
        AccountId targetAccountId = targetAccount.getId().orElseThrow(IllegalStateException::new);

        if (!sourceAccount.withdraw(command.money(), targetAccountId)) {
            return false;
        }

        if (!targetAccount.deposit(command.money(), sourceAccountId)) {
            return false;
        }

        updateAccountStatePort.updateActivities(sourceAccount);
        updateAccountStatePort.updateActivities(targetAccount);

        return true;
    }
}
