package emanondev.itemtag.actions;

import emanondev.itemedit.utility.VersionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActionHandler {
   private static final Map<String, Action> actions = VersionUtils.hasFoliaAPI() ? new ConcurrentHashMap() : new HashMap();

   public static void handleAction(Player player, String type, String action) {
      ((Action)actions.get(type)).execute(player, action);
   }

   public static void registerAction(Action action) {
      if (action == null) {
         throw new NullPointerException();
      } else if (actions.containsKey(action.getID())) {
         throw new IllegalArgumentException();
      } else {
         actions.put(action.getID().toLowerCase(Locale.ENGLISH), action);
      }
   }

   public static void clearActions() {
      actions.clear();
   }

   public static void validateActionType(String type) {
      if (!actions.containsKey(type.toLowerCase(Locale.ENGLISH))) {
         throw new IllegalArgumentException();
      }
   }

   public static void validateActionInfo(String type, String text) {
      ((Action)actions.get(type.toLowerCase(Locale.ENGLISH))).validateInfo(text);
   }

   public static List<String> tabComplete(CommandSender sender, String type, List<String> params) {
      return (List)(actions.containsKey(type.toLowerCase(Locale.ENGLISH)) ? ((Action)actions.get(type.toLowerCase(Locale.ENGLISH))).tabComplete(sender, params) : new ArrayList());
   }

   public static List<String> getTypes() {
      return new ArrayList(actions.keySet());
   }

   public static Action getAction(String type) {
      return (Action)actions.get(type.toLowerCase(Locale.ENGLISH));
   }

   public static String fixActionInfo(String type, String actionInfo) {
      return ((Action)actions.get(type.toLowerCase(Locale.ENGLISH))).fixActionInfo(actionInfo);
   }
}
