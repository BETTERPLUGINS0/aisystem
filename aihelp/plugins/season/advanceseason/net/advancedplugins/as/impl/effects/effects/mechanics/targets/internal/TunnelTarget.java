/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.util.Vector
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetArgument;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetType;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.TrenchTarget;
import net.advancedplugins.as.impl.effects.effects.settings.SettingValues;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class TunnelTarget
extends TargetType {
    public TunnelTarget() {
        super("Tunnel");
    }

    @Override
    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        HashMap<TargetArgument, String> hashMap = this.classifyTarget(string2);
        String[] stringArray = ((String)hashMap.get((Object)TargetArgument.RADIUS_CUSTOM)).split("x");
        int n = ASManager.parseInt(stringArray.length > 0 ? stringArray[0] : "0");
        int n2 = ASManager.parseInt(stringArray.length > 1 ? stringArray[1] : "0");
        int n3 = ASManager.parseInt(stringArray.length > 2 ? stringArray[2] : "0");
        if (n % 2 == 0 || n2 % 2 == 0 || n3 % 2 == 0) {
            executionTask.reportIssue(string, "Tunnel supports only odd numbers in order to work properly");
        }
        boolean bl = ASManager.parseBoolean(hashMap.getOrDefault((Object)TargetArgument.IGNORE_TOOL, "false"), false);
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
        Mode mode = Mode.valueOf((String)hashMap.get((Object)TargetArgument.MODE));
        Location location2 = executionTask.getBuilder().getMain().getLocation();
        BlockFace blockFace = TrenchTarget.getPlayerFacing(location2.getYaw(), location2.getPitch());
        Vector vector = TrenchTarget.getRightVector(blockFace);
        Vector vector2 = TrenchTarget.getUpVector(blockFace);
        Vector vector3 = blockFace.getDirection();
        int n4 = (n - 1) / 2;
        int n5 = (n2 - 1) / 2;
        for (int i = -n5; i <= n5; ++i) {
            for (int j = -n4; j <= n4; ++j) {
                for (int k = 0; k < n3; ++k) {
                    int n6 = 0;
                    if (mode == Mode.UP) {
                        n6 = k;
                    } else if (mode == Mode.DOWN) {
                        n6 = -k;
                    } else if (mode == Mode.STRAIGHT) {
                        n6 = 0;
                    } else if (mode == Mode.AUTO) {
                        if (location2.getPitch() < -20.0f) {
                            n6 = k;
                        } else if (location2.getPitch() > 20.0f) {
                            n6 = -k;
                        }
                    }
                    Vector vector4 = vector.clone().multiply(j).add(vector2.clone().multiply(i + n6)).add(vector3.clone().multiply(k));
                    Block block2 = location.getBlock().getRelative(vector4.getBlockX(), vector4.getBlockY(), vector4.getBlockZ());
                    TrenchTarget.processBlock(block2, block, location, bl, executionTask, arrayList);
                }
            }
        }
        if (SettingValues.isTunnelMoveItemsToInventory()) {
            executionTask.getBuilder().getDrops().getSettings().setAddToInventory(true);
        }
        return TargetResults.builder().targetLocations(arrayList).build();
    }

    private static enum Mode {
        UP,
        DOWN,
        AUTO,
        STRAIGHT;

    }
}

