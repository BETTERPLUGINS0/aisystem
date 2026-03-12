package emanondev.itemtag.activity.action;

import emanondev.itemedit.UtilsString;
import emanondev.itemtag.activity.ActionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TitleActionType extends ActionType {
   public TitleActionType() {
      super("title");
   }

   @NotNull
   public ActionType.Action read(@NotNull String info) {
      return new TitleActionType.TitleAction(info);
   }

   public class TitleAction extends ActionType.Action {
      private final String message;
      private final String sub;
      private final int fadeIn;
      private final int fadeOut;
      private final int stay;

      public TitleAction(@NotNull String param2) {
         super(info);
         String[] args = info.split(";");
         if (args.length != 5) {
            throw new IllegalArgumentException("Invalid format: '" + this.getInfo() + "' must be '[title];[subtitle];<fadein ticks>;<stay ticks>;<fadeout ticks>'");
         } else {
            this.message = args[0];
            this.sub = args[1];
            this.fadeIn = Integer.parseInt(args[2]);
            this.stay = Integer.parseInt(args[3]);
            this.fadeOut = Integer.parseInt(args[4]);
         }
      }

      public boolean execute(@NotNull Player player, @NotNull ItemStack item, Event event) {
         player.sendTitle(UtilsString.fix(this.message, player, true, new String[0]), UtilsString.fix(this.sub, player, true, new String[0]), this.fadeIn, this.stay, this.fadeOut);
         return true;
      }
   }
}
