package com.javajedi;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;

@WebServlet("/test")
public class CryptixTestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");
        StringBuilder result = new StringBuilder();

        result.append("Java Version: ").append(System.getProperty("java.version")).append("\n\n");

        try {
            // Mevcut  providerlar
            result.append("Mevcut Provider'lar:\n");
            for (Provider provider : Security.getProviders()) {
                result.append(provider.getName())
                        .append(" - ")
                        .append(provider.getVersionStr())
                        .append("\n");
            }

            // Cryptix provider check
            Provider cryptix = Security.getProvider("CryptixCrypto");
            if (cryptix != null) {
                result.append("\nCryptix yüklü!\n");
                result.append("Provider adı: ").append(cryptix.getName()).append("\n");
                result.append("Versiyon: ").append(cryptix.getVersion()).append("\n");
                result.append("Bilgi: ").append(cryptix.getInfo()).append("\n");
            } else {
                result.append("\nCryptix yüklü değil!\n");
            }

        } catch (Exception e) {
            result.append("Hata: ").append(e.getMessage());
        }

        response.getWriter().write(result.toString());
    }
}