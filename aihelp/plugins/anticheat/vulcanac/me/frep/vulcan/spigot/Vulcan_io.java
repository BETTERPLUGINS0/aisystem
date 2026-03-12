package me.frep.vulcan.spigot;

import java.util.HashSet;
import java.util.Set;

public final class Vulcan_io {
   private final Set Vulcan_n = new HashSet();
   private Object Vulcan_X;

   public Vulcan_io(Object var1) {
      this.Vulcan_X = var1;
   }

   public Object Vulcan_v(Object[] var1) {
      return this.Vulcan_X;
   }

   public void Vulcan_L(Object[] var1) {
      Object var2 = (Object)var1[0];
      Object var3 = this.Vulcan_X;
      this.Vulcan_X = var2;
      this.Vulcan_n.forEach(Vulcan_io::lambda$set$0);
   }

   public Vulcan_Xq Vulcan_V(Object[] var1) {
      Vulcan_Xq var2 = (Vulcan_Xq)var1[0];
      this.Vulcan_n.add(var2);
      return var2;
   }

   public void Vulcan_X(Object[] var1) {
      Vulcan_Xq var2 = (Vulcan_Xq)var1[0];
      this.Vulcan_n.remove(var2);
   }

   private static void lambda$set$0(Object var0, Object var1, Vulcan_Xq var2) {
      var2.Vulcan_a(new Object[]{var0, var1});
   }
}
