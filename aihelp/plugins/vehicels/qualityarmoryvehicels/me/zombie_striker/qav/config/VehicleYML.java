/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.ModelSize;
import me.zombie_striker.qav.VehicleTypes;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class VehicleYML {
    private final File f;
    private final FileConfiguration c;
    private boolean needsUpdate = false;

    public static boolean exists(String string) {
        File file = new File(Main.carData, "default_" + string);
        return file.exists();
    }

    public boolean contains(String string) {
        return this.c.contains(string);
    }

    public Object get(String string) {
        return this.c.get(string);
    }

    public VehicleYML set(String string, Object object) {
        return this.set(false, string, object);
    }

    public VehicleYML set(boolean bl, String string, Object object) {
        long l;
        long l2 = this.f.lastModified();
        long l3 = this.contains("lastModifiedByQA") ? (Long)this.get("lastModifiedByQA") : (l = this.contains("AllowUserModifications") && this.c.getBoolean("AllowUserModifications") ? 0L : System.currentTimeMillis());
        if (!bl && l2 - l > 5000L) {
            return this;
        }
        if (!this.contains(string) || !this.get(string).equals(object)) {
            this.c.set(string, object);
            this.needsUpdate = true;
        }
        return this;
    }

    public void verify(String string, Object object) {
        if (!this.contains(string)) {
            this.c.set(string, object);
            this.needsUpdate = true;
        }
    }

    public static VehicleYML registerVehicle(VehicleTypes vehicleTypes, String string, int n) {
        File file = new File(Main.carData, "default_" + string + ".yml");
        VehicleYML vehicleYML = new VehicleYML(file);
        vehicleYML.setType(vehicleTypes);
        if (vehicleYML.contains("AllowUserModifications")) {
            boolean bl = (Boolean)vehicleYML.get("AllowUserModifications");
            vehicleYML.set(true, "AllowUserModifications", null);
            if (!bl) {
                vehicleYML.putTimeStamp();
            }
        }
        vehicleYML.setName(string);
        vehicleYML.setID(n);
        return vehicleYML;
    }

    @NotNull
    public static VehicleYML loadVehicle(String string) {
        File file = new File(Main.carData, "default_" + string + ".yml");
        return new VehicleYML(file);
    }

    public VehicleYML(File file) {
        this.f = file;
        this.c = YamlConfiguration.loadConfiguration((File)file);
    }

    public File getFile() {
        return this.f;
    }

    public VehicleYML setDeconstructable(boolean bl) {
        this.set("canDeconstructByEnvironment", bl);
        return this;
    }

    public VehicleYML setDriverseatOffset(Vector vector) {
        this.set("driverseat.Offset", vector);
        return this;
    }

    public VehicleYML setCanJumpBlocks(boolean bl) {
        this.set("canJumpOnBlocks", bl);
        return this;
    }

    public VehicleYML setKeyInputManagerF(String string) {
        this.set("InputManager.keys.F", string);
        return this;
    }

    public VehicleYML setKeyInputManagerLMB(String string) {
        this.set("InputManager.keys.LMB", string);
        return this;
    }

    public VehicleYML setKeyInputManagerRMB(String string) {
        this.set("InputManager.keys.RMB", string);
        return this;
    }

    public VehicleYML setLore(String ... stringArray) {
        this.set("ItemLore", stringArray);
        return this;
    }

    public VehicleYML setUseHandsForModel(boolean bl) {
        this.set("useHandForModel", bl);
        return this;
    }

    public VehicleYML setMaxForwardSpeed(double d) {
        this.set("maxAcceleration", d);
        return this;
    }

    public VehicleYML setMaxBackupSpeed(double d) {
        this.set("maxReverseAcceleration", d);
        return this;
    }

    public VehicleYML setDrivingSounds(boolean bl) {
        this.set("playDrivingSounds", bl);
        return this;
    }

    public VehicleYML setCost(int n) {
        this.set("cost", n);
        return this;
    }

    public VehicleYML enablePlayerBodyFix(boolean bl) {
        this.set("enablePlayerBodyDirectionFix", bl);
        return this;
    }

    public VehicleYML setMaxFInputStates(int n) {
        this.set("MaxFInputStates", n);
        return this;
    }

    public VehicleYML setBaseAcceleration(double d) {
        this.set("baseAcceleration", d);
        return this;
    }

    public VehicleYML setStaticTurning(boolean bl) {
        this.set("useStaticTurning", bl);
        return this;
    }

    public VehicleYML setTurnSpeed(double d) {
        this.set("TurnSpeedInRadians", d);
        return this;
    }

    public VehicleYML setWidth(double d) {
        this.set("widthOffset", d);
        return this;
    }

    public VehicleYML setHeight(double d) {
        this.set("heightOffset", d);
        return this;
    }

    public VehicleYML setRequireFuel(boolean bl) {
        this.set("RequiresFuel", bl);
        return this;
    }

    public VehicleYML setMaxHealth(double d) {
        this.set("maxHealth", d);
        return this;
    }

    public VehicleYML setJumpHeight(double d) {
        this.set("jumpHeight", d);
        return this;
    }

    public VehicleYML setSound(String string) {
        this.set("sound", string);
        return this;
    }

    public boolean needsUpdate() {
        return this.needsUpdate;
    }

    public void save() {
        this.verifyNeededTags();
        if (this.needsUpdate) {
            try {
                this.putTimeStamp();
                this.c.save(this.f);
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    public void verifyNeededTags() {
        this.verify("TurnSpeedInRadians", 0.039269908169872414);
        this.verify("useStaticTurning", true);
        this.verify("activation_radius", 2);
        this.verify("vehicle_texture_material", XReflection.supports(14) ? Material.RABBIT_HIDE.name() : Material.DIAMOND_AXE.name());
        this.verify("RequiresFuel", false);
        this.verify("trunksize", 9);
        this.verify("widthOffset", 1.5);
        this.verify("maxHealth", 50);
        this.verify("heightOffset", 2);
        this.verify("allowedInShop", true);
        this.verify("cost", 1000);
        this.verify("maxAcceleration", 1.1);
        this.verify("baseAcceleration", 0.065);
        this.verify("maxReverseAcceleration", 0.45);
        this.verify("canJumpOnBlocks", true);
        this.verify("jumpHeight", 0.55);
        this.verify("model.ModelSize", ModelSize.BABY_ARMORSTAND_HEAD.name());
        this.verify("InputManager.keys.F", "none");
        this.verify("InputManager.keys.LMB", "none");
        this.verify("InputManager.keys.RMB", "none");
        this.verify("canDeconstructByEnvironment", true);
        this.verify("rotationMultiplier", 1);
        if (this.get("vehicle_type").equals(VehicleTypes.HELI.getName())) {
            this.verify("descentSpeed", -0.1);
            try {
                this.verify("sound", Sound.ENTITY_PLAYER_ATTACK_SWEEP.name().toLowerCase().replace("_", "."));
            } catch (Error | Exception throwable) {}
        } else if (this.get("vehicle_type").equals(VehicleTypes.CAR.getName()) || this.get("vehicle_type").equals(VehicleTypes.PLANE.getName())) {
            this.verify("sound", "driving");
        } else {
            this.verify("sound", "null");
        }
        this.verify("soundVolume", 1);
        this.verify("driverseat.UseOffsetSeatFromModel", false);
        this.verify("driverseat.Offset", new Vector(0, 1, 0));
    }

    public VehicleYML setComplexAdditionAt(int n, Vector vector, String string) {
        this.set("complexadditions." + n + ".name", string);
        this.set("complexadditions." + n + ".offset", vector);
        return this;
    }

    public VehicleYML setModelSize(ModelSize modelSize) {
        this.set("model.ModelSize", modelSize.name());
        return this;
    }

    public VehicleYML setMaterial(Material material) {
        this.set("vehicle_texture_material", material.name());
        return this;
    }

    public VehicleYML setType(VehicleTypes vehicleTypes) {
        this.set("vehicle_type", vehicleTypes.getName());
        return this;
    }

    public VehicleYML setTrunkSize(int n) {
        this.set("trunksize", n);
        return this;
    }

    public VehicleYML setName(String string) {
        this.set("name", string);
        return this;
    }

    public VehicleYML setDisplayname(String string) {
        this.set("displayname", string);
        return this;
    }

    public void setID(int n) {
        this.set("id", n);
    }

    public VehicleYML setPassagerLocations(Vector ... vectorArray) {
        this.set("passagers", Arrays.asList(vectorArray));
        return this;
    }

    public VehicleYML setStopProjectileDamage(boolean bl) {
        this.set("stopProjectileDamage", bl);
        return this;
    }

    public VehicleYML setStopMeleeDamage(boolean bl) {
        this.set("stopMeleeDamage", bl);
        return this;
    }

    public VehicleYML setCenter(Vector vector) {
        this.set("center", vector);
        return this;
    }

    public VehicleYML setFrontVectorOffset(double d) {
        this.set("front_vector_offset", d);
        return this;
    }

    public VehicleYML setBackVectorOffset(double d) {
        this.set("back_vector_offset", d);
        return this;
    }

    public VehicleYML setActivationRadius(double d) {
        this.set("activation_radius", d);
        return this;
    }

    public void putTimeStamp() {
        this.c.set("lastModifiedByQA", (Object)(System.currentTimeMillis() + 5000L));
        this.needsUpdate = true;
    }
}

