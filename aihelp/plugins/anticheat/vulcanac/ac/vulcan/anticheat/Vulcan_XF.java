package ac.vulcan.anticheat;

import java.io.Writer;

class Vulcan_XF extends Writer {
   private final Vulcan_o Vulcan_v;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(3977516838987940440L, -4214013738140257789L, (Object)null).a(182970768958592L);

   Vulcan_XF(Vulcan_o var1) {
      this.Vulcan_v = var1;
   }

   public void close() {
   }

   public void flush() {
   }

   public void write(int var1) {
      long var2 = a ^ 61606942154541L;
      long var4 = var2 ^ 75411543680481L;
      this.Vulcan_v.Vulcan__O(new Object[]{new Integer((char)var1), new Long(var4)});
   }

   public void write(char[] var1) {
      long var2 = a ^ 36173162009863L;
      long var4 = var2 ^ 92072243950214L;
      this.Vulcan_v.Vulcan_X(new Object[]{var1, new Long(var4)});
   }

   public void write(char[] var1, int var2, int var3) {
      long var4 = a ^ 23094560604161L;
      long var6 = var4 ^ 110091295349803L;
      this.Vulcan_v.Vulcan_PB(new Object[]{var1, new Integer(var2), new Long(var6), new Integer(var3)});
   }

   public void write(String var1) {
      long var2 = a ^ 15867880383607L;
      long var4 = var2 ^ 109723162669090L;
      this.Vulcan_v.Vulcan_FS(new Object[]{var1, new Long(var4)});
   }

   public void write(String var1, int var2, int var3) {
      long var4 = a ^ 95194394454211L;
      long var6 = var4 ^ 107226721434221L;
      this.Vulcan_v.Vulcan_N(new Object[]{new Long(var6), var1, new Integer(var2), new Integer(var3)});
   }
}
