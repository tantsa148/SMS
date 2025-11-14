package Birger.SMS.security;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBlacklist {

    private static final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public static void addToken(String token) {
        blacklist.add(token);
    }

    public static boolean removeToken(String token) {
        return blacklist.remove(token);
    }

    public static boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
