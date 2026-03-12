package me.gypopo.economyshopgui.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.methodes.UpdateChecker;
import me.gypopo.economyshopgui.providers.EconomyProvider;
import me.gypopo.economyshopgui.providers.economys.CoinsEngineEconomyV5;
import me.gypopo.economyshopgui.providers.economys.ExpEconomy;
import me.gypopo.economyshopgui.providers.economys.GemsEconomy;
import me.gypopo.economyshopgui.providers.economys.PlayerPointsEconomy;
import me.gypopo.economyshopgui.providers.economys.UltraEconomy;
import me.gypopo.economyshopgui.providers.economys.VaultEconomy;
import me.gypopo.economyshopgui.providers.economys.VotingPluginEconomy;
import me.gypopo.economyshopgui.providers.economys.ZEssentialsEconomy;
import me.gypopo.economyshopgui.providers.economys.formatter.PriceFormatter;
import me.gypopo.economyshopgui.util.exceptions.EconomyLoadException;

public class EconomyHandler {
   private final EconomyShopGUI plugin;
   private EconomyProvider defaultProvider;
   private Map<String, EconomyProvider> shopPaymentTypes = new HashMap();
   private Map<EcoType, EconomyProvider> registeredPaymentOptions = new HashMap();
   private static PriceFormatter.Format defaultFormatter;

   public EconomyHandler(EconomyShopGUI plugin) {
      this.plugin = plugin;
   }

   public static PriceFormatter.Format getDefaultFormatter() {
      return defaultFormatter;
   }

   public boolean registerPaymentSolutions() {
      defaultFormatter = PriceFormatter.getFormatter();
      if (this.loadDefaultEconomy() == null) {
         return false;
      } else {
         int i = 0;

         for(Iterator var2 = this.plugin.getShopSections().iterator(); var2.hasNext(); ++i) {
            String section = (String)var2.next();
            EcoType economyType = this.getEconomyType(section);
            if (economyType != null) {
               EconomyProvider provider = (EconomyProvider)this.registeredPaymentOptions.get(economyType);
               if (provider == null) {
                  try {
                     this.shopPaymentTypes.put(section, this.initProvider(economyType));
                     SendMessage.logDebugMessage(Lang.SECTION_ECONOMY_PROVIDER_LOADED.get().replace("%economy%", economyType.toString()).replace("%shop%", section));
                  } catch (EconomyLoadException var7) {
                     SendMessage.errorMessage(Lang.CANNOT_LOAD_ECONOMY_PROVIDER.get().replace("%shop%", section).replace("%reason%", var7.getMessage()));
                  }
               } else {
                  this.shopPaymentTypes.put(section, provider);
                  SendMessage.logDebugMessage(Lang.SECTION_ECONOMY_PROVIDER_LOADED.get().replace("%economy%", economyType.toString()).replace("%shop%", section));
               }
            }
         }

         SendMessage.infoMessage(Lang.ECONOMY_PROVIDERS_LOADED.get().replace("%total%", String.valueOf(this.registeredPaymentOptions.size())).replace("%shops%", String.valueOf(i)));
         return true;
      }
   }

   private EcoType getEconomyType(String section) {
      String type = ConfigManager.getSection(section).getString("economy");
      if (type == null) {
         return null;
      } else {
         EcoType economyType = EconomyType.getFromString(type);
         if (economyType == null) {
            SendMessage.warnMessage(Lang.CANNOT_FIND_ECONOMY_PROVIDER.get().replace("%shop%", section).replace("%type%", type));
            return null;
         } else {
            return economyType;
         }
      }
   }

   private EconomyProvider loadDefaultEconomy() {
      EcoType economyType = EconomyType.getFromString(ConfigManager.getConfig().getString("economy-provider"));
      if (economyType == null) {
         SendMessage.warnMessage(Lang.CANNOT_FIND_DEFAULT_ECONOMY_PROVIDER.get().replace("%economy%", ConfigManager.getConfig().getString("economy-provider")));
         return (this.defaultProvider = this.getAvailableEcon()) != null ? this.defaultProvider : null;
      } else {
         try {
            this.defaultProvider = this.initProvider(economyType);
         } catch (EconomyLoadException var3) {
            SendMessage.warnMessage(Lang.CANNOT_LOAD_DEFAULT_ECONOMY_PROVIDER.get().replace("%reason%", var3.getMessage()));
            return (this.defaultProvider = this.getAvailableEcon()) != null ? this.defaultProvider : null;
         }

         return this.defaultProvider;
      }
   }

   private EconomyProvider initProvider(EcoType type) throws EconomyLoadException {
      EconomyProvider provider = this.getProvider(type);
      provider.setup(this.plugin);
      this.registeredPaymentOptions.put(type, provider);
      return provider;
   }

   private EconomyProvider getProvider(EcoType type) {
      if (type == null) {
         return null;
      } else {
         switch(type.getType()) {
         case PLAYER_POINTS:
            return new PlayerPointsEconomy();
         case VAULT:
            return new VaultEconomy();
         case COINS_ENGINE:
            return this.getCoinsEngineInstance(type);
         case GEMS_ECONOMY:
            return type.getCurrency() != null ? new GemsEconomy(type.getCurrency()) : new GemsEconomy((String)null);
         case ULTRA_ECONOMY:
            return type.getCurrency() != null ? new UltraEconomy(type.getCurrency()) : new UltraEconomy((String)null);
         case VOTING_PLUGIN:
            return new VotingPluginEconomy();
         case EXP:
            return new ExpEconomy();
         case ZESSENTIALS:
            return new ZEssentialsEconomy(type.getCurrency());
         default:
            return null;
         }
      }
   }

