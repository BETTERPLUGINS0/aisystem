package com.nisovin.shopkeepers.storage;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.storage.ShopkeeperStorage;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.debug.Debug;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.storage.migration.RawDataMigrations;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.PluginUtils;
import com.nisovin.shopkeepers.util.bukkit.SchedulerUtils;
import com.nisovin.shopkeepers.util.bukkit.SingletonTask;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.persistence.DataStore;
import com.nisovin.shopkeepers.util.data.persistence.InvalidDataFormatException;
import com.nisovin.shopkeepers.util.data.persistence.bukkit.BukkitConfigDataStore;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.FileUtils;
import com.nisovin.shopkeepers.util.java.Retry;
import com.nisovin.shopkeepers.util.java.ThrowableUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKShopkeeperStorage implements ShopkeeperStorage {
   private static final String DATA_FOLDER = "data";
   private static final String SAVE_FILE_NAME = "save.yml";
   private static final String DATA_VERSION_KEY = "data-version";
   private static final List<String> HEADER = Collections.unmodifiableList(Arrays.asList("This file is not intended to be manually modified! If you want to manually edit this file anyway, ensure that the server is not running currently and that you have prepared a backup of this file."));
   private static final int DELAYED_SAVE_TICKS = 600;
   private static final int SAVING_MAX_ATTEMPTS = 20;
   private static final long SAVING_ATTEMPTS_DELAY_MILLIS = 25L;
   private static final long SAVE_ERROR_MSG_THROTTLE_MILLIS;
   private final SKShopkeepersPlugin plugin;
   private final Path saveFile;
   private final BukkitConfigDataStore saveData = BukkitConfigDataStore.ofNewYamlConfig();
   private int maxUsedShopkeeperId = 0;
   private int nextShopkeeperId = 1;
   private boolean pendingSaveRequest = false;
   private Set<AbstractShopkeeper> dirtyShopkeepers = new LinkedHashSet();
   private final Set<Integer> unsavedShopkeepers = new HashSet();
   private final Set<Integer> unsavedDeletedShopkeepers = new HashSet();
   private final Set<AbstractShopkeeper> shopkeepersToDelete = new LinkedHashSet();
   private boolean currentlyLoading = false;
   private final SKShopkeeperStorage.SaveTask saveTask;
   private boolean savingDisabled = false;
   @Nullable
   private BukkitTask delayedSaveTask = null;

   public SKShopkeeperStorage(SKShopkeepersPlugin plugin) {
      DataVersion.init();
      this.plugin = plugin;
      this.saveFile = ((SKShopkeeperStorage)Unsafe.initialized(this))._getSaveFile();
      this.saveTask = new SKShopkeeperStorage.SaveTask(plugin);
   }

   private Path getPluginDataFolder() {
      return this.plugin.getDataFolder().toPath();
   }

   private Path _getDataFolder() {
      return this.getPluginDataFolder().resolve("data");
   }

   private Path _getSaveFile() {
      return this._getDataFolder().resolve("save.yml");
   }

   public void onEnable() {
      if (!Settings.saveInstantly) {
         (new SKShopkeeperStorage.PeriodicSaveTask()).start();
      }

   }

   public void onDisable() {
      this.saveIfDirtyAndAwaitCompletion();
      boolean var10000;
      if (this.isDirty()) {
         var10000 = this.pendingSaveRequest;
         Log.warning("The shopkeeper storage is still dirty (pendingSaveRequest=" + var10000 + ", dirtyShopkeepers=" + this.dirtyShopkeepers.size() + ", unsavedShopkeepers=" + this.unsavedShopkeepers.size() + ", unsavedDeletedShopkeepers=" + this.unsavedDeletedShopkeepers.size() + ", shopkeepersToDelete=" + this.shopkeepersToDelete.size() + "). Did the previous save fail? Data might have been lost!");
      }

      if (this.saveTask.isRunning() || this.saveTask.isExecutionPending()) {
         var10000 = this.saveTask.isRunning();
         Log.warning("There is still a save of shopkeeper data in progress (" + var10000 + ") or pending execution (" + this.saveTask.isExecutionPending() + ")!");
      }

      this.saveTask.onDisable();
      this.clearSaveData();
      this.savingDisabled = false;
      this.pendingSaveRequest = false;
      this.dirtyShopkeepers.clear();
      this.unsavedShopkeepers.clear();
      this.unsavedDeletedShopkeepers.clear();
      this.shopkeepersToDelete.clear();
      this.delayedSaveTask = null;
   }

   private SKShopkeeperRegistry getShopkeeperRegistry() {
      return this.plugin.getShopkeeperRegistry();
   }

   public int getNextShopkeeperId() {
      int nextId = this.nextShopkeeperId;
      if (nextId <= 0 || !this.isUnusedId(nextId)) {
         int maxId = this.maxUsedShopkeeperId;

         assert maxId > 0;

         if (maxId < Integer.MAX_VALUE) {
            nextId = maxId + 1;

            assert this.isUnusedId(nextId);
         } else {
            for(nextId = 1; !this.isUnusedId(nextId); ++nextId) {
               if (nextId == Integer.MAX_VALUE) {
                  throw new IllegalStateException("No unused shopkeeper ids available!");
               }
            }

            assert nextId > 0;
         }

         this.nextShopkeeperId = nextId;
      }

      return nextId;
   }

   private boolean isUnusedId(int id) {
      if (this.saveData.contains(String.valueOf(id))) {
         return false;
      } else if (this.unsavedDeletedShopkeepers.contains(id)) {
         return false;
      } else {
         Iterator var2 = this.shopkeepersToDelete.iterator();

         Shopkeeper shopkeeper;
         do {
            if (!var2.hasNext()) {
               var2 = this.dirtyShopkeepers.iterator();

               do {
                  if (!var2.hasNext()) {
                     return true;
                  }

                  shopkeeper = (Shopkeeper)var2.next();
               } while(shopkeeper.getId() != id);

               return false;
            }

            shopkeeper = (Shopkeeper)var2.next();
         } while(shopkeeper.getId() != id);

         return false;
      }
   }

   public void onShopkeeperIdUsed(int id) {
      if (id > this.maxUsedShopkeeperId) {
         this.maxUsedShopkeeperId = id;
      }

      if (id >= this.nextShopkeeperId) {
         this.nextShopkeeperId = id + 1;
      }

   }

   private void rollbackNextShopkeeperId() {
      int maxUsedId = this.maxUsedShopkeeperId;

      assert maxUsedId >= 0 && maxUsedId <= Integer.MAX_VALUE;

      while(maxUsedId > 0 && this.isUnusedId(maxUsedId)) {
         --maxUsedId;
      }

      if (maxUsedId != this.maxUsedShopkeeperId) {
         this.maxUsedShopkeeperId = maxUsedId;
         this.nextShopkeeperId = maxUsedId + 1;
      }

   }

   private void clearSaveData() {
      this.saveData.clear();
      this.maxUsedShopkeeperId = 0;
      this.nextShopkeeperId = 1;
   }

   public boolean reload() {
      if (this.currentlyLoading) {
         throw new IllegalStateException("Already loading right now!");
      } else {
         this.saveIfDirtyAndAwaitCompletion();
         this.currentlyLoading = true;

         boolean result;
         try {
            result = this.doReload();
         } catch (Exception var6) {
            Log.severe((String)"Something unexpected went wrong during the loading of the saved shopkeepers data!", (Throwable)var6);
            result = false;
         } finally {
            this.currentlyLoading = false;
         }

         return result;
      }
   }

   private boolean doReload() {
      SKShopkeeperRegistry shopkeeperRegistry = this.getShopkeeperRegistry();
      shopkeeperRegistry.unloadAllShopkeepers();
      this.clearSaveData();
      Path saveFile = this.saveFile;
      Path var10000;
      if (!Files.exists(saveFile, new LinkOption[0])) {
         Path tempSaveFile = FileUtils.getTempSibling(saveFile);
         if (!Files.exists(tempSaveFile, new LinkOption[0])) {
            this.saveData.set("data-version", DataVersion.current().toString());
            return true;
         }

         var10000 = PluginUtils.relativize(this.plugin, tempSaveFile);
         Log.warning("Found no save file, but an existing temporary save file (" + String.valueOf(var10000) + ")! This might indicate an issue during a previous saving attempt! We try to load the Shopkeepers data from this temporary save file instead!");
         saveFile = tempSaveFile;
      }

      boolean rawDataMigrated = false;

      String dataVersionString;
      boolean forceSaveAllShopkeepers;
      try {
         BufferedReader reader = Files.newBufferedReader(saveFile, StandardCharsets.UTF_8);

         label150: {
            try {
               label151: {
                  dataVersionString = FileUtils.read(reader);
                  String migratedContent = RawDataMigrations.applyMigrations(dataVersionString);
                  rawDataMigrated = !dataVersionString.equals(migratedContent);
                  if (rawDataMigrated) {
                     LocalDateTime now = LocalDateTime.now();
                     String var10001 = now.format(FileUtils.DATE_TIME_FORMATTER);
                     Path backupSaveFile = saveFile.resolveSibling(var10001 + "_" + String.valueOf(saveFile.getFileName()) + ".backup");
                     var10000 = PluginUtils.relativize(this.plugin, backupSaveFile);
                     Log.info("Shopkeeper data migrated. Writing backup to " + String.valueOf(var10000));

                     try {
                        Files.copy(saveFile, backupSaveFile);
                     } catch (Exception var16) {
                        Log.severe((String)"Failed to write backup file!", (Throwable)var16);
                        forceSaveAllShopkeepers = false;
                        break label151;
                     }
                  }

                  if (Debug.isDebugging() && rawDataMigrated) {
                     Path migratedSaveFile = saveFile.resolveSibling(String.valueOf(saveFile.getFileName()) + ".migrated");
                     var10000 = PluginUtils.relativize(this.plugin, migratedSaveFile);
                     Log.info("Writing migrated save file to " + String.valueOf(var10000));

                     try {
                        FileUtils.writeSafely(migratedSaveFile, migratedContent, StandardCharsets.UTF_8, Log.getLogger(), this.getPluginDataFolder());
                     } catch (Exception var15) {
                        Log.warning((String)("Failed to write migrated save file (" + String.valueOf(PluginUtils.relativize(this.plugin, migratedSaveFile)) + "). This file is only written for debugging purposes. Continuing the data loading ..."), (Throwable)var15);
                     }
                  }

                  this.saveData.loadFromString(migratedContent);
                  break label150;
               }
            } catch (Throwable var17) {
               if (reader != null) {
                  try {
                     reader.close();
                  } catch (Throwable var13) {
                     var17.addSuppressed(var13);
                  }
               }

               throw var17;
            }

            if (reader != null) {
               reader.close();
            }

            return forceSaveAllShopkeepers;
         }

         if (reader != null) {
            reader.close();
         }
      } catch (InvalidDataFormatException var18) {
         Log.severe((String)"Failed to load the save file! Note: Server downgrades or manually editing the save file are not supported!", (Throwable)var18);
         return false;
      } catch (Exception var19) {
         Log.severe((String)"Failed to load the save file!", (Throwable)var19);
         return false;
      }

      Map<? extends String, ?> saveDataEntries = this.saveData.getValuesCopy();
      this.saveData.clear();
      this.saveData.set("data-version", DataVersion.MISSING.toString());
      this.saveData.setAll(saveDataEntries);
      dataVersionString = this.saveData.getString("data-version");

      assert dataVersionString != null;

      DataVersion dataVersion;
      try {
         dataVersion = DataVersion.parse(dataVersionString);
      } catch (IllegalArgumentException var14) {
         Log.severe((String)"Failed to parse the data version of the save file!", (Throwable)var14);
         return false;
      }

      String var26;
      if (DataVersion.current().isMinecraftDowngrade(dataVersion)) {
         var26 = String.valueOf(dataVersion);
         Log.severe("Detected Minecraft server downgrade from data version '" + var26 + "' to '" + String.valueOf(DataVersion.current()) + "'! Server downgrades are not supported. Disabling the plugin in order to prevent data loss!");
         return false;
      } else if (!DataVersion.current().isShopkeeperStorageDowngrade(dataVersion) && !DataVersion.current().isShopkeeperDataDowngrade(dataVersion)) {
         Set<? extends String> keys = this.saveData.getKeys();

         assert keys.contains("data-version");

         int shopkeepersCount = keys.size() - 1;
         if (shopkeepersCount == 0) {
            this.saveData.set("data-version", DataVersion.current().toString());
            return true;
         } else {
            Log.info("Loading the data of " + shopkeepersCount + " shopkeepers ...");
            boolean dataVersionChanged = !DataVersion.current().equals(dataVersion);
            forceSaveAllShopkeepers = rawDataMigrated || DataVersion.current().isMinecraftUpgrade(dataVersion) || DataVersion.current().isShopkeeperStorageUpgrade(dataVersion);
            if (dataVersionChanged) {
               var26 = String.valueOf(dataVersion);
               Log.info("The save file's data version has changed from '" + var26 + "' to '" + String.valueOf(DataVersion.current()) + "'.");
               this.saveData.set("data-version", DataVersion.current().toString());
               this.requestSave();
            }

            if (forceSaveAllShopkeepers) {
               Log.info("The saved data of all shopkeepers is updated.");
               this.requestSave();
            }

            Iterator var11 = keys.iterator();

            while(var11.hasNext()) {
               String key = (String)var11.next();
               if (!key.equals("data-version")) {
                  this.loadShopkeeper(key, forceSaveAllShopkeepers);
               }
            }

            return true;
         }
      } else {
         var26 = String.valueOf(dataVersion);
         Log.severe("Detected Shopkeepers plugin downgrade from data version '" + var26 + "' to '" + String.valueOf(DataVersion.current()) + "'! Plugin downgrades are not supported. Disabling the plugin in order to prevent data loss!");
         return false;
      }
   }

   @Nullable
   private ShopkeeperData getShopkeeperData(int shopkeeperId) {
      DataContainer shopkeeperDataContainer = this.saveData.getContainer(String.valueOf(shopkeeperId));
      if (shopkeeperDataContainer == null) {
         return null;
      } else {
         ShopkeeperData shopkeeperData = ShopkeeperData.ofNonNull(DataContainer.ofNonNull(shopkeeperDataContainer.getValuesCopy()));
         shopkeeperData.set(AbstractShopkeeper.ID, shopkeeperId);
         return shopkeeperData;
      }
   }

   private void loadShopkeeper(String key, boolean forceSave) {
      Integer idInt = ConversionUtils.parseInt(key);
      if (idInt != null && idInt > 0) {
         int shopkeeperId = idInt;
         if (shopkeeperId > this.maxUsedShopkeeperId) {
            this.maxUsedShopkeeperId = shopkeeperId;
         }

         ShopkeeperData shopkeeperData = this.getShopkeeperData(shopkeeperId);
         if (shopkeeperData == null) {
            this.failedToLoadShopkeeper(key, "Invalid shopkeeper data!");
         } else {
            boolean migrated;
            try {
               migrated = shopkeeperData.migrate(AbstractShopkeeper.getLogPrefix(shopkeeperId));
            } catch (InvalidDataException var10) {
               this.failedToLoadShopkeeper(key, "Shopkeeper data migration failed!", var10);
               return;
            }

            SKShopkeeperRegistry shopkeeperRegistry = this.getShopkeeperRegistry();

            AbstractShopkeeper shopkeeper;
            try {
               shopkeeper = shopkeeperRegistry.loadShopkeeper(shopkeeperData);

               assert shopkeeper != null && shopkeeper.isValid();
            } catch (InvalidDataException var11) {
               this.failedToLoadShopkeeper(key, "Shopkeeper data could not be loaded!", var11);
               return;
            } catch (Exception var12) {
               this.failedToLoadShopkeeper(key, "Unexpected error!", var12);
               return;
            }

            if (migrated || forceSave) {
               shopkeeper.markDirty();
            }

         }
      } else {
         this.failedToLoadShopkeeper(key, "Invalid id: " + key);
      }
   }

   private void failedToLoadShopkeeper(String idKey, String reason) {
      this.failedToLoadShopkeeper(idKey, reason, (Throwable)null);
   }

   private void failedToLoadShopkeeper(String idKey, String reason, @Nullable Throwable throwable) {
      Log.warning("Failed to load shopkeeper '" + idKey + "': " + reason, throwable);
   }

   public boolean isDirty() {
      assert !this.saveTask.isPostProcessing();

      if (this.pendingSaveRequest) {
         return true;
      } else if (!this.dirtyShopkeepers.isEmpty()) {
         return true;
      } else if (!this.saveTask.isRunning() && !this.unsavedShopkeepers.isEmpty()) {
         return true;
      } else {
         if (!this.saveTask.isRunning()) {
            assert this.shopkeepersToDelete.isEmpty();

            if (!this.unsavedDeletedShopkeepers.isEmpty()) {
               return true;
            }
         } else if (!this.shopkeepersToDelete.isEmpty()) {
            return true;
         }

         return false;
      }
   }

   public void deleteShopkeeper(AbstractShopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      if (this.saveTask.isRunning() && !this.saveTask.isPostProcessing()) {
         this.shopkeepersToDelete.add(shopkeeper);
      } else {
         int shopkeeperId = shopkeeper.getId();
         String key = String.valueOf(shopkeeperId);
         boolean shopkeeperDataExists = this.saveData.contains(key);
         if (shopkeeperDataExists) {
            this.saveData.remove(key);
            this.unsavedDeletedShopkeepers.add(shopkeeperId);
         }

         this.dirtyShopkeepers.remove(shopkeeper);
         this.unsavedShopkeepers.remove(shopkeeperId);
         if (!shopkeeperDataExists) {
            this.rollbackNextShopkeeperId();
         }
      }

   }

   public int getUnsavedDeletedShopkeepersCount() {
      return this.unsavedDeletedShopkeepers.size() + this.shopkeepersToDelete.size();
   }

   public void markDirty(AbstractShopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      Validate.isTrue(shopkeeper.isValid(), "shopkeeper is invalid");

      assert !this.unsavedDeletedShopkeepers.contains(shopkeeper.getId());

      assert !this.shopkeepersToDelete.contains(shopkeeper);

      this.dirtyShopkeepers.add(shopkeeper);
      if (!this.saveTask.isRunning()) {
         this.unsavedShopkeepers.remove(shopkeeper.getId());
      }

   }

   public int getUnsavedDirtyShopkeepersCount() {
      int count = this.dirtyShopkeepers.size() + this.unsavedShopkeepers.size();
      if (this.saveTask.isRunning()) {
         Iterator var2 = this.dirtyShopkeepers.iterator();

         AbstractShopkeeper shopkeeper;
         while(var2.hasNext()) {
            shopkeeper = (AbstractShopkeeper)var2.next();
            if (this.unsavedShopkeepers.contains(shopkeeper.getId())) {
               --count;
            }
         }

         var2 = this.saveTask.savingDirtyShopkeepers.iterator();

         while(var2.hasNext()) {
            shopkeeper = (AbstractShopkeeper)var2.next();

            assert !this.unsavedShopkeepers.contains(shopkeeper.getId());

            if (!this.dirtyShopkeepers.contains(shopkeeper)) {
               ++count;
            }
         }
      }

      return count;
   }

   public void disableSaving() {
      this.savingDisabled = true;
   }

   public void enableSaving() {
      this.savingDisabled = false;
   }

   private void requestSave() {
      this.pendingSaveRequest = true;
   }

   public void save() {
      if (Settings.saveInstantly) {
         this.saveNow();
      } else {
         this.requestSave();
      }

   }

   public void saveDelayed() {
      this.requestSave();
      if (Settings.saveInstantly && this.delayedSaveTask == null) {
         (new SKShopkeeperStorage.DelayedSaveTask()).start();
      }

   }

   public void saveNow() {
      this.doSave(true);
   }

   public void saveImmediate() {
      this.doSave(false);
   }

   public void saveIfDirtyAndAwaitCompletion() {
      if (this.isDirty()) {
         this.saveImmediate();
      } else {
         this.saveTask.awaitExecutions();
      }

   }

   private void doSave(boolean async) {
      if (this.savingDisabled) {
         Log.warning("Skipping save, because saving got disabled.");
      } else {
         if (async) {
            this.saveTask.run();
         } else {
            this.saveTask.runImmediately();
         }

      }
   }

   static {
      SAVE_ERROR_MSG_THROTTLE_MILLIS = TimeUnit.MINUTES.toMillis(4L);
   }

   private class SaveTask extends SingletonTask {
      Set<AbstractShopkeeper> savingDirtyShopkeepers = new LinkedHashSet();
      private final Set<AbstractShopkeeper> failedToSave = new LinkedHashSet();
      private boolean savingSucceeded = false;
      private long lastSaveErrorMsgMillis = 0L;

      SaveTask(Plugin param2) {
         super(plugin);
      }

      void onDisable() {
         this.lastSaveErrorMsgMillis = 0L;
      }

      protected SKShopkeeperStorage.SaveTask.InternalAsyncTask createInternalAsyncTask() {
         return new SKShopkeeperStorage.SaveTask.InternalAsyncTask(this);
      }

      protected SKShopkeeperStorage.SaveTask.InternalSyncCallbackTask createInternalSyncCallbackTask() {
         return new SKShopkeeperStorage.SaveTask.InternalSyncCallbackTask(this);
      }

      protected void prepare() {
         if (SKShopkeeperStorage.this.delayedSaveTask != null) {
            SKShopkeeperStorage.this.delayedSaveTask.cancel();
            SKShopkeeperStorage.this.delayedSaveTask = null;
         }

         SKShopkeeperStorage.this.saveData.getConfig().options().setHeader(SKShopkeeperStorage.HEADER);
         SKShopkeeperStorage.this.pendingSaveRequest = false;

         assert this.savingDirtyShopkeepers.isEmpty();

         Set<AbstractShopkeeper> newDirtyShopkeepers = this.savingDirtyShopkeepers;
         this.savingDirtyShopkeepers = SKShopkeeperStorage.this.dirtyShopkeepers;
         SKShopkeeperStorage.this.dirtyShopkeepers = newDirtyShopkeepers;

         assert this.failedToSave.isEmpty();

         this.savingDirtyShopkeepers.forEach(this::saveShopkeeper);
      }

      private void saveShopkeeper(AbstractShopkeeper shopkeeper) {
         assert shopkeeper.isDirty();

         String key = String.valueOf(shopkeeper.getId());
         Object previousData = SKShopkeeperStorage.this.saveData.get(key);
         ShopkeeperData newData = ShopkeeperData.ofNonNull(SKShopkeeperStorage.this.saveData.createContainer(key));

         try {
            shopkeeper.save(newData, false);
         } catch (Exception var6) {
            SKShopkeeperStorage.this.saveData.set(key, previousData);
            Log.warning((String)(shopkeeper.getLogPrefix() + "Saving failed!"), (Throwable)var6);
            this.failedToSave.add(shopkeeper);
            return;
         }

         newData.set(AbstractShopkeeper.ID.getUnvalidatedSaver(), (Object)null);
         shopkeeper.onSave();
      }

      protected void execute() {
         this.savingSucceeded = this.saveToFile(SKShopkeeperStorage.this.saveData);
      }

      private boolean saveToFile(DataStore saveData) {
         try {
            String data;
            try {
               data = saveData.saveToString();
            } catch (Exception var4) {
               throw new ShopkeeperStorageSaveException("Could not serialize shopkeeper data!", var4);
            }

            Retry.retry(() -> {
               this.doSaveToFile(data);
            }, 20, (attemptNumber, exception, retry) -> {
               assert exception != null;

               String errorMsg = "Failed to save shopkeepers (attempt " + attemptNumber + ")";
               if (attemptNumber == 1) {
                  Log.severe((String)errorMsg, (Throwable)exception);
               } else {
                  String issue = ThrowableUtils.getDescription(exception);
                  Log.severe(errorMsg + ": " + issue);
               }

               if (retry) {
                  try {
                     Thread.sleep(25L);
                  } catch (InterruptedException var5) {
                     Thread.currentThread().interrupt();
                  }
               }

            });
            return true;
         } catch (Exception var5) {
            Log.severe((String)"Saving of shopkeepers failed! Data might have been lost! :(", (Throwable)var5);
            return false;
         }
      }

      private void doSaveToFile(String data) throws ShopkeeperStorageSaveException {
         assert data != null;

         try {
            FileUtils.writeSafely(SKShopkeeperStorage.this.saveFile, data, StandardCharsets.UTF_8, Log.getLogger(), SKShopkeeperStorage.this.getPluginDataFolder());
         } catch (Exception var3) {
            throw new ShopkeeperStorageSaveException(var3.getMessage(), var3);
         }
      }

      protected void syncCallback() {
         this.printDebugInfo();
         if (this.savingSucceeded) {
            boolean shopkeepersDeleted = SKShopkeeperStorage.this.unsavedDeletedShopkeepers.size() > 0;
            SKShopkeeperStorage.this.unsavedShopkeepers.clear();
            SKShopkeeperStorage.this.unsavedDeletedShopkeepers.clear();
            if (shopkeepersDeleted) {
               SKShopkeeperStorage.this.rollbackNextShopkeeperId();
            }
         } else {
            if (!SKShopkeeperStorage.this.unsavedShopkeepers.isEmpty()) {
               SKShopkeeperStorage.this.dirtyShopkeepers.forEach((shopkeeper) -> {
                  SKShopkeeperStorage.this.unsavedShopkeepers.remove(shopkeeper.getId());
               });
            }

            this.savingDirtyShopkeepers.forEach((shopkeeper) -> {
               if (!this.failedToSave.contains(shopkeeper)) {
                  if (!SKShopkeeperStorage.this.dirtyShopkeepers.contains(shopkeeper)) {
                     SKShopkeeperStorage.this.unsavedShopkeepers.add(shopkeeper.getId());
                  }
               }
            });
         }

         SKShopkeeperStorage.this.dirtyShopkeepers.addAll(this.failedToSave);
         this.failedToSave.clear();
         this.savingDirtyShopkeepers.clear();
         SKShopkeeperStorage.this.shopkeepersToDelete.forEach(SKShopkeeperStorage.this::deleteShopkeeper);
         SKShopkeeperStorage.this.shopkeepersToDelete.clear();
         if (!this.savingSucceeded) {
            SKShopkeeperStorage.this.saveDelayed();
            long nowMillis = System.currentTimeMillis();
            if (Math.abs(nowMillis - this.lastSaveErrorMsgMillis) > SKShopkeeperStorage.SAVE_ERROR_MSG_THROTTLE_MILLIS) {
               this.lastSaveErrorMsgMillis = nowMillis;
               String var10000 = String.valueOf(ChatColor.DARK_RED);
               String errorMsg = var10000 + "[Shopkeepers] " + String.valueOf(ChatColor.RED) + "Saving shopkeepers failed! Please check the server log and look into the issue!";
               Iterator var4 = Bukkit.getOnlinePlayers().iterator();

               while(var4.hasNext()) {
                  Player player = (Player)var4.next();

                  assert player != null;

                  if (PermissionUtils.hasPermission(player, "shopkeeper.admin")) {
                     player.sendMessage(errorMsg);
                  }
               }
            }
         }

      }

      private void printDebugInfo() {
         Log.debug(() -> {
            StringBuilder sb = new StringBuilder();
            sb.append("Saved shopkeeper data (");
            sb.append(this.savingDirtyShopkeepers.size()).append(" dirty");
            if (!SKShopkeeperStorage.this.unsavedShopkeepers.isEmpty()) {
               sb.append(", ").append(SKShopkeeperStorage.this.unsavedShopkeepers.size()).append(" previously unsaved");
            }

            sb.append(", ").append(SKShopkeeperStorage.this.unsavedDeletedShopkeepers.size()).append(" deleted");
            if (!this.failedToSave.isEmpty()) {
               sb.append(", ").append(this.failedToSave.size()).append(" failed to save");
            }

            sb.append("): ");
            sb.append(this.getExecutionTimingString());
            if (!this.savingSucceeded) {
               sb.append(" -- Saving failed!");
            }

            return sb.toString();
         });
      }

      private class InternalAsyncTask extends SingletonTask.InternalAsyncTask {
         private InternalAsyncTask(final SKShopkeeperStorage.SaveTask param1) {
            super();
         }
      }

      private class InternalSyncCallbackTask extends SingletonTask.InternalSyncCallbackTask {
         private InternalSyncCallbackTask(final SKShopkeeperStorage.SaveTask param1) {
            super();
         }
      }
   }

   private class PeriodicSaveTask implements Runnable {
      private static final long PERIOD_TICKS = 6000L;

      void start() {
         Bukkit.getScheduler().runTaskTimer(SKShopkeeperStorage.this.plugin, this, 6000L, 6000L);
      }

      public void run() {
         SKShopkeeperStorage.this.saveIfDirty();
      }
   }

   private class DelayedSaveTask implements Runnable {
      void start() {
         assert SKShopkeeperStorage.this.delayedSaveTask == null;

         SKShopkeeperStorage.this.delayedSaveTask = SchedulerUtils.runTaskLaterOrOmit(SKShopkeeperStorage.this.plugin, this, 600L);
      }

      public void run() {
         SKShopkeeperStorage.this.delayedSaveTask = null;
         SKShopkeeperStorage.this.saveIfDirty();
      }
   }
}
