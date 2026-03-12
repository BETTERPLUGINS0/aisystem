package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2;

public class PKCS5S2ParametersGenerator extends PBEParametersGenerator {
   private Mac hMac;
   private byte[] state;

   public PKCS5S2ParametersGenerator(Digest digest) {
      this.hMac = new HMac(digest);
      this.state = new byte[this.hMac.getMacSize()];
   }

   private void F(byte[] S, int c, byte[] iBuf, byte[] out, int outOff) {
      if (c == 0) {
         throw new IllegalArgumentException("iteration count must be at least 1.");
      } else {
         if (S != null) {
            this.hMac.update(S, 0, S.length);
         }

         this.hMac.update(iBuf, 0, iBuf.length);
         this.hMac.doFinal(this.state, 0);
         System.arraycopy(this.state, 0, out, outOff, this.state.length);

         for(int count = 1; count < c; ++count) {
            this.hMac.update(this.state, 0, this.state.length);
            this.hMac.doFinal(this.state, 0);

            for(int j = 0; j != this.state.length; ++j) {
               out[outOff + j] ^= this.state[j];
            }
         }

      }
   }

   private byte[] generateDerivedKey(int dkLen) {
      int hLen = this.hMac.getMacSize();
      int l = (dkLen + hLen - 1) / hLen;
      byte[] iBuf = new byte[4];
      byte[] outBytes = new byte[l * hLen];
      int outPos = 0;
      CipherParameters param = new KeyParameter(this.password);
      this.hMac.init(param);

      for(int i = 1; i <= l; ++i) {
         for(int pos = 3; ++iBuf[pos] == 0; --pos) {
         }

         this.F(this.salt, this.iterationCount, iBuf, outBytes, outPos);
         outPos += hLen;
      }

      return outBytes;
   }

   public CipherParameters generateDerivedParameters(int keySize) {
      keySize /= 8;
      byte[] dKey = Arrays.copyOfRange(this.generateDerivedKey(keySize), 0, keySize);
      return new KeyParameter(dKey, 0, keySize);
   }
}
