package emanondev.itemtag.activity.action;

import emanondev.itemtag.ItemTag;
import emanondev.itemtag.activity.ActionManager;
import emanondev.itemtag.activity.ActionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class DelayedActionType extends ActionType {
   public DelayedActionType() {
      super("delayed");
   }

   @NotNull
   public ActionType.Action read(@NotNull String info) {
      return new DelayedActionType.DelayedAction(info);
   }

   public class DelayedAction extends ActionType.Action {
      private final int delay;
      private final ActionType.Action action;

      public DelayedAction(@NotNull String param2) {
         super(info);
         int index = info.indexOf(" ");
         if (index == -1) {
            throw new IllegalArgumentException("Invalid format: '" + this.getInfo() + "' must be '<delay> <action>'");
         } else {
            this.delay = Integer.parseInt(info.substring(0, index));
            if (this.delay <= 0) {
               throw new IllegalArgumentException("Invalid delay amount (must be >0)");
            } else {
               this.action = ActionManager.read(info.substring(index + 1));
            }
         }
      }

      public boolean execute(@NotNull final Player player, @NotNull final ItemStack item, final Event event) {
         (new BukkitRunnable() {
            public void run() {
               DelayedAction.this.action.execute(player, item, event);
            }
         }).runTaskLater(ItemTag.get(), (long)this.delay);
         return true;
      }
   }
}
