package com.imbabot.storage.server;

import java.util.ArrayList;
import java.util.List;

public class BasicAuthManager implements AuthManager {
    private class Entry{
        private String login;
        private String password;
        private String name;

        public Entry(String login, String password, String name){
            this.login = login;
            this.password = password;
            this.name = name;
        }
    }

    private List<Entry> user;

    public BasicAuthManager(){
        user = new ArrayList<>();
        user.add(new Entry("login1", "pass1", "user1"));
        user.add(new Entry("login2", "pass2", "user2"));
        user.add(new Entry("login3", "pass3", "user3"));
    }

    @Override
    public String getNickNameByLoginAndPassword(String login, String password) {
        for (Entry u: user){
            if (u.login.equals(login) && u.password.equals(password)){
                return u.name;
            }
        }
        return null;
    }
}
