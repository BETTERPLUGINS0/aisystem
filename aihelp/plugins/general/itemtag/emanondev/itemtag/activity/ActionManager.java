package emanondev.itemtag.activity;

import emanondev.itemedit.utility.VersionUtils;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.activity.action.ActionBarActionType;
import emanondev.itemtag.activity.action.CommandActionType;
import emanondev.itemtag.activity.action.CommandAsOpActionType;
import emanondev.itemtag.activity.action.ConditionalActionType;
import emanondev.itemtag.activity.action.DelayedActionType;
import emanondev.itemtag.activity.action.MessageActionType;
import emanondev.itemtag.activity.action.PlaySoundActionType;
import emanondev.itemtag.activity.action.RandomActionType;
import emanondev.itemtag.activity.action.ServerCommandActionType;
import emanondev.itemtag.activity.action.TitleActionType;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public class ActionManager {
   private static final HashMap<String, ActionType> actionTypes = new HashMap();

   @NotNull
   public static ActionType.Action read(@NotNull String line) {
      int index = line.indexOf(" ");
      String id = (index == -1 ? line : line.substring(0, index)).toLowerCase(Locale.ENGLISH);
      String info = index == -1 ? "" : line.substring(index + 1);
      return ((ActionType)actionTypes.get(id)).read(info);
   }

   public static void register(@NotNull ActionType action) {
      String id = action.getId();
      if (actionTypes.containsKey(id)) {
         throw new IllegalArgumentException();
      } else {
         actionTypes.put(id, action);
         ItemTag.get().log("ActionManager registered Action Type &e" + action.getId());
      }
   }

   public static void load() {
      register(new CommandActionType());
      register(new CommandAsOpActionType());
      register(new ServerCommandActionType());
      register(new MessageActionType());
      if (VersionUtils.isVersionAfter(1, 11)) {
         register(new TitleActionType());
      }

      if (VersionUtils.isVersionAfter(1, 9, 2)) {
         register(new ActionBarActionType());
      }

      register(new DelayedActionType());
      register(new ConditionalActionType());
      register(new RandomActionType());
      register(new PlaySoundActionType());
   }

   public static Collection<String> getActionTypeIds() {
      return Collections.unmodifiableSet(actionTypes.keySet());
   }
}
