package emanondev.itemtag.activity.condition;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IsNightConditionType extends PlayerRelativeConditionType {
   public IsNightConditionType() {
      super("is_night", EntityDamageEvent.class);
   }

   protected boolean getCurrentValue(@NotNull Player player, @NotNull ItemStack item, @Nullable Event event, boolean playerRelative) {
      long ticks;
      if (!playerRelative) {
         ticks = player.getWorld().getTime();
      } else if (!player.isPlayerTimeRelative()) {
         ticks = player.getPlayerTimeOffset();
      } else {
         ticks = player.getPlayerTimeOffset() + player.getWorld().getTime();
      }

      ticks %= 24000L;
      if (ticks < 0L) {
         ticks += 24000L;
      }

      return ticks >= 13000L && ticks < 23000L;
   }
}
