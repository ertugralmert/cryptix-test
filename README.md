


CrytixTest : local ortamda Cryptix'i testme add.provider ile  
SecurityFileTest: local ortamda JVM'e parametre geçerek oluşturduğumuz java.security dosyasını kullanarak Cryptix'i testme  
CryptixTestServlet : web ortamda Cryptix'i testme (Open Liberty)  
CryptoProviderLoader : Cryptix provider'ı eklemek için weblistener anotasyonunu kullanma  

Özet olarak:

JVM’nin boot aşamasında Cryptix sağlayıcısının otomatik yüklenmemesi problemiyle karşılaşıldı.

Bu problemi aşmak için, uygulama başlatıldığında programatik olarak Cryptix sağlayıcısını güvenlik sağlayıcıları listesine ekleyen bir `ServletContextListener` (CryptoProviderLoader) geliştirdik.

---

Java güvenlik altyapısı, JVM'in başlatılması sırasında yani boot progress'inde "java.security" dosyasını içindeki tanımlı providerları kullanır.

Toplantı esnasında sizin gibi cryptix kullanımı simüle etmeye çalıştım.

İlk olarak add.provider yöntemi ile Crytpix'i gördüm.

İkinci olarak resources altına java.security dosyasını oluşturdum ve buraya cryptix ve diğer providerları ekledim. Sonrasında IDE aracılığı ile JVM'in oluşturduğum java.security dosyasını kullanmasını istedim .  Local'de bu da başarılı oldu.

Liberty ortamında ise  OpenLiberty kullanarak server.xml üzerinden classloader'a ekledim. Java 17 modüler yapısından boot classpath yöntemim başarılı olmadı.

Cryptix provider JVM'nin boot aşamasında otomatik olarak yüklenmemesi sorunu çözmek için programatik provider ekleme yöntemini tercih ettim.

Uygulama başlatılırken bir ServletContextListener(Web Listener) kullanarak Cyrptix provider ekledim. Bu sayade Cryptix'i  Java Security API'sine eklemiş oldum. Buradaki amaç JVM'in boot aşamasındaki kısıtlamalarında bağımsız olarak app düzeyinde Cryptix provider'ını ekleyeme çalışmaktır.

Bu sayede boot classpath ayarlarında değişiklik yapmadan sadece app kodu ile çözümü yönetmek istedim.

```java
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
    Security.insertProviderAt(new CryptixCrypto(), 1);
    System.out.println("Cryptix Provider eklendi, pozisyon: 1");
}

@Override
public void contextDestroyed(ServletContextEvent sce) {
}
    }

```


Burada uygulama başlatılırken WebListener anotosyonu kullanarak Cryptix prover'ını Java Security API'sine eklemek.

@WebListener anotasyonu sayesinde, openLiberty,wasliberty... uygulama başlatıldığında otomatik olarak yükler.

contextInitialized metodunda Security.insertProviderAt(new CryptixCrypto(),1);  ile ekleme yapılır.

Burada provider'ların en üstüne koymamaızın sebebi kriptografik işlemler sırasında öncelik olarak kullanılmasını sağlayama çalışmam.

contextDestroyed methodu opsiyonel olarak ekledim. kullanılmasını öneriririm. uygulama sonlandıktan sonra kaynaklar serbest kalır.

Alternatif olarak Boot classpath'e eklemek.

Java 8 için bu örneği verecek olursam, Cryptix JAR'ları -Xbootclasspath parametresi ile JVM'e kolaylıkla eklenebiliyor. Java 17 modüler bir sistem olduğu için bu yöntem biraz karmaşık hal alıyor. Yani openLiberty veya tomcat serverlarda boot classpath ayarları JVM başlatılırken kullandığı security configl dosyası üzerinden okunur ve cryptix tanımı eklenmezse bu provider çalışmalar.

Programatik yöntemi tercih etme sebenim Java 17'nin modüler yapısının boot classpath ayarları karmaşık geldiği için bu yöntem sorunu programatik yöntem ile çözülebilir olmasıdır.

Bu sayede yapılandırma bağımlılığı azaltılmış oluyor. Server yapılandırılması değiştirmeden uygulama kodunu dağıarak cryptix provider rahatlıkla kullanılır.

Security.insertProviderAt(); Java'nın onaylanmış ve yaygın olarak kullanılan güvenlik API'sidir.

İlgili WAR dosyası OpenLiberty veya benzeri bir suncuya dağıtıldığında app başlatılırken cryptix otoamatik olarak ekleniyor.

Sonuç olarak bu yöntem ile server düzeyinde bir değişiklik yapmadan merkezi bir biçimde , güvenilir ve profesyonel bir çözümdür.
