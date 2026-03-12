package ac.vulcan.anticheat;

import java.io.Reader;

class Vulcan_eg extends Reader {
   private int Vulcan_n;
   private int Vulcan_w;
   private final Vulcan_o Vulcan_v;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(7597123298498138659L, 5504772188991128662L, (Object)null).a(271787253731852L);

   Vulcan_eg(Vulcan_o var1) {
      this.Vulcan_v = var1;
   }

   public void close() {
   }

   public int read() {
      // $FF: Couldn't be decompiled
   }

   public int read(char[] param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public long skip(long var1) {
      long var3 = a ^ 61089643883705L;
      int[] var5 = Vulcan_o.Vulcan_h();

      int var10000;
      long var9;
      label41: {
         label40: {
            try {
               long var10;
               var10000 = (var10 = (long)this.Vulcan_n + var1 - (long)this.Vulcan_v.Vulcan_i(new Object[0])) == 0L ? 0 : (var10 < 0L ? -1 : 1);
               if (var5 != null) {
                  break label41;
               }

               if (var10000 <= 0) {
                  break label40;
               }
            } catch (IndexOutOfBoundsException var8) {
               throw a(var8);
            }

            var1 = (long)(this.Vulcan_v.Vulcan_i(new Object[0]) - this.Vulcan_n);
         }

         try {
            var9 = var1;
            if (var5 != null) {
               return var9;
            }

            long var11;
            var10000 = (var11 = var1 - 0L) == 0L ? 0 : (var11 < 0L ? -1 : 1);
         } catch (IndexOutOfBoundsException var6) {
            throw a(var6);
         }
      }

      try {
         if (var10000 < 0) {
            return 0L;
         }
      } catch (IndexOutOfBoundsException var7) {
         throw a(var7);
      }

      this.Vulcan_n = (int)((long)this.Vulcan_n + var1);
      var9 = var1;
      return var9;
   }

   public boolean ready() {
      // $FF: Couldn't be decompiled
   }

   public boolean markSupported() {
      return true;
   }

   public void mark(int var1) {
      this.Vulcan_w = this.Vulcan_n;
   }

   public void reset() {
      this.Vulcan_n = this.Vulcan_w;
   }

   private static IndexOutOfBoundsException a(IndexOutOfBoundsException var0) {
      return var0;
   }
}
