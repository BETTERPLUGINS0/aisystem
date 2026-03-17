/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.zombie_striker.qg.api.QualityArmory
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.ModelSize;
import me.zombie_striker.qav.VehicleTypes;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.config.VehicleYML;
import me.zombie_striker.qav.customitemmanager.MaterialStorage;
import me.zombie_striker.qav.exceptions.InvalidVehicleException;
import me.zombie_striker.qav.finput.FInput;
import me.zombie_striker.qav.finput.FInputManager;
import me.zombie_striker.qav.hooks.model.Animation;
import me.zombie_striker.qav.qamini.QAMini;
import me.zombie_striker.qav.vehicles.AbstractBoat;
import me.zombie_striker.qav.vehicles.AbstractCar;
import me.zombie_striker.qav.vehicles.AbstractDrill;
import me.zombie_striker.qav.vehicles.AbstractHelicopter;
import me.zombie_striker.qav.vehicles.AbstractPlane;
import me.zombie_striker.qav.vehicles.AbstractTractor;
import me.zombie_striker.qav.vehicles.AbstractTrain;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import me.zombie_striker.qg.api.QualityArmory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class VehicleLoader {
    public static void loadVehicleFiles() {
        int n = 0;
        for (File file : Objects.requireNonNull(Main.carData.listFiles())) {
            try {
                if (!VehicleLoader.loadVehicleFile(file)) continue;
                ++n;
            } catch (Error | Exception throwable) {
                QualityArmoryVehicles.getPlugin().getLogger().warning("Could not load file " + file.getName());
                throwable.printStackTrace();
            }
        }
        if (!Main.verboseLogging) {
            QualityArmoryVehicles.getPlugin().getLogger().info("Loaded " + n + " Vehicle types");
        }
    }

    public static boolean loadVehicleFile(File file) {
        AbstractVehicle abstractVehicle;
        if (QualityArmoryVehicles.getPlugin().getConfig().getBoolean("unsafe.useTurtles", false)) {
            new VehicleYML(file).setModelSize(ModelSize.TURTLE).save();
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        String string2 = yamlConfiguration.getString("name");
        if (Main.verboseLogging) {
            QualityArmoryVehicles.getPlugin().getLogger().info("Loading vehicle \"" + string2 + "\"");
        }
        int n = yamlConfiguration.getInt("id");
        VehicleTypes vehicleTypes = VehicleTypes.getTypeByName(yamlConfiguration.getString("vehicle_type"));
        switch (vehicleTypes) {
            case BOAT: {
                abstractVehicle = new AbstractBoat(string2, n);
                break;
            }
            case CAR: {
                abstractVehicle = new AbstractCar(string2, n);
                break;
            }
            case HELI: {
                abstractVehicle = new AbstractHelicopter(string2, n);
                break;
            }
            case PLANE: {
                abstractVehicle = new AbstractPlane(string2, n);
                break;
            }
            case TRAIN: {
                abstractVehicle = new AbstractTrain(string2, n);
                break;
            }
            case DRILL: {
                abstractVehicle = new AbstractDrill(string2, n);
                break;
            }
            case TRACTOR: {
                abstractVehicle = new AbstractTractor(string2, n);
                break;
            }
            default: {
                throw new InvalidVehicleException("Vehicle type does not exist.");
            }
        }
        if (yamlConfiguration.contains("canDeconstructByEnvironment")) {
            abstractVehicle.setDeconstructable(yamlConfiguration.getBoolean("canDeconstructByEnvironment"));
        }
        if (yamlConfiguration.contains("sound")) {
            abstractVehicle.setSound(yamlConfiguration.getString("sound"));
        }
        if (abstractVehicle instanceof AbstractHelicopter && yamlConfiguration.contains("descentSpeed")) {
            ((AbstractHelicopter)abstractVehicle).setDescentSpeed(yamlConfiguration.getDouble("descentSpeed"));
        }
        if (yamlConfiguration.contains("allowedInShop")) {
            abstractVehicle.setAllowInShop(yamlConfiguration.getBoolean("allowedInShop"));
        }
        if (yamlConfiguration.contains("playDrivingSounds")) {
            abstractVehicle.setPlayCustomSounds(yamlConfiguration.getBoolean("playDrivingSounds"));
        }
        if (yamlConfiguration.contains("cost")) {
            abstractVehicle.setPrice(yamlConfiguration.getInt("cost"));
        }
        if (yamlConfiguration.contains("jumpHeight")) {
            abstractVehicle.setJumpHeight(yamlConfiguration.getDouble("jumpHeight"));
        }
        if (yamlConfiguration.contains("maxHealth")) {
            double d = yamlConfiguration.getDouble("maxHealth");
            abstractVehicle.setMaxHealth(d == -1.0 ? 2.147483647E9 : d);
        }
        if (yamlConfiguration.contains("soundVolume")) {
            abstractVehicle.setSoundVolume(Float.parseFloat("" + yamlConfiguration.getDouble("soundVolume")));
        }
        if (yamlConfiguration.contains("heightOffset")) {
            abstractVehicle.setHeight(yamlConfiguration.getDouble("heightOffset"));
        }
        if (yamlConfiguration.contains("widthOffset")) {
            abstractVehicle.setWidthRadius(yamlConfiguration.getDouble("widthOffset"));
        }
        if (yamlConfiguration.contains("vehicle_texture_material")) {
            abstractVehicle.setMaterial(Material.matchMaterial((String)yamlConfiguration.getString("vehicle_texture_material", "DIAMOND_AXE")));
        }
        if (yamlConfiguration.contains("trunksize")) {
            abstractVehicle.setTrunkSize(yamlConfiguration.getInt("trunksize"));
        }
        if (yamlConfiguration.contains("enablePlayerBodyDirectionFix")) {
            abstractVehicle.setBodyFix(yamlConfiguration.getBoolean("enablePlayerBodyDirectionFix"));
        }
        if (yamlConfiguration.contains("ItemLore")) {
            ArrayList<String> arrayList = new ArrayList<String>();
            for (Object object : yamlConfiguration.getStringList("ItemLore")) {
                arrayList.add(ChatColor.translateAlternateColorCodes((char)'&', (String)object));
            }
            abstractVehicle.setLore(arrayList);
        }
        if (yamlConfiguration.contains("canJumpOnBlocks")) {
            abstractVehicle.setCanJump(yamlConfiguration.getBoolean("canJumpOnBlocks"));
        }
        if (yamlConfiguration.contains("RequiresFuel")) {
            abstractVehicle.setEnableFuel(yamlConfiguration.getBoolean("RequiresFuel"));
        }
        if (yamlConfiguration.contains("TurnSpeedInRadians")) {
            abstractVehicle.setTurnRate(yamlConfiguration.getDouble("TurnSpeedInRadians"));
        }
        if (yamlConfiguration.contains("model.ModelSize")) {
            abstractVehicle.setModelSize(ModelSize.valueOf(yamlConfiguration.getString("model.ModelSize")));
        } else if (yamlConfiguration.contains("increaseSize")) {
            abstractVehicle.setModelSize(ModelSize.ADULT_ARMORSTAND_HEAD);
        }
        if (yamlConfiguration.contains("baseAcceleration")) {
            abstractVehicle.setAccerlationSpeed(yamlConfiguration.getDouble("baseAcceleration"));
        }
        if (yamlConfiguration.contains("maxAcceleration")) {
            abstractVehicle.setMaxSpeed(yamlConfiguration.getDouble("maxAcceleration"));
        }
        if (yamlConfiguration.contains("maxReverseAcceleration")) {
            abstractVehicle.setMaxBackupSpeed(yamlConfiguration.getDouble("maxReverseAcceleration"));
        }
        if (yamlConfiguration.contains("displayname")) {
            abstractVehicle.setDisplayname(yamlConfiguration.getString("displayname"));
        }
        if (yamlConfiguration.contains("RequiresFuel")) {
            abstractVehicle.setEnableFuel(yamlConfiguration.getBoolean("RequiresFuel"));
        }
        if (yamlConfiguration.contains("passagers")) {
            List list = yamlConfiguration.getList("passagers", new ArrayList());
            HashMap hashMap = new HashMap();
            for (Vector vector : list) {
                double d = Math.min(2.0, vector.getY());
                double d2 = vector.getY() - d;
                Vector vector2 = vector.clone();
                vector2.setY(d2);
                hashMap.put(vector2, (int)d);
            }
            abstractVehicle.setPassagerSpots(hashMap);
        }
        if (yamlConfiguration.contains("driverseat.Offset")) {
            abstractVehicle.setDriverSeat(yamlConfiguration.getVector("driverseat.Offset"));
        }
        if (yamlConfiguration.contains("stopProjectileDamage")) {
            abstractVehicle.setStopsProjectileDamage(yamlConfiguration.getBoolean("stopProjectileDamage"));
        }
        if (yamlConfiguration.contains("stopMeleeDamage")) {
            abstractVehicle.setStopsMeleeDamage(yamlConfiguration.getBoolean("stopMeleeDamage"));
        }
        if (yamlConfiguration.contains("center")) {
            abstractVehicle.setCenter(yamlConfiguration.getVector("center"));
        }
        if (yamlConfiguration.contains("rotationMultiplier")) {
            abstractVehicle.setRotationMultiplier(yamlConfiguration.getDouble("rotationMultiplier"));
        }
        if (yamlConfiguration.contains("model.Animations")) {
            yamlConfiguration.getStringList("model.Animations").forEach(string -> {
                String[] stringArray = string.split(":");
                if (stringArray.length >= 2) {
                    Animation.AnimationType animationType = Animation.AnimationType.getType(stringArray[0]);
                    if (animationType != null) {
                        abstractVehicle.getAnimations().add(new Animation(animationType, stringArray[1], stringArray.length > 2 ? stringArray[2] : null));
                    } else {
                        QualityArmoryVehicles.getPlugin().getLogger().warning("Invalid animation: " + stringArray[0] + " for vehicle: " + abstractVehicle.getName());
                    }
                }
            });
        }
        VehicleLoader.registerInput(abstractVehicle, FInput.ClickType.RIGHT, (ConfigurationSection)yamlConfiguration);
        VehicleLoader.registerInput(abstractVehicle, FInput.ClickType.F, (ConfigurationSection)yamlConfiguration);
        VehicleLoader.registerInput(abstractVehicle, FInput.ClickType.LEFT, (ConfigurationSection)yamlConfiguration);
        Main.vehicleTypes.add(abstractVehicle);
        try {
            QualityArmory.registerNewUsedExpansionItem((Material)abstractVehicle.getMaterial(), (int)abstractVehicle.getItemData());
        } catch (Error | Exception throwable) {
            QAMini.registeredItems.add(MaterialStorage.getMS(abstractVehicle.getMaterial(), abstractVehicle.getItemData(), 0));
        }
        return true;
    }

    private static void registerInput(AbstractVehicle abstractVehicle,  @NotNull FInput.ClickType clickType, @NotNull ConfigurationSection configurationSection) {
        if (!configurationSection.contains("InputManager.keys." + clickType.getId())) {
            return;
        }
        String string = configurationSection.getString("InputManager.keys." + clickType.getId(), "none");
        if (!string.equalsIgnoreCase("none")) {
            abstractVehicle.getInputs().put(clickType.toString(), FInputManager.getHandler(string));
        }
    }
}

