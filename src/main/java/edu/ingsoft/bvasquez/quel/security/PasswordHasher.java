package edu.ingsoft.bvasquez.quel.security;

import edu.ingsoft.bvasquez.quel.security.Bcrypt.BCrypt;

public class PasswordHasher {

    private static final int LOG_ROUNDS = 12;

    public String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    public boolean matches(String rawPassword, String hash) {
        try {
            return BCrypt.checkpw(rawPassword, hash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
