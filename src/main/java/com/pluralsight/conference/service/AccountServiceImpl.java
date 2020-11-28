package com.pluralsight.conference.service;

import com.pluralsight.conference.model.Account;
import com.pluralsight.conference.model.VerificationToken;
import com.pluralsight.conference.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account create(Account account) {
        return accountRepository.create(account);
    }

    @Override
    public void createVerificationToken(Account account, String token) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsername(account.getUsername());

        accountRepository.saveToken(verificationToken);
    }

    @Override
    public void confirmAccount(String token) {

    }
}
