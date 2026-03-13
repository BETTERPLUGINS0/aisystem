/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package net.advancedplugins.as.impl.effects.effects.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Material;

public class SettingValues {
    private static HashSet<String> holyWhitescrollWorldBlacklist = new HashSet();
    private static Set<Material> trenchBlacklist = ASManager.createMaterialSet(Arrays.asList("BEDROCK", "PORTAL", "END_PORTAL", "ENDER_PORTAL", "END_PORTAL_FRAME", "ENDER_PORTAL_FRAME", "END_GATEWAY", "SPAWNER", "BUDDING_AMETHYST", "BARRIER", "LIGHT", "COMMAND_BLOCK", "CHAIN_COMMAND_BLOCK", "REPEATING_COMMAND_BLOCK"));
    private static boolean breakBlockEffectDoAllDurabilityDamage = true;
    private static boolean trenchMoveItemsToInventory = true;
    private static boolean tunnelMoveItemsToInventory = true;
    private static List<String> bannedMaterials = Arrays.asList("SPAWNER", "AIR", "LAVA", "WATER", "CAULDRON", "BEDROCK");
    private static boolean breakTreeRespectPlayerPlacedBlocks = true;
    private static boolean addBrokenBlocksToMCStats = true;
    private static boolean expEffectIgnoreSilktouch = false;
    private static List<String> ignoredMetaDrops = new ArrayList<String>();
    private static boolean bowFullPower = true;
    private static boolean callBlockBreakEvents = true;

    public static void setHolyWhitescrollWorldBlacklist(HashSet<String> hashSet) {
        holyWhitescrollWorldBlacklist = hashSet;
    }

    public static HashSet<String> getHolyWhitescrollWorldBlacklist() {
        return holyWhitescrollWorldBlacklist;
    }

    public static void setTrenchBlacklist(Set<Material> set) {
        trenchBlacklist = set;
    }

    public static Set<Material> getTrenchBlacklist() {
        return trenchBlacklist;
    }

    public static void setBreakBlockEffectDoAllDurabilityDamage(boolean bl) {
        breakBlockEffectDoAllDurabilityDamage = bl;
    }

    public static boolean isBreakBlockEffectDoAllDurabilityDamage() {
        return breakBlockEffectDoAllDurabilityDamage;
    }

    public static void setTrenchMoveItemsToInventory(boolean bl) {
        trenchMoveItemsToInventory = bl;
    }

    public static boolean isTrenchMoveItemsToInventory() {
        return trenchMoveItemsToInventory;
    }

    public static void setTunnelMoveItemsToInventory(boolean bl) {
        tunnelMoveItemsToInventory = bl;
    }

    public static boolean isTunnelMoveItemsToInventory() {
        return tunnelMoveItemsToInventory;
    }

    public static void setBannedMaterials(List<String> list) {
        bannedMaterials = list;
    }

    public static List<String> getBannedMaterials() {
        return bannedMaterials;
    }

    public static boolean isBreakTreeRespectPlayerPlacedBlocks() {
        return breakTreeRespectPlayerPlacedBlocks;
    }

    public static void setBreakTreeRespectPlayerPlacedBlocks(boolean bl) {
        breakTreeRespectPlayerPlacedBlocks = bl;
    }

    public static boolean isAddBrokenBlocksToMCStats() {
        return addBrokenBlocksToMCStats;
    }

    public static void setAddBrokenBlocksToMCStats(boolean bl) {
        addBrokenBlocksToMCStats = bl;
    }

    public static boolean isExpEffectIgnoreSilktouch() {
        return expEffectIgnoreSilktouch;
    }

    public static void setExpEffectIgnoreSilktouch(boolean bl) {
        expEffectIgnoreSilktouch = bl;
    }

    public static List<String> getIgnoredMetaDrops() {
        return ignoredMetaDrops;
    }

    public static void setIgnoredMetaDrops(List<String> list) {
        ignoredMetaDrops = list;
    }

    public static boolean isBowFullPower() {
        return bowFullPower;
    }

    public static void setBowFullPower(boolean bl) {
        bowFullPower = bl;
    }

    public static boolean isCallBlockBreakEvents() {
        return callBlockBreakEvents;
    }

    public static void setCallBlockBreakEvents(boolean bl) {
        callBlockBreakEvents = bl;
    }
}

