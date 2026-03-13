package me.gypopo.economyshopgui.providers;

import com.bgsoftware.wildstacker.api.WildStacker;
import com.bgsoftware.wildstacker.api.WildStackerAPI;
import de.dustplanet.util.SilkUtil;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.events.SpawnerBreakEvent;
import me.gypopo.economyshopgui.events.SpawnerInteractEvent;
import me.gypopo.economyshopgui.events.SpawnerPlaceEvent;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.spawners.DefaultSpawnerProvider;
import me.gypopo.economyshopgui.providers.spawners.MineableSpawnersHook;
import me.gypopo.economyshopgui.providers.spawners.RoseStackerHook;
import me.gypopo.economyshopgui.providers.spawners.SilkSpawnersHook;
import me.gypopo.economyshopgui.providers.spawners.SilkSpawnersV2Hook;
import me.gypopo.economyshopgui.providers.spawners.SmartSpawnerHook;
import me.gypopo.economyshopgui.providers.spawners.SpawnerLegacyHookV3;
import me.gypopo.economyshopgui.providers.spawners.SpawnerMetaHook;
import me.gypopo.economyshopgui.providers.spawners.UltimateStackerHook;
import me.gypopo.economyshopgui.providers.spawners.WildStackerHook;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

public class SpawnerManager {
   private SpawnerInteractEvent interactEvent;
   private SpawnerBreakEvent breakEvent;
   private SpawnerPlaceEvent placeEvent;
   private final EconomyShopGUI plugin;
   private SpawnerProvider provider;

   public SpawnerManager(EconomyShopGUI plugin) {
      this.plugin = plugin;
   }

   public void init() {
      this.provider = null;
      if (this.breakEvent != null) {
         BlockBreakEvent.getHandlerList().unregister(this.breakEvent);
      }

      if (this.placeEvent != null) {
         BlockPlaceEvent.getHandlerList().unregister(this.placeEvent);
      }

      if (this.interactEvent != null) {
         PlayerInteractEvent.getHandlerList().unregister(this.interactEvent);
      }

      SpawnerManager.Provider providerName = SpawnerManager.Provider.getFromString(ConfigManager.getConfig().getString("spawner-provider", "DEFAULT"));
      SpawnerProvider provider = this.setup(providerName, false);
      if (provider == null) {
         this.provider = this.initProvider(SpawnerManager.Provider.DEFAULT, (Object)null);
         SendMessage.infoMessage(Lang.USING_DEFAULT_SPAWNERS.get());
      }

   }

