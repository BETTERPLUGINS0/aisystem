package me.gypopo.economyshopgui.providers.priceModifiers.seasons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import me.casperge.realisticseasons.api.SeasonChangeEvent;
import me.casperge.realisticseasons.api.SeasonsAPI;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.shops.ShopSection;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.exceptions.ModifierLoadException;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SeasonProvider implements Listener {
   private final EconomyShopGUI plugin;
   private final Map<String, SeasonProvider.SeasonModifiers> LOADED_MODIFIERS = new HashMap();
   private static final Map<String, Season> LOADED_SEASONS = new HashMap();
   private boolean ready = false;
   private SeasonsAPI api;

   public SeasonProvider(EconomyShopGUI plugin) throws ModifierLoadException {
      this.plugin = plugin;
      if (plugin.getServer().getPluginManager().getPlugin("RealisticSeasons") != null) {
         try {
            this.api = SeasonsAPI.getInstance();
            this.loadModifiers(plugin);
            SendMessage.infoMessage(Lang.ENABLED_PLUGIN_HOOK.get().replace("%plugin%", "RealisticSeasons"));
            this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
         } catch (NullPointerException var4) {
            SendMessage.infoMessage("RealisticSeasons found, waiting...");
            Consumer<SeasonsAPI> result = (api) -> {
               if (api != null) {
                  SendMessage.infoMessage(Lang.ENABLED_PLUGIN_HOOK.get().replace("%plugin%", "RealisticSeasons"));
                  this.api = api;
                  this.loadModifiers(plugin);
                  this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
               } else {
                  SendMessage.warnMessage(Lang.FAILED_PLUGIN_INTEGRATION.get().replace("%plugin%", "RealisticSeasons"));
               }
            };
            plugin.runTaskLater(() -> {
               result.accept(SeasonsAPI.getInstance());
            }, 1L);
         }

      } else {
         throw new ModifierLoadException("Failed to find RealisticSeasons");
      }
   }

   public static Season getSeason(String world) {
      return (Season)LOADED_SEASONS.get(world);
   }

   private void setSeason(SeasonType type, String world) {
      Season season = this.getSeason(type);
      LOADED_SEASONS.put(world, season);
   }

   private Season getSeason(SeasonType type) {
      String season = this.matchConfig(type);
      if (season == null) {
         return new Season("null", "null", type);
      } else {
         String icon = ConfigManager.getConfig().getString("seasonal-lore." + season + ".icon", "null");
         String name = ConfigManager.getConfig().getString("seasonal-lore." + season + ".name", "null");
         return new Season(ChatUtil.formatColors(icon), ChatUtil.formatColors(name), type);
      }
   }

   private void loadModifiers(EconomyShopGUI plugin) {
      SendMessage.infoMessage(Lang.LOADING_SEASON_MODIFIERS.get());
      Iterator var2 = plugin.getServer().getWorlds().iterator();

      while(var2.hasNext()) {
         World world = (World)var2.next();
         int i = false;
         if (world.getEnvironment() != Environment.NETHER && world.getEnvironment() != Environment.THE_END) {
            SeasonType sType = SeasonType.get(this.api.getSeason(world));
            if (sType != null && sType != SeasonType.DISABLED) {
               String season = this.matchConfig(sType);
               if (season != null) {
                  this.setSeason(sType, world.getName());
                  Map<String, SeasonModifier> modifiers = this.getNewSeasonModifiers(world, sType, true);
                  if (modifiers == null) {
                     SendMessage.warnMessage(Lang.NO_SEASON_MODIFIERS_FOUND.get().replace("%season%", sType.name()));
                     return;
                  }

                  if (!this.plugin.getSections().isEmpty()) {
                     this.enableModifiers(modifiers);
                  } else {
                     plugin.runTaskLater(() -> {
                        this.enableModifiers(modifiers);
                     }, 1L);
                  }
               }
            }
         }
      }

      this.ready = true;
   }

   private void enableModifiers(Map<String, SeasonModifier> modifiers) {
      Iterator var2 = modifiers.keySet().iterator();

      while(var2.hasNext()) {
         String path = (String)var2.next();

         try {
            SeasonModifier modifier = (SeasonModifier)modifiers.get(path);
            if (path.split("\\.").length <= 2) {
               throw new NullPointerException();
            }

            this.plugin.getShopItem(path).addPriceModifier(modifier);
            ((SeasonProvider.SeasonModifiers)this.LOADED_MODIFIERS.computeIfAbsent(path, (s) -> {
               return new SeasonProvider.SeasonModifiers();
            })).add(modifier);
         } catch (NullPointerException var5) {
            SendMessage.warnMessage(Lang.CANNOT_LOAD_SEASON_MODIFIER.get().replace("%path%", path));
         }
      }

   }

   private void disableModifiers(String world) {
      Iterator var2 = this.LOADED_MODIFIERS.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, SeasonProvider.SeasonModifiers> e = (Entry)var2.next();
         SeasonModifier modifier = ((SeasonProvider.SeasonModifiers)e.getValue()).getByWorld(world);
         if (modifier != null) {
            try {
               if (((String)e.getKey()).split("\\.").length > 2) {
                  this.plugin.getShopItem((String)e.getKey()).removePriceModifier(modifier);
               } else {
                  ShopSection section = this.plugin.getSection(((String)e.getKey()).split("\\.")[0]);
                  section.getShopItems().forEach((i) -> {
                     i.removePriceModifier(modifier);
                  });
               }

               ((SeasonProvider.SeasonModifiers)e.getValue()).removeByWorld(world);
            } catch (NullPointerException var6) {
               SendMessage.warnMessage("Failed to disable season modifier for '" + (String)e.getKey() + "' because there was no loaded section/item found.");
            }
         }
      }

   }

   private String matchConfig(SeasonType sType) {
      Iterator var2 = ConfigManager.getConfig().getConfigurationSection("season-price-modifiers").getKeys(false).iterator();

      String season;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         season = (String)var2.next();
      } while(!sType.name().equalsIgnoreCase(season));

      return season;
   }

   private int getModifier(String path) {
      String modifier = ConfigManager.getConfig().getString(path, (String)null);
      if (modifier == null) {
         SendMessage.errorMessage(Lang.INVALID_SEASON_MODIFIER.get().replace("%path%", path).replace("%current%", ConfigManager.getConfig().get(path) + ""));
         return 0;
      } else {
         modifier = modifier.replace("%", "");

         try {
            int m = Integer.parseInt(modifier);
            if (m <= 100 && m >= -100) {
               return m;
            }
         } catch (NumberFormatException var4) {
         }

         SendMessage.errorMessage(Lang.INVALID_SEASON_MODIFIER.get().replace("%path%", path).replace("%current%", ConfigManager.getConfig().get(path) + ""));
         return 0;
      }
   }

   private boolean exists(String path) {
      if (this.plugin.getShopSections().contains(path.split("\\.")[0])) {
         if (path.split("\\.").length > 2) {
            if (this.plugin.getShopItem(path) != null) {
               return true;
            }
         } else if (path.split("\\.")[1].equalsIgnoreCase("all")) {
            return true;
         }
      }

      SendMessage.warnMessage(Lang.CANNOT_LOAD_SEASON_MODIFIER.get().replace("%path%", path));
      return false;
   }

   private boolean existsSoft(String path) {
      String section = path.split("\\.")[0];
      if (this.plugin.getShopSections().contains(section)) {
         label24: {
            if (path.split("\\.").length > 2) {
               if (!this.plugin.getConfigManager().getItemsRaw(section).contains(path.split("\\.", 2)[1])) {
                  break label24;
               }
            } else if (!path.split("\\.")[1].equalsIgnoreCase("all")) {
               break label24;
            }

            if (!ConfigManager.getShop(section).contains("pages." + path.split("\\.", 2)[1] + ".action")) {
               return true;
            }

            SendMessage.warnMessage(Lang.CANNOT_LOAD_SEASON_MODIFIER_ACTION_ITEM.get().replace("%path%", path));
            return false;
         }
      }

      SendMessage.warnMessage(Lang.CANNOT_LOAD_SEASON_MODIFIER.get().replace("%path%", path));
      return false;
   }

   private Map<String, SeasonModifier> getNewSeasonModifiers(World world, SeasonType sType, boolean soft) {
      String season = this.matchConfig(sType);
      if (season == null) {
         return null;
      } else {
         Map<String, SeasonModifier> modifiers = new LinkedHashMap();
         int i = 0;
         Iterator var7 = ConfigManager.getConfig().getConfigurationSection("season-price-modifiers." + season).getKeys(false).iterator();

         label46:
         while(var7.hasNext()) {
            String shop = (String)var7.next();
            Iterator var9 = ConfigManager.getConfig().getConfigurationSection("season-price-modifiers." + season + "." + shop).getKeys(false).iterator();

            while(true) {
               String item;
               String path;
               while(true) {
                  if (!var9.hasNext()) {
                     continue label46;
                  }

                  item = (String)var9.next();
                  path = shop + "." + item.replace(":", ".");
                  if (soft) {
                     if (!this.existsSoft(path)) {
                        continue;
                     }
                  } else if (!this.exists(path)) {
                     continue;
                  }
                  break;
               }

               int modifier = this.getModifier("season-price-modifiers." + season + "." + shop + "." + item);
               if (modifier != 0) {
                  ++i;
                  SeasonModifier sModifier = new SeasonModifier(world.getName(), modifier);
                  if (item.equalsIgnoreCase("all")) {
                     this.plugin.getSection(shop).getShopItems().forEach((e) -> {
                        modifiers.put(e.getItemPath(), sModifier);
                     });
                  } else {
                     modifiers.put(path, sModifier);
                  }
               }
            }
         }

         if (i != 0) {
            SendMessage.infoMessage(Lang.LOADED_SEASON_MODIFIERS.get().replace("%total%", String.valueOf(i)).replace("%season%", sType.name()).replace("%world%", world.getName()));
         } else {
            SendMessage.warnMessage(Lang.NO_SEASON_MODIFIERS_FOUND.get().replace("%season%", sType.name()));
         }

         return modifiers;
      }
   }

   public void reloadModifiers() {
      this.LOADED_MODIFIERS.clear();
      LOADED_SEASONS.clear();
      this.loadModifiers(this.plugin);
   }

   @EventHandler
   public void onSeasonChange(SeasonChangeEvent e) {
      if (!e.isCancelled()) {
         World world = e.getWorld();
         SeasonType sType = SeasonType.get(e.getNewSeason());
         SendMessage.infoMessage(Lang.SEASON_CHANGE.get().replace("%world%", world.getName()).replace("%season%", e.getNewSeason().name()));
         this.disableModifiers(world.getName());
         this.setSeason(sType, world.getName());
         if (sType != SeasonType.DISABLED) {
            Map<String, SeasonModifier> modifiers = this.getNewSeasonModifiers(world, sType, false);
            if (modifiers == null) {
               SendMessage.warnMessage(Lang.NO_SEASON_MODIFIERS_FOUND.get().replace("%season%", sType.name()));
            } else {
               this.enableModifiers(modifiers);
            }
         }
      }
   }

   private class SeasonModifiers extends ArrayList<SeasonModifier> {
      public SeasonModifiers() {
      }

      public SeasonProvider.SeasonModifiers append(SeasonModifier modifier) {
         this.add(modifier);
         return this;
      }

      public SeasonModifier getByWorld(String world) {
         Iterator var2 = this.iterator();

         SeasonModifier modifier;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            modifier = (SeasonModifier)var2.next();
         } while(!modifier.getWorld().equals(world));

         return modifier;
      }

      public boolean removeByWorld(String world) {
         Iterator var2 = this.iterator();

         SeasonModifier modifier;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            modifier = (SeasonModifier)var2.next();
         } while(!modifier.getWorld().equals(world));

         return this.remove(modifier);
      }
   }
}
