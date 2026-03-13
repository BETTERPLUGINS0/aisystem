package github.nighter.smartspawner.libs.mariadb.plugin.tls.main;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.client.tls.HostnameVerifier;
import github.nighter.smartspawner.libs.mariadb.client.tls.MariaDbX509EphemeralTrustingManager;
import github.nighter.smartspawner.libs.mariadb.client.tls.MariaDbX509KeyManager;
import github.nighter.smartspawner.libs.mariadb.client.tls.MariaDbX509TrustingManager;
import github.nighter.smartspawner.libs.mariadb.export.ExceptionFactory;
import github.nighter.smartspawner.libs.mariadb.export.SslMode;
import github.nighter.smartspawner.libs.mariadb.plugin.TlsSocketPlugin;
import github.nighter.smartspawner.libs.mariadb.util.log.Logger;
import github.nighter.smartspawner.libs.mariadb.util.log.Loggers;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStore.LoadStoreParameter;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
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
            char[] keyStorePasswordChars = keyStorePassword == null ? null : (keyStorePassword.isEmpty() ? null : keyStorePassword.toCharArray());
            char[] keyStoreChars = keyPassword == null ? keyStorePasswordChars : (keyPassword.isEmpty() ? null : keyPassword.toCharArray());
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

   public TrustManager[] getTrustManager(Configuration conf, ExceptionFactory exceptionFactory, HostAddress hostAddress) throws SQLException {
      TrustManager[] trustManager = null;
      SslMode sslMode = hostAddress.sslMode == null ? conf.sslMode() : hostAddress.sslMode;
      if (sslMode == SslMode.TRUST) {
         trustManager = new X509TrustManager[]{new MariaDbX509TrustingManager()};
      } else {
         int var47;
         if (conf.serverSslCert() == null && conf.trustStore() == null) {
            if (conf.fallbackToSystemTrustStore()) {
               try {
                  TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                  tmf.init((KeyStore)null);
                  TrustManager[] var45 = tmf.getTrustManagers();
                  int var46 = var45.length;

                  for(var47 = 0; var47 < var46; ++var47) {
                     TrustManager tm = var45[var47];
                     if (tm instanceof X509TrustManager) {
                        trustManager = new X509TrustManager[]{new MariaDbX509EphemeralTrustingManager((X509TrustManager)tm)};
                        break;
                     }
                  }
               } catch (Exception var34) {
                  throw new SQLException("No X509TrustManager found", var34);
               }
            }
         } else {
            KeyStore ks;
            try {
               ks = KeyStore.getInstance(conf.trustStoreType() != null ? conf.trustStoreType() : KeyStore.getDefaultType());
            } catch (GeneralSecurityException var33) {
               throw exceptionFactory.create("Failed to create keystore instance", "08000", var33);
            }

            int var49;
            if (conf.trustStore() != null) {
               Object inStream;
               try {
                  inStream = loadFromUrl(conf.trustStore());
               } catch (IOException var32) {
                  try {
                     inStream = new FileInputStream(conf.trustStore());
                  } catch (FileNotFoundException var31) {
                     throw new SQLException("Failed to find trustStore file. trustStore=" + conf.trustStore(), "08000", var31);
                  }
               }

               try {
                  ks.load((InputStream)inStream, conf.trustStorePassword() == null ? null : conf.trustStorePassword().toCharArray());
               } catch (NoSuchAlgorithmException | CertificateException | IOException var29) {
                  throw exceptionFactory.create("Failed load keyStore", "08000", var29);
               } finally {
                  try {
                     ((InputStream)inStream).close();
                  } catch (IOException var27) {
                  }

               }

               try {
                  TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                  tmf.init(ks);
                  TrustManager[] var48 = tmf.getTrustManagers();
                  var49 = var48.length;

                  for(int var52 = 0; var52 < var49; ++var52) {
                     TrustManager tm = var48[var52];
                     if (tm instanceof X509TrustManager) {
                        trustManager = new X509TrustManager[]{new MariaDbX509EphemeralTrustingManager((X509TrustManager)tm)};
                        break;
                     }
                  }
               } catch (GeneralSecurityException var35) {
                  throw exceptionFactory.create("Failed to load certificates from serverSslCert/trustStore", "08000", var35);
               }
            } else {
               try {
                  InputStream inStream = getInputStreamFromPath(conf.serverSslCert());

                  try {
                     ks.load((LoadStoreParameter)null);
                     CertificateFactory cf = CertificateFactory.getInstance("X.509");
                     Collection<? extends Certificate> caList = cf.generateCertificates(inStream);
                     Iterator var10 = caList.iterator();

                     while(var10.hasNext()) {
                        Certificate ca = (Certificate)var10.next();
                        ks.setCertificateEntry(UUID.randomUUID().toString(), ca);
                     }
                  } catch (Throwable var37) {
                     if (inStream != null) {
                        try {
                           inStream.close();
                        } catch (Throwable var28) {
                           var37.addSuppressed(var28);
                        }
                     }

                     throw var37;
                  }

                  if (inStream != null) {
                     inStream.close();
                  }
               } catch (IOException var38) {
                  throw exceptionFactory.create("Failed load keyStore", "08000", var38);
               } catch (GeneralSecurityException var39) {
                  throw exceptionFactory.create("Failed to store certificate from serverSslCert into a keyStore", "08000", var39);
               }

               try {
                  TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                  tmf.init(ks);
                  TrustManager[] var43 = tmf.getTrustManagers();
                  var47 = var43.length;

                  for(var49 = 0; var49 < var47; ++var49) {
                     TrustManager tm = var43[var49];
                     if (tm instanceof X509TrustManager) {
                        trustManager = new X509TrustManager[]{(X509TrustManager)tm};
                        break;
                     }
                  }
               } catch (GeneralSecurityException var36) {
                  throw exceptionFactory.create("Failed to load certificates from serverSslCert/trustStore", "08000", var36);
               }
            }
         }

         if (trustManager == null) {
            throw new SQLException("No X509TrustManager found");
         }
      }

      return trustManager;
   }

   public KeyManager[] getKeyManager(Configuration conf, ExceptionFactory exceptionFactory) throws SQLException {
      KeyManager[] keyManager = null;
      if (conf.keyStore() != null) {
         keyManager = new KeyManager[]{loadClientCerts(conf.keyStore(), conf.keyStorePassword(), conf.keyPassword(), conf.keyStoreType(), exceptionFactory)};
      } else if (conf.fallbackToSystemKeyStore()) {
         String keyStore = System.getProperty("javax.net.ssl.keyStore");
         String keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword", conf.keyStorePassword());
         String keyStoreType = System.getProperty("javax.net.ssl.keyStoreType", conf.keyStoreType());
         if (keyStore != null) {
            try {
               keyManager = new KeyManager[]{loadClientCerts(keyStore, keyStorePassword, keyStorePassword, keyStoreType, exceptionFactory)};
            } catch (SQLException var8) {
               keyManager = null;
               logger.error("Error loading key manager from system properties", (Throwable)var8);
            }
         }
      }

      return keyManager;
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
