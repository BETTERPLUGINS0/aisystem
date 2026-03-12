package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import com.google.common.collect.Lists;
import java.util.Deque;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Auto Clicker",
   type = 'P',
   complexType = "Identical",
   description = "Identical statistical values."
)
public class AutoClickerP extends AbstractCheck {
   private final Deque Vulcan_p = Lists.newLinkedList();
   private double Vulcan_l;
   private double Vulcan_w;
   private double Vulcan_N;

   public AutoClickerP(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      Event var4 = (Event)var1[1];
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
