package fr.xephi.authme.libs.org.picketbox.util;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

public class KeyStoreUtil {
   public static KeyStore getKeyStore(File keyStoreFile, char[] storePass) throws GeneralSecurityException, IOException {
      return getKeyStore(KeyStore.getDefaultType(), keyStoreFile, storePass);
   }

   public static KeyStore getKeyStore(String fileURL, char[] storePass) throws GeneralSecurityException, IOException {
      return getKeyStore(KeyStore.getDefaultType(), fileURL, storePass);
   }

   public static KeyStore getKeyStore(URL url, char[] storePass) throws GeneralSecurityException, IOException {
      return getKeyStore(KeyStore.getDefaultType(), url, storePass);
   }

   public static KeyStore getKeyStore(InputStream ksStream, char[] storePass) throws GeneralSecurityException, IOException {
      return getKeyStore(KeyStore.getDefaultType(), ksStream, storePass);
   }

   public static KeyStore getKeyStore(String keyStoreType, File keyStoreFile, char[] storePass) throws GeneralSecurityException, IOException {
      FileInputStream fis = null;

      KeyStore var4;
      try {
         fis = new FileInputStream(keyStoreFile);
         var4 = getKeyStore(keyStoreType, (InputStream)fis, storePass);
      } finally {
         safeClose((InputStream)fis);
      }

      return var4;
   }

   public static KeyStore getKeyStore(String keyStoreType, String fileURL, char[] storePass) throws GeneralSecurityException, IOException {
      if (fileURL == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("fileURL");
      } else {
         File file = new File(fileURL);
         FileInputStream fis = null;

         KeyStore var5;
         try {
            fis = new FileInputStream(file);
            var5 = getKeyStore(keyStoreType, (InputStream)fis, storePass);
         } finally {
            safeClose((InputStream)fis);
         }

         return var5;
      }
   }

   public static KeyStore getKeyStore(String keyStoreType, URL url, char[] storePass) throws GeneralSecurityException, IOException {
      if (url == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("url");
      } else {
         InputStream is = null;

         KeyStore var4;
         try {
            is = url.openStream();
            var4 = getKeyStore(keyStoreType, is, storePass);
         } finally {
            safeClose(is);
         }

         return var4;
      }
   }

   public static KeyStore getKeyStore(String keyStoreType, InputStream ksStream, char[] storePass) throws GeneralSecurityException, IOException {
      if (ksStream == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("ksStream");
      } else {
         KeyStore ks = KeyStore.getInstance(keyStoreType == null ? KeyStore.getDefaultType() : keyStoreType);
         ks.load(ksStream, storePass);
         return ks;
      }
   }

   public static KeyPair generateKeyPair(String algo) throws GeneralSecurityException {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance(algo);
      return kpg.genKeyPair();
   }

   public static PublicKey getPublicKey(KeyStore ks, String alias, char[] password) throws KeyStoreException, NoSuchAlgorithmException, GeneralSecurityException {
      PublicKey publicKey = null;
      Key key = ks.getKey(alias, password);
      Certificate cert;
      if (key instanceof PrivateKey) {
         cert = ks.getCertificate(alias);
         publicKey = cert.getPublicKey();
      }

      if (publicKey == null) {
         cert = ks.getCertificate(alias);
         if (cert != null) {
            publicKey = cert.getPublicKey();
         }
      }

      return publicKey;
   }

   public static void addCertificate(File keystoreFile, char[] storePass, String alias, Certificate cert) throws GeneralSecurityException, IOException {
      addCertificate(KeyStore.getDefaultType(), keystoreFile, storePass, alias, cert);
   }

   public static void addCertificate(String keyStoreType, File keystoreFile, char[] storePass, String alias, Certificate cert) throws GeneralSecurityException, IOException {
      KeyStore keystore = getKeyStore(keyStoreType, keystoreFile, storePass);
      keystore.setCertificateEntry(alias, cert);
      FileOutputStream out = null;

      try {
         out = new FileOutputStream(keystoreFile);
         keystore.store(out, storePass);
         out.close();
      } finally {
         safeClose((OutputStream)out);
      }

   }

   public static KeyPair getPrivateKey(KeyStore keystore, String alias, char[] password) throws Exception {
      Key key = keystore.getKey(alias, password);
      if (key instanceof PrivateKey) {
         Certificate cert = keystore.getCertificate(alias);
         PublicKey publicKey = cert.getPublicKey();
         return new KeyPair(publicKey, (PrivateKey)key);
      } else {
         return null;
      }
   }

   public static KeyStore createKeyStore(String keyStoreType, char[] keyStorePWD) throws Exception {
      KeyStore ks = KeyStore.getInstance(keyStoreType);
      ks.load((InputStream)null, keyStorePWD);
      return ks;
   }

   private static void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var2) {
      }

   }

   private static void safeClose(OutputStream os) {
      try {
         if (os != null) {
            os.close();
         }
      } catch (Exception var2) {
      }

   }
}
