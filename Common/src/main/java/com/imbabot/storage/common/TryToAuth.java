package com.imbabot.storage.common;

public class TryToAuth extends AbstractMessage{

    private String login;
    private String password;

    public String getLogin(){
        return login;
    }

    public String getPassword(){
        return password;
    }

    public TryToAuth(String login, String password){
        this.login = login;
        this.password = password;
    }

}
