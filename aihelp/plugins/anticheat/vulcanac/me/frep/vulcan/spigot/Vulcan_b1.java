package me.frep.vulcan.spigot;

import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityDeactivateEvent;
import java.lang.invoke.MethodHandles;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Vulcan_b1 implements Listener {
   private static int[] Vulcan_X;
   private static final long a = Vulcan_n.a(-6523925853196005033L, 4357313034264377255L, MethodHandles.lookup().lookupClass()).a(14829866733271L);

   @EventHandler
   public void Vulcan_a(McMMOPlayerAbilityActivateEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler
   public void Vulcan_y(McMMOPlayerAbilityDeactivateEvent param1) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_y(int[] var0) {
      Vulcan_X = var0;
   }

   public static int[] Vulcan_E() {
      return Vulcan_X;
   }

   static {
      if (Vulcan_E() != null) {
         Vulcan_y(new int[2]);
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
