package emanondev.itemtag.activity.condition;

import emanondev.itemtag.activity.ConditionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PermissionConditionType extends ConditionType {
   public PermissionConditionType() {
      super("permission");
   }

   @NotNull
   public ConditionType.Condition read(@NotNull String info, boolean reversed) {
      return new PermissionConditionType.PermissionCondition(info, reversed);
   }

   private class PermissionCondition extends ConditionType.Condition {
      public PermissionCondition(@NotNull String param2, boolean param3) {
         super(info, reversed);
         if (info.isEmpty() || info.contains(" ")) {
            throw new IllegalArgumentException("Invalid format: '" + this.getInfo() + "' must be '<permission>'");
         }
      }

      protected boolean evaluateImpl(@NotNull Player player, @NotNull ItemStack item, Event event) {
         return player.hasPermission(this.getInfo());
      }
   }
}
