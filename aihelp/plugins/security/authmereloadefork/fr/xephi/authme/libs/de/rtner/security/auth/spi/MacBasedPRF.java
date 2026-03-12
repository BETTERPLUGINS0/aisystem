package fr.xephi.authme.libs.de.rtner.security.auth.spi;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MacBasedPRF implements PRF {
   protected Mac mac;
   protected int hLen;
   protected String macAlgorithm;

   public MacBasedPRF(String macAlgorithm) {
      this.macAlgorithm = macAlgorithm;

      try {
         this.mac = Mac.getInstance(macAlgorithm);
         this.hLen = this.mac.getMacLength();
      } catch (NoSuchAlgorithmException var3) {
         throw new RuntimeException(var3);
      }
   }

   public MacBasedPRF(String macAlgorithm, String provider) {
      this.macAlgorithm = macAlgorithm;

      try {
         this.mac = Mac.getInstance(macAlgorithm, provider);
         this.hLen = this.mac.getMacLength();
      } catch (NoSuchAlgorithmException var4) {
         throw new RuntimeException(var4);
      } catch (NoSuchProviderException var5) {
         throw new RuntimeException(var5);
      }
   }

   public byte[] doFinal(byte[] M) {
      byte[] r = this.mac.doFinal(M);
      return r;
   }

   public int getHLen() {
      return this.hLen;
   }

   public void init(byte[] P) {
      try {
         this.mac.init(new SecretKeySpec(P, this.macAlgorithm));
      } catch (InvalidKeyException var3) {
         throw new RuntimeException(var3);
      }
   }
}
