package emanondev.itemtag.activity.action;

import emanondev.itemtag.activity.ActionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class EmptyActionType extends ActionType {
   public static final EmptyActionType INST = new EmptyActionType();

   private EmptyActionType() {
      super("empty");
   }

   @NotNull
   public ActionType.Action read(@NotNull String info) {
      return new EmptyActionType.EmptyAction(info);
   }

   public final class EmptyAction extends ActionType.Action {
      public EmptyAction(@NotNull String param2) {
         super(info);
      }

      @NotNull
      public String toString() {
         return this.getInfo();
      }

      public boolean execute(@NotNull Player player, @NotNull ItemStack item, Event event) {
         return false;
      }
   }
}
