package fr.xephi.authme.libs.de.rtner.security.auth.spi;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SimplePBKDF2 extends PBKDF2Engine {
   protected int saltSize;
   protected SecureRandom sr;
   protected PBKDF2Formatter formatter;

   public SimplePBKDF2() {
      this(8, 1000);
   }

   protected SimplePBKDF2(int saltSize, PBKDF2Parameters parameters) {
      super(parameters);
      this.saltSize = 8;
      this.setSaltSize(saltSize);
   }

   public SimplePBKDF2(int saltSize, int iterationCount) {
      this(saltSize, new PBKDF2Parameters("HmacSHA1", "ISO-8859-1", (byte[])null, iterationCount < 0 ? 0 : iterationCount));

      try {
         this.formatter = new PBKDF2HexFormatter();
         this.sr = SecureRandom.getInstance("SHA1PRNG");
      } catch (NoSuchAlgorithmException var4) {
      }

   }

   public PBKDF2Formatter getFormatter() {
      return this.formatter;
   }

   public void setFormatter(PBKDF2Formatter formatter) {
      this.formatter = formatter;
   }

   public int getSaltSize() {
      return this.saltSize;
   }

   public void setSaltSize(int saltSize) {
      if (saltSize <= 0) {
         throw new IllegalArgumentException("Salt size must be positive.");
      } else {
         this.saltSize = saltSize;
      }
   }

   public String deriveKeyFormatted(String inputPassword) {
      PBKDF2Parameters p = this.getParameters();
      byte[] salt = this.generateSalt();
      p.setSalt(salt);
      p.setDerivedKey(this.deriveKey(inputPassword));
      String formatted = this.getFormatter().toString(p);
      return formatted;
   }

   protected byte[] generateSalt() {
      byte[] salt = new byte[this.getSaltSize()];
      this.sr.nextBytes(salt);
      return salt;
   }

   public boolean verifyKeyFormatted(String formatted, String candidatePassword) {
      PBKDF2Parameters p = this.getParameters();
      PBKDF2Parameters q = new PBKDF2Parameters();
      q.hashAlgorithm = p.hashAlgorithm;
      q.hashCharset = p.hashCharset;
      boolean verifyOK = false;
      if (!this.getFormatter().fromString(q, formatted)) {
         try {
            this.setParameters(q);
            verifyOK = this.verifyKey(candidatePassword);
         } finally {
            this.setParameters(p);
         }
      }

      return verifyOK;
   }
}
