package me.gypopo.economyshopgui.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.requirements.ItemRequirement;
import me.gypopo.economyshopgui.providers.requirements.RequirementType;
import me.gypopo.economyshopgui.providers.requirements.Requirements;
import me.gypopo.economyshopgui.providers.requirements.items.TimeRequirement;
import me.gypopo.economyshopgui.providers.requirements.items.quest.QuestManager;
import me.gypopo.economyshopgui.providers.requirements.items.quest.QuestRequirement;
import me.gypopo.economyshopgui.providers.requirements.items.region.RegionManager;
import me.gypopo.economyshopgui.providers.requirements.items.region.RegionRequirement;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.exceptions.ItemRequirementLoadException;
import org.bukkit.entity.Player;

public class RequirementManager {
   private final EconomyShopGUI plugin;
   private QuestManager questManager;
   private RegionManager regionManager;

   public RequirementManager(EconomyShopGUI plugin) {
      this.plugin = plugin;
   }

   public Requirements load(String section, String item) {
      Requirements requirements = new Requirements();
      Iterator var4 = ConfigManager.getShop(section).getStringList(item + ".requirements").iterator();

      while(var4.hasNext()) {
         String s = (String)var4.next();

         RequirementType type;
         try {
            type = RequirementType.valueOf(s.split("::")[0]);
         } catch (IllegalArgumentException var10) {
            SendMessage.warnMessage(Lang.INVALID_ITEM_REQUIREMENT_TYPE.get().replace("%type%", s.split("::")[0]));
            SendMessage.errorShops(section, item);
            continue;
         }

         try {
            String requirement = s.split("::")[1];
            List<String> lore = ConfigManager.getConfig().getStringList(type.getLorePath());
            requirements.add(this.createRequirement(section + "." + item, type, requirement, lore));
         } catch (ItemRequirementLoadException var9) {
            SendMessage.warnMessage(Lang.INVALID_ITEM_REQUIREMENT.get().replace("%requirement%", s) + ": " + var9.getMessage());
            SendMessage.errorShops(section, item);
         }
      }

      return requirements.isEmpty() ? null : requirements;
   }

   private ItemRequirement createRequirement(String path, RequirementType type, String value, List<String> lore) throws ItemRequirementLoadException {
      switch(type) {
      case TIME:
         return new TimeRequirement(value, lore);
      case QUEST:
         if (this.questManager == null) {
            this.questManager = new QuestManager(this.plugin);
         }

         QuestRequirement questRequirement = new QuestRequirement(this.questManager.getProvider(), value, lore);
         return this.questManager.addRequirement(questRequirement, path);
      case REGION:
         if (this.regionManager == null) {
            this.regionManager = new RegionManager(this.plugin);
         }

         RegionRequirement regionRequirement = new RegionRequirement(this.regionManager.getProvider(), value, lore);
         return this.regionManager.addRequirement(regionRequirement, path);
      default:
         return null;
      }
   }

   public static Object getFormatted(RequirementType type, long requirement) {
      switch(type) {
      case TIME:
         return requirement;
      default:
         return requirement;
      }
   }

   public static RequirementManager.RequirementLore getLore(List<String> list, String def, String... formatted) {
      return (RequirementManager.RequirementLore)(EconomyShopGUI.getInstance().paperMeta ? new RequirementManager.CompRequirementLore(list, def, formatted) : new RequirementManager.BukkitRequirementLore(list, def, formatted));
   }

