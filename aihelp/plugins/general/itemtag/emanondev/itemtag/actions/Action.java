package emanondev.itemtag.actions;

import java.util.List;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Action {
   private final String id;

   public Action(String id) {
      this.id = id.toLowerCase(Locale.ENGLISH);
   }

   public String getID() {
      return this.id;
   }

   public abstract void validateInfo(String var1);

   public abstract void execute(Player var1, String var2);

   public abstract List<String> tabComplete(CommandSender var1, List<String> var2);

   public abstract List<String> getInfo();

   public String fixActionInfo(String actionInfo) {
      return actionInfo;
   }
}
