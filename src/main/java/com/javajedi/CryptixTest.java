
package com.javajedi;
import java.security.Provider;
import java.security.Security;

public class CryptixTest {
    public static void main(String[] args) {
        System.out.println("Java Version: ".concat(System.getProperty("java.version")));
        try{
            // providerları görelim
            System.out.println("\nMevcut Providerlar: ");
            listProviders();
            // providerları ekleyelim
            testCryptix();
        }catch (Exception e){
            System.err.println("Hata ".concat(e.getMessage()));
        }

    }

    private static void listProviders() {
        for (Provider provider : Security.getProviders()) {
            System.out.println(provider.getName() + " - " + provider.getVersionStr());

        }
    }

    private static void testCryptix() {
        try {
            //Cryptix providerı ekleeme
            Provider cryptixProvider = new cryptix.jce.provider.CryptixCrypto();
            Security.addProvider(cryptixProvider);
            System.out.println("\nCryptix eklendikten sonra:");
            listProviders();

            // provider kntrol
            Provider cryptix = Security.getProvider("CryptixCrypto");
            if ( cryptix != null){
                System.out.println("\nCryptix başarıyla yüklendi!");
                System.out.println("Provider adı: " + cryptix.getName());
                System.out.println("Versiyon: " + cryptix.getVersion());
                System.out.println("Bilgi: " + cryptix.getInfo());
            }else {
                System.out.println("Cryptix yüklenemedi!");
            }
        }catch (Exception e){
            System.err.println("Cryptix ekleme hata ".concat(e.getMessage()));
            e.printStackTrace();
        }
    }

}
