package com.example.cleanarchitecture.account.application.port.out;

import com.example.cleanarchitecture.account.domain.Account;
import com.example.cleanarchitecture.account.domain.Account.AccountId;
import java.time.LocalDateTime;

public interface LoadAccountPort {

    Account createAccount();

    Account loadAccount(AccountId accountId, LocalDateTime baselineDate);
}
