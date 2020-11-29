package com.pluralsight.conference.repository;

import com.pluralsight.conference.model.Account;
import com.pluralsight.conference.model.ConferenceUserDetails;
import com.pluralsight.conference.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

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

    @Override
    public VerificationToken findByToken(String token) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        VerificationToken verificationToken = jdbcTemplate.queryForObject("select * from verification_tokens where token = ?",
                new RowMapper<VerificationToken>() {
                    @Override
                    public VerificationToken mapRow(ResultSet resultSet, int i) throws SQLException {
                        VerificationToken rsToken = new VerificationToken();
                        rsToken.setUsername(resultSet.getString("username"));
                        rsToken.setToken(resultSet.getString("token"));
                        rsToken.setExpiryDate(resultSet.getTimestamp("expiry_date"));
                        return rsToken;
                    }

                }, token );
        return verificationToken;
    }

    @Override
    public Account findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Account account = jdbcTemplate.queryForObject("select username, firstname, lastname, password from accounts where username = ?",
                new RowMapper<Account>() {
                    @Override
                    public Account mapRow(ResultSet resultSet, int i) throws SQLException {
                        Account account = new Account();
                        account.setUsername(resultSet.getString("username"));
                        account.setFirstName(resultSet.getString("firstname"));
                        account.setLastName(resultSet.getString("lastname"));
                        account.setPassword(resultSet.getString("password"));
                        return account;
                    }
                }, username);
        return account;
    }

    @Override
    public void createUserDetails(ConferenceUserDetails userDetails) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("insert into users (username,password,enabled) values (?,?,?)",
                userDetails.getUsername(),
                userDetails.getPassword(),
                1);
    }

    @Override
    public void createAuthorities(ConferenceUserDetails userDetails) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Iterator<GrantedAuthority> itr = userDetails.getAuthorities().iterator();
        while (itr.hasNext()) {
            jdbcTemplate.update("insert into authorities(username, authority) values (?,?)",
                    userDetails.getUsername(),
                    itr.next().getAuthority());
        }
    }

    @Override
    public void delete(Account account) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("delete from accounts where username = ? ", account.getUsername());
    }

    @Override
    public void deleteToken(String token) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("delete from verification_tokens where token = ?",token);
    }


}
