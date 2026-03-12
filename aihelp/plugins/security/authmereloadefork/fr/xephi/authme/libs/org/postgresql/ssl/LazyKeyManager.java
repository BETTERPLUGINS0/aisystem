package fr.xephi.authme.libs.org.postgresql.ssl;

import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collection;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.ssl.X509KeyManager;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.x500.X500Principal;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LazyKeyManager implements X509KeyManager {
   @Nullable
   private X509Certificate[] cert;
   @Nullable
   private PrivateKey key;
   @Nullable
   private final String certfile;
   @Nullable
   private final String keyfile;
   private final CallbackHandler cbh;
   private final boolean defaultfile;
   @Nullable
   private PSQLException error;

   public LazyKeyManager(@Nullable String certfile, @Nullable String keyfile, CallbackHandler cbh, boolean defaultfile) {
      this.certfile = certfile;
      this.keyfile = keyfile;
      this.cbh = cbh;
      this.defaultfile = defaultfile;
   }

   public void throwKeyManagerException() throws PSQLException {
      if (this.error != null) {
         throw this.error;
      }
   }

   @Nullable
   public String chooseClientAlias(String[] keyType, @Nullable Principal[] issuers, @Nullable Socket socket) {
      if (this.certfile == null) {
         return null;
      } else if (issuers != null && issuers.length != 0) {
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
               Principal[] var14 = issuers;
               var11 = issuers.length;

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
   public String chooseServerAlias(String keyType, @Nullable Principal[] issuers, @Nullable Socket socket) {
      return null;
   }

   @Nullable
   public X509Certificate[] getCertificateChain(String alias) {
      if (this.cert == null && this.certfile != null) {
         CertificateFactory cf;
         try {
            cf = CertificateFactory.getInstance("X.509");
         } catch (CertificateException var18) {
            this.error = new PSQLException(GT.tr("Could not find a java cryptographic algorithm: X.509 CertificateFactory not available."), PSQLState.CONNECTION_FAILURE, var18);
            return null;
         }

         FileInputStream certfileStream = null;

         Collection certs;
         try {
            label168: {
               Object var6;
               try {
                  certfileStream = new FileInputStream(this.certfile);
                  certs = cf.generateCertificates(certfileStream);
                  break label168;
               } catch (FileNotFoundException var20) {
                  if (!this.defaultfile) {
                     this.error = new PSQLException(GT.tr("Could not open SSL certificate file {0}.", this.certfile), PSQLState.CONNECTION_FAILURE, var20);
                  }
               } catch (CertificateException var21) {
                  this.error = new PSQLException(GT.tr("Loading the SSL certificate {0} into a KeyManager failed.", this.certfile), PSQLState.CONNECTION_FAILURE, var21);
                  var6 = null;
                  return (X509Certificate[])var6;
               }

               var6 = null;
               return (X509Certificate[])var6;
            }
         } finally {
            if (certfileStream != null) {
               try {
                  certfileStream.close();
               } catch (IOException var19) {
                  if (!this.defaultfile) {
                     this.error = new PSQLException(GT.tr("Could not close SSL certificate file {0}.", this.certfile), PSQLState.CONNECTION_FAILURE, var19);
                  }
               }
            }

         }

         this.cert = (X509Certificate[])certs.toArray(new X509Certificate[0]);
      }

      return this.cert;
   }

   @Nullable
   public String[] getClientAliases(String keyType, @Nullable Principal[] issuers) {
      String alias = this.chooseClientAlias(new String[]{keyType}, issuers, (Socket)null);
      return alias == null ? new String[0] : new String[]{alias};
   }

   private static byte[] readFileFully(String path) throws IOException {
      RandomAccessFile raf = new RandomAccessFile(path, "r");

      byte[] var3;
      try {
         byte[] ret = new byte[(int)raf.length()];
         raf.readFully(ret);
         var3 = ret;
      } finally {
         raf.close();
      }

      return var3;
   }

   @Nullable
   public PrivateKey getPrivateKey(String alias) {
      try {
         if (this.key == null && this.keyfile != null) {
            X509Certificate[] cert = this.getCertificateChain("user");
            if (cert == null || cert.length == 0) {
               return null;
            }

            byte[] keydata;
            try {
               keydata = readFileFully(this.keyfile);
            } catch (FileNotFoundException var18) {
               if (!this.defaultfile) {
                  throw var18;
               }

               return null;
            }

            KeyFactory kf = KeyFactory.getInstance(cert[0].getPublicKey().getAlgorithm());

            try {
               KeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keydata);
               this.key = kf.generatePrivate(pkcs8KeySpec);
            } catch (InvalidKeySpecException var17) {
               EncryptedPrivateKeyInfo ePKInfo = new EncryptedPrivateKeyInfo(keydata);

               Cipher cipher;
               try {
                  cipher = Cipher.getInstance(ePKInfo.getAlgName());
               } catch (NoSuchPaddingException var15) {
                  throw new NoSuchAlgorithmException(var15.getMessage(), var15);
               }

               PasswordCallback pwdcb = new PasswordCallback(GT.tr("Enter SSL password: "), false);

               try {
                  this.cbh.handle(new Callback[]{pwdcb});
               } catch (UnsupportedCallbackException var16) {
                  if (this.cbh instanceof LibPQFactory.ConsoleCallbackHandler && "Console is not available".equals(var16.getMessage())) {
                     this.error = new PSQLException(GT.tr("Could not read password for SSL key file, console is not available."), PSQLState.CONNECTION_FAILURE, var16);
                  } else {
                     this.error = new PSQLException(GT.tr("Could not read password for SSL key file by callbackhandler {0}.", this.cbh.getClass().getName()), PSQLState.CONNECTION_FAILURE, var16);
                  }

                  return null;
               }

               try {
                  PBEKeySpec pbeKeySpec = new PBEKeySpec(pwdcb.getPassword());
                  pwdcb.clearPassword();
                  SecretKeyFactory skFac = SecretKeyFactory.getInstance(ePKInfo.getAlgName());
                  Key pbeKey = skFac.generateSecret(pbeKeySpec);
                  AlgorithmParameters algParams = ePKInfo.getAlgParameters();
                  cipher.init(2, pbeKey, algParams);
                  KeySpec pkcs8KeySpec = ePKInfo.getKeySpec(cipher);
                  this.key = kf.generatePrivate(pkcs8KeySpec);
               } catch (GeneralSecurityException var14) {
                  this.error = new PSQLException(GT.tr("Could not decrypt SSL key file {0}.", this.keyfile), PSQLState.CONNECTION_FAILURE, var14);
                  return null;
               }
            }
         }
      } catch (IOException var19) {
         this.error = new PSQLException(GT.tr("Could not read SSL key file {0}.", this.keyfile), PSQLState.CONNECTION_FAILURE, var19);
      } catch (NoSuchAlgorithmException var20) {
         this.error = new PSQLException(GT.tr("Could not find a java cryptographic algorithm: {0}.", var20.getMessage()), PSQLState.CONNECTION_FAILURE, var20);
         return null;
      }

      return this.key;
   }

   @Nullable
   public String[] getServerAliases(String keyType, @Nullable Principal[] issuers) {
      return new String[0];
   }
}
