package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.tls.main;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.tls.HostnameVerifier;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.tls.MariaDbX509KeyManager;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.tls.MariaDbX509TrustingManager;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.ExceptionFactory;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.SslMode;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.TlsSocketPlugin;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Logger;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Loggers;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.KeyStore.LoadStoreParameter;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class DefaultTlsSocketPlugin implements TlsSocketPlugin {
   private static final Logger logger = Loggers.getLogger(DefaultTlsSocketPlugin.class);

   private static KeyManager loadClientCerts(String keyStoreUrl, String keyStorePassword, String keyPassword, String storeType, ExceptionFactory exceptionFactory) throws SQLException {
      try {
         InputStream inStream = loadFromUrl(keyStoreUrl);

         MariaDbX509KeyManager var9;
         try {
            char[] keyStorePasswordChars = keyStorePassword == null ? null : (keyStorePassword.equals("") ? null : keyStorePassword.toCharArray());
            char[] keyStoreChars = keyPassword == null ? keyStorePasswordChars : (keyPassword.equals("") ? null : keyPassword.toCharArray());
            KeyStore ks = KeyStore.getInstance(storeType != null ? storeType : KeyStore.getDefaultType());
            ks.load(inStream, keyStorePasswordChars);
            var9 = new MariaDbX509KeyManager(ks, keyStoreChars);
         } catch (Throwable var11) {
            if (inStream != null) {
               try {
                  inStream.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }
            }

            throw var11;
         }

         if (inStream != null) {
            inStream.close();
         }

         return var9;
      } catch (GeneralSecurityException | IOException var12) {
         throw exceptionFactory.create("Failed to read keyStore file. Option keyStore=" + keyStoreUrl, "08000", var12);
      }
   }

   private static InputStream loadFromUrl(String keyStoreUrl) throws FileNotFoundException {
      try {
         return (new URI(keyStoreUrl)).toURL().openStream();
      } catch (Exception var2) {
         return new FileInputStream(keyStoreUrl);
      }
   }

   private static InputStream getInputStreamFromPath(String path) throws IOException {
      try {
         return (new URI(path)).toURL().openStream();
      } catch (Exception var3) {
         if (path.startsWith("-----")) {
            return new ByteArrayInputStream(path.getBytes());
         } else {
            File f = new File(path);
            if (f.exists() && !f.isDirectory()) {
               return f.toURI().toURL().openStream();
            } else {
               throw new IOException(String.format("File not found for option `serverSslCert` (value: '%s')", path), var3);
            }
         }
      }
   }

   public String type() {
      return "DEFAULT";
   }

   public SSLSocketFactory getSocketFactory(Configuration conf, ExceptionFactory exceptionFactory) throws SQLException {
      TrustManager[] trustManager = null;
      KeyManager[] keyManager = null;
      if (conf.sslMode() == SslMode.TRUST) {
         trustManager = new X509TrustManager[]{new MariaDbX509TrustingManager()};
      } else if (conf.serverSslCert() != null) {
         KeyStore ks;
         try {
            ks = KeyStore.getInstance(conf.trustStoreType() != null ? conf.trustStoreType() : KeyStore.getDefaultType());
         } catch (GeneralSecurityException var18) {
            throw exceptionFactory.create("Failed to create keystore instance", "08000", var18);
         }

         try {
            InputStream inStream = getInputStreamFromPath(conf.serverSslCert());

            try {
               ks.load((LoadStoreParameter)null);
               CertificateFactory cf = CertificateFactory.getInstance("X.509");
               Collection<? extends Certificate> caList = cf.generateCertificates(inStream);
               Iterator var9 = caList.iterator();

               while(var9.hasNext()) {
                  Certificate ca = (Certificate)var9.next();
                  ks.setCertificateEntry(UUID.randomUUID().toString(), ca);
               }

               TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
               tmf.init(ks);
               TrustManager[] var27 = tmf.getTrustManagers();
               int var11 = var27.length;
               int var12 = 0;

               while(true) {
                  if (var12 < var11) {
                     TrustManager tm = var27[var12];
                     if (!(tm instanceof X509TrustManager)) {
                        ++var12;
                        continue;
                     }

                     trustManager = new X509TrustManager[]{(X509TrustManager)tm};
                  }

                  if (trustManager != null) {
                     break;
                  }

                  throw new SQLException("No X509TrustManager found");
               }
            } catch (Throwable var19) {
               if (inStream != null) {
                  try {
                     inStream.close();
                  } catch (Throwable var14) {
                     var19.addSuppressed(var14);
                  }
               }

               throw var19;
            }

            if (inStream != null) {
               inStream.close();
            }
         } catch (IOException var20) {
            throw exceptionFactory.create("Failed load keyStore", "08000", var20);
         } catch (GeneralSecurityException var21) {
            throw exceptionFactory.create("Failed to store certificate from serverSslCert into a keyStore", "08000", var21);
         }
      }

      if (conf.keyStore() != null) {
         keyManager = new KeyManager[]{loadClientCerts(conf.keyStore(), conf.keyStorePassword(), conf.keyPassword(), conf.keyStoreType(), exceptionFactory)};
      } else {
         String keyStore = System.getProperty("javax.net.ssl.keyStore");
         String keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword", conf.keyStorePassword());
         String keyStoreType = System.getProperty("javax.net.ssl.keyStoreType", conf.keyStoreType());
         if (keyStore != null) {
            try {
               keyManager = new KeyManager[]{loadClientCerts(keyStore, keyStorePassword, keyStorePassword, keyStoreType, exceptionFactory)};
            } catch (SQLException var17) {
               keyManager = null;
               logger.error("Error loading key manager from system properties", (Throwable)var17);
            }
         }
      }

      try {
         SSLContext sslContext = SSLContext.getInstance("TLS");
         sslContext.init(keyManager, trustManager, (SecureRandom)null);
         return sslContext.getSocketFactory();
      } catch (KeyManagementException var15) {
         throw exceptionFactory.create("Could not initialize SSL context", "08000", var15);
      } catch (NoSuchAlgorithmException var16) {
         throw exceptionFactory.create("SSLContext TLS Algorithm not unknown", "08000", var16);
      }
   }

   public void verify(String host, SSLSession session, long serverThreadId) throws SSLException {
      try {
         Certificate[] certs = session.getPeerCertificates();
         X509Certificate cert = (X509Certificate)certs[0];
         HostnameVerifier.verify(host, cert, serverThreadId);
      } catch (SSLException var7) {
         logger.info(var7.getMessage(), (Throwable)var7);
         throw var7;
      }
   }
}
