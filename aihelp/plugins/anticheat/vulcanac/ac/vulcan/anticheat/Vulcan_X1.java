package ac.vulcan.anticheat;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** @deprecated */
public class Vulcan_X1 {
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-1239099958098585652L, -4645445105614655275L, (Object)null).a(38079792285971L);

   public static Vulcan_XY Vulcan_T(Object[] var0) {
      Class var1 = (Class)var0[0];
      long var3 = (Long)var0[1];
      String var2 = (String)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 33906092707031L;
      return Vulcan_XY.Vulcan_n(new Object[]{new Long(var5), var1, var2});
   }

   public static Vulcan_Xa Vulcan_m(Object[] var0) {
      Class var3 = (Class)var0[0];
      int var4 = (Integer)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 50937779836477L;
      return (Vulcan_Xa)Vulcan_Xa.Vulcan_L(new Object[]{var3, new Long(var5), new Integer(var4)});
   }

   public static Map Vulcan_V(Object[] var0) {
      Class var1 = (Class)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 56757600468557L;
      return Vulcan_XY.Vulcan_l(new Object[]{var1, new Long(var4)});
   }

   public static List Vulcan_t(Object[] var0) {
      Class var3 = (Class)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 139339495818372L;
      return Vulcan_XY.Vulcan_w(new Object[]{var3, new Long(var4)});
   }

   public static Iterator Vulcan_Y(Object[] var0) {
      long var2 = (Long)var0[0];
      Class var1 = (Class)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 3761071578515L;
      return Vulcan_XY.Vulcan_w(new Object[]{var1, new Long(var4)}).iterator();
   }
}
