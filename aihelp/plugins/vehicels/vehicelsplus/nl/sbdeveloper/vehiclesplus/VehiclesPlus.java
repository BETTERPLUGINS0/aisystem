/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import lombok.Generated;
import me.m56738.smoothcoasters.api.SmoothCoastersAPI;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleDespawnEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.Vehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import nl.sbdeveloper.vehiclesplus.handlers.ACFHandler;
import nl.sbdeveloper.vehiclesplus.handlers.EconomyAdapter;
import nl.sbdeveloper.vehiclesplus.handlers.MovementHandler;
import nl.sbdeveloper.vehiclesplus.handlers.StorageHandler;
import nl.sbdeveloper.vehiclesplus.handlers.WGFlagHandler;
import nl.sbdeveloper.vehiclesplus.libs.bstats.bukkit.Metrics;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.XReflection;
import nl.sbdeveloper.vehiclesplus.listeners.ChunkListener;
import nl.sbdeveloper.vehiclesplus.listeners.EntityDismountListener;
import nl.sbdeveloper.vehiclesplus.listeners.EntityInteractListener;
import nl.sbdeveloper.vehiclesplus.listeners.FuelHelmetPreventionListener;
import nl.sbdeveloper.vehiclesplus.listeners.InputListener;
import nl.sbdeveloper.vehiclesplus.listeners.LegacyEntityDismountListener;
import nl.sbdeveloper.vehiclesplus.listeners.PlayerJoinListener;
import nl.sbdeveloper.vehiclesplus.listeners.PlayerQuitListener;
import nl.sbdeveloper.vehiclesplus.listeners.TrunkInventoryListener;
import nl.sbdeveloper.vehiclesplus.listeners.VehicleDestroyListener;
import nl.sbdeveloper.vehiclesplus.listeners.WGVehicleSpawnListener;
import nl.sbdeveloper.vehiclesplus.storage.db.DataStorage;
import nl.sbdeveloper.vehiclesplus.storage.db.MySQLDB;
import nl.sbdeveloper.vehiclesplus.storage.db.QueuedSavable;
import nl.sbdeveloper.vehiclesplus.storage.db.SQLiteDB;
import nl.sbdeveloper.vehiclesplus.storage.db.exceptions.DataStorageException;
import nl.sbdeveloper.vehiclesplus.tasks.SaveTask;
import nl.sbdeveloper.vehiclesplus.utils.UpdateManager;
import nl.sbdeveloper.vehiclesplus.utils.Verify;
import nl.sbdeveloper.vehiclesplus.utils.inventories.Inventory;
import nl.sbdeveloper.vehiclesplus.utils.wg.WorldGuardHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class VehiclesPlus
extends JavaPlugin {
    private static boolean enabled = false;
    private static VehiclesPlus instance;
    private static StorageHandler storage;
    private static SmoothCoastersAPI smoothCoasters;
    private static InputListener inputListener;

    public void onLoad() {
        if (WorldGuardHelper.hasWorldGuard()) {
            WGFlagHandler.load();
        }
    }

    public void onEnable() {
        VehiclesPlus.loadConfig0();
        instance = this;
        this.getLogger().info("-------------------------------");
        this.getLogger().info("VehiclesPlus v" + this.getDescription().getVersion());
        this.getLogger().info("Made by SBDeveloper");
        this.getLogger().info("Running on: " + XReflection.getVersionInformation());
        if ("%%__USERNAME__%%".equalsIgnoreCase("%%__USERNAME__%%")) {
            this.getLogger().info("Bought on: SpigotMC (383518).");
        } else {
            this.getLogger().info("Bought on: Polymart by %%__USERNAME__%% (383518).");
        }
        this.getLogger().info(" ");
        if ("%%__POLYMART__".equals("1")) {
            this.getLogger().info("Validating your purchase...");
            if (new Verify(this).isValidated().booleanValue()) {
                this.getLogger().info("Your purchase is valid. Thanks for your purchase!");
            }
        }
        if (XMaterial.getVersion() < 17) {
            this.getLogger().severe("VehiclesPlus v3 requires Minecraft 1.17 or higher to function correctly! Please update your server to use this plugin.");
            this.getLogger().severe("Disabling the plugin...");
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        if (!EconomyAdapter.load()) {
            this.getLogger().warning("Vault and/or an Economy manager are missing! Economy support will not work.");
        }
        storage = new StorageHandler(this);
        this.getLogger().info("Loading the configuration...");
        VehiclesPlusPluginManager.init(VehiclesPlus.getInstance(), storage.loadConfig());
        Bukkit.getScheduler().runTaskLater((Plugin)this, () -> {
            try {
                List<QueuedSavable> list;
                this.getLogger().info("Loading storage...");
                storage.load();
                DataStorage.registerType("SQLITE", SQLiteDB.class);
                DataStorage.registerType("MYSQL", MySQLDB.class);
                if (!DataStorage.newInstance(this, storage.getConfig().getDataSettings().getType())) {
                    this.getLogger().severe("Failed to create a database connection! Please check your settings.");
                    this.getLogger().severe("Disabling the plugin...");
                    this.getServer().getPluginManager().disablePlugin((Plugin)this);
                }
                this.getLogger().info("Loading vehicles from storage...");
                try {
                    DataStorage.getInstance().prepare();
                    list = DataStorage.getInstance().loadVehicles();
                    list.forEach(storageVehicle -> VehiclesPlusAPI.getVehicles().put(storageVehicle.getUuid(), (Vehicle)storageVehicle));
                    for (StorageVehicle storageVehicle2 : list) {
                        if (!storageVehicle2.isPersistent()) continue;
                        if (storageVehicle2.getPersistentLocation() != null) {
                            storageVehicle2.spawnPersistent(storageVehicle2.getPersistentLocation());
                            continue;
                        }
                        this.getLogger().warning("Persistent vehicle " + String.valueOf(storageVehicle2.getUuid()) + " has no (valid) location set! Removing...");
                        storageVehicle2.remove();
                    }
                } catch (DataStorageException dataStorageException) {
                    this.getLogger().log(Level.SEVERE, "Failed to load vehicles from storage!", dataStorageException);
                }
                this.getLogger().info("Loading garages from storage...");
                try {
                    list = DataStorage.getInstance().loadGarages();
                    list.forEach(garage -> VehiclesPlusAPI.addGarage(garage, true));
                } catch (DataStorageException dataStorageException) {
                    this.getLogger().log(Level.SEVERE, "Failed to load garages from storage!", dataStorageException);
                }
                this.getLogger().info("Loaded all the data from storage!");
            } catch (Exception exception) {
                this.getLogger().log(Level.SEVERE, "Failed to load the storage!", exception);
                this.getLogger().severe("Disabling the plugin...");
                this.getServer().getPluginManager().disablePlugin((Plugin)this);
            }
        }, 1L);
        this.getLogger().info("Loading tasks...");
        long l = (long)storage.getConfig().getDataSettings().getInterval() * 20L;
        new SaveTask().runTaskTimerAsynchronously((Plugin)this, l, l);
        this.getLogger().info("Registering commands...");
        ACFHandler.init(this);
        this.getLogger().info("Registering events...");
        inputListener = new InputListener();
        Bukkit.getPluginManager().registerEvents((Listener)inputListener, (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new MovementHandler(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new EntityInteractListener(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new FuelHelmetPreventionListener(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new PlayerQuitListener(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new PlayerJoinListener(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new TrunkInventoryListener(this), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new VehicleDestroyListener(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new ChunkListener(), (Plugin)this);
        try {
            Class.forName("org.bukkit.event.entity.EntityDismountEvent");
            Bukkit.getPluginManager().registerEvents((Listener)new EntityDismountListener(), (Plugin)this);
        } catch (ClassNotFoundException classNotFoundException) {
            this.getLogger().warning("Enabled legacy EntityDismountEvent listener, which is required for older versions (below 1.20.4 for Spigot or 1.20 for PaperSpigot).");
            Bukkit.getPluginManager().registerEvents((Listener)new LegacyEntityDismountListener(), (Plugin)this);
        }
        this.getLogger().info("Loading inventories...");
        Inventory.init(this);
        if (WorldGuardHelper.hasWorldGuard()) {
            this.getLogger().info("Loading WorldGuard (v" + WorldGuardHelper.getInstance().getWorldGuardVersion() + ".x) flags...");
            Bukkit.getPluginManager().registerEvents((Listener)new WGVehicleSpawnListener(), (Plugin)this);
        }
        this.getLogger().info("Loading SmoothCoastersAPI...");
        smoothCoasters = new SmoothCoastersAPI((Plugin)this);
        this.getLogger().info("Loading metrics... (disable in bStats config)");
        new Metrics((Plugin)this, 11396);
        if (!"70523".equalsIgnoreCase("70523") && storage.getConfig().getUpdateCheckerSettings().isEnabled()) {
            UpdateManager updateManager = new UpdateManager((Plugin)this, UpdateManager.CheckType.SPIGOT);
            updateManager.handleResponse((versionResponse, version) -> {
                switch (versionResponse) {
                    case FOUND_NEW: {
                        this.getLogger().warning("There is a new version available! Current: " + this.getDescription().getVersion() + " New: " + version.get());
                        if (!storage.getConfig().getUpdateCheckerSettings().isDownloadOnUpdate()) break;
                        this.getLogger().info("Trying to download the update. This could take some time...");
                        updateManager.handleDownloadResponse((downloadResponse, string) -> {
                            switch (downloadResponse) {
                                case DONE: {
                                    this.getLogger().info("Update downloaded! If you restart your server, it will be loaded. Filename: " + string);
                                    break;
                                }
                                case ERROR: {
                                    this.getLogger().severe("Something went wrong when trying downloading the latest version.");
                                    break;
                                }
                                case UNAVAILABLE: {
                                    this.getLogger().warning("Unable to download the latest version.");
                                }
                            }
                        }).runUpdate();
                        break;
                    }
                    case LATEST: {
                        this.getLogger().info("You are running the latest version [" + this.getDescription().getVersion() + "]!");
                        break;
                    }
                    case THIS_NEWER: {
                        this.getLogger().info("You are running a newer version [" + this.getDescription().getVersion() + "]! This is probably fine.");
                        break;
                    }
                    case UNAVAILABLE: {
                        this.getLogger().severe("Unable to perform an update check.");
                    }
                }
            }).check();
        }
        this.getLogger().info("Plugin enabled!");
        this.getLogger().info("-------------------------------");
        enabled = true;
    }

    public void onDisable() {
        if (!enabled) {
            return;
        }
        if (DataStorage.getInstance() != null) {
            this.getLogger().info("Saving vehicles...");
            for (Vehicle vehicle : VehiclesPlusAPI.getVehicles().values()) {
                if (!vehicle.isSpawned()) continue;
                Location location = vehicle.getSpawnedVehicle().getHolder().getLocation();
                StorageVehicle storageVehicle = vehicle.getSpawnedVehicle().despawn(VehicleDespawnEvent.DespawnReason.SHUTDOWN, true);
                if (storageVehicle.isPersistent() && storageVehicle.getPersistentLocation() == null) {
                    storageVehicle.setPersistentLocation(location);
                }
                this.getLogger().info("Despawned " + String.valueOf(storageVehicle.getUuid()) + "! Saving it to storage...");
                try {
                    storageVehicle.forceSave();
                } catch (DataStorageException dataStorageException) {
                    this.getLogger().log(Level.SEVERE, "Failed to save vehicle " + String.valueOf(storageVehicle.getUuid()) + " to storage!", dataStorageException);
                }
            }
            this.getLogger().info("Closing the database connection...");
            DataStorage.getInstance().closeConnection();
        }
        if (inputListener != null) {
            inputListener.cleanup();
        }
        if (smoothCoasters != null) {
            this.getLogger().info("Disabling SmoothCoastersAPI...");
            smoothCoasters.unregister();
        }
        this.getLogger().info("Plugin disabled!");
        instance = null;
    }

    @Generated
    public static VehiclesPlus getInstance() {
        return instance;
    }

    @Generated
    public static StorageHandler getStorage() {
        return storage;
    }

    @Generated
    public static SmoothCoastersAPI getSmoothCoasters() {
        return smoothCoasters;
    }

    @Generated
    public static InputListener getInputListener() {
        return inputListener;
    }

    private static /* bridge */ /* synthetic */ void loadConfig0() {
        try {
            URLConnection con = new URL("https://api.spigotmc.org/legacy/premium.php?user_id=%%__USER__%%&resource_id=%%__RESOURCE__%%&nonce=%%__NONCE__%%").openConnection();
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            ((HttpURLConnection)con).setInstanceFollowRedirects(true);
            String response = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if ("false".equals(response)) {
                throw new RuntimeException("Access to this plugin has been disabled! Please contact the author!");
            }
        } catch (IOException iOException) {
            // empty catch block
        }
    }
}

