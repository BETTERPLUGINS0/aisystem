package fr.xephi.authme.libs.org.postgresql.ssl;

import fr.xephi.authme.libs.org.postgresql.jdbc.ResourceLock;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.ProtectionParameter;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509KeyManager;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.x500.X500Principal;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PKCS12KeyManager implements X509KeyManager {
   private final CallbackHandler cbh;
   @Nullable
   private PSQLException error;
   private final String keyfile;
   private final KeyStore keyStore;
   boolean keystoreLoaded;
   private final ResourceLock lock = new ResourceLock();

   public PKCS12KeyManager(String pkcsFile, CallbackHandler cbh) throws PSQLException {
      try {
         this.keyStore = KeyStore.getInstance("pkcs12");
         this.keyfile = pkcsFile;
         this.cbh = cbh;
      } catch (KeyStoreException var4) {
         throw new PSQLException(GT.tr("Unable to find pkcs12 keystore."), PSQLState.CONNECTION_FAILURE, var4);
      }
   }

   public void throwKeyManagerException() throws PSQLException {
      if (this.error != null) {
         throw this.error;
      }
   }

   @Nullable
   public String[] getClientAliases(String keyType, @Nullable Principal[] principals) {
      String alias = this.chooseClientAlias(new String[]{keyType}, principals, (Socket)null);
      return alias == null ? null : new String[]{alias};
   }

   @Nullable
   public String chooseClientAlias(String[] keyType, @Nullable Principal[] principals, @Nullable Socket socket) {
      if (principals != null && principals.length != 0) {
         X509Certificate[] certchain = this.getCertificateChain("user");
         if (certchain == null) {
            return null;
         } else {
            X509Certificate cert = certchain[certchain.length - 1];
            X500Principal ourissuer = cert.getIssuerX500Principal();
            String certKeyType = cert.getPublicKey().getAlgorithm();
            boolean keyTypeFound = false;
            boolean found = false;
            int var11;
            int var12;
            if (keyType != null && keyType.length > 0) {
               String[] var10 = keyType;
               var11 = keyType.length;

               for(var12 = 0; var12 < var11; ++var12) {
                  String kt = var10[var12];
                  if (kt.equalsIgnoreCase(certKeyType)) {
                     keyTypeFound = true;
                  }
               }
            } else {
               keyTypeFound = true;
            }

            if (keyTypeFound) {
               Principal[] var14 = principals;
               var11 = principals.length;

               for(var12 = 0; var12 < var11; ++var12) {
                  Principal issuer = var14[var12];
                  if (ourissuer.equals(issuer)) {
                     found = keyTypeFound;
                  }
               }
            }

            return found ? "user" : null;
         }
      } else {
         return "user";
      }
   }

   @Nullable
   public String[] getServerAliases(String s, @Nullable Principal[] principals) {
      return new String[0];
   }

   @Nullable
   public String chooseServerAlias(String s, @Nullable Principal[] principals, @Nullable Socket socket) {
      return null;
   }

   @Nullable
   public X509Certificate[] getCertificateChain(String alias) {
      try {
         this.loadKeyStore();
         Certificate[] certs = this.keyStore.getCertificateChain(alias);
         if (certs == null) {
            return null;
         } else {
            X509Certificate[] x509Certificates = new X509Certificate[certs.length];
            int i = 0;
            Certificate[] var5 = certs;
            int var6 = certs.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Certificate cert = var5[var7];
               x509Certificates[i++] = (X509Certificate)cert;
            }

            return x509Certificates;
         }
      } catch (Exception var9) {
         this.error = new PSQLException(GT.tr("Could not find a java cryptographic algorithm: X.509 CertificateFactory not available."), PSQLState.CONNECTION_FAILURE, var9);
         return null;
      }
   }

   @Nullable
   public PrivateKey getPrivateKey(String s) {
      try {
         this.loadKeyStore();
         PasswordCallback pwdcb = new PasswordCallback(GT.tr("Enter SSL password: "), false);
         this.cbh.handle(new Callback[]{pwdcb});
         ProtectionParameter protParam = new PasswordProtection(pwdcb.getPassword());
         PrivateKeyEntry pkEntry = (PrivateKeyEntry)this.keyStore.getEntry("user", protParam);
         return pkEntry == null ? null : pkEntry.getPrivateKey();
      } catch (Exception var5) {
         this.error = new PSQLException(GT.tr("Could not read SSL key file {0}.", this.keyfile), PSQLState.CONNECTION_FAILURE, var5);
         return null;
      }
   }

   private void loadKeyStore() throws Exception {
      ResourceLock ignore = this.lock.obtain();

      label56: {
         try {
            if (this.keystoreLoaded) {
               break label56;
            }

            PasswordCallback pwdcb = new PasswordCallback(GT.tr("Enter SSL password: "), false);

            try {
               this.cbh.handle(new Callback[]{pwdcb});
            } catch (UnsupportedCallbackException var5) {
               if (this.cbh instanceof LibPQFactory.ConsoleCallbackHandler && "Console is not available".equals(var5.getMessage())) {
                  this.error = new PSQLException(GT.tr("Could not read password for SSL key file, console is not available."), PSQLState.CONNECTION_FAILURE, var5);
               } else {
                  this.error = new PSQLException(GT.tr("Could not read password for SSL key file by callbackhandler {0}.", this.cbh.getClass().getName()), PSQLState.CONNECTION_FAILURE, var5);
               }
            }

            this.keyStore.load(new FileInputStream(this.keyfile), pwdcb.getPassword());
            this.keystoreLoaded = true;
         } catch (Throwable var6) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var4) {
                  var6.addSuppressed(var4);
               }
            }

            throw var6;
         }

         if (ignore != null) {
            ignore.close();
         }

         return;
      }

      if (ignore != null) {
         ignore.close();
      }

   }
}
