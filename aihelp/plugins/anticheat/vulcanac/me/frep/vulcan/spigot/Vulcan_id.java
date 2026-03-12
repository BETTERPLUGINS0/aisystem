package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRiptideEvent;

public class Vulcan_id implements Listener {
   private static final long a = Vulcan_n.a(-8056445438138863072L, -4100142959950483317L, MethodHandles.lookup().lookupClass()).a(28106543521273L);

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void Vulcan__(PlayerRiptideEvent var1) {
      long var2 = a ^ 66654473713493L;
      boolean var10000 = Vulcan_Xx.Vulcan_a();
      Player var5 = var1.getPlayer();
      boolean var4 = var10000;
      Vulcan_iE var6 = Vulcan_Xs.INSTANCE.Vulcan_e().Vulcan_Z(new Object[]{var5});

      Vulcan_iE var8;
      label22: {
         try {
            var8 = var6;
            if (var4) {
               break label22;
            }

            if (var6 == null) {
               return;
            }
         } catch (RuntimeException var7) {
            throw a(var7);
         }

         var8 = var6;
      }

      var8.Vulcan_e(new Object[0]).Vulcan_P(new Object[]{0});
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
