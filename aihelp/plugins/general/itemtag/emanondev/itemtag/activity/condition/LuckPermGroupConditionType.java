package emanondev.itemtag.activity.condition;

import emanondev.itemtag.activity.ConditionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LuckPermGroupConditionType extends ConditionType {
   public LuckPermGroupConditionType() {
      super("luckpermgroup");
   }

   @NotNull
   public ConditionType.Condition read(@NotNull String info, boolean reversed) {
      return new LuckPermGroupConditionType.LuckPermGroupCondition(info, reversed);
   }

   private class LuckPermGroupCondition extends ConditionType.Condition {
      private final String perm;

      public LuckPermGroupCondition(@NotNull String param2, boolean param3) {
         super(info, reversed);
         if (!info.isEmpty() && !info.contains(" ")) {
            this.perm = "group." + info;
         } else {
            throw new IllegalArgumentException("Invalid format: '" + this.getInfo() + "' must be '<group>'");
         }
      }

      protected boolean evaluateImpl(@NotNull Player player, @NotNull ItemStack item, Event event) {
         return player.hasPermission(this.perm);
      }
   }
}