   private EconomyProvider getAvailableEcon() {
      EconomyProvider provider = null;
      if (this.plugin.getServer().getPluginManager().isPluginEnabled("Vault")) {
         SendMessage.infoMessage("Detected Vault, initializing...");
         provider = new VaultEconomy();
      } else if (this.plugin.getServer().getPluginManager().isPluginEnabled("UltraEconomy")) {
         SendMessage.infoMessage("Detected UltraEconomy, initializing...");
         provider = new UltraEconomy((String)null);
      } else if (this.plugin.getServer().getPluginManager().isPluginEnabled("CoinsEngine")) {
         SendMessage.infoMessage("Detected CoinsEngine, initializing...");
         provider = this.getCoinsEngineInstance(new EcoType(EconomyType.COINS_ENGINE));
      } else if (this.plugin.getServer().getPluginManager().isPluginEnabled("GemsEconomy")) {
         SendMessage.infoMessage("Detected GemsEconomy, initializing...");
         provider = new GemsEconomy((String)null);
      } else if (this.plugin.getServer().getPluginManager().isPluginEnabled("PlayerPoints")) {
         SendMessage.infoMessage("Detected PlayerPoints, initializing...");
         provider = new PlayerPointsEconomy();
      } else if (this.plugin.getServer().getPluginManager().isPluginEnabled("VotingPlugin")) {
         SendMessage.infoMessage("Detected VotingPlugin, initializing...");
         provider = new VotingPluginEconomy();
      } else if (this.plugin.getServer().getPluginManager().isPluginEnabled("zEssentials")) {
         SendMessage.infoMessage("Detected zEssentials, initializing...");
         provider = new ZEssentialsEconomy((String)null);
      }

      if (provider != null) {
         try {
            ((EconomyProvider)provider).setup(this.plugin);
            this.registeredPaymentOptions.put(((EconomyProvider)provider).getType(), provider);
            return (EconomyProvider)provider;
         } catch (EconomyLoadException var3) {
         }
      }

      SendMessage.warnMessage("===================================================");
      SendMessage.warnMessage(" ");
      SendMessage.warnMessage("Failed to automatically load an available economy provider, this plugin requires one, disabling...");
      SendMessage.warnMessage("See a list of available economy providers on our wiki here: https://wiki.gpplugins.com/economyshopgui/file-configuration/config.yml#economy-provider");
      SendMessage.warnMessage(" ");
      SendMessage.warnMessage("===================================================");
      return null;
   }

   private EconomyProvider getCoinsEngineInstance(EcoType type) {
      String plugin = this.plugin.getServer().getPluginManager().getPlugin("CoinsEngine") != null ? "CoinsEngine" : (this.plugin.getServer().getPluginManager().getPlugin("ExcellentEconomy") != null ? "ExcellentEconomy" : null);
      UpdateChecker.Version ver = new UpdateChecker.Version(this.plugin.getServer().getPluginManager().getPlugin(plugin).getDescription().getVersion());
      if (ver.isGreater(new UpdateChecker.Version("2.6.1"))) {
         return type.getCurrency() != null ? new CoinsEngineEconomyV5(type.getCurrency()) : new CoinsEngineEconomyV5((String)null);
      } else {
         String version = "CoinsEngineEconomyV4";
         if (ver.isGreater(new UpdateChecker.Version("2.5.3"))) {
            version = "CoinsEngineEconomyV4";
         } else if (ver.isGreater(new UpdateChecker.Version("2.3.5"))) {
            version = "CoinsEngineEconomyV3";
         } else if (ver.isGreater(new UpdateChecker.Version("2.1.9"))) {
            version = "CoinsEngineEconomyV2";
         } else {
            version = "CoinsEngineEconomyV1";
         }

         try {
            Class<?> clazz = Class.forName("me.gypopo.economyshopgui.providers.economys." + version);
            Constructor<?> cons = clazz.getConstructor(String.class);
            return type.getCurrency() != null ? (EconomyProvider)cons.newInstance(type.getCurrency()) : (EconomyProvider)cons.newInstance((String)null);
         } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | ClassNotFoundException var7) {
            SendMessage.errorMessage("Failed to initialize " + version + "\nDetected CoinsEngine version: " + ver.toString());
            var7.printStackTrace();
            return null;
         }
      }
   }

   public boolean relead() {
      if (this.shopPaymentTypes.size() > 0) {
         this.shopPaymentTypes.clear();
      }

      if (this.registeredPaymentOptions.size() > 0) {
         this.registeredPaymentOptions.clear();
      }

      this.defaultProvider = null;
      return this.registerPaymentSolutions();
   }

   public EconomyProvider getEcon(String section) {
      return this.shopPaymentTypes.containsKey(section) ? (EconomyProvider)this.shopPaymentTypes.get(section) : this.defaultProvider;
   }

   public EconomyProvider getEcon(EcoType type) {
      return type == null ? this.defaultProvider : (EconomyProvider)this.registeredPaymentOptions.get(type);
   }

   public EconomyProvider getDefaultProvider() {
      return this.defaultProvider;
   }

   public EconomyProvider getLoaded(String eco) {
      EcoType type = EconomyType.getFromString(eco);
      return (EconomyProvider)this.registeredPaymentOptions.get(type);
   }
}
