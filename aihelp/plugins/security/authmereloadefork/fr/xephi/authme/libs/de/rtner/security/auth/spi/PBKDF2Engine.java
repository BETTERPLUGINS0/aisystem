package fr.xephi.authme.libs.de.rtner.security.auth.spi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class PBKDF2Engine implements PBKDF2 {
   protected PBKDF2Parameters parameters;
   protected PRF prf;

   public PBKDF2Engine() {
      this.parameters = null;
      this.prf = null;
   }

   public PBKDF2Engine(PBKDF2Parameters parameters) {
      this.parameters = parameters;
      this.prf = null;
   }

   public PBKDF2Engine(PBKDF2Parameters parameters, PRF prf) {
      this.parameters = parameters;
      this.prf = prf;
   }

   public byte[] deriveKey(String inputPassword) {
      return this.deriveKey(inputPassword, 0);
   }

   public byte[] deriveKey(String inputPassword, int dkLen) {
      byte[] r = null;
      byte[] P = null;
      String charset = this.parameters.getHashCharset();
      if (inputPassword == null) {
         inputPassword = "";
      }

      byte[] P;
      try {
         if (charset == null) {
            P = inputPassword.getBytes();
         } else {
            P = inputPassword.getBytes(charset);
         }
      } catch (UnsupportedEncodingException var7) {
         throw new RuntimeException(var7);
      }

      this.assertPRF(P);
      if (dkLen == 0) {
         dkLen = this.prf.getHLen();
      }

      byte[] r = this.PBKDF2(this.prf, this.parameters.getSalt(), this.parameters.getIterationCount(), dkLen);
      return r;
   }

   public boolean verifyKey(String inputPassword) {
      byte[] referenceKey = this.getParameters().getDerivedKey();
      if (referenceKey != null && referenceKey.length != 0) {
         byte[] inputKey = this.deriveKey(inputPassword, referenceKey.length);
         if (inputKey != null && inputKey.length == referenceKey.length) {
            int z = 0;

            for(int i = 0; i < inputKey.length; ++i) {
               z |= inputKey[i] ^ referenceKey[i];
            }

            return z == 0;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   protected void assertPRF(byte[] P) {
      if (this.prf == null) {
         this.prf = new MacBasedPRF(this.parameters.getHashAlgorithm());
      }

      this.prf.init(P);
   }

   public PRF getPseudoRandomFunction() {
      return this.prf;
   }

   protected byte[] PBKDF2(PRF prf, byte[] S, int c, int dkLen) {
      if (S == null) {
         S = new byte[0];
      }

      int hLen = prf.getHLen();
      int l = this.ceil(dkLen, hLen);
      int r = dkLen - (l - 1) * hLen;
      byte[] T = new byte[l * hLen];
      int ti_offset = 0;

      for(int i = 1; i <= l; ++i) {
         this._F(T, ti_offset, prf, S, c, i);
         ti_offset += hLen;
      }

      if (r < hLen) {
         byte[] DK = new byte[dkLen];
         System.arraycopy(T, 0, DK, 0, dkLen);
         return DK;
      } else {
         return T;
      }
   }

   protected int ceil(int a, int b) {
      int m = 0;
      if (a % b > 0) {
         m = 1;
      }

      return a / b + m;
   }

   protected void _F(byte[] dest, int offset, PRF prf, byte[] S, int c, int blockIndex) {
      int hLen = prf.getHLen();
      byte[] U_r = new byte[hLen];
      byte[] U_i = new byte[S.length + 4];
      System.arraycopy(S, 0, U_i, 0, S.length);
      this.INT(U_i, S.length, blockIndex);

      for(int i = 0; i < c; ++i) {
         U_i = prf.doFinal(U_i);
         this.xor(U_r, U_i);
      }

      System.arraycopy(U_r, 0, dest, offset, hLen);
   }

   protected void xor(byte[] dest, byte[] src) {
      for(int i = 0; i < dest.length; ++i) {
         dest[i] ^= src[i];
      }

   }

   protected void INT(byte[] dest, int offset, int i) {
      dest[offset + 0] = (byte)(i / 16777216);
      dest[offset + 1] = (byte)(i / 65536);
      dest[offset + 2] = (byte)(i / 256);
      dest[offset + 3] = (byte)i;
   }

   public PBKDF2Parameters getParameters() {
      return this.parameters;
   }

   public void setParameters(PBKDF2Parameters parameters) {
      this.parameters = parameters;
   }

   public void setPseudoRandomFunction(PRF prf) {
      this.prf = prf;
   }

   public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
      String password = "password";
      String candidate = null;
      PBKDF2Formatter formatter = new PBKDF2HexFormatter();
      int iterations = 1000;
      if (args.length >= 2 && args[0].equals("-i")) {
         iterations = Integer.parseInt(args[1]);
         args = (String[])Arrays.copyOfRange(args, 2, args.length);
      }

      if (args.length >= 1) {
         password = args[0];
      }

      if (args.length >= 2) {
         candidate = args[1];
      }

      if (candidate == null) {
         SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
         byte[] salt = new byte[8];
         sr.nextBytes(salt);
         PBKDF2Parameters p = new PBKDF2Parameters("HmacSHA1", "ISO-8859-1", salt, iterations);
         PBKDF2Engine e = new PBKDF2Engine(p);
         p.setDerivedKey(e.deriveKey(password));
         candidate = formatter.toString(p);
         System.out.println(candidate);
      } else {
         PBKDF2Parameters p = new PBKDF2Parameters();
         p.setHashAlgorithm("HmacSHA1");
         p.setHashCharset("ISO-8859-1");
         if (formatter.fromString(p, candidate)) {
            throw new IllegalArgumentException("Candidate data does not have correct format (\"" + candidate + "\")");
         }

         PBKDF2Engine e = new PBKDF2Engine(p);
         boolean verifyOK = p.getIterationCount() >= iterations && e.verifyKey(password);
         System.out.println(verifyOK ? "OK" : "FAIL");
         System.exit(verifyOK ? 0 : 1);
      }

   }
}
