package fr.xephi.authme.libs.org.postgresql.ssl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public abstract class DbKeyStoreSocketFactory extends WrappedFactory {
   public DbKeyStoreSocketFactory() throws DbKeyStoreSocketFactory.DbKeyStoreSocketException {
      KeyStore keys;
      char[] password;
      try {
         keys = KeyStore.getInstance("JKS");
         password = this.getKeyStorePassword();
         keys.load(this.getKeyStoreStream(), password);
      } catch (GeneralSecurityException var7) {
         throw new DbKeyStoreSocketFactory.DbKeyStoreSocketException("Failed to load keystore: " + var7.getMessage());
      } catch (FileNotFoundException var8) {
         throw new DbKeyStoreSocketFactory.DbKeyStoreSocketException("Failed to find keystore file." + var8.getMessage());
      } catch (IOException var9) {
         throw new DbKeyStoreSocketFactory.DbKeyStoreSocketException("Failed to read keystore file: " + var9.getMessage());
      }

      try {
         KeyManagerFactory keyfact = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
         keyfact.init(keys, password);
         TrustManagerFactory trustfact = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
         trustfact.init(keys);
         SSLContext ctx = SSLContext.getInstance("SSL");
         ctx.init(keyfact.getKeyManagers(), trustfact.getTrustManagers(), (SecureRandom)null);
         this.factory = ctx.getSocketFactory();
      } catch (GeneralSecurityException var6) {
         throw new DbKeyStoreSocketFactory.DbKeyStoreSocketException("Failed to set up database socket factory: " + var6.getMessage());
      }
   }

   public abstract char[] getKeyStorePassword();

   public abstract InputStream getKeyStoreStream();

   public static class DbKeyStoreSocketException extends Exception {
      public DbKeyStoreSocketException(String message) {
         super(message);
      }
   }
}
