package me.gypopo.economyshopgui.objects.mappings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;

public class ClickMappings {
   private final Map<ClickType, ClickAction> mappings = new HashMap();

   public ClickMappings(String section) {
      Map<ClickAction, ClickType> settings = new HashMap();
      ClickAction[] var10000 = new ClickAction[]{ClickAction.BUY, ClickAction.SELL, ClickAction.SELL_ALL};
      ClickType[] types = new ClickType[]{ClickType.LEFT, ClickType.SHIFT_LEFT, ClickType.RIGHT, ClickType.SHIFT_RIGHT, ClickType.UNKNOWN};
      ConfigurationSection mappings = (section != null ? ConfigManager.getSection(section) : ConfigManager.getConfig()).getConfigurationSection("click-mappings");

      Iterator var6;
      ClickAction action;
      ClickType click;
      for(var6 = mappings.getKeys(false).iterator(); var6.hasNext(); settings.put(action, click)) {
         String key = (String)var6.next();
         action = null;
         click = null;

         try {
            action = ClickAction.valueOf(key);
         } catch (IllegalArgumentException var14) {
            SendMessage.warnMessage(Lang.INVALID_CLICK_ACTION.get().replace("%click-action%", key).replace("%click-mapping%", key + ": " + mappings.getString(key)));
            if (section != null) {
               SendMessage.errorSections(section, "click-mappings");
            } else {
               SendMessage.errorItemConfig("click-mappings");
            }
         }

         try {
            String s = mappings.getString(key, "NONE");
            if (s.equals("NONE")) {
               click = ClickType.UNKNOWN;
            } else {
               click = ClickType.valueOf(mappings.getString(key, "null"));
               if (Arrays.stream(types).noneMatch((t) -> {
                  return t == click;
               })) {
                  throw new IllegalArgumentException();
               }
            }
         } catch (IllegalArgumentException var13) {
            click = null;
            SendMessage.warnMessage(Lang.INVALID_CLICK_TYPE.get().replace("%click-type%", mappings.getString(key, "null")).replace("%click-mapping%", key + ": " + mappings.getString(key)));
            if (section != null) {
               SendMessage.errorSections(section, "click-mappings");
            } else {
               SendMessage.errorItemConfig("click-mappings");
            }
         }

         ConfigurationSection def = ConfigManager.getConfig().getDef().getConfigurationSection("click-mappings");
         if (click == null && action != null) {
            click = ClickType.valueOf(ConfigManager.getConfig().getDef().getString("click-mappings." + action));
         }

         if (action == null && click != null) {
            Iterator var11 = def.getKeys(false).iterator();

            while(var11.hasNext()) {
               String k = (String)var11.next();
               if (def.getString(k).equals(click.name())) {
                  action = ClickAction.valueOf(k);
               }
            }
         }
      }

      if (!settings.containsKey(ClickAction.BUY)) {
         settings.put(ClickAction.BUY, (ClickType)Arrays.stream(types).filter((t) -> {
            return !settings.containsValue(t);
         }).findFirst().orElse(ClickType.UNKNOWN));
      }

      if (!settings.containsKey(ClickAction.SELL)) {
         settings.put(ClickAction.SELL, (ClickType)Arrays.stream(types).filter((t) -> {
            return !settings.containsValue(t);
         }).findFirst().orElse(ClickType.UNKNOWN));
      }

      if (!settings.containsKey(ClickAction.SELL_ALL)) {
         settings.put(ClickAction.SELL_ALL, (ClickType)Arrays.stream(types).filter((t) -> {
            return !settings.containsValue(t);
         }).findFirst().orElse(ClickType.UNKNOWN));
      }

      var6 = (new HashSet(settings.entrySet())).iterator();

      while(var6.hasNext()) {
         Entry<ClickAction, ClickType> e = (Entry)var6.next();
         if (e.getValue() == ClickType.UNKNOWN) {
            settings.remove(e.getKey());
         }
      }

      settings.forEach((keyx, value) -> {
         this.mappings.put(value, keyx);
      });
   }

   public ClickAction getAction(ClickType clickType) {
      return (ClickAction)this.mappings.get(clickType);
   }
}
