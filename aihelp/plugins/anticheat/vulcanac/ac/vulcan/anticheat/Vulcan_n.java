package ac.vulcan.anticheat;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public interface Vulcan_n {
   long b = me.frep.vulcan.spigot.Vulcan_n.a(-1977698304014855283L, 8418180797445186717L, MethodHandles.lookup().lookupClass()).a(256066836898084L);

   boolean Vulcan_L(Object[] var1);

   default boolean Vulcan_S(Object[] var1) {
      return Bukkit.getServer().isPrimaryThread();
   }

   boolean Vulcan_f(Object[] var1);

   boolean Vulcan_u(Object[] var1);

   Vulcan_iF Vulcan_H(Object[] var1);

   Vulcan_iF Vulcan_d(Object[] var1);

   Vulcan_iF Vulcan_R(Object[] var1);

   /** @deprecated */
   @Deprecated
   default Vulcan_iF Vulcan_w(Object[] var1) {
      Plugin var3 = (Plugin)var1[0];
      Runnable var2 = (Runnable)var1[1];
      return this.Vulcan_H(new Object[]{var2});
   }

   /** @deprecated */
   @Deprecated
   default Vulcan_iF Vulcan_j(Object[] var1) {
      Plugin var4 = (Plugin)var1[0];
      long var2 = (Long)var1[1];
      Runnable var5 = (Runnable)var1[2];
      long var6 = (Long)var1[3];
      long var8 = var2 ^ 95755193009617L;
      return this.Vulcan_d(new Object[]{var5, var6, var8});
   }

   /** @deprecated */
   @Deprecated
   default Vulcan_iF Vulcan_v(Object[] var1) {
      Plugin var9 = (Plugin)var1[0];
      long var7 = (Long)var1[1];
      Runnable var6 = (Runnable)var1[2];
      long var2 = (Long)var1[3];
      long var4 = (Long)var1[4];
      long var10 = var7 ^ 30475034392680L;
      return this.Vulcan_R(new Object[]{var6, var2, var4, var10});
   }

   default Vulcan_iF Vulcan_X(Object[] var1) {
      Location var3 = (Location)var1[0];
      Runnable var2 = (Runnable)var1[1];
      return this.Vulcan_H(new Object[]{var2});
   }

   default Vulcan_iF Vulcan_h(Object[] var1) {
      Location var7 = (Location)var1[0];
      long var3 = (Long)var1[1];
      Runnable var2 = (Runnable)var1[2];
      long var5 = (Long)var1[3];
      long var8 = var3 ^ 128675749373478L;
      return this.Vulcan_d(new Object[]{var2, var5, var8});
   }

   default Vulcan_iF Vulcan_F(Object[] var1) {
      Location var2 = (Location)var1[0];
      long var8 = (Long)var1[1];
      Runnable var3 = (Runnable)var1[2];
      long var4 = (Long)var1[3];
      long var6 = (Long)var1[4];
      long var10 = var8 ^ 66713271748839L;
      return this.Vulcan_R(new Object[]{var3, var4, var6, var10});
   }

   /** @deprecated */
   @Deprecated
   default Vulcan_iF Vulcan_a(Object[] var1) {
      long var3 = (Long)var1[0];
      Runnable var2 = (Runnable)var1[1];
      long var5 = (Long)var1[2];
      var3 ^= b;
      long var7 = var3 ^ 86345703818201L;
      return this.Vulcan_d(new Object[]{var2, var5, var7});
   }

   /** @deprecated */
   @Deprecated
   default Vulcan_iF Vulcan_N(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      return this.Vulcan_H(new Object[]{var2});
   }

   /** @deprecated */
   @Deprecated
   default Vulcan_iF Vulcan_s(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      long var3 = (Long)var1[1];
      long var5 = (Long)var1[2];
      long var7 = (Long)var1[3];
      var7 ^= b;
      long var9 = var7 ^ 80000845762083L;
      return this.Vulcan_R(new Object[]{var2, var3, var5, var9});
   }

   default Vulcan_iF Vulcan_L(Object[] var1) {
      Entity var2 = (Entity)var1[0];
      Runnable var3 = (Runnable)var1[1];
      return this.Vulcan_H(new Object[]{var3});
   }

   default Vulcan_iF Vulcan_V(Object[] var1) {
      long var4 = (Long)var1[0];
      Entity var7 = (Entity)var1[1];
      Runnable var6 = (Runnable)var1[2];
      long var2 = (Long)var1[3];
      long var8 = var4 ^ 6595765015125L;
      return this.Vulcan_d(new Object[]{var6, var2, var8});
   }

   default Vulcan_iF Vulcan_Z(Object[] var1) {
      long var8 = (Long)var1[0];
      Entity var6 = (Entity)var1[1];
      Runnable var7 = (Runnable)var1[2];
      long var2 = (Long)var1[3];
      long var4 = (Long)var1[4];
      long var10 = var8 ^ 71241605012593L;
      return this.Vulcan_R(new Object[]{var7, var2, var4, var10});
   }

   Vulcan_iF Vulcan_W(Object[] var1);

   Vulcan_iF Vulcan_O(Object[] var1);

   Vulcan_iF Vulcan_c(Object[] var1);

   /** @deprecated */
   @Deprecated
   default Vulcan_iF Vulcan_A(Object[] var1) {
      Plugin var2 = (Plugin)var1[0];
      Runnable var3 = (Runnable)var1[1];
      return this.Vulcan_W(new Object[]{var3});
   }

   /** @deprecated */
   @Deprecated
   default Vulcan_iF Vulcan_J(Object[] var1) {
      long var2 = (Long)var1[0];
      Plugin var6 = (Plugin)var1[1];
      Runnable var7 = (Runnable)var1[2];
      long var4 = (Long)var1[3];
      long var8 = var2 ^ 31262340390390L;
      return this.Vulcan_O(new Object[]{var7, var4, var8});
   }

   /** @deprecated */
   @Deprecated
   default Vulcan_iF Vulcan_E(Object[] var1) {
      long var2 = (Long)var1[0];
      Plugin var9 = (Plugin)var1[1];
      Runnable var8 = (Runnable)var1[2];
      long var4 = (Long)var1[3];
      long var6 = (Long)var1[4];
      return this.Vulcan_c(new Object[]{var8, var4, var6});
   }

   default Future Vulcan_l(Object[] var1) {
      Callable var2 = (Callable)var1[0];
      CompletableFuture var3 = new CompletableFuture();
      this.Vulcan_Y(new Object[]{Vulcan_n::lambda$callSyncMethod$0});
      return var3;
   }

   void Vulcan_Y(Object[] var1);

   default void Vulcan_m(Object[] var1) {
      Location var3 = (Location)var1[0];
      Runnable var2 = (Runnable)var1[1];
      this.Vulcan_Y(new Object[]{var2});
   }

   default void Vulcan_W(Object[] var1) {
      Entity var3 = (Entity)var1[0];
      Runnable var2 = (Runnable)var1[1];
      this.Vulcan_Y(new Object[]{var2});
   }

   void Vulcan_y(Object[] var1);

   void Vulcan_x(Object[] var1);

   private static void lambda$callSyncMethod$0(CompletableFuture var0, Callable var1) {
      try {
         var0.complete(var1.call());
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }
}