   private SpawnerProvider setup(SpawnerManager.Provider providerName, boolean silent) {
      switch(providerName.ordinal()) {
      case 0:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
         if (this.plugin.getServer().getPluginManager().getPlugin(providerName.getPluginName()) != null) {
            SendMessage.infoMessage(getLog(Lang.SPAWNER_PROVIDER_FOUND.get(), providerName.getName()));
            if (this.plugin.getServer().getPluginManager().getPlugin(providerName.getPluginName()).isEnabled()) {
               this.provider = this.initProvider(providerName, (Object)null);
               SendMessage.infoMessage(getLog(Lang.USING_EXTERNAL_SPAWNERS.get(), providerName.getName()));
            } else {
               SendMessage.warnMessage(getLog(Lang.FAILED_SPAWNER_PROVIDER_INTEGRATION.get(), providerName.getName()));
            }
         } else if (!silent) {
            SendMessage.warnMessage(getLog(Lang.SPAWNER_PROVIDER_NOT_FOUND.get(), providerName.getName()));
         }
         break;
      case 1:
         if (this.plugin.getServer().getPluginManager().getPlugin(providerName.getPluginName()) != null) {
            SendMessage.infoMessage(getLog(Lang.SPAWNER_PROVIDER_FOUND.get(), providerName.getName()));
            WildStacker ws = WildStackerAPI.getWildStacker();
            if (ws != null && this.plugin.getServer().getPluginManager().getPlugin(providerName.getPluginName()).isEnabled()) {
               this.provider = this.initProvider(providerName, ws);
               SendMessage.infoMessage(getLog(Lang.USING_EXTERNAL_SPAWNERS.get(), providerName.getName()));
            } else {
               SendMessage.warnMessage(getLog(Lang.FAILED_SPAWNER_PROVIDER_INTEGRATION.get(), providerName.getName()));
            }
         } else if (!silent) {
            SendMessage.warnMessage(getLog(Lang.SPAWNER_PROVIDER_NOT_FOUND.get(), providerName.getName()));
         }
         break;
      case 2:
         if (this.plugin.getServer().getPluginManager().getPlugin(providerName.getPluginName()) != null) {
            SendMessage.infoMessage(getLog(Lang.SPAWNER_PROVIDER_FOUND.get(), providerName.getName()));

            try {
               SilkUtil su = SilkUtil.hookIntoSilkSpanwers();
               if (su != null && this.plugin.getServer().getPluginManager().getPlugin(providerName.getPluginName()).isEnabled()) {
                  this.provider = this.initProvider(providerName, su);
                  SendMessage.infoMessage(getLog(Lang.USING_EXTERNAL_SPAWNERS.get(), providerName.getName()));
               } else {
                  SendMessage.warnMessage(getLog(Lang.FAILED_SPAWNER_PROVIDER_INTEGRATION.get(), providerName.getName()));
               }
            } catch (NoClassDefFoundError var7) {
               try {
                  Class.forName("de.corneliusmay.silkspawners.plugin.SilkSpawners");
                  SendMessage.warnMessage("SilkSpawners_V1 not found but instead found SilkSpawners_V2, updating config setting to use SilkSpawnersV2...");
                  if (this.plugin.getServer().getPluginManager().getPlugin(SpawnerManager.Provider.SILKSPAWNERSV2.getPluginName()).isEnabled()) {
                     this.provider = this.initProvider(SpawnerManager.Provider.SILKSPAWNERSV2, (Object)null);
                  } else {
                     SendMessage.warnMessage(getLog(Lang.FAILED_SPAWNER_PROVIDER_INTEGRATION.get(), SpawnerManager.Provider.SILKSPAWNERSV2.getName()));
                  }

                  ConfigManager.getConfig().set("spawner-provider", SpawnerManager.Provider.SILKSPAWNERSV2.name());
                  ConfigManager.saveConfig();
               } catch (ClassNotFoundException var6) {
                  SendMessage.warnMessage(getLog(Lang.FAILED_SPAWNER_PROVIDER_INTEGRATION.get(), providerName.getName()));
               }
            }
         } else if (!silent) {
            SendMessage.warnMessage(getLog(Lang.SPAWNER_PROVIDER_NOT_FOUND.get(), providerName.getName()));
         }
         break;
      case 9:
         this.provider = this.initProvider(providerName, (Object)null);
         SendMessage.infoMessage(Lang.SPAWNER_PROVIDER_DISABLED.get());
         break;
      case 10:
         this.provider = this.initProvider(providerName, (Object)null);
         SendMessage.infoMessage(Lang.USING_DEFAULT_SPAWNERS.get());
         break;
      case 11:
         SendMessage.infoMessage("Automatically searching for compatible spawner provider....");
         Iterator var3 = Arrays.asList(SpawnerManager.Provider.WILDSTACKER, SpawnerManager.Provider.ROSESTACKER, SpawnerManager.Provider.ULTIMATESTACKER, SpawnerManager.Provider.SILKSPAWNERSV2, SpawnerManager.Provider.SILKSPAWNERS, SpawnerManager.Provider.MINEABLESPAWNERS, SpawnerManager.Provider.SPAWNERMETA, SpawnerManager.Provider.SPAWNERLEGACY, SpawnerManager.Provider.SMARTSPAWNER).iterator();

         while(var3.hasNext()) {
            SpawnerManager.Provider provider = (SpawnerManager.Provider)var3.next();
            SpawnerProvider spawnerProvider = this.setup(provider, true);
            if (spawnerProvider != null) {
               break;
            }
         }

         if (this.provider == null) {
            SendMessage.infoMessage("Failed to automatically find compatible spawner provider, using default...");
            this.provider = this.initProvider(SpawnerManager.Provider.DEFAULT, (Object)null);
         }
      }

      return this.provider;
   }

   public SpawnerProvider getProvider() {
      return this.provider;
   }

   private static Translatable getLog(Translatable message, String provider) {
      return message.replace("%provider%", provider);
   }

   public static Translatable getDefaultName(EntityType type) {
      Translatable name = EconomyShopGUI.getInstance().getSpawnerName(type);
      if (name != null) {
         return name;
      } else {
         String[] arr = type.name().toLowerCase().replace("_", " ").split(" ");
         StringBuffer sb = new StringBuffer();

         for(int i = 0; i < arr.length; ++i) {
            sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
         }

         return Lang.DEFAULT_SPAWNER_NAME.get().replace("%spawner-type%", sb.toString().trim());
      }
   }

