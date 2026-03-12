package emanondev.itemtag.activity.action;

import emanondev.itemedit.UtilsString;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.activity.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CommandActionType extends ActionType {
   public CommandActionType() {
      super("command");
   }

   @NotNull
   public ActionType.Action read(@NotNull String info) {
      return new CommandActionType.CommandAction(info);
   }

   public class CommandAction extends ActionType.Action {
      public CommandAction(@NotNull String param2) {
         super(info);
      }

      public boolean execute(@NotNull Player player, @NotNull ItemStack item, Event event) {
         String text = UtilsString.fix(this.getInfo(), player, true, new String[0]);
         if (ItemTag.get().getConfig().loadBoolean("actions.player_command.fires_playercommandpreprocessevent", true)) {
            PlayerCommandPreprocessEvent evt = new PlayerCommandPreprocessEvent(player, text);
            Bukkit.getPluginManager().callEvent(evt);
            if (evt.isCancelled()) {
               return false;
            }

            text = evt.getMessage();
         }

         return Bukkit.dispatchCommand(player, UtilsString.fix(text, player, true, new String[0]));
      }
   }
}
