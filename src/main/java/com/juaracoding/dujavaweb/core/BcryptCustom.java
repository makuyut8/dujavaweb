package com.juaracoding.dujavaweb.core;/*
Created By IntelliJ IDEA 2022.2.3 (Community Edition)
IntelliJ IDEA 2022.3.2 (Community Edition)
Build #IC-223.8617.56, built on January 26, 2023
@Author USER a.k.a. Deby Utari
Java Developer
Created on 27/02/2023 22:24
@Last Modified 27/02/2023 22:24
Version 1.0
*/

import org.mindrot.jbcrypt.BCrypt;

import java.util.function.Function;

public class BcryptCustom {
    private final int logRounds;

    public BcryptCustom(int logRounds) {
        this.logRounds = logRounds;
    }

    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }

    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public boolean verifyAndUpdateHash(String password, String hash, Function<String, Boolean> updateFunc) {
        if (BCrypt.checkpw(password, hash)) {
            int intRounds = getRounds(hash);
            System.out.println("ROUNDS VALUE -> "+intRounds);
            if(intRounds ==0)
            {
                return false;
            }
            if (intRounds != logRounds) {
                String newHash = hash(password);
                return updateFunc.apply(newHash);
            }
            return true;
        }
        return false;
    }

    private int getRounds(String salt) {
        char minor = (char)0;
        int off = 0;

        if (salt.charAt(0) != '$' || salt.charAt(1) != '2')
        {
            System.out.println("Salt Version not valid");
            return 0;
        }
        if (salt.charAt(2) == '$')
        {
            off = 3;
        }
        else
        {
            minor = salt.charAt(2);
            if (minor != 'a' || salt.charAt(3) != '$')
            {
                System.out.println("Salt Version not valid");
                return 0;
            }
            off = 4;
        }
        if (salt.charAt(off + 2) > '$')
        {
            System.out.println("Salt rounds are  Missing");
            return 0;
        }
        return Integer.parseInt(salt.substring(off, off + 2));
    }
}
