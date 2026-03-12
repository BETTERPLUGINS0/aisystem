package emanondev.itemtag.activity.action;

import emanondev.itemtag.activity.ActionType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ActionBarActionType extends ActionType {
   public ActionBarActionType() {
      super("actionbar");
   }

   @NotNull
   public ActionType.Action read(@NotNull String info) {
      return new ActionBarActionType.ActionBarAction(info);
   }

   public class ActionBarAction extends ActionType.Action {
      public ActionBarAction(@NotNull String param2) {
         super(info);
      }

      public boolean execute(@NotNull Player player, @NotNull ItemStack item, Event event) {
         player.spigot().sendMessage(ChatMessageType.ACTION_BAR, (new ComponentBuilder(this.getInfo())).create());
         return true;
      }
   }
}
