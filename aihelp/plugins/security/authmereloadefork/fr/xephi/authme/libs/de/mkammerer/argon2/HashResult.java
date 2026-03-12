package fr.xephi.authme.libs.de.mkammerer.argon2;

public class HashResult {
   private final byte[] raw;
   private final String encoded;

   public HashResult(byte[] raw, String encoded) {
      this.raw = raw;
      this.encoded = encoded;
   }

   public byte[] getRaw() {
      return this.raw;
   }

   public String getEncoded() {
      return this.encoded;
   }
}
