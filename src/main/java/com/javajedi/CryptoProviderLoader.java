package com.javajedi;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import cryptix.jce.provider.CryptixCrypto;
import java.security.Security;

@WebListener
public class CryptoProviderLoader implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Cryptix sağlayıcısını ekleyev
        int position = Security.insertProviderAt(new CryptixCrypto(), 1);
        System.out.println("Cryptix Provider eklendi, pozisyon: " + position);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
