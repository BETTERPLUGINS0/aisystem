/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config;

import com.magmaguy.magmacore.config.ConfigurationEngine;
import com.magmaguy.magmacore.config.ConfigurationFile;
import java.util.List;

public class DefaultConfig
extends ConfigurationFile {
    private static int lowestYNormalCustom;
    private static int highestYNormalCustom;
    private static int lowestYNether;
    private static int highestYNether;
    private static int lowestYEnd;
    private static int highestYEnd;
    private static int normalCustomAirBuildingMinAltitude;
    private static int normalCustomAirBuildingMaxAltitude;
    private static int endAirBuildMinAltitude;
    private static int endAirBuildMaxAltitude;
    private static boolean newBuildingWarn;
    private static String regionProtectedMessage;
    private static boolean protectEliteMobsRegions;
    private static DefaultConfig instance;
    private static boolean setupDone;
    private static int modularChunkPastingSpeed;
    private static double percentageOfTickUsedForPasting;
    private static double percentageOfTickUsedForPregeneration;
    private static double pregenerationTPSPauseThreshold;
    private static double pregenerationTPSResumeThreshold;
    private static int distanceSurface;
    private static int distanceShallow;
    private static int distanceDeep;
    private static int distanceSky;
    private static int distanceLiquid;
    private static int distanceDungeon;
    private static int maxOffsetSurface;
    private static int maxOffsetShallow;
    private static int maxOffsetDeep;
    private static int maxOffsetSky;
    private static int maxOffsetLiquid;
    private static int maxOffsetDungeon;

    public DefaultConfig() {
        super("config.yml");
        instance = this;
    }

    public static void toggleSetupDone() {
        setupDone = !setupDone;
        ConfigurationEngine.writeValue(setupDone, DefaultConfig.instance.file, instance.getFileConfiguration(), "setupDone");
    }

    public static void toggleSetupDone(boolean value) {
        setupDone = value;
        ConfigurationEngine.writeValue(setupDone, DefaultConfig.instance.file, instance.getFileConfiguration(), "setupDone");
    }

    public static boolean toggleWarnings() {
        newBuildingWarn = !newBuildingWarn;
        ConfigurationEngine.writeValue(newBuildingWarn, DefaultConfig.instance.file, DefaultConfig.instance.fileConfiguration, "warnAdminsAboutNewBuildings");
        return newBuildingWarn;
    }

    @Override
    public void initializeValues() {
        lowestYNormalCustom = ConfigurationEngine.setInt(this.fileConfiguration, "lowestYNormalCustom", -60);
        highestYNormalCustom = ConfigurationEngine.setInt(this.fileConfiguration, "highestYNormalCustom", 320);
        lowestYNether = ConfigurationEngine.setInt(this.fileConfiguration, "lowestYNether", 4);
        highestYNether = ConfigurationEngine.setInt(this.fileConfiguration, "highestYNether", 120);
        lowestYEnd = ConfigurationEngine.setInt(this.fileConfiguration, "lowestYEnd", 0);
        highestYEnd = ConfigurationEngine.setInt(this.fileConfiguration, "highestYEnd", 320);
        normalCustomAirBuildingMinAltitude = ConfigurationEngine.setInt(this.fileConfiguration, "normalCustomAirBuildingMinAltitude", 80);
        normalCustomAirBuildingMaxAltitude = ConfigurationEngine.setInt(this.fileConfiguration, "normalCustomAirBuildingMaxAltitude", 120);
        endAirBuildMinAltitude = ConfigurationEngine.setInt(this.fileConfiguration, "endAirBuildMinAltitude", 80);
        endAirBuildMaxAltitude = ConfigurationEngine.setInt(this.fileConfiguration, "endAirBuildMaxAltitude", 120);
        newBuildingWarn = ConfigurationEngine.setBoolean(this.fileConfiguration, "warnAdminsAboutNewBuildings", true);
        regionProtectedMessage = ConfigurationEngine.setString(this.fileConfiguration, "regionProtectedMessage", "&8[BetterStructures] &cDefeat the zone's bosses to edit blocks!");
        protectEliteMobsRegions = ConfigurationEngine.setBoolean(this.fileConfiguration, "protectEliteMobsRegions", true);
        setupDone = ConfigurationEngine.setBoolean(this.fileConfiguration, "setupDone", false);
        modularChunkPastingSpeed = ConfigurationEngine.setInt(this.fileConfiguration, "modularChunkPastingSpeed", 10);
        percentageOfTickUsedForPasting = ConfigurationEngine.setDouble(List.of((Object)"Sets the maximum percentage of a tick that BetterStructures will use to paste builds, however many it maybe trying to generate.", (Object)"Ranges from 0.01 to 1, where 0.01 is 1% and 1 is 100%.", (Object)"Slower speeds will lower performance impact, but can lead to other problems such as builds suddenly popping in."), this.fileConfiguration, "percentageOfTickUsedForPasting", 0.2);
        percentageOfTickUsedForPregeneration = ConfigurationEngine.setDouble(List.of((Object)"Sets the maximum percentage of a tick that BetterStructures will use for world pregeneration when using the pregenerate command.", (Object)"Ranges from 0.01 to 1, where 0.01 is 1% and 1 is 100%.", (Object)"This controls how much of each server tick is dedicated to generating chunks, allowing you to balance generation speed with server performance.", (Object)"Lower values will generate chunks more slowly but reduce server lag, while higher values will generate faster but may impact server performance."), this.fileConfiguration, "percentageOfTickUsedForPregeneration", 0.1);
        pregenerationTPSPauseThreshold = ConfigurationEngine.setDouble(List.of((Object)"The TPS threshold at which chunk pregeneration will pause to protect server performance.", (Object)"When server TPS drops below this value, pregeneration will pause until TPS recovers.", (Object)"Default: 12.0"), this.fileConfiguration, "pregenerationTPSPauseThreshold", 12.0);
        pregenerationTPSResumeThreshold = ConfigurationEngine.setDouble(List.of((Object)"The TPS threshold at which chunk pregeneration will resume after being paused.", (Object)"Pregeneration will only resume when server TPS is at or above this value.", (Object)"Should be higher than the pause threshold to prevent rapid pause/resume cycles.", (Object)"Default: 14.0"), this.fileConfiguration, "pregenerationTPSResumeThreshold", 14.0);
        distanceSurface = ConfigurationEngine.setInt(List.of((Object)"Sets the distance between structures in the surface of a world.", (Object)"Shorter distances between structures will result in more structures overall."), this.fileConfiguration, "distanceSurface", 27);
        distanceShallow = ConfigurationEngine.setInt(List.of((Object)"Sets the distance between structures in shallow underground structure generation.", (Object)"Shorter distances between structures will result in more structures overall."), this.fileConfiguration, "distanceShallow", 22);
        distanceDeep = ConfigurationEngine.setInt(List.of((Object)"Sets the distance between structures in deep underground structure generation.", (Object)"Shorter distances between structures will result in more structures overall."), this.fileConfiguration, "distanceDeep", 22);
        distanceSky = ConfigurationEngine.setInt(List.of((Object)"Sets the distance between structures in placed in the air.", (Object)"Shorter distances between structures will result in more structures overall."), this.fileConfiguration, "distanceSky", 90);
        distanceLiquid = ConfigurationEngine.setInt(List.of((Object)"Sets the distance between structures liquid surfaces such as oceans.", (Object)"Shorter distances between structures will result in more structures overall."), this.fileConfiguration, "distanceLiquid", 60);
        distanceDungeon = ConfigurationEngine.setInt(List.of((Object)"Sets the distance between dungeons.", (Object)"Shorter distances between dungeons will result in more dungeons overall."), this.fileConfiguration, "distanceDungeonV2", 80);
        maxOffsetSurface = ConfigurationEngine.setInt(List.of((Object)"Used to tweak the randomization of the distance between structures in the surface of a world.", (Object)"Smaller values will result in structures being more on a grid, and larger values will result in them being less predictably placed."), this.fileConfiguration, "maxOffsetSurface", 5);
        maxOffsetShallow = ConfigurationEngine.setInt(List.of((Object)"Used to tweak the randomization of the distance between structures in the shallow underworld of a world.", (Object)"Smaller values will result in structures being more on a grid, and larger values will result in them being less predictably placed."), this.fileConfiguration, "maxOffsetShallow", 5);
        maxOffsetDeep = ConfigurationEngine.setInt(List.of((Object)"Used to tweak the randomization of the distance between structures in the deep underground of a world.", (Object)"Smaller values will result in structures being more on a grid, and larger values will result in them being less predictably placed."), this.fileConfiguration, "maxOffsetDeep", 5);
        maxOffsetSky = ConfigurationEngine.setInt(List.of((Object)"Used to tweak the randomization of the distance between structures in the sky.", (Object)"Smaller values will result in structures being more on a grid, and larger values will result in them being less predictably placed."), this.fileConfiguration, "maxOffsetSky", 5);
        maxOffsetLiquid = ConfigurationEngine.setInt(List.of((Object)"Used to tweak the randomization of the distance between structures on oceans.", (Object)"Smaller values will result in structures being more on a grid, and larger values will result in them being less predictably placed."), this.fileConfiguration, "maxOffsetLiquid", 5);
        maxOffsetDungeon = ConfigurationEngine.setInt(List.of((Object)"Used to tweak the randomization of the distance between dungeons.", (Object)"Smaller values will result in dungeons being more on a grid, and larger values will result in them being less predictably placed."), this.fileConfiguration, "maxOffsetDungeonV2", 18);
        ConfigurationEngine.fileSaverOnlyDefaults(this.fileConfiguration, this.file);
    }

    public static int getLowestYNormalCustom() {
        return lowestYNormalCustom;
    }

    public static int getHighestYNormalCustom() {
        return highestYNormalCustom;
    }

    public static int getLowestYNether() {
        return lowestYNether;
    }

    public static int getHighestYNether() {
        return highestYNether;
    }

    public static int getLowestYEnd() {
        return lowestYEnd;
    }

    public static int getHighestYEnd() {
        return highestYEnd;
    }

    public static int getNormalCustomAirBuildingMinAltitude() {
        return normalCustomAirBuildingMinAltitude;
    }

    public static int getNormalCustomAirBuildingMaxAltitude() {
        return normalCustomAirBuildingMaxAltitude;
    }

    public static int getEndAirBuildMinAltitude() {
        return endAirBuildMinAltitude;
    }

    public static int getEndAirBuildMaxAltitude() {
        return endAirBuildMaxAltitude;
    }

    public static boolean isNewBuildingWarn() {
        return newBuildingWarn;
    }

    public static String getRegionProtectedMessage() {
        return regionProtectedMessage;
    }

    public static boolean isProtectEliteMobsRegions() {
        return protectEliteMobsRegions;
    }

    public static boolean isSetupDone() {
        return setupDone;
    }

    public static int getModularChunkPastingSpeed() {
        return modularChunkPastingSpeed;
    }

    public static double getPercentageOfTickUsedForPasting() {
        return percentageOfTickUsedForPasting;
    }

    public static double getPercentageOfTickUsedForPregeneration() {
        return percentageOfTickUsedForPregeneration;
    }

    public static double getPregenerationTPSPauseThreshold() {
        return pregenerationTPSPauseThreshold;
    }

    public static double getPregenerationTPSResumeThreshold() {
        return pregenerationTPSResumeThreshold;
    }

    public static int getDistanceSurface() {
        return distanceSurface;
    }

    public static int getDistanceShallow() {
        return distanceShallow;
    }

    public static int getDistanceDeep() {
        return distanceDeep;
    }

    public static int getDistanceSky() {
        return distanceSky;
    }

    public static int getDistanceLiquid() {
        return distanceLiquid;
    }

    public static int getDistanceDungeon() {
        return distanceDungeon;
    }

    public static int getMaxOffsetSurface() {
        return maxOffsetSurface;
    }

    public static int getMaxOffsetShallow() {
        return maxOffsetShallow;
    }

    public static int getMaxOffsetDeep() {
        return maxOffsetDeep;
    }

    public static int getMaxOffsetSky() {
        return maxOffsetSky;
    }

    public static int getMaxOffsetLiquid() {
        return maxOffsetLiquid;
    }

    public static int getMaxOffsetDungeon() {
        return maxOffsetDungeon;
    }

    static {
        modularChunkPastingSpeed = 10;
        percentageOfTickUsedForPasting = 0.2;
        percentageOfTickUsedForPregeneration = 0.1;
        pregenerationTPSPauseThreshold = 12.0;
        pregenerationTPSResumeThreshold = 14.0;
    }
}

