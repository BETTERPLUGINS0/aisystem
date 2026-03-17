/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus.handlers;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.fuel.FuelType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.rims.RimDesign;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.MovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.StrategyFactory;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.VehicleType;
import nl.sbdeveloper.vehiclesplus.config.Config;
import nl.sbdeveloper.vehiclesplus.handlers.FirstRunHandler;
import nl.sbdeveloper.vehiclesplus.storage.file.HJSONFile;
import nl.sbdeveloper.vehiclesplus.storage.file.YamlFile;
import nl.sbdeveloper.vehiclesplus.utils.jackson.JacksonHelper;
import org.bukkit.plugin.java.JavaPlugin;
import org.hjson.ParseException;

public class StorageHandler {
    private final JavaPlugin plugin;
    private final YamlFile config;

    public StorageHandler(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
        this.config = new YamlFile(javaPlugin, "config");
        this.config.loadDefaults();
    }

    public Config loadConfig() {
        return new Config(this.config.getFile().getInt("config-version"), Locale.forLanguageTag(this.config.getFile().getString("locale")), this.config.getFile().contains("data.host") ? new Config.Data(this.config.getFile().getString("data.type"), this.config.getFile().getInt("data.interval"), this.config.getFile().getBoolean("data.verbose"), this.config.getFile().getString("data.database"), this.config.getFile().getString("data.host"), this.config.getFile().getInt("data.port"), this.config.getFile().getString("data.username"), this.config.getFile().getString("data.password")) : new Config.Data(this.config.getFile().getString("data.type"), this.config.getFile().getInt("data.interval"), this.config.getFile().getBoolean("data.verbose"), this.config.getFile().getString("data.database")), new Config.Collision(this.config.getFile().getString("collision.damageLevel").toUpperCase(), this.config.getFile().getBoolean("collision.despawnOnZeroHealth"), this.config.getFile().getBoolean("collision.dropTrunkItems"), this.config.getFile().getBoolean("collision.behavior.slabDriving"), this.config.getFile().getBoolean("collision.behavior.blockDriving"), this.config.getFile().getBoolean("collision.behavior.stopAtVehicle"), this.config.getFile().getBoolean("collision.behavior.stopAtEntity")), this.config.getFile().getDouble("repairCostDivision"), this.config.getFile().getDouble("renameCost"), this.config.getFile().getBoolean("actionBar"), this.config.getFile().getBoolean("spawnLocked"), new Config.UpdateChecker(this.config.getFile().getBoolean("updateChecker.enabled"), this.config.getFile().getBoolean("updateChecker.downloadOnUpdate")), new Config.Limits(this.config.getFile().getInt("limits.have"), this.config.getFile().getInt("limits.spawn")), this.config.getFile().getString("defaultRimDesignId"), this.config.getFile().getIntegerList("fuel"), new Config.Permissions(this.config.getFile().getString("permissions.buy"), this.config.getFile().getString("permissions.adjust"), this.config.getFile().getString("permissions.spawn"), this.config.getFile().getString("permissions.ride")));
    }

    public void load() {
        VehiclesPlusPluginManager.load();
        FirstRunHandler.run();
        this.loadTypes();
        this.loadModels();
        this.plugin.getLogger().info("Loaded the vehicle types and models!");
    }

    public void reload() {
        VehiclesPlusPluginManager.reinit(VehiclesPlus.getInstance(), this.loadConfig());
    }

    public Config getConfig() {
        return VehiclesPlusPluginManager.getConfig();
    }

    protected void setVersion(int n) {
        this.config.getFile().set("config-version", (Object)n);
        this.config.saveFile();
    }

