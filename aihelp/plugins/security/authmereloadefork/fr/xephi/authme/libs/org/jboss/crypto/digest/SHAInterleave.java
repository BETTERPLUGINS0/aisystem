package fr.xephi.authme.libs.org.jboss.crypto.digest;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;

public class SHAInterleave extends MessageDigestSpi {
   private static final int SHA_HASH_LEN = 20;
   private ByteArrayOutputStream evenBytes;
   private ByteArrayOutputStream oddBytes;
   private int count;
   private boolean skipLeadingZeros;
   private MessageDigest sha;

   public SHAInterleave() {
      try {
         this.sha = MessageDigest.getInstance("SHA");
      } catch (NoSuchAlgorithmException var2) {
         throw PicketBoxMessages.MESSAGES.failedToObtainSHAMessageDigest(var2);
      }

      this.evenBytes = new ByteArrayOutputStream();
      this.oddBytes = new ByteArrayOutputStream();
      this.engineReset();
   }

   protected int engineGetDigestLength() {
      return 40;
   }

   protected byte[] engineDigest() {
      byte[] E = this.evenBytes.toByteArray();
      byte[] G = this.sha.digest(E);
      byte[] F = this.oddBytes.toByteArray();
      int offset = 0;
      if (this.count % 2 == 1) {
         offset = 1;
      }

      this.sha.reset();
      this.sha.update(F, offset, F.length - offset);
      byte[] H = this.sha.digest();
      int length = G.length + H.length;
      byte[] digest = new byte[length];

      int i;
      for(i = 0; i < G.length; ++i) {
         digest[2 * i] = G[i];
      }

      for(i = 0; i < H.length; ++i) {
         digest[2 * i + 1] = H[i];
      }

      this.engineReset();
      return digest;
   }

   protected void engineReset() {
      this.skipLeadingZeros = true;
      this.count = 0;
      this.evenBytes.reset();
      this.oddBytes.reset();
      this.sha.reset();
   }

   protected void engineUpdate(byte input) {
      if (!this.skipLeadingZeros || input != 0) {
         this.skipLeadingZeros = false;
         if (this.count % 2 == 0) {
            this.evenBytes.write(input);
         } else {
            this.oddBytes.write(input);
         }

         ++this.count;
      }
   }

   protected void engineUpdate(byte[] input, int offset, int len) {
      for(int i = offset; i < offset + len; ++i) {
         this.engineUpdate(input[i]);
      }

   }
}
