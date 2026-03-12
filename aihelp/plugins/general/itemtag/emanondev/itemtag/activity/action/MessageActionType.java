package emanondev.itemtag.activity.action;

import emanondev.itemedit.UtilsString;
import emanondev.itemtag.activity.ActionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MessageActionType extends ActionType {
   public MessageActionType() {
      super("message");
   }

   @NotNull
   public ActionType.Action read(@NotNull String info) {
      return new MessageActionType.MessageAction(info);
   }

   public class MessageAction extends ActionType.Action {
      public MessageAction(@NotNull String param2) {
         super(info);
      }

      public boolean execute(@NotNull Player player, @NotNull ItemStack item, Event event) {
         player.sendMessage(UtilsString.fix(this.getInfo(), player, true, new String[0]));
         return true;
      }
   }
}
