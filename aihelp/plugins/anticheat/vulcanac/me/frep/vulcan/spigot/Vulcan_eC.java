package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Vulcan_eC implements Listener {
   private static AbstractCheck[] Vulcan_i;
   private static final long a = Vulcan_n.a(880403378087576356L, -3587427077007658902L, MethodHandles.lookup().lookupClass()).a(58514879907244L);

   @EventHandler
   public void Vulcan_I(EntityDamageByEntityEvent param1) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_h(AbstractCheck[] var0) {
      Vulcan_i = var0;
   }

   public static AbstractCheck[] Vulcan_W() {
      return Vulcan_i;
   }

   static {
      if (Vulcan_W() != null) {
         Vulcan_h(new AbstractCheck[1]);
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
