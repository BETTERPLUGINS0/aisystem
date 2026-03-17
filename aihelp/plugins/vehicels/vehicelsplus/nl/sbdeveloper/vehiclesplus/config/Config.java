/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.config;

import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import lombok.Generated;

public class Config {
    private final Integer version;
    private final Locale locale;
    private final Data dataSettings;
    private final Collision collision;
    private final double repairCostDivision;
    private final double renameCost;
    private final boolean actionBar;
    private final boolean spawnLocked;
    private final UpdateChecker updateCheckerSettings;
    private final Limits limits;
    private final String defaultRimDesignId;
    private final List<Integer> fuelGUIAmounts;
    private final Permissions permissions;

    @Generated
    public Integer getVersion() {
        return this.version;
    }

    @Generated
    public Locale getLocale() {
        return this.locale;
    }

    @Generated
    public Data getDataSettings() {
        return this.dataSettings;
    }

    @Generated
    public Collision getCollision() {
        return this.collision;
    }

    @Generated
    public double getRepairCostDivision() {
        return this.repairCostDivision;
    }

    @Generated
    public double getRenameCost() {
        return this.renameCost;
    }

    @Generated
    public boolean isActionBar() {
        return this.actionBar;
    }

    @Generated
    public boolean isSpawnLocked() {
        return this.spawnLocked;
    }

    @Generated
    public UpdateChecker getUpdateCheckerSettings() {
        return this.updateCheckerSettings;
    }

    @Generated
    public Limits getLimits() {
        return this.limits;
    }

    @Generated
    public String getDefaultRimDesignId() {
        return this.defaultRimDesignId;
    }

    @Generated
    public List<Integer> getFuelGUIAmounts() {
        return this.fuelGUIAmounts;
    }

    @Generated
    public Permissions getPermissions() {
        return this.permissions;
    }

    @Generated
    public Config(Integer n, Locale locale, Data data, Collision collision, double d, double d2, boolean bl, boolean bl2, UpdateChecker updateChecker, Limits limits, String string, List<Integer> list, Permissions permissions) {
        this.version = n;
        this.locale = locale;
        this.dataSettings = data;
        this.collision = collision;
        this.repairCostDivision = d;
        this.renameCost = d2;
        this.actionBar = bl;
        this.spawnLocked = bl2;
        this.updateCheckerSettings = updateChecker;
        this.limits = limits;
        this.defaultRimDesignId = string;
        this.fuelGUIAmounts = list;
        this.permissions = permissions;
    }

    public static class Data {
        private final String type;
        private final int interval;
        private final boolean verbose;
        private final String database;
        private String host;
        private int port;
        private String username;
        private String password;

        @Generated
        public Data(String string, int n, boolean bl, String string2) {
            this.type = string;
            this.interval = n;
            this.verbose = bl;
            this.database = string2;
        }

        @Generated
        public Data(String string, int n, boolean bl, String string2, String string3, int n2, String string4, String string5) {
            this.type = string;
            this.interval = n;
            this.verbose = bl;
            this.database = string2;
            this.host = string3;
            this.port = n2;
            this.username = string4;
            this.password = string5;
        }

        @Nullable
        @Generated
        public String getType() {
            return this.type;
        }

        @Nullable
        @Generated
        public int getInterval() {
            return this.interval;
        }

        @Nullable
        @Generated
        public boolean isVerbose() {
            return this.verbose;
        }

        @Nullable
        @Generated
        public String getDatabase() {
            return this.database;
        }

        @Nullable
        @Generated
        public String getHost() {
            return this.host;
        }

        @Nullable
        @Generated
        public int getPort() {
            return this.port;
        }

        @Nullable
        @Generated
        public String getUsername() {
            return this.username;
        }

        @Nullable
        @Generated
        public String getPassword() {
            return this.password;
        }
    }

    public static class Collision {
        private final String damageLevel;
        private final boolean despawnVehicle;
        private final boolean dropTrunkItems;
        private final boolean slabDriving;
        private final boolean blockDriving;
        private final boolean stopAtVehicle;
        private final boolean stopAtEntity;

        @Generated
        public Collision(String string, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6) {
            this.damageLevel = string;
            this.despawnVehicle = bl;
            this.dropTrunkItems = bl2;
            this.slabDriving = bl3;
            this.blockDriving = bl4;
            this.stopAtVehicle = bl5;
            this.stopAtEntity = bl6;
        }

        @Nullable
        @Generated
        public String getDamageLevel() {
            return this.damageLevel;
        }

        @Nullable
        @Generated
        public boolean isDespawnVehicle() {
            return this.despawnVehicle;
        }

        @Nullable
        @Generated
        public boolean isDropTrunkItems() {
            return this.dropTrunkItems;
        }

        @Nullable
        @Generated
        public boolean isSlabDriving() {
            return this.slabDriving;
        }

        @Nullable
        @Generated
        public boolean isBlockDriving() {
            return this.blockDriving;
        }

        @Nullable
        @Generated
        public boolean isStopAtVehicle() {
            return this.stopAtVehicle;
        }

        @Nullable
        @Generated
        public boolean isStopAtEntity() {
            return this.stopAtEntity;
        }
    }

    public static class UpdateChecker {
        private final boolean enabled;
        private final boolean downloadOnUpdate;

        @Generated
        public UpdateChecker(boolean bl, boolean bl2) {
            this.enabled = bl;
            this.downloadOnUpdate = bl2;
        }

        @Nullable
        @Generated
        public boolean isEnabled() {
            return this.enabled;
        }

        @Nullable
        @Generated
        public boolean isDownloadOnUpdate() {
            return this.downloadOnUpdate;
        }
    }

    public static class Limits {
        private final int have;
        private final int spawn;

        @Generated
        public Limits(int n, int n2) {
            this.have = n;
            this.spawn = n2;
        }

        @Nullable
        @Generated
        public int getHave() {
            return this.have;
        }

        @Nullable
        @Generated
        public int getSpawn() {
            return this.spawn;
        }
    }

    public static class Permissions {
        private final String buy;
        private final String adjust;
        private final String spawn;
        private final String ride;

        @Generated
        public Permissions(String string, String string2, String string3, String string4) {
            this.buy = string;
            this.adjust = string2;
            this.spawn = string3;
            this.ride = string4;
        }

        @Nullable
        @Generated
        public String getBuy() {
            return this.buy;
        }

        @Nullable
        @Generated
        public String getAdjust() {
            return this.adjust;
        }

        @Nullable
        @Generated
        public String getSpawn() {
            return this.spawn;
        }

        @Nullable
        @Generated
        public String getRide() {
            return this.ride;
        }
    }
}