   private SpawnerProvider initProvider(SpawnerManager.Provider provider, @Nullable Object api) {
      switch(provider.ordinal()) {
      case 0:
         return new UltimateStackerHook();
      case 1:
         return new WildStackerHook((WildStacker)api);
      case 2:
         return new SilkSpawnersHook((SilkUtil)api);
      case 3:
         return new SilkSpawnersV2Hook();
      case 4:
         return new RoseStackerHook();
      case 5:
         return new MineableSpawnersHook();
      case 6:
         return new SpawnerMetaHook();
      case 7:
         return this.getSpawnerLegacyHook();
      case 8:
         return new SmartSpawnerHook();
      case 9:
         return new DefaultSpawnerProvider(this.plugin, (SpawnerInteractEvent)null, (SpawnerPlaceEvent)null, (SpawnerBreakEvent)null);
      case 10:
         return new DefaultSpawnerProvider(this.plugin, !ConfigManager.getConfig().getBoolean("allow-spawner-type-change") ? (this.interactEvent = new SpawnerInteractEvent()) : null, ConfigManager.getConfig().getBoolean("enable-spawnerplace") ? (this.placeEvent = new SpawnerPlaceEvent(this.plugin)) : null, ConfigManager.getConfig().getBoolean("enable-spawnerbreak") ? (this.breakEvent = new SpawnerBreakEvent(this.plugin)) : null);
      default:
         return new DefaultSpawnerProvider(this.plugin, !ConfigManager.getConfig().getBoolean("allow-spawner-type-change") ? (this.interactEvent = new SpawnerInteractEvent()) : null, ConfigManager.getConfig().getBoolean("enable-spawnerplace") ? (this.placeEvent = new SpawnerPlaceEvent(this.plugin)) : null, ConfigManager.getConfig().getBoolean("enable-spawnerbreak") ? (this.breakEvent = new SpawnerBreakEvent(this.plugin)) : null);
      }
   }

   private SpawnerProvider getSpawnerLegacyHook() {
      try {
         Class.forName("mc.rellox.spawnerlegacyapi.SLAPI");
         return new SpawnerLegacyHookV3();
      } catch (ClassNotFoundException var5) {
         try {
            Class.forName("mc.rellox.spawnerlegacy.lib.spawnerlegacyapi.SLAPI");
            return (SpawnerProvider)Class.forName("me.gypopo.economyshopgui.providers.spawners.SpawnerLegacyHookV2").newInstance();
         } catch (Exception var4) {
            try {
               return (SpawnerProvider)Class.forName("me.gypopo.economyshopgui.providers.spawners.SpawnerLegacyHookV1").newInstance();
            } catch (Exception var3) {
               SendMessage.errorMessage(getLog(Lang.FAILED_SPAWNER_PROVIDER_INTEGRATION.get(), "SpawnerLegacy"));
               var3.printStackTrace();
               return null;
            }
         }
      }
   }

   private static enum Provider {
      ULTIMATESTACKER("UltimateStacker"),
      WILDSTACKER("WildStacker"),
      SILKSPAWNERS("SilkSpawners"),
      SILKSPAWNERSV2("SilkSpawners_v2"),
      ROSESTACKER("RoseStacker"),
      MINEABLESPAWNERS("MineableSpawners"),
      SPAWNERMETA("SpawnerMeta"),
      SPAWNERLEGACY("SpawnerLegacy"),
      SMARTSPAWNER("SmartSpawner"),
      DISABLED("DISABLED"),
      DEFAULT("DEFAULT"),
      AUTO("AUTO");

      private final String name;

      private Provider(String param3) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      public String getPluginName() {
         return this.name;
      }

      public static SpawnerManager.Provider getFromString(String rawName) {
         try {
            SendMessage.infoMessage(SpawnerManager.getLog(Lang.SPAWNER_COMPATIBILITY_ENABLED.get(), rawName));
            return valueOf(rawName.toUpperCase(Locale.ENGLISH));
         } catch (IllegalArgumentException var2) {
            SendMessage.warnMessage("Failed to find a spawner provider using name '" + rawName + "'");
            return AUTO;
         }
      }

      // $FF: synthetic method
      private static SpawnerManager.Provider[] $values() {
         return new SpawnerManager.Provider[]{ULTIMATESTACKER, WILDSTACKER, SILKSPAWNERS, SILKSPAWNERSV2, ROSESTACKER, MINEABLESPAWNERS, SPAWNERMETA, SPAWNERLEGACY, SMARTSPAWNER, DISABLED, DEFAULT, AUTO};
      }
   }
}
