package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import fr.xephi.authme.libs.org.jboss.crypto.digest.DigestCallback;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.security.auth.callback.Callback;

public class RFC2617Digest implements DigestCallback {
   public static final String REALM = "realm";
   public static final String USERNAME = "username";
   public static final String DIGEST_URI = "digest-uri";
   public static final String NONCE = "nonce";
   public static final String CNONCE = "cnonce";
   public static final String NONCE_COUNT = "nc";
   public static final String QOP = "qop";
   public static final String ALGORITHM = "algorithm";
   public static final String AUTH_PARAM = "auth-param";
   public static final String METHOD = "method";
   public static final String A2HASH = "a2hash";
   private static char[] MD5_HEX = "0123456789abcdef".toCharArray();
   private MapCallback info;
   private String username;
   private String password;
   private boolean passwordIsA1Hash;
   String rfc2617;

   public void init(Map options) {
      this.username = (String)options.get("javax.security.auth.login.name");
      this.password = (String)options.get("javax.security.auth.login.password");
      String flag = (String)options.get("passwordIsA1Hash");
      if (flag != null) {
         this.passwordIsA1Hash = Boolean.valueOf(flag);
      }

      this.info = new MapCallback();
      Callback[] callbacks = new Callback[]{this.info};
      options.put("callbacks", callbacks);
   }

   public void preDigest(MessageDigest digest) {
   }

   public void postDigest(MessageDigest digest) {
      String qop = (String)this.info.getInfo("qop");
      String realm = (String)this.info.getInfo("realm");
      String algorithm = (String)this.info.getInfo("algorithm");
      String nonce = (String)this.info.getInfo("nonce");
      String cnonce = (String)this.info.getInfo("cnonce");
      String method = (String)this.info.getInfo("method");
      String nc = (String)this.info.getInfo("nc");
      String digestURI = (String)this.info.getInfo("digest-uri");
      if (algorithm == null) {
         algorithm = digest.getAlgorithm();
      }

      digest.reset();
      String hA1 = null;
      String hA2;
      if (algorithm != null && !algorithm.equals("MD5")) {
         if (!algorithm.equals("MD5-sess")) {
            throw PicketBoxMessages.MESSAGES.unsupportedAlgorithm(algorithm);
         }

         if (this.passwordIsA1Hash) {
            hA1 = this.password + ":" + nonce + ":" + cnonce;
         } else {
            hA2 = this.username + ":" + realm + ":" + this.password;
            hA1 = H(hA2, digest) + ":" + nonce + ":" + cnonce;
         }
      } else if (this.passwordIsA1Hash) {
         hA1 = this.password;
      } else {
         hA2 = this.username + ":" + realm + ":" + this.password;
         hA1 = H(hA2, digest);
      }

      hA2 = (String)this.info.getInfo("a2hash");
      String extra;
      if (hA2 == null) {
         extra = null;
         if (qop != null && !qop.equals("auth")) {
            throw PicketBoxMessages.MESSAGES.unsupportedQOP(qop);
         }

         extra = method + ":" + digestURI;
         hA2 = H(extra, digest);
      }

      if (qop == null) {
         extra = nonce + ":" + hA2;
         KD(hA1, extra, digest);
      } else if (qop.equals("auth")) {
         extra = nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + hA2;
         KD(hA1, extra, digest);
      }

   }

   public String getInfoDigest(MessageDigest digest) {
      if (this.rfc2617 == null) {
         byte[] data = digest.digest();
         this.rfc2617 = cvtHex(data);
      }

      return this.rfc2617;
   }

   private static String H(String data, MessageDigest digest) {
      digest.reset();
      byte[] x = digest.digest(data.getBytes());
      return cvtHex(x);
   }

   private static void KD(String secret, String data, MessageDigest digest) {
      String x = secret + ":" + data;
      digest.reset();
      digest.update(x.getBytes());
   }

   static String cvtHex(byte[] data) {
      char[] hash = new char[32];

      for(int i = 0; i < 16; ++i) {
         int j = data[i] >> 4 & 15;
         hash[i * 2] = MD5_HEX[j];
         j = data[i] & 15;
         hash[i * 2 + 1] = MD5_HEX[j];
      }

      return new String(hash);
   }

   public static void main(String[] args) throws NoSuchAlgorithmException {
      if (args.length != 3) {
         System.err.println("Usage: RFC2617Digest username realm password");
         System.err.println(" - username : the username");
         System.err.println(" - realm : the web app realm name");
         System.err.println(" - password : the plain text password");
         System.exit(1);
      }

      String username = args[0];
      String realm = args[1];
      String password = args[2];
      String A1 = username + ":" + realm + ":" + password;
      MessageDigest digest = MessageDigest.getInstance("MD5");
      String hA1 = H(A1, digest);
      System.out.println("RFC2617 A1 hash: " + hA1);
   }
}
