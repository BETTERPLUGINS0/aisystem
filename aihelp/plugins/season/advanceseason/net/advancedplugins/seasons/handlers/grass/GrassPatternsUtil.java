/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 */
package net.advancedplugins.seasons.handlers.grass;

import com.google.common.collect.ImmutableList;
import java.util.Random;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.seasons.handlers.grass.GrassPattern;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class GrassPatternsUtil {
    private static final Random random = new Random();
    private static ImmutableList<GrassPattern> grassPatterns = null;

    private static void init() {
        ImmutableList.Builder builder = ImmutableList.builder();
        builder.add(new GrassPattern(ImmutableList.of(Material.FERN), 10, 1, 2, 100));
        builder.add(new GrassPattern(ImmutableList.of(Material.DANDELION), 55, 2, 20, 40));
        builder.add(new GrassPattern(ImmutableList.of(Material.POPPY), 64, 2, 10, 40));
        builder.add(new GrassPattern(ImmutableList.of(Material.POPPY), 45, 3, 10, 40));
        builder.add(new GrassPattern(ImmutableList.of(Material.DANDELION, Material.AZURE_BLUET), 90, 5, 5, 20));
        builder.add(new GrassPattern(ImmutableList.of(Material.DANDELION, Material.AZURE_BLUET), 222, 5, 10, 20));
        grassPatterns = builder.build();
    }

    public static void addNewGrassPattern(GrassPattern grassPattern) {
        if (grassPatterns == null) {
            GrassPatternsUtil.init();
        }
        grassPatterns = ((ImmutableList.Builder)((ImmutableList.Builder)ImmutableList.builder().addAll(grassPatterns)).add(grassPattern)).build();
    }

    public static Material process(Block block) {
        if (grassPatterns == null) {
            GrassPatternsUtil.init();
        }
        int n = block.getX();
        int n2 = block.getZ();
        for (GrassPattern grassPattern : grassPatterns) {
            int n3 = grassPattern.getPatchInterval();
            int n4 = grassPattern.getPatchRadius();
            int n5 = n / n3 * n3;
            int n6 = n2 / n3 * n3;
            int n7 = n5 + random.nextInt(21) - grassPattern.getOffset();
            int n8 = n6 + random.nextInt(21) - grassPattern.getOffset();
            if (Math.abs(n - n7) <= n4 && Math.abs(n2 - n8) <= n4 && ASManager.doChancesPass(grassPattern.getChance())) {
                return (Material)grassPattern.getMaterials().get(random.nextInt(grassPattern.getMaterials().size()));
            }
            if (!ASManager.doChancesPass(15 / grassPatterns.size())) continue;
            return Material.GRASS;
        }
        return Material.AIR;
    }
}

