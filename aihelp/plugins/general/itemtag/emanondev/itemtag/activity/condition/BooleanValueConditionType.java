package emanondev.itemtag.activity.condition;

import emanondev.itemtag.activity.ConditionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BooleanValueConditionType extends ConditionType {
   public BooleanValueConditionType(@NotNull String id, @Nullable Class<? extends Event> clazz) {
      super(id, clazz);
   }

   @NotNull
   public ConditionType.Condition read(@NotNull String info, boolean reversed) {
      return new BooleanValueConditionType.BooleanValueCondition(info, reversed);
   }

   protected abstract boolean getCurrentValue(@NotNull Player var1, @NotNull ItemStack var2, Event var3);

   private class BooleanValueCondition extends ConditionType.Condition {
      public BooleanValueCondition(@NotNull String param2, boolean param3) {
         super(info, reversed);
      }

      protected boolean evaluateImpl(@NotNull Player player, @NotNull ItemStack item, Event event) {
         return BooleanValueConditionType.this.getCurrentValue(player, item, event);
      }
   }
}
