/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetArgument;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetType;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.text.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class VeinTarget
extends TargetType {
    public VeinTarget() {
        super("Veinmine");
    }

    @Override
    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        int n = ASManager.parseInt(this.classifyTarget(string2).getOrDefault((Object)TargetArgument.DEPTH, "75"));
        if (executionTask.getAbility().getWhitelist().isEmpty()) {
            executionTask.getBuilder().getMain().sendMessage(Text.modify("\u00a74AdvancedEnchantments \u00a7cVeinminer requires whitelisted blocks. Without whitelist set, it cannot activate. Read more here: https://wiki.advancedplugins.net/abilities/targets"));
            ASManager.debug(Text.modify("\u00a74AdvancedEnchantments \u00a7cVeinminer requires whitelisted blocks. Without whitelist set, it cannot activate. Read more here: https://wiki.advancedplugins.net/abilities/targets"));
            return this.noTarget();
        }
        if (!executionTask.getAbility().getWhitelist().contains(executionTask.getBuilder().getBlock().getType().name())) {
            return this.noTarget();
        }
        List<Object> list = new ArrayList<Location>();
        Block block = executionTask.getBuilder().getBlock();
        if (!block.hasMetadata("non-natural")) {
            list.addAll(this.findBlocks(block, n));
            list.remove(executionTask.getBuilder().getBlock().getLocation());
            list = list.stream().filter(location -> !location.getBlock().hasMetadata("non-natural")).collect(Collectors.toList());
        }
        return TargetResults.builder().targetLocations(list).build();
    }

    private List<Location> findBlocks(Block block, int n) {
        HashSet<Location> hashSet = new HashSet<Location>();
        HashSet<Location> hashSet2 = new HashSet<Location>();
        this.findBlock(block, block.getType(), hashSet, n, hashSet2);
        hashSet.remove(block.getLocation());
        return new ArrayList<Location>(hashSet);
    }

    private void findBlock(Block block, Material material, HashSet<Location> hashSet, int n, HashSet<Location> hashSet2) {
        if (hashSet.contains(block.getLocation()) || hashSet.size() >= n || block.getType() != material) {
            return;
        }
        if (block.getType().equals((Object)material)) {
            hashSet.add(block.getLocation());
        }
        for (BlockFace blockFace : BlockFace.values()) {
            Block block2;
            if (blockFace.equals((Object)BlockFace.SELF) || hashSet2.contains((block2 = block.getRelative(blockFace)).getLocation())) continue;
            hashSet2.add(block2.getLocation());
            this.findBlock(block2, material, hashSet, n, hashSet2);
        }
    }
}

