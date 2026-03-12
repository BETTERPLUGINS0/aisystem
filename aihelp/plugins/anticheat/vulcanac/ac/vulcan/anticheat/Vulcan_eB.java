package ac.vulcan.anticheat;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Vulcan_eB {
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-8595409845711202786L, 6655439258641499770L, (Object)null).a(49113171327473L);

   public static Vulcan_z Vulcan_U(Object[] var0) {
      Class var1 = (Class)var0[0];
      String var2 = (String)var0[1];
      long var3 = (Long)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 127042867303598L;
      return Vulcan_z.Vulcan_m(new Object[]{var1, new Long(var5), var2});
   }

   public static Vulcan_Z Vulcan_V(Object[] var0) {
      long var2 = (Long)var0[0];
      Class var1 = (Class)var0[1];
      int var4 = (Integer)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 118881648133730L;
      return (Vulcan_Z)Vulcan_Z.Vulcan_T(new Object[]{var1, new Long(var5), new Integer(var4)});
   }

   public static Map Vulcan_m(Object[] var0) {
      long var2 = (Long)var0[0];
      Class var1 = (Class)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 71031678605894L;
      return Vulcan_z.Vulcan_Z(new Object[]{var1, new Long(var4)});
   }

   public static List Vulcan_e(Object[] var0) {
      Class var3 = (Class)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 71904515154678L;
      return Vulcan_z.Vulcan_x(new Object[]{var3, new Long(var4)});
   }

   public static Iterator Vulcan_J(Object[] var0) {
      long var1 = (Long)var0[0];
      Class var3 = (Class)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 136812731932715L;
      return Vulcan_z.Vulcan_x(new Object[]{var3, new Long(var4)}).iterator();
   }
}
