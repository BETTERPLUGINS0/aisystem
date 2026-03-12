package fr.xephi.authme.libs.org.postgresql.ssl;

import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.jdbc.SslMode;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.ObjectFactory;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Locale;
import java.util.Properties;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LibPQFactory extends WrappedFactory {
   @Nullable
   KeyManager km;
   boolean defaultfile;

   private CallbackHandler getCallbackHandler(Properties info) throws PSQLException {
      String sslpasswordcallback = PGProperty.SSL_PASSWORD_CALLBACK.getOrDefault(info);
      Object cbh;
      if (sslpasswordcallback != null) {
         try {
            cbh = (CallbackHandler)ObjectFactory.instantiate(CallbackHandler.class, sslpasswordcallback, info, false, (String)null);
         } catch (Exception var5) {
            throw new PSQLException(GT.tr("The password callback class provided {0} could not be instantiated.", sslpasswordcallback), PSQLState.CONNECTION_FAILURE, var5);
         }
      } else {
         cbh = new LibPQFactory.ConsoleCallbackHandler(PGProperty.SSL_PASSWORD.getOrDefault(info));
      }

      return (CallbackHandler)cbh;
   }

   private void initPk8(String sslkeyfile, String defaultdir, Properties info) throws PSQLException {
      String sslcertfile = PGProperty.SSL_CERT.getOrDefault(info);
      if (sslcertfile == null) {
         this.defaultfile = true;
         sslcertfile = defaultdir + "postgresql.crt";
      }

      this.km = new LazyKeyManager("".equals(sslcertfile) ? null : sslcertfile, "".equals(sslkeyfile) ? null : sslkeyfile, this.getCallbackHandler(info), this.defaultfile);
   }

   private void initP12(String sslkeyfile, Properties info) throws PSQLException {
      this.km = new PKCS12KeyManager(sslkeyfile, this.getCallbackHandler(info));
   }

   public LibPQFactory(Properties info) throws PSQLException {
      try {
         SSLContext ctx = SSLContext.getInstance("TLS");
         String pathsep = System.getProperty("file.separator");
         String defaultdir;
         if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")) {
            defaultdir = System.getenv("APPDATA") + pathsep + "postgresql" + pathsep;
         } else {
            defaultdir = System.getProperty("user.home") + pathsep + ".postgresql" + pathsep;
         }

         String sslkeyfile = PGProperty.SSL_KEY.getOrDefault(info);
         if (sslkeyfile == null) {
            this.defaultfile = true;
            sslkeyfile = defaultdir + "postgresql.pk8";
         }

         if (!sslkeyfile.endsWith(".p12") && !sslkeyfile.endsWith(".pfx")) {
            this.initPk8(sslkeyfile, defaultdir, info);
         } else {
            this.initP12(sslkeyfile, info);
         }

         SslMode sslMode = SslMode.of(info);
         TrustManager[] tm;
         if (!sslMode.verifyCertificate()) {
            tm = new TrustManager[]{new NonValidatingFactory.NonValidatingTM()};
         } else {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");

            KeyStore ks;
            try {
               ks = KeyStore.getInstance("jks");
            } catch (KeyStoreException var29) {
               throw new NoSuchAlgorithmException("jks KeyStore not available");
            }

            String sslrootcertfile = PGProperty.SSL_ROOT_CERT.getOrDefault(info);
            if (sslrootcertfile == null) {
               sslrootcertfile = defaultdir + "root.crt";
            }

            FileInputStream fis;
            try {
               fis = new FileInputStream(sslrootcertfile);
            } catch (FileNotFoundException var28) {
               throw new PSQLException(GT.tr("Could not open SSL root certificate file {0}.", sslrootcertfile), PSQLState.CONNECTION_FAILURE, var28);
            }

            try {
               CertificateFactory cf = CertificateFactory.getInstance("X.509");
               Object[] certs = cf.generateCertificates(fis).toArray(new Certificate[0]);
               ks.load((InputStream)null, (char[])null);
               int i = 0;

               while(true) {
                  if (i >= certs.length) {
                     tmf.init(ks);
                     break;
                  }

                  ks.setCertificateEntry("cert" + i, (Certificate)certs[i]);
                  ++i;
               }
            } catch (IOException var30) {
               throw new PSQLException(GT.tr("Could not read SSL root certificate file {0}.", sslrootcertfile), PSQLState.CONNECTION_FAILURE, var30);
            } catch (GeneralSecurityException var31) {
               throw new PSQLException(GT.tr("Loading the SSL root certificate {0} into a TrustManager failed.", sslrootcertfile), PSQLState.CONNECTION_FAILURE, var31);
            } finally {
               try {
                  fis.close();
               } catch (IOException var26) {
               }

            }

            tm = tmf.getTrustManagers();
         }

         try {
            KeyManager km = this.km;
            ctx.init(km == null ? null : new KeyManager[]{km}, tm, (SecureRandom)null);
         } catch (KeyManagementException var27) {
            throw new PSQLException(GT.tr("Could not initialize SSL context."), PSQLState.CONNECTION_FAILURE, var27);
         }

         this.factory = ctx.getSocketFactory();
      } catch (NoSuchAlgorithmException var33) {
         throw new PSQLException(GT.tr("Could not find a java cryptographic algorithm: {0}.", var33.getMessage()), PSQLState.CONNECTION_FAILURE, var33);
      }
   }

   public void throwKeyManagerException() throws PSQLException {
      if (this.km != null) {
         if (this.km instanceof LazyKeyManager) {
            ((LazyKeyManager)this.km).throwKeyManagerException();
         }

         if (this.km instanceof PKCS12KeyManager) {
            ((PKCS12KeyManager)this.km).throwKeyManagerException();
         }
      }

   }

   public static class ConsoleCallbackHandler implements CallbackHandler {
      @Nullable
      private char[] password;

      ConsoleCallbackHandler(@Nullable String password) {
         if (password != null) {
            this.password = password.toCharArray();
         }

      }

      public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
         Console cons = System.console();
         char[] password = this.password;
         if (cons == null && password == null) {
            throw new UnsupportedCallbackException(callbacks[0], "Console is not available");
         } else {
            Callback[] var4 = callbacks;
            int var5 = callbacks.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Callback callback = var4[var6];
               if (!(callback instanceof PasswordCallback)) {
                  throw new UnsupportedCallbackException(callback);
               }

               PasswordCallback pwdCallback = (PasswordCallback)callback;
               if (password != null) {
                  pwdCallback.setPassword(password);
               } else {
                  pwdCallback.setPassword(((Console)Nullness.castNonNull(cons, "System.console()")).readPassword("%s", new Object[]{pwdCallback.getPrompt()}));
               }
            }

         }
      }
   }
}
