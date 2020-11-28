package com.pluralsight.conference.repository;

import com.pluralsight.conference.model.Account;
import com.pluralsight.conference.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AccountRepositoryImpl implements AccountRepository{

    @Autowired
    private DataSource dataSource;

    @Override
    public Account create(Account account) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("insert into accounts (username,password,email,firstname,lastname) values " +
                "(?,?,?,?,?)", account.getUsername(),
                account.getPassword(),
                account.getEmail(),
                account.getFirstName(),
                account.getLastName());
        return account;
    }

    @Override
    public void saveToken(VerificationToken verificationToken) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("insert into verification_tokens (username,token, expiry_date) values " +
                        "(?,?,?)", verificationToken.getUsername(),
                verificationToken.getToken(),
                verificationToken.calculateExpiryDate(verificationToken.EXPIRATION));

    }
}