   private static List<String> translate(List<String> list) {
      if (list.isEmpty()) {
         return list;
      } else {
         List<String> lore = new ArrayList();
         Iterator var2 = list.iterator();

         while(var2.hasNext()) {
            String s = (String)var2.next();
            if (s.contains("%translations-")) {
               try {
                  Lang placeholder = Lang.valueOf(s.split("%translations-")[1].split("%")[0].toUpperCase(Locale.ENGLISH).replace("-", "_"));
                  s = s.replace("%translations-" + placeholder.getKey() + "%", placeholder.getRaw().replace("\\n", "\n"));
               } catch (IllegalArgumentException var8) {
               }
            }

            String[] var9 = s.split("\n");
            int var5 = var9.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String e = var9[var6];
               lore.add(EconomyShopGUI.getInstance().paperMeta ? ChatUtil.getAdventureUtils().formatLegacyToMini(e) : ChatUtil.formatColors(e));
            }
         }

         return lore;
      }
   }

   private static String translate(String s) {
      if (s == null) {
         return "";
      } else {
         if (s.contains("%translations-")) {
            try {
               Lang placeholder = Lang.valueOf(s.split("%translations-")[1].split("%")[0].toUpperCase(Locale.ENGLISH).replace("-", "_"));
               s = s.replace("%translations-" + placeholder.getKey() + "%", placeholder.getRaw());
            } catch (IllegalArgumentException var2) {
            }
         }

         return EconomyShopGUI.getInstance().paperMeta ? ChatUtil.getAdventureUtils().formatLegacyToMini(s) : ChatUtil.formatColors(s);
      }
   }

   private static boolean isEmpty(List<String> list) {
      return list.stream().allMatch(String::isEmpty);
   }

   private static final class CompRequirementLore implements RequirementManager.RequirementLore {
      private final Map<Integer, String> placeholders = new HashMap();
      private boolean replaced = false;
      private final String[] lore;
      private final String[] compLore;

      public CompRequirementLore(List<String> list, String def, String... formatted) {
         this.lore = list != null ? (!RequirementManager.isEmpty(list) ? (String[])((List)RequirementManager.translate(list).stream().map((sx) -> {
            return this.translateLocal(sx, formatted);
         }).collect(Collectors.toList())).toArray(new String[list.size()]) : new String[0]) : RequirementManager.translate(this.translateLocal(def, formatted)).split("\n");
         this.compLore = (String[])Arrays.stream(this.lore).map(ChatUtil::getGsonComponent).toArray((x$0) -> {
            return new String[x$0];
         });
         int i = 0;
         String[] var5 = this.lore;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String s = var5[var7];
            Stream var10000 = Arrays.stream(new String[]{"%requirement%", "%progress%"});
            Objects.requireNonNull(s);
            if (var10000.anyMatch(s::contains)) {
               this.placeholders.put(i, s);
            }

            ++i;
         }

      }

      public boolean isReplaced() {
         return this.replaced;
      }

      private String translateLocal(String s, String... formatted) {
         if (formatted.length == 1) {
            if (s.contains("%requirement%")) {
               s = s.replace("%requirement%", formatted[0]);
            }
         } else if (formatted.length == 2) {
            if (s.contains("%minTime%")) {
               s = s.replace("%minTime%", formatted[0]);
            }

            if (s.contains("%maxTime%")) {
               s = s.replace("%maxTime%", formatted[1]);
            }
         }

         return s;
      }

      public void replace(String target, String replacement) {
         for(int i = 0; i < this.compLore.length; ++i) {
            if (this.compLore[i].contains(target)) {
               this.lore[i] = this.lore[i].replace(target, replacement);
               this.compLore[i] = this.compLore[i].replace(target, replacement);
               this.replaced = true;
            }
         }

      }

      public String[] get(Player p, boolean fast, boolean pr, String... replacements) {
         String[] clone = (String[])(fast ? this.compLore : this.lore).clone();
         Iterator var6 = this.placeholders.entrySet().iterator();

         while(var6.hasNext()) {
            Entry<Integer, String> ph = (Entry)var6.next();

            for(int i = 0; i < replacements.length; i += 2) {
               if (((String)ph.getValue()).contains(replacements[i])) {
                  clone[(Integer)ph.getKey()] = clone[(Integer)ph.getKey()].replace(replacements[i], replacements[i + 1]);
               }
            }
         }

         return clone;
      }

      public String[] get(Player p, boolean nbt, boolean pr) {
         return nbt ? this.compLore : this.lore;
      }
   }

   private static final class BukkitRequirementLore implements RequirementManager.RequirementLore {
      private final Map<Integer, String> placeholders = new HashMap();
      private boolean replaced = false;
      private final String[] lore;
      private final String[] fastLore;

      public BukkitRequirementLore(List<String> list, String def, String... formatted) {
         this.lore = list != null ? (!RequirementManager.isEmpty(list) ? (String[])((List)RequirementManager.translate(list).stream().map((sx) -> {
            return this.translateLocal(sx, formatted);
         }).collect(Collectors.toList())).toArray(new String[list.size()]) : new String[0]) : RequirementManager.translate(this.translateLocal(def, formatted)).split("\n");
         this.fastLore = (String[])Arrays.stream(this.lore).map((sx) -> {
            return EconomyShopGUI.getInstance().versionHandler.toNBT(sx);
         }).toArray((x$0) -> {
            return new String[x$0];
         });
         int i = 0;
         String[] var5 = this.lore;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String s = var5[var7];
            Stream var10000 = Arrays.stream(new String[]{"%requirement%", "%progress%"});
            Objects.requireNonNull(s);
            if (var10000.anyMatch(s::contains)) {
               this.placeholders.put(i, s);
            }

            ++i;
         }

      }

      public boolean isReplaced() {
         return this.replaced;
      }

      private String translateLocal(String s, String... formatted) {
         if (formatted.length == 1) {
            if (s.contains("%requirement%")) {
               s = s.replace("%requirement%", formatted[0]);
            }
         } else if (formatted.length == 2) {
            if (s.contains("%minTime%")) {
               s = s.replace("%minTime%", formatted[0]);
            }

            if (s.contains("%maxTime%")) {
               s = s.replace("%maxTime%", formatted[1]);
            }
         }

         return s;
      }

      public void replace(String target, String replacement) {
         for(int i = 0; i < this.lore.length; ++i) {
            if (this.lore[i].contains(target)) {
               this.lore[i] = this.lore[i].replace(target, replacement);
               this.fastLore[i] = this.fastLore[i].replace(target, replacement);
               this.replaced = true;
            }
         }

      }

      public String[] get(Player p, boolean fast, boolean pr, String... replacements) {
         String[] clone = (String[])(fast && !pr ? this.fastLore : this.lore).clone();
         Iterator var6 = this.placeholders.entrySet().iterator();

         while(var6.hasNext()) {
            Entry<Integer, String> ph = (Entry)var6.next();

            for(int i = 0; i < replacements.length; i += 2) {
               if (((String)ph.getValue()).contains(replacements[i])) {
                  clone[(Integer)ph.getKey()] = ((String)ph.getValue()).replace(replacements[i], replacements[i + 1]);
               }
            }
         }

         return clone;
      }

      public String[] get(Player p, boolean nbt, boolean pr) {
         return nbt && !pr ? this.fastLore : this.lore;
      }
   }

   public interface RequirementLore {
      boolean isReplaced();

      void replace(String var1, String var2);

      String[] get(Player var1, boolean var2, boolean var3, String... var4);

      String[] get(Player var1, boolean var2, boolean var3);
   }
}
