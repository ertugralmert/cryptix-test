// com.javajedi paketi altında SecurityFileTest.java
package com.javajedi;

import java.security.Provider;
import java.security.Security;

public class SecurityFileTest {
    public static void main(String[] args) {
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("\nMevcut Providerlar: ");
        listProviders();
    }

    private static void listProviders() {
        for (Provider provider : Security.getProviders()) {
            System.out.println(provider.getName() + " - " + provider.getVersionStr());
        }
    }
}