/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetArgument;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetType;
import net.advancedplugins.as.impl.effects.effects.settings.SettingValues;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import net.advancedplugins.as.impl.utils.hooks.plugins.ItemsAdderHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.OraxenHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.ProtectionStonesHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.SlimeFunHook;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class TrenchTarget
extends TargetType {
    public TrenchTarget() {
        super("Trench");
    }

    public static BlockFace getPlayerFacing(float f, float f2) {
        if (f2 <= -80.0f) {
            return BlockFace.UP;
        }
        if (f2 >= 80.0f) {
            return BlockFace.DOWN;
        }
        if (f < 0.0f) {
            f += 360.0f;
        }
        if (f >= 315.0f || f < 45.0f) {
            return BlockFace.SOUTH;
        }
        if (f < 135.0f) {
            return BlockFace.WEST;
        }
        if (f < 225.0f) {
            return BlockFace.NORTH;
        }
        return BlockFace.EAST;
    }

    @Override
    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        int n;
        int n2;
        int n3;
        HashMap<TargetArgument, String> hashMap = this.classifyTarget(string2);
        boolean bl = false;
        if (hashMap.containsKey((Object)TargetArgument.RADIUS_CUSTOM)) {
            String[] stringArray = ((String)hashMap.get((Object)TargetArgument.RADIUS_CUSTOM)).split("x");
            n3 = ASManager.parseInt(stringArray.length > 0 ? stringArray[0] : "0");
            n2 = ASManager.parseInt(stringArray.length > 1 ? stringArray[1] : "0");
            n = ASManager.parseInt(stringArray.length > 2 ? stringArray[2] : "0");
            bl = true;
            if (n3 % 2 == 0 || n2 % 2 == 0 || n % 2 == 0) {
                executionTask.reportIssue(string, "Trench supports only odd numbers in order to work properly");
            }
        } else {
            int n4 = ASManager.parseInt(hashMap.getOrDefault((Object)TargetArgument.RADIUS, "0"));
            if (n4 == 0) {
                n4 = 3;
            }
            if (n4 % 2 == 0) {
                executionTask.reportIssue(string, "Trench supports only odd numbers in order to work properly");
            }
            n2 = n = (n4 = (n4 - 1) / 2);
            n3 = n;
        }
        boolean bl2 = ASManager.parseBoolean(hashMap.getOrDefault((Object)TargetArgument.IGNORE_TOOL, "false"), false);
        ArrayList<Location> arrayList = new ArrayList<Location>();
        Block block = executionTask.getBuilder().getBlock();
        if (block == null) {
            return TargetResults.builder().targetLocations(arrayList).build();
        }
        if (block.getType().name().equalsIgnoreCase("FIRE")) {
            return TargetResults.builder().targetLocations(arrayList).build();
        }
        List<String> list = executionTask.getAbility().getWhitelist();
        List<String> list2 = executionTask.getAbility().getBlacklist();
        if (list != null && !list.isEmpty() ? !list.contains(block.getType().name()) : list2 != null && !list2.isEmpty() && list2.contains(block.getType().name())) {
            return TargetResults.builder().targetLocations(arrayList).build();
        }
        Location location = block.getLocation();
        World world = location.getWorld();
        if (bl) {
            Location location2 = executionTask.getBuilder().getMain().getLocation();
            BlockFace blockFace = TrenchTarget.getPlayerFacing(location2.getYaw(), location2.getPitch());
            Vector vector = TrenchTarget.getRightVector(blockFace);
            Vector vector2 = TrenchTarget.getUpVector(blockFace);
            Vector vector3 = blockFace.getDirection();
            int n5 = (n3 - 1) / 2;
            int n6 = (n2 - 1) / 2;
            for (int i = -n6; i <= n6; ++i) {
                for (int j = -n5; j <= n5; ++j) {
                    for (int k = 0; k < n; ++k) {
                        Vector vector4 = vector.clone().multiply(j).add(vector2.clone().multiply(i)).add(vector3.clone().multiply(k));
                        Block block2 = location.getBlock().getRelative(vector4.getBlockX(), vector4.getBlockY(), vector4.getBlockZ());
                        TrenchTarget.processBlock(block2, block, location, bl2, executionTask, arrayList);
                    }
                }
            }
        } else {
            for (int i = location.getBlockX() - n3; i <= location.getBlockX() + n3; ++i) {
                for (int j = location.getBlockZ() - n; j <= location.getBlockZ() + n; ++j) {
                    for (int k = location.getBlockY() - n2; k <= location.getBlockY() + n2; ++k) {
                        Block block3 = world.getBlockAt(i, k, j);
                        TrenchTarget.processBlock(block3, block, location, bl2, executionTask, arrayList);
                    }
                }
            }
        }
        if (SettingValues.isTrenchMoveItemsToInventory()) {
            executionTask.getBuilder().getDrops().getSettings().setAddToInventory(true);
        }
        return TargetResults.builder().targetLocations(arrayList).build();
    }

    public static void processBlock(Block block, Block block2, Location location, boolean bl, ExecutionTask executionTask, List<Location> list) {
        if (block.equals((Object)location.getBlock()) || block.getType().name().equals("AIR") || block.isLiquid()) {
            ItemsAdderHook itemsAdderHook;
            if (HooksHandler.isEnabled(HookPlugin.ITEMSADDER) && (itemsAdderHook = (ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER)).isCustomBlock(block)) {
                block.setMetadata("ae-skip-trigger", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
            }
            return;
        }
        if (HooksHandler.isEnabled(HookPlugin.SLIMEFUN) && ((SlimeFunHook)HooksHandler.getHook(HookPlugin.SLIMEFUN)).isSlimefunItem(block)) {
            return;
        }
        if (HooksHandler.isEnabled(HookPlugin.PROTECTIONSTONES) && ((ProtectionStonesHook)HooksHandler.getHook(HookPlugin.PROTECTIONSTONES)).isProtectionStone(block)) {
            return;
        }
        if (!bl && !ASManager.isCorrectTool(executionTask.getBuilder().getItem(), block.getType())) {
            return;
        }
        if (block.hasMetadata("cancelBreak")) {
            return;
        }
        if (SettingValues.getTrenchBlacklist().contains(block.getType())) {
            return;
        }
        if (block.getType().name().contains("SHULKER_BOX")) {
            return;
        }
        List<String> list2 = executionTask.getAbility().getWhitelist();
        List<String> list3 = executionTask.getAbility().getBlacklist();
        if (list2 != null && !list2.isEmpty() ? !list2.contains(block.getType().name()) : list3 != null && !list3.isEmpty() && list3.contains(block.getType().name())) {
            return;
        }
        TrenchTarget.handleCustomBlockMetadata(block, block2, location, bl, executionTask, list);
        list.add(block.getLocation());
    }

    public static Vector getRightVector(BlockFace blockFace) {
        return switch (blockFace) {
            case BlockFace.NORTH -> new Vector(-1, 0, 0);
            case BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN -> new Vector(1, 0, 0);
            case BlockFace.EAST -> new Vector(0, 0, 1);
            case BlockFace.WEST -> new Vector(0, 0, -1);
            default -> new Vector(0, 0, 0);
        };
    }

    public static Vector getUpVector(BlockFace blockFace) {
        return switch (blockFace) {
            case BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST -> new Vector(0, 1, 0);
            case BlockFace.UP -> new Vector(0, 0, -1);
            case BlockFace.DOWN -> new Vector(0, 0, 1);
            default -> new Vector(0, 0, 0);
        };
    }

    private static void handleCustomBlockMetadata(Block block, Block block2, Location location, boolean bl, ExecutionTask executionTask, List<Location> list) {
        PluginHookInstance pluginHookInstance;
        if (HooksHandler.isEnabled(HookPlugin.ITEMSADDER) && ((ItemsAdderHook)(pluginHookInstance = (ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER))).isCustomBlock(block) && (bl || ((ItemsAdderHook)pluginHookInstance).canBeBrokenWith(executionTask.getBuilder().getItem(), block))) {
            block.setMetadata("ae_custom_block_break", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
            if (block.equals((Object)location.getBlock())) {
                block2.setMetadata("ae-skip-trigger", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
                return;
            }
            list.add(block.getLocation());
            return;
        }
        if (HooksHandler.isEnabled(HookPlugin.ORAXEN) && ((OraxenHook)(pluginHookInstance = (OraxenHook)HooksHandler.getHook(HookPlugin.ORAXEN))).isCustomBlock(block) && (bl || ((OraxenHook)pluginHookInstance).canBeBrokenWith(executionTask.getBuilder().getItem(), block))) {
            if (block.equals((Object)location.getBlock())) {
                block2.setMetadata("ae-oraxen-cancel", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
            }
            block.setMetadata("ae_custom_block_break", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
            list.add(block.getLocation());
            return;
        }
    }
}

