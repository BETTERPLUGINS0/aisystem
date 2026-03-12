package ac.vulcan.anticheat;

import java.util.Random;

public final class Vulcan_b0 extends Random {
   private static final long serialVersionUID = 1L;
   private static final Random Vulcan_k;
   private boolean Vulcan_B = false;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-1498507165799886731L, -3312261099084893993L, (Object)null).a(133260209433099L);
   private static final String b;

   public Vulcan_b0() {
      this.Vulcan_B = true;
   }

   public synchronized void setSeed(long var1) {
      long var3 = a ^ 92650760829191L;

      try {
         if (this.Vulcan_B) {
            throw new UnsupportedOperationException();
         }
      } catch (UnsupportedOperationException var5) {
         throw a(var5);
      }
   }

   public synchronized double nextGaussian() {
      throw new UnsupportedOperationException();
   }

   public void nextBytes(byte[] var1) {
      throw new UnsupportedOperationException();
   }

   public int nextInt() {
      return this.nextInt(Integer.MAX_VALUE);
   }

   public int nextInt(int var1) {
      return Vulcan_k.nextInt(var1);
   }

   public long nextLong() {
      long var1 = a ^ 2235965753143L;
      long var3 = var1 ^ 39948982083977L;
      return Vulcan_Q(new Object[]{new Long(Long.MAX_VALUE), new Long(var3)});
   }

   public static long Vulcan_Q(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public boolean nextBoolean() {
      return Vulcan_k.nextBoolean();
   }

   public float nextFloat() {
      return Vulcan_k.nextFloat();
   }

   public double nextDouble() {
      return Vulcan_k.nextDouble();
   }

   private static long Vulcan_g(Object[] var0) {
      return Vulcan_k.nextLong() & Long.MAX_VALUE;
   }

   private static int Vulcan_P(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      char[] var10002 = "\tGP54\f#3BN4fJ..\u0017N5>X\b2C\u0000=3_5|UEp6C25CI&#".toCharArray();
      int var10003 = var10002.length;
      int var1 = 0;
      byte var13 = 14;
      char[] var10001 = var10002;
      int var11 = var10003;
      char[] var10004;
      int var10005;
      byte var14;
      if (var10003 <= 1) {
         var10004 = var10002;
         var14 = 14;
         var10005 = var1;
      } else {
         var13 = 14;
         var11 = var10003;
         if (var10003 <= var1) {
            b = (new String(var10002)).intern();
            Vulcan_k = new Random();
            return;
         }

         var10004 = var10002;
         var14 = 14;
         var10005 = var1;
      }

      while(true) {
         char var9 = var10004[var10005];
         byte var10;
         switch(var1 % 7) {
         case 0:
            var10 = 82;
            break;
         case 1:
            var10 = 57;
            break;
         case 2:
            var10 = 46;
            break;
         case 3:
            var10 = 94;
            break;
         case 4:
            var10 = 72;
            break;
         case 5:
            var10 = 34;
            break;
         default:
            var10 = 79;
         }

         var10004[var10005] = (char)(var9 ^ var14 ^ var10);
         ++var1;
         if (var13 == 0) {
            var10005 = var13;
            var10004 = var10001;
            var14 = var13;
         } else {
            if (var11 <= var1) {
               b = (new String(var10001)).intern();
               Vulcan_k = new Random();
               return;
            }

            var10004 = var10001;
            var14 = var13;
            var10005 = var1;
         }
      }
   }

   private static UnsupportedOperationException a(UnsupportedOperationException var0) {
      return var0;
   }
}
