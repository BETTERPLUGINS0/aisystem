package fr.xephi.authme.util.message;

import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.util.Utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

public class I18NUtils {
   private static Map<UUID, String> PLAYER_LOCALE = new ConcurrentHashMap();
   private static final Map<String, String> LOCALE_MAP = new HashMap();
   private static final List<String> LOCALE_LIST = Arrays.asList("en", "bg", "de", "eo", "es", "et", "eu", "fi", "fr", "gl", "hu", "id", "it", "ja", "ko", "lt", "nl", "pl", "pt", "ro", "ru", "sk", "sr", "tr", "uk");

   public static String getLocale(Player player) {
      if (Utils.MAJOR_VERSION > 15) {
         return player.getLocale().toLowerCase();
      } else {
         return PLAYER_LOCALE.containsKey(player.getUniqueId()) ? (String)PLAYER_LOCALE.get(player.getUniqueId()) : null;
      }
   }

   public static void addLocale(UUID uuid, String locale) {
      if (PLAYER_LOCALE == null) {
         PLAYER_LOCALE = new ConcurrentHashMap();
      }

      PLAYER_LOCALE.put(uuid, locale);
   }

   public static void removeLocale(UUID uuid) {
      PLAYER_LOCALE.remove(uuid);
   }

   public static String localeToCode(String locale, Settings settings) {
      if (!((Set)settings.getProperty(PluginSettings.I18N_CODE_REDIRECT)).isEmpty()) {
         Iterator var2 = ((Set)settings.getProperty(PluginSettings.I18N_CODE_REDIRECT)).iterator();

         while(var2.hasNext()) {
            String raw = (String)var2.next();
            String[] split = raw.split(":");
            if (split.length == 2 && locale.equalsIgnoreCase(split[0])) {
               return split[1];
            }
         }
      }

      if (LOCALE_MAP.containsKey(locale)) {
         return (String)LOCALE_MAP.get(locale);
      } else {
         if (locale.contains("_")) {
            locale = locale.substring(0, locale.indexOf("_"));
         }

         return LOCALE_LIST.contains(locale) ? locale : (String)settings.getProperty(PluginSettings.MESSAGES_LANGUAGE);
      }
   }

   static {
      LOCALE_MAP.put("pt_br", "br");
      LOCALE_MAP.put("cs_cz", "cz");
      LOCALE_MAP.put("nds_de", "de");
      LOCALE_MAP.put("sxu", "de");
      LOCALE_MAP.put("swg", "de");
      LOCALE_MAP.put("rpr", "ru");
      LOCALE_MAP.put("sl_si", "si");
      LOCALE_MAP.put("vi_vn", "vn");
      LOCALE_MAP.put("lzh", "zhcn");
      LOCALE_MAP.put("zh_cn", "zhcn");
      LOCALE_MAP.put("zh_hk", "zhhk");
      LOCALE_MAP.put("zh_tw", "zhtw");
   }
}
