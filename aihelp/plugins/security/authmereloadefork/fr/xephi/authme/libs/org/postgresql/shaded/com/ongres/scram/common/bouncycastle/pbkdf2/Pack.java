package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2;

public abstract class Pack {
   public static int bigEndianToInt(byte[] bs, int off) {
      int n = bs[off] << 24;
      ++off;
      n |= (bs[off] & 255) << 16;
      ++off;
      n |= (bs[off] & 255) << 8;
      ++off;
      n |= bs[off] & 255;
      return n;
   }

   public static long bigEndianToLong(byte[] bs, int off) {
      int hi = bigEndianToInt(bs, off);
      int lo = bigEndianToInt(bs, off + 4);
      return ((long)hi & 4294967295L) << 32 | (long)lo & 4294967295L;
   }

   public static void longToBigEndian(long n, byte[] bs, int off) {
      intToBigEndian((int)(n >>> 32), bs, off);
      intToBigEndian((int)(n & 4294967295L), bs, off + 4);
   }

   public static byte[] longToBigEndian(long[] ns) {
      byte[] bs = new byte[8 * ns.length];
      longToBigEndian(ns, bs, 0);
      return bs;
   }

   public static void longToBigEndian(long[] ns, byte[] bs, int off) {
      for(int i = 0; i < ns.length; ++i) {
         longToBigEndian(ns[i], bs, off);
         off += 8;
      }

   }

   public static short littleEndianToShort(byte[] bs, int off) {
      int n = bs[off] & 255;
      ++off;
      n |= (bs[off] & 255) << 8;
      return (short)n;
   }

   public static void intToBigEndian(int n, byte[] bs, int off) {
      bs[off] = (byte)(n >>> 24);
      ++off;
      bs[off] = (byte)(n >>> 16);
      ++off;
      bs[off] = (byte)(n >>> 8);
      ++off;
      bs[off] = (byte)n;
   }
}
