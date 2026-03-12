package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.protocol.player.User;
import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.entity.Player;

public final class Vulcan_e7 {
   private final Map Vulcan_b = new ConcurrentHashMap();
   private static AbstractCheck[] Vulcan_w;
   private static final long a = Vulcan_n.a(3874176950029951071L, 6274234923374244994L, MethodHandles.lookup().lookupClass()).a(80213889121196L);

   public Vulcan_iE Vulcan_Z(Object[] var1) {
      Player var2 = (Player)var1[0];
      return (Vulcan_iE)this.Vulcan_b.get(var2.getUniqueId());
   }

   public Vulcan_iE Vulcan_b(Object[] var1) {
      User var2 = (User)var1[0];
      return (Vulcan_iE)this.Vulcan_b.get(var2.getUUID());
   }

   public void Vulcan_Y(Object[] var1) {
      long var3 = (Long)var1[0];
      User var2 = (User)var1[1];
      long var10000 = a ^ var3;
      AbstractCheck[] var7 = Vulcan_D();
      this.Vulcan_b.put(var2.getUUID(), new Vulcan_iE(var2));
      AbstractCheck[] var5 = var7;
      if (var5 == null) {
         int var6 = AbstractCheck.Vulcan_V();
         ++var6;
         AbstractCheck.Vulcan_H(var6);
      }

   }

   public void Vulcan_X(Object[] var1) {
      UUID var2 = (UUID)var1[0];
      this.Vulcan_b.remove(var2);
   }

   public void Vulcan_a(Object[] var1) {
      long var3 = (Long)var1[0];
      User var2 = (User)var1[1];
      long var10000 = a ^ var3;
      Vulcan_D();
      this.Vulcan_b.remove(var2.getUUID());

      try {
         if (AbstractCheck.Vulcan_m() != 0) {
            Vulcan_g(new AbstractCheck[4]);
         }

      } catch (RuntimeException var6) {
         throw a(var6);
      }
   }

   public Collection Vulcan_U(Object[] var1) {
      return this.Vulcan_b.values();
   }

   public static void Vulcan_g(AbstractCheck[] var0) {
      Vulcan_w = var0;
   }

   public static AbstractCheck[] Vulcan_D() {
      return Vulcan_w;
   }

   static {
      if (Vulcan_D() == null) {
         Vulcan_g(new AbstractCheck[2]);
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
