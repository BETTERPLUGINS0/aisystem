package emanondev.itemtag.activity.action;

import emanondev.itemedit.UtilsString;
import emanondev.itemedit.YMLConfig;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.activity.ActionType;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CommandAsOpActionType extends ActionType {
   private final YMLConfig data = ItemTag.get().getConfig("crash-safe-data");

   public CommandAsOpActionType() {
      super("op");
      Iterator var1 = this.data.getKeys(false).iterator();

      while(var1.hasNext()) {
         String key = (String)var1.next();

         try {
            Bukkit.getOfflinePlayer(UUID.fromString(key)).setOp(false);
            this.data.set(key, (Object)null);
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   @NotNull
   public ActionType.Action read(@NotNull String info) {
      return new CommandAsOpActionType.CommandAsOpAction(info);
   }

   public class CommandAsOpAction extends ActionType.Action {
      public CommandAsOpAction(@NotNull String param2) {
         super(info);
      }

      public boolean execute(@NotNull Player player, @NotNull ItemStack item, Event event) {
         String text = UtilsString.fix(this.getInfo(), player, true, new String[0]);
         boolean op = player.isOp();
         if (!op) {
            player.setOp(true);
            CommandAsOpActionType.this.data.set(player.getUniqueId().toString(), true);
            CommandAsOpActionType.this.data.save();
         }

         try {
            boolean result;
            try {
               if (ItemTag.get().getConfig().loadBoolean("actions.player_command.fires_playercommandpreprocessevent", true)) {
                  PlayerCommandPreprocessEvent evt = new PlayerCommandPreprocessEvent(player, text);
                  Bukkit.getPluginManager().callEvent(evt);
                  if (evt.isCancelled()) {
                     boolean var8 = false;
                     return var8;
                  }

                  text = evt.getMessage();
               }

               result = Bukkit.dispatchCommand(player, UtilsString.fix(text, player, true, new String[0]));
            } catch (Throwable var12) {
               var12.printStackTrace();
               result = false;
            }

            return result;
         } finally {
            if (!op) {
               player.setOp(false);
               CommandAsOpActionType.this.data.set(player.getUniqueId().toString(), (Object)null);
            }

         }
      }
   }
}
