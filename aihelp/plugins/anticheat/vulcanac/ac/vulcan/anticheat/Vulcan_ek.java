package ac.vulcan.anticheat;

import java.io.Serializable;

public class Vulcan_ek implements Vulcan_i8, Serializable, Comparable {
   private static final long serialVersionUID = -4830728138360036487L;
   private boolean Vulcan_Y;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-5842654807618358495L, 5349440041565307379L, (Object)null).a(23900697644367L);

   public Vulcan_ek() {
   }

   public Vulcan_ek(boolean var1) {
      this.Vulcan_Y = var1;
   }

   public Vulcan_ek(Boolean var1) {
      this.Vulcan_Y = var1;
   }

   public Object Vulcan_G(Object[] var1) {
      int var4 = (Integer)var1[0];
      int var2 = (Integer)var1[1];
      int var3 = (Integer)var1[2];
      long var5 = (long)var4 << 32 | (long)var2 << 40 >>> 32 | (long)var3 << 56 >>> 56;
      long var7 = var5 ^ 90030330710674L;
      boolean var10001 = this.Vulcan_Y;
      return Vulcan_is.Vulcan_v(new Object[]{new Long(var7), new Boolean(var10001)});
   }

   public void Vulcan_c(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Y = var2;
   }

   public void Vulcan_Z(Object[] var1) {
      Object var2 = (Object)var1[0];
      this.Vulcan_c(new Object[]{new Boolean((Boolean)var2)});
   }

   public boolean Vulcan_Z(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_B(Object[] var1) {
      long var2 = (Long)var1[0];
      long var10000 = a ^ var2;
      String[] var4 = Vulcan_XA.Vulcan_c();

      boolean var6;
      label32: {
         try {
            var6 = this.Vulcan_Y;
            if (var4 != null) {
               return var6;
            }

            if (!var6) {
               break label32;
            }
         } catch (RuntimeException var5) {
            throw a(var5);
         }

         var6 = false;
         return var6;
      }

      var6 = true;
      return var6;
   }

   public boolean Vulcan_d(Object[] var1) {
      return this.Vulcan_Y;
   }

   public Boolean Vulcan_e(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 18703880139321L;
      boolean var10001 = this.Vulcan_Y;
      return Vulcan_is.Vulcan_v(new Object[]{new Long(var4), new Boolean(var10001)});
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      // $FF: Couldn't be decompiled
   }

   public int compareTo(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      return String.valueOf(this.Vulcan_Y);
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
