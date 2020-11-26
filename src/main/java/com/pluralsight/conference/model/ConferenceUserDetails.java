package com.pluralsight.conference.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ConferenceUserDetails extends org.springframework.security.core.userdetails.User {
    @Autowired
    private String nickname;

    public ConferenceUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
