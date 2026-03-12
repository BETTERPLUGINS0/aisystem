package emanondev.itemtag.activity.action;

import emanondev.itemedit.UtilsString;
import emanondev.itemtag.activity.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ServerCommandActionType extends ActionType {
   public ServerCommandActionType() {
      super("server");
   }

   @NotNull
   public ActionType.Action read(@NotNull String info) {
      return new ServerCommandActionType.ServerCommandAction(info);
   }

   public class ServerCommandAction extends ActionType.Action {
      public ServerCommandAction(@NotNull String param2) {
         super(info);
      }

      public boolean execute(@NotNull Player player, @NotNull ItemStack item, Event event) {
         return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), UtilsString.fix(this.getInfo(), player, true, new String[0]));
      }
   }
}
