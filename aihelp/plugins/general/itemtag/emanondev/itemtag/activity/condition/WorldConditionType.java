package emanondev.itemtag.activity.condition;

import emanondev.itemtag.activity.ConditionType;
import java.util.Arrays;
import java.util.HashSet;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WorldConditionType extends ConditionType {
   public WorldConditionType() {
      super("world");
   }

   @NotNull
   public ConditionType.Condition read(@NotNull String info, boolean reversed) {
      return new WorldConditionType.WorldCondition(info, reversed);
   }

   private class WorldCondition extends ConditionType.Condition {
      private final HashSet<String> worlds = new HashSet();

      public WorldCondition(@NotNull String param2, boolean param3) {
         super(info, reversed);
         if (!info.isEmpty() && !info.contains(" ")) {
            this.worlds.addAll(Arrays.asList(info.split(";")));
         } else {
            throw new IllegalArgumentException("Invalid format: '" + this.getInfo() + "' must be '<world>[;world2][;world3]...'");
         }
      }

      protected boolean evaluateImpl(@NotNull Player player, @NotNull ItemStack item, Event event) {
         return this.worlds.contains(player.getWorld().getName());
      }
   }
}
