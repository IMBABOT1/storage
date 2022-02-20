package com.imbabot.storage.server;

import java.sql.SQLException;

public interface AuthManager {
    String getNickNameByLoginAndPassword(String login, String password);
}
