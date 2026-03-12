package emanondev.itemtag.activity.condition;

import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnvironmentConditionType extends EnumValueConditionType<Environment> {
   public EnvironmentConditionType() {
      super("environment", (Class)null, Environment.class);
   }

   protected Environment getCurrentEnumValue(@NotNull Player player, @NotNull ItemStack item, Event event) {
      return player.getWorld().getEnvironment();
   }
}
