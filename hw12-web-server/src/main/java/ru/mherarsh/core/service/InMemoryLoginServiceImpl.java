package ru.mherarsh.core.service;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.util.security.Password;

import java.util.HashMap;
import java.util.Map;

public class InMemoryLoginServiceImpl extends AbstractLoginService {
    private final Map<String, String> users = new HashMap<>();

    public InMemoryLoginServiceImpl() {
        users.put("user1", "123");
        users.put("user2", "123");
        users.put("user3", "123");
    }

    @Override
    protected String[] loadRoleInfo(UserPrincipal userPrincipal) {
        return new String[] {"user"};
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        if(!users.containsKey(login)){
            return null;
        }

        return new UserPrincipal(login, new Password(users.get(login)));
    }
}
