package com.example.auth;

import org.apache.hive.service.auth.PasswdAuthenticationProvider;
import javax.security.sasl.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

public class EnvPasswdAuthenticator implements PasswdAuthenticationProvider {

    private static final String ENV = "HIVE_AUTH_USERS";
    private static final Map<String, String> USERS = load();

    @Override
    public void Authenticate(String user, String password) throws AuthenticationException {
        String expected = USERS.get(user);
        if (expected == null || !expected.equals(password)) {
            throw new AuthenticationException("Invalid username or password");
        }
    }

    private static Map<String, String> load() {
        Map<String, String> map = new HashMap<>();
        String raw = System.getenv(ENV);
        if (raw == null || raw.trim().isEmpty()) {
            System.err.println("[EnvPasswdAuthenticator] No users in env " + ENV);
            return map;
        }
        for (String pair : raw.split(",")) {
            if (pair == null) continue;
            pair = pair.trim();
            if (pair.isEmpty()) continue;
            int i = pair.indexOf(':');
            if (i <= 0 || i == pair.length() - 1) continue;
            String u = pair.substring(0, i).trim();
            String p = pair.substring(i + 1).trim();
            if (!u.isEmpty()) map.put(u, p);
        }
        System.out.println("[EnvPasswdAuthenticator] Users loaded: " + map.keySet());
        return map;
    }
}
