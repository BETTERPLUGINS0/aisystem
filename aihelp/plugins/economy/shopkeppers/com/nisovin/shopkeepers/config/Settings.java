package com.nisovin.shopkeepers.config;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.config.lib.Config;
import com.nisovin.shopkeepers.config.lib.ConfigData;
import com.nisovin.shopkeepers.config.lib.ConfigLoadException;
import com.nisovin.shopkeepers.config.lib.setting.Setting;
import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.config.lib.value.types.ItemDataValue;
import com.nisovin.shopkeepers.config.lib.value.types.ListValue;
import com.nisovin.shopkeepers.config.migration.ConfigMigrations;
import com.nisovin.shopkeepers.currency.Currencies;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.items.ItemUpdates;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.playershops.MaxShopsPermission;
import com.nisovin.shopkeepers.playershops.PlayerShopsLimit;
import com.nisovin.shopkeepers.shopcreation.ShopCreationItem;
import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.tradelog.TradeLogStorageType;
import com.nisovin.shopkeepers.util.bukkit.ConfigUtils;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.bukkit.ServerUtils;
import com.nisovin.shopkeepers.util.bukkit.SoundEffect;
import com.nisovin.shopkeepers.util.inventory.ItemData;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.MathUtils;
import com.nisovin.shopkeepers.util.java.Trilean;
import com.nisovin.shopkeepers.util.logging.Log;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Settings extends Config {
   public static int configVersion = 10;
   public static int dataVersion = 4325;
   public static boolean debug = false;
   public static List<String> debugOptions = new ArrayList(0);
   public static boolean enableMetrics = true;
   public static String language = "en-default";
   public static boolean saveInstantly = true;
   public static boolean ignoreFailedServerAssumptionTests = false;
   public static boolean bypassSpawnBlocking = true;
   public static boolean deleteUnspawnableShopkeepers = false;
   public static boolean checkShopInteractionResult = false;
   public static boolean enableWorldGuardRestrictions = false;
   public static boolean requireWorldGuardAllowShopFlag = false;
   public static boolean registerWorldGuardAllowShopFlag = true;
   public static boolean enableTownyRestrictions = false;
   public static boolean checkSpawnLocationInteractionResult = false;
   public static boolean disableInventoryVerification = false;
   public static ItemData shopCreationItem;
   public static boolean addShopCreationItemTag;
   public static boolean identifyShopCreationItemByTag;
   public static boolean preventShopCreationItemRegularUsage;
   public static boolean invertShopTypeAndObjectTypeSelection;
   public static boolean deletingPlayerShopReturnsCreationItem;
   public static boolean requireContainerRecentlyPlaced;
   public static int maxContainerDistance;
   public static int maxShopsPerPlayer;
   public static String maxShopsPermOptions;
   public static boolean protectContainers;
   public static boolean preventItemMovement;
   public static boolean preventCopperGolemAccess;
   public static boolean deleteShopkeeperOnBreakContainer;
   public static int playerShopkeeperInactiveDays;
   public static List<String> enabledLivingShops;
   public static boolean enableEndCrystalShops;
   public static boolean allowEndCrystalShopsInTheEnd;
   public static boolean disableGravity;
   public static int gravityChunkRange;
   public static int entityBehaviorTickPeriod;
   public static boolean shulkerPeekIfPlayerNearby;
   public static float shulkerPeekHeight;
   public static int slimeMaxSize;
   public static int magmaCubeMaxSize;
   public static boolean silenceShopEntities;
   public static boolean showNameplates;
   public static boolean alwaysShowNameplates;
   public static boolean enableCitizenShops;
   public static EntityType defaultCitizenNpcType;
   public static boolean setCitizenNpcOwnerOfPlayerShops;
   public static Trilean citizenNpcFluidPushable;
   public static boolean cancelCitizenNpcInteractions;
   public static boolean saveCitizenNpcsInstantly;
   public static boolean snapshotsSaveCitizenNpcData;
   public static boolean deleteInvalidCitizenShopkeepers;
   public static boolean enableSignShops;
   public static boolean enableSignPostShops;
   public static boolean enableHangingSignShops;
   public static boolean enableGlowingSignText;
   public static String nameRegex;
   public static boolean namingOfPlayerShopsViaItem;
   public static boolean allowRenamingOfPlayerNpcShops;
   public static ItemData sellingEmptyTradeResultItem;
   public static ItemData sellingEmptyTradeItem1;
   public static ItemData sellingEmptyTradeItem2;
   public static ItemData sellingEmptyItem1;
   public static ItemData sellingEmptyItem2;
   public static ItemData buyingEmptyTradeResultItem;
   public static ItemData buyingEmptyTradeItem1;
   public static ItemData buyingEmptyTradeItem2;
   public static ItemData buyingEmptyResultItem;
   public static ItemData buyingEmptyItem2;
   public static ItemData tradingEmptyTradeResultItem;
   public static ItemData tradingEmptyTradeItem1;
   public static ItemData tradingEmptyTradeItem2;
   public static ItemData tradingEmptyResultItem;
   public static ItemData tradingEmptyItem1;
   public static ItemData tradingEmptyItem2;
   public static ItemData bookEmptyTradeResultItem;
   public static ItemData bookEmptyTradeItem1;
   public static ItemData bookEmptyTradeItem2;
   public static ItemData bookEmptyItem1;
   public static ItemData bookEmptyItem2;
   public static int maxTradesPages;
   public static ItemData previousPageItem;
   public static ItemData nextPageItem;
   public static ItemData currentPageItem;
   public static ItemData tradeSetupItem;
   public static ItemData shopInformationItem;
   public static ItemData placeholderItem;
   public static ItemData shopOpenItem;
   public static ItemData shopClosedItem;
   public static ItemData nameItem;
   public static boolean enableAllEquipmentEditorSlots;
   public static boolean enableMovingOfPlayerShops;
   public static ItemData moveItem;
   public static boolean enableContainerOptionOnPlayerShop;
   public static ItemData containerItem;
   public static ItemData tradeNotificationsItem;
   public static ItemData deleteItem;
   public static boolean disableOtherVillagers;
   public static List<String> disableOtherVillagersWorlds;
   public static boolean blockVillagerSpawns;
   public static List<String> blockVillagerSpawnsWorlds;
   public static boolean disableZombieVillagerCuring;
   public static List<String> disableZombieVillagerCuringWorlds;
   public static boolean hireOtherVillagers;
   public static List<String> hireOtherVillagersWorlds;
   public static boolean disableWanderingTraders;
   public static List<String> disableWanderingTradersWorlds;
   public static boolean blockWanderingTraderSpawns;
   public static List<String> blockWanderingTraderSpawnsWorlds;
   public static boolean hireWanderingTraders;
   public static List<String> hireWanderingTradersWorlds;
   public static boolean editRegularVillagers;
   public static boolean editRegularWanderingTraders;
   public static ItemData hireItem;
   public static int hireOtherVillagersCosts;
   public static boolean hireRequireCreationPermission;
   public static boolean preventTradingWithOwnShop;
   public static boolean preventTradingWhileOwnerIsOnline;
   public static boolean useStrictItemComparison;
   public static boolean incrementVillagerStatistics;
   public static boolean simulateVillagerTradingSounds;
   public static boolean simulateVillagerAmbientSounds;
   public static boolean simulateWanderingTraderTradingSounds;
   public static boolean simulateWanderingTraderAmbientSounds;
   public static boolean simulateTradingSoundsOnlyForTheTradingPlayer;
   public static SoundEffect tradeSucceededSound;
   public static SoundEffect tradeFailedSound;
   public static int taxRate;
   public static boolean taxRoundUp;
   public static boolean notifyPlayersAboutTrades;
   public static SoundEffect tradeNotificationSound;
   public static boolean notifyShopOwnersAboutTrades;
   public static SoundEffect shopOwnerTradeNotificationSound;
   public static TradeLogStorageType tradeLogStorage;
   public static int tradeLogMergeDurationTicks;
   public static int tradeLogNextMergeTimeoutTicks;
   public static boolean logItemMetadata;
   public static ItemData currencyItem;
   public static ItemData highCurrencyItem;
   public static int highCurrencyValue;
   public static int highCurrencyMinCost;
   private static final Settings INSTANCE;

   public static Settings.AsyncSettings async() {
      return Settings.AsyncSettings.INSTANCE;
   }

   public static void onSettingsChanged() {
      Currencies.load();
      Settings.DerivedSettings.setup();
      Settings.AsyncSettings.refresh();
   }

   private static ConfigData getPluginConfigData() {
      Plugin plugin = SKShopkeepersPlugin.getInstance();
      FileConfiguration pluginConfig = plugin.getConfig();
      ConfigUtils.disablePathSeparator(pluginConfig);
      return ConfigData.of((Object)pluginConfig);
   }

   @Nullable
   public static ConfigLoadException loadConfig() {
      Log.info("Loading config.");
      Plugin plugin = SKShopkeepersPlugin.getInstance();
      plugin.saveDefaultConfig();
      plugin.reloadConfig();
      ConfigData configData = getPluginConfigData();
      if (configData.isEmpty()) {
         return new ConfigLoadException("The config file is empty or could not be loaded. Please check the log and config file for errors, or delete the file to have it reset to the default config during the next restart.");
      } else {
         boolean configChanged;
         try {
            configChanged = loadConfig(configData);
         } catch (ConfigLoadException var4) {
            return var4;
         }

         if (configChanged) {
            saveConfig();
         }

         return null;
      }
   }

   private static boolean loadConfig(ConfigData configData) throws ConfigLoadException {
      boolean configChanged = false;
      boolean migrated = ConfigMigrations.applyMigrations(configData);
      if (migrated) {
         configChanged = true;
      }

      boolean insertedDefaults = INSTANCE.insertMissingDefaultValues(configData);
      if (insertedDefaults) {
         configChanged = true;
      }

      Setting<?> dataVersionSetting = INSTANCE.getSetting("data-version");

      assert dataVersionSetting != null;

      INSTANCE.loadSetting(configData, dataVersionSetting);
      ItemData.setSerializerDataVersion(dataVersion);
      INSTANCE.load(configData);
      boolean dataVersionChanged = INSTANCE.updateDataVersion();
      if (dataVersionChanged) {
         configChanged = true;
      }

      onSettingsChanged();
      return configChanged;
   }

   public static void saveConfig() {
      Log.info("Saving config.");
      Plugin plugin = SKShopkeepersPlugin.getInstance();
      ConfigData configData = getPluginConfigData();
      getInstance().save(configData);
      plugin.saveConfig();
   }

   public static Settings getInstance() {
      return INSTANCE;
   }

   private Settings() {
   }

   protected void validateSettings() {
      if (maxContainerDistance > 50) {
         Log.warning(this.getLogPrefix() + "'max-container-distance' can be at most 50.");
         maxContainerDistance = 50;
      }

      if (gravityChunkRange < 0) {
         Log.warning(this.getLogPrefix() + "'gravity-chunk-range' cannot be negative.");
         gravityChunkRange = 0;
      }

      if (entityBehaviorTickPeriod <= 0) {
         Log.warning(this.getLogPrefix() + "'entity-behavior-tick-period' has to be positive.");
         entityBehaviorTickPeriod = 1;
      }

      String var10000;
      if (shulkerPeekHeight < 0.0F || shulkerPeekHeight > 1.0F) {
         var10000 = this.getLogPrefix();
         Log.warning(var10000 + "'shulker-peek-height' must be between 0.0 and 1.0.");
         shulkerPeekHeight = (float)(shulkerPeekHeight < 0.0F ? 0 : 1);
      }

      if (slimeMaxSize < 1 || slimeMaxSize > 10) {
         Log.warning(this.getLogPrefix() + "'slime-max-size' must be between 1 and 10.");
         slimeMaxSize = MathUtils.clamp(slimeMaxSize, 1, 10);
      }

      if (magmaCubeMaxSize < 1 || magmaCubeMaxSize > 10) {
         Log.warning(this.getLogPrefix() + "'magma-cube-max-size' must be between 1 and 10.");
         magmaCubeMaxSize = MathUtils.clamp(magmaCubeMaxSize, 1, 10);
      }

      if (shopCreationItem.getType() == Material.AIR) {
         Log.warning(this.getLogPrefix() + "'shop-creation-item' can not be AIR.");
         shopCreationItem = shopCreationItem.withType(Material.VILLAGER_SPAWN_EGG);
      }

      if (hireItem.getType() == Material.AIR) {
         Log.warning(this.getLogPrefix() + "'hire-item' can not be AIR.");
         hireItem = hireItem.withType(Material.EMERALD);
      }

      if (currencyItem.getType() == Material.AIR) {
         Log.warning(this.getLogPrefix() + "'currency-item' can not be AIR.");
         currencyItem = currencyItem.withType(Material.EMERALD);
      }

      if (namingOfPlayerShopsViaItem && nameItem.getType() == Material.AIR) {
         Log.warning(this.getLogPrefix() + "'name-item' can not be AIR if 'naming-of-player-shops-via-item' is enabled!");
         nameItem = nameItem.withType(Material.NAME_TAG);
      }

      if (identifyShopCreationItemByTag && !addShopCreationItemTag) {
         Log.warning(this.getLogPrefix() + "'identify-shop-creation-item-by-tag' enabled, but 'add-shop-creation-item-tag' is disabled! Intended?");
      }

      if (maxTradesPages < 1) {
         Log.warning(this.getLogPrefix() + "'max-trades-pages' can not be less than 1!");
         maxTradesPages = 1;
      } else if (maxTradesPages > 10) {
         Log.warning(this.getLogPrefix() + "'max-trades-pages' can not be greater than 10!");
         maxTradesPages = 10;
      }

      if (taxRate < 0) {
         Log.warning(this.getLogPrefix() + "'tax-rate' can not be less than 0!");
         taxRate = 0;
      } else if (taxRate > 100) {
         Log.warning(this.getLogPrefix() + "'tax-rate' can not be larger than 100!");
         taxRate = 100;
      }

      if (tradeLogMergeDurationTicks < 0) {
         Log.warning(this.getLogPrefix() + "'trade-log-merge-duration-ticks' cannot be negative.");
         tradeLogMergeDurationTicks = 0;
      }

      if (tradeLogNextMergeTimeoutTicks < 0) {
         Log.warning(this.getLogPrefix() + "'trade-log-next-merge-timeout-ticks' cannot be negative.");
         tradeLogNextMergeTimeoutTicks = 0;
      }

      if (!disableInventoryVerification) {
         String serverName = Bukkit.getServer().getName();
         boolean forceDisableInventoryVerification = false;
         String serverDisplayName = "";
         if (serverName.contains("Mohist")) {
            forceDisableInventoryVerification = true;
            serverDisplayName = "Mohist";
         } else if (serverName.contains("Magma")) {
            forceDisableInventoryVerification = true;
            serverDisplayName = "Magma";
         }

         if (forceDisableInventoryVerification) {
            var10000 = this.getLogPrefix();
            Log.warning(var10000 + "Forcefully enabled 'disable-inventory-verification' to resolve a known incompatibility with " + serverDisplayName + " servers.");
            disableInventoryVerification = true;
         }
      }

   }

   private boolean updateDataVersion() {
      int currentDataVersion = ServerUtils.getDataVersion();
      if (dataVersion == currentDataVersion) {
         return false;
      } else {
         String var10000 = this.getLogPrefix();
         Log.info(var10000 + "'data-version' updated from " + dataVersion + " to " + currentDataVersion + ".");
         dataVersion = currentDataVersion;
         return true;
      }
   }

   public int updateItems() {
      int updatedItems = 0;
      Iterator var2 = this.getSettings().iterator();

      while(var2.hasNext()) {
         Setting<?> setting = (Setting)var2.next();
         ValueType<?> valueType = setting.getValueType();
         if (valueType instanceof ItemDataValue) {
            if (this.updateItemDataSetting((Setting)Unsafe.cast(setting))) {
               ++updatedItems;
            }
         } else if (valueType instanceof ListValue) {
            ListValue<?> listValueType = (ListValue)valueType;
            if (listValueType.getElementValueType() instanceof ItemDataValue) {
               updatedItems += this.updateItemDataListSetting((Setting)Unsafe.cast(setting));
            }
         }
      }

      if (updatedItems > 0) {
         this.validateSettings();
         onSettingsChanged();
         saveConfig();
      }

      return updatedItems;
   }

   private boolean updateItemDataSetting(Setting<ItemData> setting) {
      ItemData value = (ItemData)setting.getValue();
      ItemData newItemData = ItemUpdates.updateItemData(value);
      if (newItemData == value) {
         return false;
      } else {
         assert newItemData != null;

         String configKey = setting.getConfigKey();

         String var10000;
         try {
            setting.setValue(newItemData);
            var10000 = DebugOptions.itemUpdates;
            String var10001 = this.getLogPrefix();
            Log.debug(var10000, var10001 + "Updated item for setting '" + configKey + "'.");
            return true;
         } catch (ValueLoadException var8) {
            var10000 = this.getLogPrefix();
            Log.warning(var10000 + "Could not update item for setting '" + configKey + "': " + var8.getMessage());
            Iterator var6 = var8.getExtraMessages().iterator();

            while(var6.hasNext()) {
               String extraMessage = (String)var6.next();
               var10000 = this.getLogPrefix();
               Log.warning(var10000 + extraMessage);
            }

            return false;
         }
      }
   }

   private int updateItemDataListSetting(Setting<List<ItemData>> setting) {
      List<ItemData> value = (List)setting.getValue();
      if (value == null) {
         return 0;
      } else {
         int updatedItems = 0;
         int index = -1;
         ListIterator iterator = value.listIterator();

         while(iterator.hasNext()) {
            ++index;
            ItemData itemData = (ItemData)iterator.next();
            ItemData newItemData = ItemUpdates.updateItemData(itemData);
            if (newItemData != itemData) {
               assert newItemData != null;

               String configKey = setting.getConfigKey();
               Log.debug(DebugOptions.itemUpdates, this.getLogPrefix() + "Updated item " + index + " for setting '" + configKey + "'.");
               ++updatedItems;
               iterator.set(newItemData);
            }
         }

         return updatedItems;
      }
   }

   static {
      shopCreationItem = new ItemData(Material.VILLAGER_SPAWN_EGG, "{\"text\":\"Shopkeeper\",\"italic\":false,\"color\":\"green\"}", (List)null);
      addShopCreationItemTag = true;
      identifyShopCreationItemByTag = true;
      preventShopCreationItemRegularUsage = true;
      invertShopTypeAndObjectTypeSelection = false;
      deletingPlayerShopReturnsCreationItem = false;
      requireContainerRecentlyPlaced = true;
      maxContainerDistance = 15;
      maxShopsPerPlayer = -1;
      maxShopsPermOptions = "5,15,25";
      protectContainers = true;
      preventItemMovement = true;
      preventCopperGolemAccess = true;
      deleteShopkeeperOnBreakContainer = false;
      playerShopkeeperInactiveDays = 0;
      enabledLivingShops = (List)CollectionUtils.addAll(new ArrayList(Arrays.asList(EntityType.VILLAGER.name())), CollectionUtils.sort(Arrays.asList(EntityType.COW.name(), EntityType.MOOSHROOM.name(), EntityType.SHEEP.name(), EntityType.PIG.name(), EntityType.CHICKEN.name(), EntityType.OCELOT.name(), EntityType.RABBIT.name(), EntityType.WOLF.name(), EntityType.SNOW_GOLEM.name(), EntityType.IRON_GOLEM.name(), EntityType.BLAZE.name(), EntityType.SILVERFISH.name(), EntityType.POLAR_BEAR.name(), EntityType.SKELETON.name(), EntityType.STRAY.name(), EntityType.WITHER_SKELETON.name(), EntityType.SPIDER.name(), EntityType.CAVE_SPIDER.name(), EntityType.CREEPER.name(), EntityType.WITCH.name(), EntityType.ENDERMAN.name(), EntityType.ZOMBIE.name(), EntityType.ZOMBIE_VILLAGER.name(), EntityType.HUSK.name(), EntityType.GIANT.name(), EntityType.GHAST.name(), EntityType.SLIME.name(), EntityType.MAGMA_CUBE.name(), EntityType.SQUID.name(), EntityType.HORSE.name(), EntityType.MULE.name(), EntityType.DONKEY.name(), EntityType.SKELETON_HORSE.name(), EntityType.ZOMBIE_HORSE.name(), EntityType.SHULKER.name(), EntityType.ARMOR_STAND.name(), EntityType.EVOKER.name(), EntityType.VEX.name(), EntityType.VINDICATOR.name(), EntityType.ILLUSIONER.name(), EntityType.PARROT.name(), EntityType.TURTLE.name(), EntityType.PHANTOM.name(), EntityType.COD.name(), EntityType.SALMON.name(), EntityType.PUFFERFISH.name(), EntityType.TROPICAL_FISH.name(), EntityType.DROWNED.name(), EntityType.DOLPHIN.name(), EntityType.CAT.name(), EntityType.PANDA.name(), EntityType.PILLAGER.name(), EntityType.RAVAGER.name(), EntityType.LLAMA.name(), EntityType.TRADER_LLAMA.name(), EntityType.WANDERING_TRADER.name(), EntityType.FOX.name(), EntityType.BEE.name(), EntityType.ZOMBIFIED_PIGLIN.name(), EntityType.PIGLIN.name(), EntityType.HOGLIN.name(), EntityType.ZOGLIN.name(), EntityType.STRIDER.name(), EntityType.PIGLIN_BRUTE.name(), EntityType.AXOLOTL.name(), EntityType.GOAT.name(), EntityType.GLOW_SQUID.name(), EntityType.ALLAY.name(), EntityType.FROG.name(), EntityType.TADPOLE.name(), EntityType.WARDEN.name(), EntityType.CAMEL.name(), EntityType.SNIFFER.name(), EntityType.ARMADILLO.name(), EntityType.BOGGED.name(), EntityType.BREEZE.name(), EntityType.CREAKING.name(), "HAPPY_GHAST", "COPPER_GOLEM", "MANNEQUIN", "CAMEL_HUSK", "NAUTILUS", "PARCHED", "ZOMBIE_NAUTILUS"), String::compareTo));
      enableEndCrystalShops = true;
      allowEndCrystalShopsInTheEnd = false;
      disableGravity = false;
      gravityChunkRange = 4;
      entityBehaviorTickPeriod = 3;
      shulkerPeekIfPlayerNearby = true;
      shulkerPeekHeight = 0.3F;
      slimeMaxSize = 5;
      magmaCubeMaxSize = 5;
      silenceShopEntities = true;
      showNameplates = true;
      alwaysShowNameplates = false;
      enableCitizenShops = true;
      defaultCitizenNpcType = EntityType.PLAYER;
      setCitizenNpcOwnerOfPlayerShops = false;
      citizenNpcFluidPushable = Trilean.FALSE;
      cancelCitizenNpcInteractions = true;
      saveCitizenNpcsInstantly = false;
      snapshotsSaveCitizenNpcData = true;
      deleteInvalidCitizenShopkeepers = false;
      enableSignShops = true;
      enableSignPostShops = true;
      enableHangingSignShops = true;
      enableGlowingSignText = true;
      nameRegex = "[A-Za-z0-9 ]{3,25}";
      namingOfPlayerShopsViaItem = false;
      allowRenamingOfPlayerNpcShops = false;
      sellingEmptyTradeResultItem = new ItemData(Material.GRAY_STAINED_GLASS_PANE);
      sellingEmptyTradeItem1 = new ItemData(Material.GRAY_STAINED_GLASS_PANE);
      sellingEmptyTradeItem2 = new ItemData(Material.GRAY_STAINED_GLASS_PANE);
      sellingEmptyItem1 = new ItemData(Material.BARRIER);
      sellingEmptyItem2 = new ItemData(Material.BARRIER);
      buyingEmptyTradeResultItem = new ItemData(Material.GRAY_STAINED_GLASS_PANE);
      buyingEmptyTradeItem1 = new ItemData(Material.GRAY_STAINED_GLASS_PANE);
      buyingEmptyTradeItem2 = new ItemData(Material.AIR);
      buyingEmptyResultItem = new ItemData(Material.BARRIER);
      buyingEmptyItem2 = new ItemData(Material.AIR);
      tradingEmptyTradeResultItem = new ItemData(Material.GRAY_STAINED_GLASS_PANE);
      tradingEmptyTradeItem1 = new ItemData(Material.GRAY_STAINED_GLASS_PANE);
      tradingEmptyTradeItem2 = new ItemData(Material.GRAY_STAINED_GLASS_PANE);
      tradingEmptyResultItem = new ItemData(Material.BARRIER);
      tradingEmptyItem1 = new ItemData(Material.BARRIER);
      tradingEmptyItem2 = new ItemData(Material.BARRIER);
      bookEmptyTradeResultItem = new ItemData(Material.GRAY_STAINED_GLASS_PANE);
      bookEmptyTradeItem1 = new ItemData(Material.GRAY_STAINED_GLASS_PANE);
      bookEmptyTradeItem2 = new ItemData(Material.GRAY_STAINED_GLASS_PANE);
      bookEmptyItem1 = new ItemData(Material.BARRIER);
      bookEmptyItem2 = new ItemData(Material.BARRIER);
      maxTradesPages = 5;
      previousPageItem = new ItemData(Material.WRITABLE_BOOK);
      nextPageItem = new ItemData(Material.WRITABLE_BOOK);
      currentPageItem = new ItemData(Material.WRITABLE_BOOK);
      tradeSetupItem = new ItemData(Material.PAPER);
      shopInformationItem = new ItemData(Material.PAPER);
      placeholderItem = new ItemData(Material.PAPER);
      shopOpenItem = new ItemData(Material.GREEN_BANNER);
      shopClosedItem = new ItemData(Material.RED_BANNER);
      nameItem = new ItemData(Material.NAME_TAG);
      enableAllEquipmentEditorSlots = false;
      enableMovingOfPlayerShops = true;
      moveItem = new ItemData(Material.ENDER_PEARL);
      enableContainerOptionOnPlayerShop = true;
      containerItem = new ItemData(Material.CHEST);
      tradeNotificationsItem = new ItemData(Material.BELL);
      deleteItem = new ItemData(Material.BONE);
      disableOtherVillagers = false;
      disableOtherVillagersWorlds = new ArrayList();
      blockVillagerSpawns = false;
      blockVillagerSpawnsWorlds = new ArrayList();
      disableZombieVillagerCuring = false;
      disableZombieVillagerCuringWorlds = new ArrayList();
      hireOtherVillagers = false;
      hireOtherVillagersWorlds = new ArrayList();
      disableWanderingTraders = false;
      disableWanderingTradersWorlds = new ArrayList();
      blockWanderingTraderSpawns = false;
      blockWanderingTraderSpawnsWorlds = new ArrayList();
      hireWanderingTraders = false;
      hireWanderingTradersWorlds = new ArrayList();
      editRegularVillagers = false;
      editRegularWanderingTraders = false;
      hireItem = new ItemData(Material.EMERALD);
      hireOtherVillagersCosts = 1;
      hireRequireCreationPermission = true;
      preventTradingWithOwnShop = true;
      preventTradingWhileOwnerIsOnline = false;
      useStrictItemComparison = false;
      incrementVillagerStatistics = false;
      simulateVillagerTradingSounds = true;
      simulateVillagerAmbientSounds = false;
      simulateWanderingTraderTradingSounds = true;
      simulateWanderingTraderAmbientSounds = false;
      simulateTradingSoundsOnlyForTheTradingPlayer = true;
      tradeSucceededSound = (new SoundEffect(Sound.UI_BUTTON_CLICK)).withPitch(2.0F).withVolume(0.3F);
      tradeFailedSound = (new SoundEffect(Sound.BLOCK_BARREL_CLOSE)).withPitch(2.0F).withVolume(0.5F);
      taxRate = 0;
      taxRoundUp = false;
      notifyPlayersAboutTrades = false;
      tradeNotificationSound = SoundEffect.EMPTY;
      notifyShopOwnersAboutTrades = true;
      shopOwnerTradeNotificationSound = (new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP)).withVolume(0.25F);
      tradeLogStorage = TradeLogStorageType.DISABLED;
      tradeLogMergeDurationTicks = 300;
      tradeLogNextMergeTimeoutTicks = 100;
      logItemMetadata = false;
      currencyItem = new ItemData(Material.EMERALD);
      highCurrencyItem = new ItemData(Material.EMERALD_BLOCK);
      highCurrencyValue = 9;
      highCurrencyMinCost = 20;
      INSTANCE = new Settings();
   }

   public static final class AsyncSettings {
      private static volatile Settings.AsyncSettings INSTANCE = new Settings.AsyncSettings();
      public final boolean debug;
      public final List<? extends String> debugOptions;

      private static void refresh() {
         INSTANCE = new Settings.AsyncSettings();
      }

      private AsyncSettings() {
         this.debug = Settings.debug;
         this.debugOptions = Collections.unmodifiableList(new ArrayList(Settings.debugOptions));
      }
   }

   public static class DerivedSettings {
      private static boolean initialSetup = true;
      public static DateTimeFormatter dateTimeFormatter = (DateTimeFormatter)Unsafe.uncheckedNull();
      public static TradingRecipeDraft sellingEmptyTrade = (TradingRecipeDraft)Unsafe.uncheckedNull();
      public static TradingRecipeDraft sellingEmptyTradeSlotItems = (TradingRecipeDraft)Unsafe.uncheckedNull();
      public static TradingRecipeDraft buyingEmptyTrade = (TradingRecipeDraft)Unsafe.uncheckedNull();
      public static TradingRecipeDraft buyingEmptyTradeSlotItems = (TradingRecipeDraft)Unsafe.uncheckedNull();
      public static TradingRecipeDraft tradingEmptyTrade = (TradingRecipeDraft)Unsafe.uncheckedNull();
      public static TradingRecipeDraft tradingEmptyTradeSlotItems = (TradingRecipeDraft)Unsafe.uncheckedNull();
      public static TradingRecipeDraft bookEmptyTrade = (TradingRecipeDraft)Unsafe.uncheckedNull();
      public static TradingRecipeDraft bookEmptyTradeSlotItems = (TradingRecipeDraft)Unsafe.uncheckedNull();
      public static ItemData shopCreationItemData = (ItemData)Unsafe.uncheckedNull();
      public static ItemData placeholderItemData = (ItemData)Unsafe.uncheckedNull();
      public static ItemData namingItemData = (ItemData)Unsafe.uncheckedNull();
      public static ItemData shopOpenButtonItem = (ItemData)Unsafe.uncheckedNull();
      public static ItemData shopClosedButtonItem = (ItemData)Unsafe.uncheckedNull();
      public static ItemData nameButtonItem = (ItemData)Unsafe.uncheckedNull();
      public static ItemData moveButtonItem = (ItemData)Unsafe.uncheckedNull();
      public static ItemData containerButtonItem = (ItemData)Unsafe.uncheckedNull();
      public static ItemData deleteButtonItem = (ItemData)Unsafe.uncheckedNull();
      public static ItemData hireButtonItem = (ItemData)Unsafe.uncheckedNull();
      public static ItemData deleteVillagerButtonItem = (ItemData)Unsafe.uncheckedNull();
      public static ItemData nameVillagerButtonItem = (ItemData)Unsafe.uncheckedNull();
      public static ItemData villagerInventoryButtonItem = (ItemData)Unsafe.uncheckedNull();
      public static Pattern shopNamePattern = (Pattern)Unsafe.uncheckedNull();
      public static final List<MaxShopsPermission> maxShopsPermissions = new ArrayList();
      public static final Set<EntityType> enabledLivingShops = new LinkedHashSet();

      private static void setup() {
         String var10000;
         try {
            dateTimeFormatter = DateTimeFormatter.ofPattern(Messages.dateTimeFormat).withZone((ZoneId)Unsafe.assertNonNull(ZoneId.systemDefault()));
         } catch (IllegalArgumentException var5) {
            var10000 = Messages.getInstance().getLogPrefix();
            Log.warning(var10000 + "'date-time-format' is not a valid format pattern ('" + Messages.dateTimeFormat + "'). Reverting to default.");
            Messages.dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
            dateTimeFormatter = DateTimeFormatter.ofPattern(Messages.dateTimeFormat).withZone((ZoneId)Unsafe.assertNonNull(ZoneId.systemDefault()));
         }

         sellingEmptyTrade = new TradingRecipeDraft(ItemUtils.setDisplayNameAndLore(Settings.sellingEmptyTradeResultItem.createItemStack(), Messages.sellingShop_emptyTrade_resultItem, Messages.sellingShop_emptyTrade_resultItemLore), ItemUtils.setDisplayNameAndLore(Settings.sellingEmptyTradeItem1.createItemStack(), Messages.sellingShop_emptyTrade_item1, Messages.sellingShop_emptyTrade_item1Lore), Currencies.isHighCurrencyEnabled() ? ItemUtils.setDisplayNameAndLore(Settings.sellingEmptyTradeItem2.createItemStack(), Messages.sellingShop_emptyTrade_item2, Messages.sellingShop_emptyTrade_item2Lore) : Settings.sellingEmptyTradeItem2.createItemStack());
         sellingEmptyTradeSlotItems = new TradingRecipeDraft((ItemStack)null, ItemUtils.setDisplayNameAndLore(Settings.sellingEmptyItem1.createItemStack(), Messages.sellingShop_emptyItem1, Messages.sellingShop_emptyItem1Lore), Currencies.isHighCurrencyEnabled() ? ItemUtils.setDisplayNameAndLore(Settings.sellingEmptyItem2.createItemStack(), Messages.sellingShop_emptyItem2, Messages.sellingShop_emptyItem2Lore) : Settings.sellingEmptyItem2.createItemStack());
         buyingEmptyTrade = new TradingRecipeDraft(ItemUtils.setDisplayNameAndLore(Settings.buyingEmptyTradeResultItem.createItemStack(), Messages.buyingShop_emptyTrade_resultItem, Messages.buyingShop_emptyTrade_resultItemLore), ItemUtils.setDisplayNameAndLore(Settings.buyingEmptyTradeItem1.createItemStack(), Messages.buyingShop_emptyTrade_item1, Messages.buyingShop_emptyTrade_item1Lore), Settings.buyingEmptyTradeItem2.createItemStack());
         buyingEmptyTradeSlotItems = new TradingRecipeDraft(ItemUtils.setDisplayNameAndLore(Settings.buyingEmptyResultItem.createItemStack(), Messages.buyingShop_emptyResultItem, Messages.buyingShop_emptyResultItemLore), (ItemStack)null, Settings.buyingEmptyItem2.createItemStack());
         tradingEmptyTrade = new TradingRecipeDraft(ItemUtils.setDisplayNameAndLore(Settings.tradingEmptyTradeResultItem.createItemStack(), Messages.tradingShop_emptyTrade_resultItem, Messages.tradingShop_emptyTrade_resultItemLore), ItemUtils.setDisplayNameAndLore(Settings.tradingEmptyTradeItem1.createItemStack(), Messages.tradingShop_emptyTrade_item1, Messages.tradingShop_emptyTrade_item1Lore), ItemUtils.setDisplayNameAndLore(Settings.tradingEmptyTradeItem2.createItemStack(), Messages.tradingShop_emptyTrade_item2, Messages.tradingShop_emptyTrade_item2Lore));
         tradingEmptyTradeSlotItems = new TradingRecipeDraft(ItemUtils.setDisplayNameAndLore(Settings.tradingEmptyResultItem.createItemStack(), Messages.tradingShop_emptyResultItem, Messages.tradingShop_emptyResultItemLore), ItemUtils.setDisplayNameAndLore(Settings.tradingEmptyItem1.createItemStack(), Messages.tradingShop_emptyItem1, Messages.tradingShop_emptyItem1Lore), ItemUtils.setDisplayNameAndLore(Settings.tradingEmptyItem2.createItemStack(), Messages.tradingShop_emptyItem2, Messages.tradingShop_emptyItem2Lore));
         bookEmptyTrade = new TradingRecipeDraft(ItemUtils.setDisplayNameAndLore(Settings.bookEmptyTradeResultItem.createItemStack(), Messages.bookShop_emptyTrade_resultItem, Messages.bookShop_emptyTrade_resultItemLore), ItemUtils.setDisplayNameAndLore(Settings.bookEmptyTradeItem1.createItemStack(), Messages.bookShop_emptyTrade_item1, Messages.bookShop_emptyTrade_item1Lore), Currencies.isHighCurrencyEnabled() ? ItemUtils.setDisplayNameAndLore(Settings.bookEmptyTradeItem2.createItemStack(), Messages.bookShop_emptyTrade_item2, Messages.bookShop_emptyTrade_item2Lore) : Settings.bookEmptyTradeItem2.createItemStack());
         bookEmptyTradeSlotItems = new TradingRecipeDraft((ItemStack)null, ItemUtils.setDisplayNameAndLore(Settings.bookEmptyItem1.createItemStack(), Messages.bookShop_emptyItem1, Messages.bookShop_emptyItem1Lore), Currencies.isHighCurrencyEnabled() ? ItemUtils.setDisplayNameAndLore(Settings.bookEmptyItem2.createItemStack(), Messages.bookShop_emptyItem2, Messages.bookShop_emptyItem2Lore) : Settings.bookEmptyItem2.createItemStack());
         if (Settings.addShopCreationItemTag) {
            ItemStack shopCreationItemStack = Settings.shopCreationItem.createItemStack();
            ShopCreationItem shopCreationItemHelper = new ShopCreationItem(shopCreationItemStack);
            shopCreationItemHelper.addTag();
            shopCreationItemHelper.applyItemMeta();
            shopCreationItemData = new ItemData(UnmodifiableItemStack.ofNonNull(shopCreationItemStack));
         } else {
            shopCreationItemData = Settings.shopCreationItem;
         }

         placeholderItemData = new ItemData(UnmodifiableItemStack.ofNonNull(ItemUtils.setDisplayName(Settings.placeholderItem.createItemStack(), (String)null)));
         namingItemData = new ItemData(UnmodifiableItemStack.ofNonNull(ItemUtils.setDisplayName(Settings.nameItem.createItemStack(), (String)null)));
         shopOpenButtonItem = new ItemData(Settings.shopOpenItem, Messages.buttonShopOpen, Messages.buttonShopOpenLore);
         shopClosedButtonItem = new ItemData(Settings.shopClosedItem, Messages.buttonShopClosed, Messages.buttonShopClosedLore);
         nameButtonItem = new ItemData(Settings.nameItem, Messages.buttonName, Messages.buttonNameLore);
         moveButtonItem = new ItemData(Settings.moveItem, Messages.buttonMove, Messages.buttonMoveLore);
         containerButtonItem = new ItemData(Settings.containerItem, Messages.buttonContainer, Messages.buttonContainerLore);
         deleteButtonItem = new ItemData(Settings.deleteItem, Messages.buttonDelete, Messages.buttonDeleteLore);
         hireButtonItem = new ItemData(Settings.hireItem, Messages.buttonHire, Messages.buttonHireLore);
         deleteVillagerButtonItem = new ItemData(Settings.deleteItem, Messages.buttonDeleteVillager, Messages.buttonDeleteVillagerLore);
         nameVillagerButtonItem = new ItemData(Settings.nameItem, Messages.buttonNameVillager, Messages.buttonNameVillagerLore);
         villagerInventoryButtonItem = new ItemData(Settings.containerItem, Messages.buttonVillagerInventory, Messages.buttonVillagerInventoryLore);

         try {
            shopNamePattern = Pattern.compile("^" + Settings.nameRegex + "$");
         } catch (PatternSyntaxException var4) {
            var10000 = Settings.INSTANCE.getLogPrefix();
            Log.warning(var10000 + "'name-regex' is not a valid regular expression ('" + Settings.nameRegex + "'). Reverting to default.");
            Settings.nameRegex = "[A-Za-z0-9 ]{3,25}";
            shopNamePattern = Pattern.compile("^" + Settings.nameRegex + "$");
         }

         PlayerShopsLimit.updateMaxShopsPermissions((invalidPermissionOption) -> {
            String var10000 = Settings.INSTANCE.getLogPrefix();
            Log.warning(var10000 + "Ignoring invalid entry in 'max-shops-perm-options': " + invalidPermissionOption);
         });
         enabledLivingShops.clear();
         boolean foundInvalidEntityType = false;
         Iterator var7 = Settings.enabledLivingShops.iterator();

         while(true) {
            while(var7.hasNext()) {
               String entityTypeId = (String)var7.next();
               EntityType entityType = EntityUtils.parseEntityType(entityTypeId);
               if (entityType != null && entityType.isAlive() && entityType.isSpawnable()) {
                  enabledLivingShops.add(entityType);
               } else if (!initialSetup) {
                  foundInvalidEntityType = true;
                  if ("PIG_ZOMBIE".equals(entityTypeId)) {
                     Log.warning(Settings.INSTANCE.getLogPrefix() + "Ignoring mob type 'PIG_ZOMBIE' in setting 'enabled-living-shops'. This mob no longer exists since MC 1.16. Consider replacing it with 'ZOMBIFIED_PIGLIN'.");
                  } else if ("MUSHROOM_COW".equals(entityTypeId)) {
                     Log.warning(Settings.INSTANCE.getLogPrefix() + "Ignoring mob type 'MUSHROOM_COW' in setting 'enabled-living-shops'. This mob was renamed in MC 1.20.5: Consider replacing it with 'MOOSHROOM'.");
                  } else if ("SNOWMAN".equals(entityTypeId)) {
                     Log.warning(Settings.INSTANCE.getLogPrefix() + "Ignoring mob type 'SNOWMAN' in setting 'enabled-living-shops'. This mob was renamed in MC 1.20.5: Consider replacing it with 'SNOW_GOLEM'.");
                  } else {
                     var10000 = Settings.INSTANCE.getLogPrefix();
                     Log.warning(var10000 + "Invalid living entity type name in 'enabled-living-shops': " + entityTypeId);
                  }
               }
            }

            if (foundInvalidEntityType) {
               Log.warning(Settings.INSTANCE.getLogPrefix() + "All existing entity type names can be found here: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html");
            }

            return;
         }
      }

      private DerivedSettings() {
      }

      static {
         setup();
         initialSetup = false;
      }
   }
}
