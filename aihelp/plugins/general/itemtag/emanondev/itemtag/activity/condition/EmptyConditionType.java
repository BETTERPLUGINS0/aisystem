package emanondev.itemtag.activity.condition;

import emanondev.itemtag.activity.ConditionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EmptyConditionType extends ConditionType {
   public static final EmptyConditionType INST = new EmptyConditionType();

   private EmptyConditionType() {
      super("empty");
   }

   @NotNull
   public ConditionType.Condition read(@NotNull String info, boolean reversed) {
      return new EmptyConditionType.EmptyCondition(info);
   }

   public final class EmptyCondition extends ConditionType.Condition {
      public EmptyCondition(@NotNull String param2) {
         super(info, false);
      }

      @NotNull
      public String toString() {
         return this.getInfo();
      }

      public boolean evaluate(@NotNull Player player, @NotNull ItemStack item, Event event) {
         return false;
      }

      protected boolean evaluateImpl(@NotNull Player player, @NotNull ItemStack item, Event event) {
         return false;
      }
   }
}
