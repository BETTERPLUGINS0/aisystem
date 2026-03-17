/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.configuration.serialization.ConfigurationSerialization
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.zombie_striker.qav.GarageCommand;
import me.zombie_striker.qav.ItemFact;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.MetricsLite;
import me.zombie_striker.qav.QAVListener;
import me.zombie_striker.qav.UnlockedVehicle;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.config.VehicleLoader;
import me.zombie_striker.qav.customitemmanager.AbstractItem;
import me.zombie_striker.qav.customitemmanager.CustomItemManager;
import me.zombie_striker.qav.customitemmanager.pack.MultiVersionPackProvider;
import me.zombie_striker.qav.customitemmanager.pack.StaticPackProvider;
import me.zombie_striker.qav.customitemmanager.qav.versions.V1_13.CustomVehicleItem;
import me.zombie_striker.qav.debugmanager.DebugManager;
import me.zombie_striker.qav.finput.FInputManager;
import me.zombie_striker.qav.finput.inputs.F40mmLauncher;
import me.zombie_striker.qav.finput.inputs.FCarHonk;
import me.zombie_striker.qav.finput.inputs.FMininukeBomber;
import me.zombie_striker.qav.finput.inputs.FShootBullet;
import me.zombie_striker.qav.finput.inputs.FSiren;
import me.zombie_striker.qav.finput.inputs.FTNTBomber;
import me.zombie_striker.qav.fuel.FuelItemStack;
import me.zombie_striker.qav.fuel.RepairItemStack;
import me.zombie_striker.qav.hooks.ProtectionHandler;
import me.zombie_striker.qav.hooks.QualityArmoryListener;
import me.zombie_striker.qav.hooks.QuickShopHook;
import me.zombie_striker.qav.hooks.implementation.WorldGuardHook;
import me.zombie_striker.qav.hooks.model.ModelEngineHook;
import me.zombie_striker.qav.input.LegacyInputListener;
import me.zombie_striker.qav.input.ModernInputListener;
import me.zombie_striker.qav.nms.NMSUtil;
import me.zombie_striker.qav.premium.PremiumHandler;
import me.zombie_striker.qav.qamini.EconHandler;
import me.zombie_striker.qav.qamini.ParticleHandlers;
import me.zombie_striker.qav.qamini.QAMini;
import me.zombie_striker.qav.util.ForksUtil;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class Main
extends JavaPlugin {
    public static boolean debugWithCommand = false;
    public static boolean debug = false;
    public static boolean verboseLogging = false;
    public static String prefix = "&6&lQAVehicles &f&l\u00bb&7";
    public static QAMini minihandler = null;
    public static boolean ENABLE_FILE_CREATION = true;
    public static String PASSAGER_PREFIX = "QA-Passager=";
    public static String MODEL_PREFIX = "QA-Model=";
    public static boolean enableGarage = false;
    public static boolean enableGarageCallback = false;
    public static boolean useChatForMessage = false;
    public static String VEHICLEPREFIX = "(QAV)";
    public static File carData;
    public static final double YOFFSET = 4.71238898038469;
    public static boolean cleanVehiclesOnEmpty;
    public static List<String> blacklistedWorlds;
    public static HashMap<Material, Double> customSpeedModifier;
    public static File playerUnlock;
    public static File items;
    public static File fuelYML;
    public static File repairYML;
    public static RepairItemStack repairItem;
    public static int maxYheightForVehicles;
    public static boolean enableVehicleLimiter;
    public static boolean allowVehiclePickup;
    public static boolean setOwnerOnPlacement;
    public static boolean disableAllFuelChecks;
    public static boolean enableVehicleDamage;
    public static boolean disableCreativeCloning;
    public static boolean removeVehicleOnDismount;
    public static boolean removeVehicleONLEAVE;
    public static boolean destroyVehicleONLEAVE;
    public static boolean destroyOnWater;
    public static boolean swapEndermiteWithChicken;
    public static boolean garageFuel;
    public static boolean enableVehiclePlayerCollision;
    public static boolean enableCrossVehicleCollision;
    public static boolean requirePermissionToDrive;
    public static boolean setOwnerIfNoneExist;
    public static boolean enableTrunks;
    public static boolean antiCheatHook;
    public static boolean freezeOnDestroy;
    public static boolean bypassCoalInCreative;
    public static boolean sendActionBarOnMove;
    public static boolean enableShopCooldown;
    public static boolean onlyPublicVehicles;
    public static boolean enable_RequirePermToBuyVehicle;
    public static boolean useHeads;
    private static File vehicledatayml;
    private static boolean USE_MANUAL_13;
    public static boolean useDamage;
    public static boolean separateModelAndDriver;
    public static boolean modernPlaneMovements;
    public static List<AbstractVehicle> vehicleTypes;
    public static List<VehicleEntity> vehicles;

    public static void DEBUG(String string) {
        DebugManager.sendDebugMessages(string);
    }

    public static void DEBUG(Object @NotNull ... objectArray) {
        for (Object object : objectArray) {
            DebugManager.sendDebugMessages(object.toString());
        }
    }

    public static boolean isVersionHigherThan(int n, int n2) {
        return QAMini.isVersionHigherThan(n, n2);
    }

    public void onLoad() {
        try {
            if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
                WorldGuardHook.register();
            }
        } catch (Error | Exception throwable) {
            // empty catch block
        }
    }

    public void onEnable() {
        Object object;
        Main.loadConfig0();
        ConfigurationSerialization.registerClass(UnlockedVehicle.class);
        ConfigurationSerialization.registerClass(VehicleEntity.class);
        if (this.getDescription().getVersion().contains("SNAPSHOT")) {
            this.getLogger().warning(String.format("You are using a SNAPSHOT version of %s(%s). This is not recommended for production use.", this.getName(), this.getDescription().getVersion()));
        } else if (!PremiumHandler.isPremium()) {
            this.getLogger().warning("You are using a leaked version of the plugin. Please consider buying the premium version.");
        }
        MetricsLite metricsLite = new MetricsLite(this, 12753);
        metricsLite.addCustomChart(new MetricsLite.SimplePie("premium", () -> PremiumHandler.isPremium() ? "true" : "false"));
        carData = new File(this.getDataFolder(), "vehicles");
        if (!carData.exists()) {
            carData.mkdirs();
        }
        this.initVals();
        ParticleHandlers.initValues();
        if (XReflection.supports(9)) {
            FInputManager.init(this);
            new FMininukeBomber();
            new FCarHonk();
            new FSiren();
            new F40mmLauncher();
            new FTNTBomber();
        }
        try {
            new FShootBullet();
        } catch (Error | Exception throwable) {
            // empty catch block
        }
        if (ENABLE_FILE_CREATION) {
            object = null;
            object = !Main.isVersionHigherThan(1, 14) || USE_MANUAL_13 ? new CustomVehicleItem() : new me.zombie_striker.qav.customitemmanager.qav.versions.V1_14.CustomVehicleItem();
            CustomItemManager.registerItemType("vehicles", (AbstractItem)object);
            ((AbstractItem)object).initItems(this.getDataFolder());
        }
        if (!QAMini.overrideURL) {
            this.getConfig().set("QAMini.resourcepackurl", CustomItemManager.getResourcepackProvider().serialize());
            this.saveConfig();
        } else if (!this.getConfig().contains("QAMini.resourcepackurl")) {
            this.getConfig().set("QAMini.resourcepackurl", CustomItemManager.getResourcepackProvider().serialize());
            this.saveConfig();
        } else if (this.getConfig().get("QAMini.resourcepackurl") instanceof String) {
            CustomItemManager.setResourcepack(new StaticPackProvider(this.getConfig().getString("QAMini.resourcepackurl")));
        } else {
            object = this.getConfig().getConfigurationSection("QAMini.resourcepackurl");
            if (object != null) {
                if (object.contains("21")) {
                    object.set("21-4", (Object)object.getString("21"));
                    object.set("21", null);
                    this.saveConfig();
                }
                CustomItemManager.setResourcepack(new MultiVersionPackProvider((ConfigurationSection)object));
            }
        }
        NMSUtil.init();
        Main.loadComplexParts(false);
        Main.loadVehicles(false);
        object = new QAVCommand();
        this.getCommand("QualityArmoryVehicles").setExecutor((CommandExecutor)object);
        this.getCommand("QualityArmoryVehicles").setTabCompleter((TabCompleter)object);
        if (enableGarage) {
            this.getCommand("garage").setExecutor((CommandExecutor)new GarageCommand());
        }
        Bukkit.getPluginManager().registerEvents((Listener)new QAVListener(this), (Plugin)this);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)vehicledatayml);
        if (yamlConfiguration.contains("data")) {
            for (VehicleEntity vehicleEntity : (List)yamlConfiguration.get("data")) {
                if (vehicleEntity == null || vehicleEntity.getDriverSeat() == null) continue;
                vehicles.add(vehicleEntity);
            }
            this.getLogger().info("Successfully loaded " + vehicles.size() + " spawned vehicles.");
        }
        new BukkitRunnable(){

            public void run() {
                for (VehicleEntity vehicleEntity : new ArrayList<VehicleEntity>(vehicles)) {
                    if (vehicleEntity == null || vehicleEntity.getDriverSeat() == null) continue;
                    vehicleEntity.tick();
                }
            }
        }.runTaskTimer((Plugin)this, 1L, 1L);
        ProtectionHandler.init();
        if (Bukkit.getPluginManager().isPluginEnabled("QuickShop")) {
            Bukkit.getPluginManager().registerEvents((Listener)new QuickShopHook(), (Plugin)this);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("ModelEngine") && ((Boolean)Main.a("hooks.ModelEngine", false)).booleanValue()) {
            ModelEngineHook.init();
        }
        if (XReflection.supports(21, 2)) {
            new ModernInputListener().register();
        } else {
            new LegacyInputListener().register();
        }
    }

    public void onDisable() {
        ModernInputListener.unregister();
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("data", vehicles);
        try {
            yamlConfiguration.save(vehicledatayml);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        for (VehicleEntity vehicleEntity : vehicles) {
            if (vehicleEntity == null) continue;
            vehicleEntity.deconstruct(null, "Disabling", true);
        }
        vehicles.clear();
    }

    public void initVals() {
        Object object;
        this.reloadConfig();
        vehicledatayml = new File(this.getDataFolder(), "vehicledata.yml");
        QualityArmoryVehicles.setPlugin(this);
        EconHandler.setupEconomy();
        if (Bukkit.getPluginManager().getPlugin("QualityArmory") == null) {
            minihandler = new QAMini();
            Bukkit.getPluginManager().registerEvents((Listener)minihandler, (Plugin)this);
            ParticleHandlers.initValues();
            try {
                ParticleHandlers.initValues();
            } catch (Error | Exception throwable) {
                // empty catch block
            }
            QAMini.overrideURL = (Boolean)Main.a("QAMini.resourcepackurl_override", QAMini.overrideURL);
            QAMini.S_ITEM_VARIENTS_NEW = ChatColor.translateAlternateColorCodes((char)'&', (String)((String)Main.a("QAMini.variantPrefix", QAMini.S_ITEM_VARIENTS_NEW)));
            QAMini.shouldSend = (Boolean)Main.a("QAMini.sendResourcepack", QAMini.shouldSend);
            QAMini.sendTitleOnJoin = (Boolean)Main.a("QAMini.sendResourcepackTitleOnJoin", QAMini.sendTitleOnJoin);
            QAMini.sendOnJoin = (Boolean)Main.a("QAMini.sendResourcepackOnJoin", QAMini.sendOnJoin);
            QAMini.kickIfDeny = (Boolean)Main.a("QAMini.kickIfRejectResourcepack", QAMini.kickIfDeny);
            QAMini.verboseLogging = (Boolean)Main.a("QAMini.verboseItemLogging", QAMini.verboseLogging);
        } else {
            object = Bukkit.getPluginManager().getPlugin("QualityArmory");
            QAMini.verboseLogging = object.getConfig().getBoolean("verboseItemLogging");
            try {
                this.getServer().getPluginManager().registerEvents((Listener)new QualityArmoryListener(), (Plugin)this);
            } catch (Error | Exception throwable) {
                // empty catch block
            }
        }
        items = new File(this.getDataFolder(), "items");
        if (!items.exists()) {
            items.mkdirs();
        }
        fuelYML = new File(items, "fuels.yml");
        object = new File(this.getDataFolder(), "fuels.yml");
        if (((File)object).exists()) {
            try {
                Files.move((File)object, fuelYML);
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        if (!fuelYML.exists()) {
            try {
                fuelYML.createNewFile();
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        if (!(repairYML = new File(items, "repair.yml")).exists()) {
            try {
                repairYML.createNewFile();
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        try {
            if (repairItem == null) {
                repairItem = RepairItemStack.loadFromFile();
            }
            repairItem.reload();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        if (this.getConfig().contains("fuel_ratios")) {
            for (String string : this.getConfig().getConfigurationSection("fuel_ratios").getKeys(false)) {
                try {
                    FuelItemStack.registerNewFuelToConfig(null, Material.matchMaterial((String)string), (short)0, null, this.getConfig().getInt("fuel_ratios." + string), fuelYML);
                } catch (Error | Exception throwable) {
                    throwable.printStackTrace();
                }
            }
            this.getConfig().set("fuel_ratios", null);
            this.saveConfig();
        } else {
            FuelItemStack.loadFuels(fuelYML);
        }
        playerUnlock = new File(this.getDataFolder(), "playerdata");
        playerUnlock.mkdirs();
        prefix = ChatColor.translateAlternateColorCodes((char)'&', (String)((String)Main.a("prefix", prefix)));
        USE_MANUAL_13 = (Boolean)Main.a("USE_1_13_MODEL_SYSTEM", USE_MANUAL_13);
        enableVehicleLimiter = (Boolean)Main.a("enable_VehicleLimiter", enableVehicleLimiter);
        allowVehiclePickup = (Boolean)Main.a("enable_PickupVehicles", allowVehiclePickup);
        enableGarage = (Boolean)Main.a("enable_UnlockableVehicles", enableGarage);
        enableGarageCallback = (Boolean)Main.a("enable_GarageCallback", enableGarageCallback);
        requirePermissionToDrive = (Boolean)Main.a("enable_RequirePermsToDriveType", requirePermissionToDrive);
        setOwnerOnPlacement = (Boolean)Main.a("enable_SetOwnerOnVehicleSpawn", setOwnerOnPlacement);
        enableVehicleDamage = (Boolean)Main.a("enable_VehicleDamage", enableVehicleDamage);
        disableAllFuelChecks = (Boolean)Main.a("enable_Debug_RemoveFuelChecks", disableAllFuelChecks);
        setOwnerIfNoneExist = (Boolean)Main.a("enable_SetOwnerOfVehicleIfUnowned", setOwnerIfNoneExist);
        ENABLE_FILE_CREATION = (Boolean)Main.a("Enable_Creation_Of_Default_Files", ENABLE_FILE_CREATION);
        removeVehicleOnDismount = (Boolean)Main.a("enable_RemoveVehiclesOnDismount", removeVehicleOnDismount);
        removeVehicleONLEAVE = (Boolean)Main.a("enable_RemoveVehiclesOnPlayerQuit", removeVehicleONLEAVE);
        destroyVehicleONLEAVE = (Boolean)Main.a("enable_DestroyVehiclesOnPlayerQuit", destroyVehicleONLEAVE);
        destroyOnWater = (Boolean)Main.a("enable_DestroyVehiclesOnWater", destroyOnWater);
        disableCreativeCloning = (Boolean)Main.a("enable_StopCreativeDuplication", disableCreativeCloning);
        enableVehiclePlayerCollision = (Boolean)Main.a("enable_VehiclePlayerCollision", enableVehiclePlayerCollision);
        enableCrossVehicleCollision = (Boolean)Main.a("enable_CrossVehicleCollision", enableCrossVehicleCollision);
        swapEndermiteWithChicken = (Boolean)Main.a("enable_SwapEndermiteWithCheckenForLowRider", swapEndermiteWithChicken);
        garageFuel = (Boolean)Main.a("enable_FuelCarsWhenSpawnedFromGarage", garageFuel);
        useHeads = (Boolean)Main.a("enable_UseHeadsForGUI", useHeads);
        debug = (Boolean)Main.a("ENABLE_DEBUG", debug);
        debugWithCommand = (Boolean)Main.a("override_debug_withCommand", debugWithCommand);
        antiCheatHook = (Boolean)Main.a("enable_AntiCheatHook", antiCheatHook);
        freezeOnDestroy = (Boolean)Main.a("freezeOnDestroy", freezeOnDestroy);
        bypassCoalInCreative = (Boolean)Main.a("bypassCoalInCreative", bypassCoalInCreative);
        sendActionBarOnMove = (Boolean)Main.a("sendActionBarOnMove", sendActionBarOnMove);
        enableShopCooldown = (Boolean)Main.a("enableShopCooldown", enableShopCooldown);
        onlyPublicVehicles = (Boolean)Main.a("makeVehiclesPublic", onlyPublicVehicles);
        useDamage = (Boolean)Main.a("unsafe.useDamageInsteadOfCustomModelData", useDamage);
        separateModelAndDriver = (Boolean)Main.a("unsafe.separateModelAndDriver", separateModelAndDriver);
        modernPlaneMovements = (Boolean)Main.a("modernPlaneMovements", modernPlaneMovements);
        enable_RequirePermToBuyVehicle = (Boolean)Main.a("enable_RequirePermToBuyVehicle", enable_RequirePermToBuyVehicle);
        DebugManager.setShouldDisplayInConsole((Boolean)Main.a("ENABLE_DEBUG", false));
        enableTrunks = (Boolean)Main.a("enable_VehiclesHaveTrunks", enableTrunks);
        Main.a("blockAccelerationReduction.BLOCK_NAME", 1.0);
        for (String string : this.getConfig().getConfigurationSection("blockAccelerationReduction").getKeys(false)) {
            try {
                Material material = Material.matchMaterial((String)string);
                customSpeedModifier.put(material, this.getConfig().getDouble("blockAccelerationReduction." + string));
            } catch (Error | Exception throwable) {}
        }
        cleanVehiclesOnEmpty = (Boolean)Main.a("enable_RemoveVehiclesOnEmpty", cleanVehiclesOnEmpty);
        blacklistedWorlds = (List)Main.a("BlacklistedWorlds", Arrays.asList("example_world_name"));
        maxYheightForVehicles = (Integer)Main.a("maxYHeight", maxYheightForVehicles);
        useChatForMessage = (Boolean)Main.a("enable_useChatForOutOfdFuelMessage", useChatForMessage);
        MessagesConfig.init();
        ItemFact.init();
        ForksUtil.init();
    }

    public static Object a(String string, Object object) {
        if (QualityArmoryVehicles.getPlugin().getConfig().contains(string)) {
            return QualityArmoryVehicles.getPlugin().getConfig().get(string);
        }
        QualityArmoryVehicles.getPlugin().getConfig().set(string, object);
        QualityArmoryVehicles.getPlugin().saveConfig();
        return object;
    }

    public static void loadVehicles(boolean bl) {
        if (bl) {
            vehicleTypes.clear();
        }
        VehicleLoader.loadVehicleFiles();
        if (bl) {
            for (VehicleEntity vehicleEntity : vehicles) {
                try {
                    vehicleEntity.setType(QualityArmoryVehicles.getVehicle(vehicleEntity.getType().getName()));
                } catch (Error | Exception throwable) {}
            }
        }
    }

    public static void loadComplexParts(boolean bl) {
    }

    static {
        cleanVehiclesOnEmpty = false;
        blacklistedWorlds = new ArrayList<String>();
        customSpeedModifier = new HashMap();
        maxYheightForVehicles = 256;
        enableVehicleLimiter = false;
        allowVehiclePickup = true;
        setOwnerOnPlacement = true;
        disableAllFuelChecks = false;
        enableVehicleDamage = true;
        disableCreativeCloning = false;
        removeVehicleOnDismount = false;
        removeVehicleONLEAVE = false;
        destroyVehicleONLEAVE = false;
        destroyOnWater = true;
        swapEndermiteWithChicken = false;
        garageFuel = true;
        enableVehiclePlayerCollision = true;
        enableCrossVehicleCollision = false;
        requirePermissionToDrive = false;
        setOwnerIfNoneExist = false;
        enableTrunks = true;
        antiCheatHook = false;
        freezeOnDestroy = false;
        bypassCoalInCreative = true;
        sendActionBarOnMove = true;
        enableShopCooldown = false;
        onlyPublicVehicles = false;
        enable_RequirePermToBuyVehicle = false;
        useHeads = true;
        USE_MANUAL_13 = false;
        useDamage = false;
        separateModelAndDriver = false;
        modernPlaneMovements = true;
        vehicleTypes = new ArrayList<AbstractVehicle>();
        vehicles = new ArrayList<VehicleEntity>();
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