    /*
     * WARNING - void declaration
     */
    private void loadTypes() {
        void var6_13;
        Object object;
        File file2 = new File(this.plugin.getDataFolder(), "vehicletypes");
        for (File file3 : file2.listFiles((file, string) -> string.endsWith(".hjson"))) {
            VehicleType vehicleType;
            String object3 = file3.getName().substring(0, file3.getName().lastIndexOf(46));
            try {
                vehicleType = JacksonHelper.fromHJSON(VehicleType.class, file3, true);
            } catch (IOException iOException) {
                this.plugin.getLogger().log(Level.SEVERE, "Couldn't load vehicle type " + object3 + "!", iOException);
                continue;
            }
            VehiclesPlusAPI.getVehicleTypes().put(object3, vehicleType);
        }
        File file4 = new File(this.plugin.getDataFolder(), "fuels");
        for (File file3 : file4.listFiles((file, string) -> string.endsWith(".hjson"))) {
            String string2 = file3.getName().substring(0, file3.getName().lastIndexOf(46));
            try {
                object = JacksonHelper.fromHJSON(FuelType.class, file3, true);
            } catch (IOException iOException) {
                this.plugin.getLogger().log(Level.SEVERE, "Couldn't load fuel type " + string2 + "!", iOException);
                continue;
            }
            VehiclesPlusAPI.getFuelTypes().put(string2, (FuelType)object);
        }
        File file5 = new File(this.plugin.getDataFolder(), "rims");
        File[] fileArray = file5.listFiles((file, string) -> string.endsWith(".hjson"));
        int n = fileArray.length;
        boolean bl = false;
        while (var6_13 < n) {
            block10: {
                RimDesign rimDesign;
                File file6 = fileArray[var6_13];
                object = file6.getName().substring(0, file6.getName().lastIndexOf(46));
                try {
                    rimDesign = JacksonHelper.fromHJSON(RimDesign.class, file6, true);
                } catch (IOException iOException) {
                    this.plugin.getLogger().log(Level.SEVERE, "Couldn't load rim design " + (String)object + "!", iOException);
                    break block10;
                }
                if (!VehiclesPlusAPI.getRimDesign(rimDesign.getName()).isPresent()) {
                    VehiclesPlusAPI.getRimDesigns().put((String)object, rimDesign);
                }
            }
            ++var6_13;
        }
    }

    private void loadModels() {
        for (Map.Entry<String, VehicleType> entry : VehiclesPlusAPI.getVehicleTypes().entrySet()) {
            File file2 = new File(this.plugin.getDataFolder(), "vehicles/" + entry.getKey());
            if (!file2.exists()) {
                if (!file2.mkdirs()) {
                    this.plugin.getLogger().log(Level.SEVERE, "Couldn't create folder " + file2.getAbsolutePath() + "!");
                    continue;
                }
                StorageHandler.saveTypeWithDefaultModel(entry.getValue());
                this.plugin.getLogger().log(Level.INFO, "Created folder for type " + entry.getKey() + " with default model.");
            }
            for (File file3 : file2.listFiles((file, string) -> string.endsWith(".hjson"))) {
                VehicleModel vehicleModel;
                String string2 = file3.getName().substring(0, file3.getName().lastIndexOf(46));
                try {
                    vehicleModel = JacksonHelper.fromHJSON(VehicleModel.class, file3, true);
                } catch (IOException | ParseException exception) {
                    this.plugin.getLogger().log(Level.SEVERE, "Couldn't load vehicle model " + string2 + "!", exception);
                    continue;
                }
                VehiclesPlusAPI.getVehicleModels().put(string2, vehicleModel);
            }
        }
    }

    protected static void saveTypeWithDefaultModel(VehicleType vehicleType) {
        StorageHandler.save(vehicleType, "vehicletypes", vehicleType.getName());
        VehicleModel vehicleModel = vehicleType.constructDefaultModel();
        vehicleType.getMovementTypes().forEach(movementType -> vehicleModel.getTypeStrategies().add((MovementStrategy)StrategyFactory.createStrategy(vehicleModel, movementType)));
        StorageHandler.save(vehicleModel, "vehicles/" + vehicleType.getName(), vehicleModel.getId());
    }

    public static void save(Object object, String string, String string2) {
        File file = new File(VehiclesPlus.getInstance().getDataFolder(), string);
        if (!file.exists() && !file.mkdirs()) {
            return;
        }
        HJSONFile hJSONFile = new HJSONFile(VehiclesPlus.getInstance(), string + "/" + string2);
        try {
            hJSONFile.write(object);
        } catch (IOException iOException) {
            VehiclesPlus.getInstance().getLogger().log(Level.SEVERE, "Couldn't save to the file " + string2, iOException);
        }
    }
}

