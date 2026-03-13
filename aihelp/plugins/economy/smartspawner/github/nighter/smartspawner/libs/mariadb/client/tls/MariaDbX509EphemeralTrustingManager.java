package github.nighter.smartspawner.libs.mariadb.client.tls;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class MariaDbX509EphemeralTrustingManager implements X509TrustManager {
   X509TrustManager internal;
   byte[] fingerprint = null;

   public MariaDbX509EphemeralTrustingManager(X509TrustManager javaTrustManager) {
      this.internal = javaTrustManager;
   }

   public void checkClientTrusted(X509Certificate[] x509Certificates, String string) throws CertificateException {
      this.internal.checkClientTrusted(x509Certificates, string);
   }

   public void checkServerTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
      try {
         this.internal.checkServerTrusted(x509Certificates, authType);
      } catch (CertificateExpiredException var6) {
         throw var6;
      } catch (CertificateException var7) {
         if (x509Certificates == null || x509Certificates.length < 1) {
            throw var7;
         }

         try {
            this.fingerprint = getThumbprint(x509Certificates[0], "SHA-256");
         } catch (CertificateEncodingException | NoSuchAlgorithmException var5) {
            throw var7;
         }
      }

   }

   public byte[] getFingerprint() {
      return this.fingerprint;
   }

   private static byte[] getThumbprint(X509Certificate cert, String algorithm) throws NoSuchAlgorithmException, CertificateEncodingException {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      byte[] der = cert.getEncoded();
      md.update(der);
      return md.digest();
   }

   public X509Certificate[] getAcceptedIssuers() {
      return null;
   }
}
