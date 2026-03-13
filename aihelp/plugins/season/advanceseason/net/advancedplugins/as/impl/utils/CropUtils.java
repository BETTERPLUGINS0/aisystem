/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.data.Ageable
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.utils;

import java.util.HashMap;
import java.util.Map;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.MathUtils;
import net.advancedplugins.as.impl.utils.VanillaEnchants;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.BinomialDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.ItemStack;

public class CropUtils {
    private static final Map<String, String> materialConversions = new HashMap<String, String>();

    public static Material convertToMaterial(String string) {
        if (MinecraftVersion.isNew()) {
            try {
                return Material.valueOf((String)string);
            } catch (IllegalArgumentException illegalArgumentException) {
                return Material.AIR;
            }
        }
        for (Map.Entry<String, String> entry : materialConversions.entrySet()) {
            if (!string.equals(entry.getKey())) continue;
            return Material.valueOf((String)string.replace(entry.getKey(), entry.getValue()));
        }
        return Material.matchMaterial((String)string);
    }

    public static int getDropAmount(Block block, Material material, ItemStack itemStack) {
        if (!CropUtils.isFullyGrown(block)) {
            return 1;
        }
        int n = itemStack.getEnchantmentLevel(VanillaEnchants.displayNameToEnchant("FORTUNE"));
        int n2 = new BinomialDistribution(Math.max(3, 3 + n), 0.57).sample();
        return switch (block.getType().name().replace("LEGACY_", "")) {
            case "CARROT", "CARROTS", "POTATO", "POTATOES" -> 2 + n2;
            case "BEETROOTS", "BEETROOT" -> {
                if (material.name().contains("SEEDS")) {
                    yield MathUtils.randomBetween(2, 4) + n2;
                }
                yield 1;
            }
            case "COCOA" -> 3;
            case "NETHER_WART" -> new UniformIntegerDistribution(2, 4 + n).sample();
            case "CROPS", "WHEAT" -> {
                if (material.name().contains("SEEDS")) {
                    yield MathUtils.randomBetween(0, 3) + n2;
                }
                yield 1;
            }
            default -> 1;
        };
    }

    public static boolean isCrop(Material material) {
        String string;
        if (!ASManager.isValid(material)) {
            return false;
        }
        switch (string = material.name()) {
            case "CROPS": 
            case "WHEAT": 
            case "CARROTS": 
            case "POTATOES": 
            case "SUGAR_CANE": 
            case "BEETROOTS": 
            case "CARROT": 
            case "POTATO": 
            case "BEETROOT": 
            case "COCOA": 
            case "NETHER_WART": 
            case "NETHER_WARTS": 
            case "TORCHFLOWER": 
            case "PITCHER_CROP": {
                return true;
            }
        }
        return false;
    }

    public static boolean isFullyGrown(Block block) {
        if (!ASManager.isValid(block) || !CropUtils.isCrop(block.getType())) {
            return false;
        }
        if (block.getType().name().equals("TORCHFLOWER")) {
            return true;
        }
        if (!(block.getBlockData() instanceof Ageable)) {
            return false;
        }
        Ageable ageable = (Ageable)block.getBlockData();
        return ageable.getAge() == ageable.getMaximumAge();
    }

    public static boolean isSeeded(Material material) {
        return CropUtils.getSeed(material) != material;
    }

    public static Material getSeed(Material material) {
        String string;
        if (!ASManager.isValid(material)) {
            return material;
        }
        switch (string = material.name()) {
            case "WHEAT": 
            case "CROPS": {
                if (MinecraftVersion.isNew()) {
                    return Material.WHEAT_SEEDS;
                }
                return Material.matchMaterial((String)"SEEDS");
            }
            case "BEETROOTS": 
            case "BEETROOT": {
                return Material.BEETROOT_SEEDS;
            }
        }
        return material;
    }

    public static boolean isWheat(Material material) {
        if (!ASManager.isValid(material)) {
            return false;
        }
        String string = material.name();
        return string.equals("WHEAT") || string.equals("CROPS");
    }

    public static boolean isBeetroot(Material material) {
        if (!ASManager.isValid(material)) {
            return false;
        }
        String string = material.name();
        return string.equals("BEETROOT") || string.equals("BEETROOTS");
    }

    public static int getCropAmount() {
        return MathUtils.randomBetween(2, 5);
    }

    public static int getSeedAmount() {
        return MathUtils.randomBetween(1, 3);
    }

    static {
        materialConversions.put("FARMLAND", "SOIL");
        materialConversions.put("WHEAT_SEEDS", "SEEDS");
        materialConversions.put("WHEAT", "CROPS");
        materialConversions.put("CARROTS", "CARROT");
        materialConversions.put("CARROT", "CARROT_ITEM");
        materialConversions.put("POTATOES", "POTATO");
        materialConversions.put("POTATO", "POTATO_ITEM");
        materialConversions.put("NETHER_WART", "NETHER_WARTS");
    }
}

