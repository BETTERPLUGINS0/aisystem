/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package nl.mtvehicles.core.infrastructure.dataconfig;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.enums.DriveUp;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.enums.WGFlag;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DefaultConfig
extends MTVConfig {
    private final GasStationConfig gasStations = new GasStationConfig();

    public DefaultConfig() {
        super(ConfigType.DEFAULT);
    }

    @Deprecated
    public String getMessage(String key) {
        return TextUtils.colorize(this.getConfiguration().getString(key));
    }

    public Object get(Option configOption) {
        return this.getConfiguration().get(configOption.getPath());
    }

    public boolean hasOldVersionChecking() {
        return this.getConfiguration().get("Config-Versie") != null;
    }

    public DriveUp driveUpSlabs() {
        DriveUp returns = DriveUp.BOTH;
        try {
            switch (Objects.requireNonNull(this.get(Option.DRIVE_UP).toString().toLowerCase())) {
                case "blocks": 
                case "block": {
                    returns = DriveUp.BLOCKS;
                    break;
                }
                case "slabs": 
                case "slab": {
                    returns = DriveUp.SLABS;
                }
            }
        } catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return returns;
    }

    public boolean isHoneySlowdownEnabled() {
        return (Boolean)this.get(Option.SLOW_ON_HONEY);
    }

    public boolean isIceSlippery() {
        return (Boolean)this.get(Option.SLIPPERY_ICE);
    }

    public boolean canUseJerryCan(Player player, Location loc) {
        if (!this.gasStations.areGasStationsEnabled()) {
            return true;
        }
        if (DependencyModule.worldGuard.isInRegionWithFlag(player, loc, WGFlag.GAS_STATION, false)) {
            return false;
        }
        if (this.gasStations.canUseJerryCanOutsideOfGasStation()) {
            return true;
        }
        return DependencyModule.worldGuard.isInsideGasStation(player, loc);
    }

    public boolean canUseJerryCan(Player player) {
        return this.canUseJerryCan(player, player.getLocation());
    }

    public boolean canFillJerryCans(Player p, Location loc) {
        if (!this.gasStations.areGasStationsEnabled()) {
            return false;
        }
        if (!this.gasStations.isFillJerryCansEnabled()) {
            return false;
        }
        if (!DependencyModule.worldGuard.isInsideGasStation(p, loc)) {
            return false;
        }
        if (!this.gasStations.hasFillJerryCansPermission(p)) {
            p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.NO_PERMISSION)));
            return false;
        }
        return true;
    }

    public boolean jerryCanPlaySound() {
        return (Boolean)this.get(Option.GAS_STATIONS_FILL_JERRYCANS_PLAY_SOUND);
    }

    public boolean isFillJerryCansLeverEnabled() {
        return (Boolean)this.get(Option.GAS_STATIONS_FILL_JERRYCANS_LEVER);
    }

    public boolean isFillJerryCansTripwireHookEnabled() {
        return (Boolean)this.get(Option.GAS_STATIONS_FILL_JERRYCANS_TRIPWIRE_HOOK);
    }

    public boolean isFillJerryCanPriceEnabled() {
        if (!this.gasStations.areGasStationsEnabled()) {
            return false;
        }
        if (!this.gasStations.isFillJerryCansEnabled()) {
            return false;
        }
        if (!DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) {
            return false;
        }
        if (!DependencyModule.vault.isEconomySetUp()) {
            return false;
        }
        return (Boolean)this.get(Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_ENABLED);
    }

    public double getFillJerryCanPrice() {
        if ((Double)this.get(Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE) <= 0.0) {
            return 30.0;
        }
        return (Double)this.get(Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE);
    }

    public boolean isWorldDisabled(String worldName) {
        if (this.getDisabledWorlds().isEmpty()) {
            return false;
        }
        return this.getDisabledWorlds().contains(worldName);
    }

    private List<String> getDisabledWorlds() {
        return (List)this.get(Option.DISABLED_WORLDS);
    }

    public boolean isBlockWhitelistEnabled() {
        return (Boolean)this.get(Option.BLOCK_WHITELIST_ENABLED);
    }

    public List<Material> blockWhiteList() {
        return ((List)this.get(Option.BLOCK_WHITELIST_LIST)).stream().map(Material::getMaterial).collect(Collectors.toList());
    }

    private RegionAction.ListType getRegionActionListType(RegionAction action) {
        String configOption = "disabled";
        switch (action) {
            case PLACE: {
                configOption = this.get(Option.REGION_ACTIONS_PLACE).toString().toLowerCase(Locale.ROOT);
                break;
            }
            case PICKUP: {
                configOption = this.get(Option.REGION_ACTIONS_PICKUP).toString().toLowerCase(Locale.ROOT);
                break;
            }
            case ENTER: {
                configOption = this.get(Option.REGION_ACTIONS_ENTER).toString().toLowerCase(Locale.ROOT);
                break;
            }
            case RIDE: {
                configOption = this.get(Option.REGION_ACTIONS_RIDE).toString().toLowerCase(Locale.ROOT);
            }
        }
        if (configOption.equalsIgnoreCase("whitelist")) {
            return RegionAction.ListType.WHITELIST;
        }
        if (configOption.equalsIgnoreCase("blacklist")) {
            return RegionAction.ListType.BLACKLIST;
        }
        return RegionAction.ListType.DISABLED;
    }

    public boolean canProceedWithAction(RegionAction action, VehicleType vehicleType, Location loc, Player p) {
        if (this.isWorldDisabled(loc.getWorld().getName())) {
            return false;
        }
        if (!DependencyModule.isDependencyEnabled(SoftDependency.WORLD_GUARD)) {
            return true;
        }
        if (vehicleType.isUsageDisabled(p, loc)) {
            return false;
        }
        boolean returns = true;
        RegionAction.ListType listType = this.getRegionActionListType(action);
        if (!listType.isEnabled()) {
            return true;
        }
        boolean isWhitelist = listType.isWhitelist();
        boolean isBlacklist = listType.isBlacklist();
        switch (action) {
            case PLACE: {
                if (isWhitelist) {
                    returns = DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.PLACE, true);
                    break;
                }
                if (!isBlacklist) break;
                returns = !DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.PLACE, false);
                break;
            }
            case PICKUP: {
                if (isWhitelist) {
                    returns = DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.PICKUP, true);
                    break;
                }
                if (!isBlacklist) break;
                returns = !DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.PICKUP, false);
                break;
            }
            case ENTER: {
                if (isWhitelist) {
                    returns = DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.ENTER, true);
                    break;
                }
                if (!isBlacklist) break;
                returns = !DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.ENTER, false);
                break;
            }
            case RIDE: {
                if (isWhitelist) {
                    returns = DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.RIDE, true);
                    break;
                }
                if (!isBlacklist) break;
                returns = !DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.RIDE, false);
            }
        }
        return returns;
    }

    public boolean usePlayerFacingDriving() {
        return (Boolean)this.get(Option.USE_PLAYER_FACING);
    }

    private class GasStationConfig {
        private GasStationConfig() {
        }

        private boolean areGasStationsEnabled() {
            if (!DependencyModule.isDependencyEnabled(SoftDependency.WORLD_GUARD)) {
                return false;
            }
            return (Boolean)DefaultConfig.this.get(Option.GAS_STATIONS_ENABLED);
        }

        private boolean canUseJerryCanOutsideOfGasStation() {
            return (Boolean)DefaultConfig.this.get(Option.GAS_STATIONS_CAN_USE_JERRYCAN_OUTSIDE_OF_GAS_STATION);
        }

        private boolean isFillJerryCansEnabled() {
            return (Boolean)DefaultConfig.this.get(Option.GAS_STATIONS_FILL_JERRYCANS_ENABLED);
        }

        private boolean hasFillJerryCansPermission(Player p) {
            if (!((Boolean)DefaultConfig.this.get(Option.GAS_STATIONS_FILL_JERRYCANS_NEED_PERMISSION)).booleanValue()) {
                return true;
            }
            return p.hasPermission("mtvehicles.filljerrycans");
        }
    }

    public static enum Option {
        AUTO_UPDATE("autoUpdate", true),
        VEHICLE_MENU_SIZE("vehicleMenuSize", 3),
        HELICOPTER_BLADES_ALWAYS_ON("helicopterBladesAlwaysOn", true),
        DISABLE_PICKUP_FROM_WATER("disablePickupFromWater", false),
        TRUNK_ENABLED("trunkEnabled", true),
        PUT_ONESELF_AS_OWNER("putOneselfAsOwner", false),
        MAX_FLYING_HEIGHT("maxFlyingHeight", 150),
        TAKE_OFF_SPEED("takeOffSpeed", 0.4),
        AIRPLANE_TNT("airplaneTNT", false),
        AIRPLANE_COOLDOWN("airplaneTNTCooldown", 3),
        CAR_PICKUP("carPickup", false),
        FUEL_ENABLED("fuelEnabled", true),
        FUEL_MULTIPLIER("fuelMultiplier", 1),
        JERRYCANS("jerrycans", new ArrayList<Integer>(Arrays.asList(25, 50, 75))),
        DAMAGE_ENABLED("damageEnabled", false),
        DAMAGE_MULTIPLIER("damageMultiplier", 0.5),
        EXPLODING_VEHICLE("explodingVehicle", false),
        DESTRUCTIBLE_VEHICLE("destructibleVehicle", false),
        HORN_COOLDOWN("hornCooldown", 5),
        HORN_TYPE("hornType", "minetopiaclassic.horn1"),
        HEADLIGHTS_ENABLED("headlightsEnabled", false),
        TANK_TNT("tankTNT", false),
        TANK_COOLDOWN("tankCooldown", 10),
        DRIVE_UP("driveUp", "both"),
        EXTREME_HELICOPTER_FALL("extremeHelicopterFall", false),
        HELICOPTER_FALL_DAMAGE("helicopterFallDamage", 40.0),
        DRIVE_ON_CARPETS("driveOnCarpets", true),
        SLOW_ON_HONEY("slowDownOnHoney", false),
        SLIPPERY_ICE("slipperyIce", false),
        BLOCK_WHITELIST_ENABLED("blockWhitelist.enabled", false),
        BLOCK_WHITELIST_LIST("blockWhitelist.list", new ArrayList<String>().add("GRAY_CONCRETE")),
        DISABLED_WORLDS("disabledWorlds", new ArrayList<E>()),
        GAS_STATIONS_ENABLED("gasStations.enabled", false),
        GAS_STATIONS_CAN_USE_JERRYCAN_OUTSIDE_OF_GAS_STATION("gasStations.canUseJerryCanOutsideOfGasStation", true),
        GAS_STATIONS_FILL_JERRYCANS_ENABLED("gasStations.fillJerryCans.enabled", true),
        GAS_STATIONS_FILL_JERRYCANS_NEED_PERMISSION("gasStations.fillJerryCans.needPermission", false),
        GAS_STATIONS_FILL_JERRYCANS_PLAY_SOUND("gasStations.fillJerryCans.playSound", true),
        GAS_STATIONS_FILL_JERRYCANS_LEVER("gasStations.fillJerryCans.lever", true),
        GAS_STATIONS_FILL_JERRYCANS_TRIPWIRE_HOOK("gasStations.fillJerryCans.tripwireHook", false),
        GAS_STATIONS_FILL_JERRYCANS_PRICE_ENABLED("gasStations.fillJerryCans.price.enabled", true),
        GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE("gasStations.fillJerryCans.price.pricePerLitre", 30.0),
        REGION_ACTIONS_PLACE("regionActions.place", "disabled"),
        REGION_ACTIONS_ENTER("regionActions.enter", "disabled"),
        REGION_ACTIONS_PICKUP("regionActions.pickup", "disabled"),
        REGION_ACTIONS_RIDE("regionActions.ride", "disabled"),
        USE_PLAYER_FACING("usePlayerFacing", false);

        private final String path;
        private final Object defaultValue;

        private Option(String path, Object defaultValue) {
            this.path = path;
            this.defaultValue = defaultValue;
        }

        public Object getDefaultValue() {
            return this.defaultValue;
        }

        public String getPath() {
            return this.path;
        }

        public Type getValueType() {
            return this.getDefaultValue().getClass();
        }
    }
}

