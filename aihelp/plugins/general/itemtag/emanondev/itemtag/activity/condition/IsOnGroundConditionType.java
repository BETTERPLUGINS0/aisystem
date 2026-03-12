package emanondev.itemtag.activity.condition;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IsOnGroundConditionType extends BooleanValueConditionType {
   public IsOnGroundConditionType() {
      super("is_onground", (Class)null);
   }

   public boolean getCurrentValue(@NotNull Player player, @NotNull ItemStack item, Event event) {
      return player.isOnGround();
   }
}
