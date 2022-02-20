package com.imbabot.storage.server;

import java.util.ArrayList;
import java.util.List;

public class BasicAuthManager {
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

    private List<Entry> list;

    public BasicAuthManager(){
        list = new ArrayList<>();
        list.add(new Entry("login1", "pass1", "user1"));
        list.add(new Entry("login2", "pass2", "user2"));
        list.add(new Entry("login3", "pass3", "user3"));
    }
}
