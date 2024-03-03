package com.example.cleanarchitecture.account.adapter.in.web;

import com.example.cleanarchitecture.account.application.port.in.CreateAccountUseCase;
import com.example.cleanarchitecture.common.annotations.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class CreateAccountController {

    private final CreateAccountUseCase createAccountUseCase;

    @PostMapping("/accounts")
    public ResponseEntity<Boolean> createAccount() {
        boolean createAccount = createAccountUseCase.createAccount();

        return ResponseEntity.ok(createAccount);
    }
}
