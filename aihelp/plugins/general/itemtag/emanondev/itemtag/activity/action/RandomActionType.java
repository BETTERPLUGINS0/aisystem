package emanondev.itemtag.activity.action;

import emanondev.itemtag.activity.ActionManager;
import emanondev.itemtag.activity.ActionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RandomActionType extends ActionType {
   public RandomActionType() {
      super("random");
   }

   @NotNull
   public ActionType.Action read(@NotNull String info) {
      return new RandomActionType.ConditionalAction(info);
   }

   private class ConditionalAction extends ActionType.Action {
      private final double chance;
      private final ActionType.Action action;
      private final ActionType.Action alternative;

      public ConditionalAction(@NotNull String param2) {
         super(info);
         String[] values = info.split(";R;");
         if (values.length != 2 && values.length != 3) {
            throw new IllegalArgumentException("Invalid format '" + info + "' must be '<chance>;R;<action>[;R;alternativeaction]'");
         } else {
            this.chance = Double.parseDouble(values[0]);
            this.action = ActionManager.read(values[1]);
            this.alternative = values.length == 2 ? null : ActionManager.read(values[2]);
         }
      }

      public boolean execute(@NotNull Player player, @NotNull ItemStack item, Event event) {
         if (Math.random() < this.chance) {
            return this.action.execute(player, item, event);
         } else {
            return this.alternative == null ? false : this.alternative.execute(player, item, event);
         }
      }
   }
}
