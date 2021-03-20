package com.geekbrains.server;


public class SimpleAuthService implements AuthService {
    private final DBHelper dbhelper = DBHelper.getInstance();

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        return dbhelper.findByLoginAndPassword(login, password);
    }

    @Override
    public boolean updateNickname(String oldNickname, String newNickname) {
        int result  = dbhelper.updateNickname(oldNickname, newNickname);
        return result != 0;
    }
}
