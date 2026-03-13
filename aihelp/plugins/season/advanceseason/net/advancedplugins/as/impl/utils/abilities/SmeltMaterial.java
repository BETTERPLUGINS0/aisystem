/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.utils.abilities;

import java.util.concurrent.ThreadLocalRandom;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.items.ItemBuilder;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SmeltMaterial {
    public static ItemStack material(ItemStack itemStack) {
        if (!ASManager.isValid(itemStack)) {
            return null;
        }
        String string = itemStack.getType().name();
        if (MinecraftVersion.isNew() && itemStack.getType().isLegacy()) {
            string = ASManager.matchMaterial(string, 1, 0, true, false).getType().name();
        }
        switch (string) {
            case "COBBLED_DEEPSLATE": {
                return new ItemStack(Material.getMaterial((String)"DEEPSLATE"));
            }
            case "COBBLESTONE": {
                return new ItemStack(Material.STONE);
            }
            case "DEEPSLATE_COAL_ORE": 
            case "COAL_ORE": {
                return new ItemStack(Material.COAL);
            }
            case "DEEPSLATE_IRON_ORE": 
            case "RAW_IRON": 
            case "IRON_ORE": {
                return new ItemStack(Material.IRON_INGOT);
            }
            case "DEEPSLATE_COPPER_ORE": 
            case "RAW_COPPER": 
            case "COPPER_ORE": {
                return new ItemStack(Material.COPPER_INGOT);
            }
            case "DEEPSLATE_GOLD_ORE": 
            case "NETHER_GOLD_ORE": 
            case "RAW_GOLD": 
            case "GOLD_ORE": {
                return new ItemStack(Material.GOLD_INGOT);
            }
            case "DEEPSLATE_LAPIS_ORE": 
            case "LAPIS_ORE": {
                return new ItemStack(Material.LAPIS_LAZULI);
            }
            case "DEEPSLATE_REDSTONE_ORE": 
            case "REDSTONE_ORE": {
                return new ItemStack(Material.REDSTONE, ThreadLocalRandom.current().nextInt(4) + 1);
            }
            case "DEEPSLATE_DIAMOND_ORE": 
            case "DIAMOND_ORE": {
                return new ItemStack(Material.DIAMOND);
            }
            case "DEEPSLATE_EMERALD_ORE": 
            case "EMERALD_ORE": {
                return new ItemStack(Material.EMERALD);
            }
            case "QUARTZ_ORE": {
                return new ItemStack(Material.QUARTZ);
            }
            case "ANCIENT_DEBRIS": {
                return new ItemStack(Material.NETHERITE_SCRAP);
            }
            case "RED_SAND": 
            case "SAND": {
                return new ItemStack(Material.GLASS);
            }
            case "SANDSTONE": {
                return new ItemStack(Material.getMaterial((String)"SMOOTH_SANDSTONE"));
            }
            case "BASALT": {
                return new ItemStack(Material.getMaterial((String)"SMOOTH_BASALT"));
            }
            case "NETHERRACK": {
                return new ItemStack(Material.matchMaterial((String)"NETHER_BRICK"));
            }
            case "CLAY": 
            case "CLAY_ITEM": 
            case "CLAY_BALL": {
                return new ItemBuilder(Material.matchMaterial((String)"BRICK")).setAmount(itemStack.getAmount()).toItemStack();
            }
            case "WET_SPONGE": {
                return new ItemStack(Material.getMaterial((String)"SPONGE"));
            }
            case "RED_SANDSTONE": {
                return new ItemStack(Material.getMaterial((String)"SMOOTH_RED_SANDSTONE"));
            }
        }
        return new ItemStack(itemStack);
    }

    public static ItemStack material(Material material) {
        if (material == null) {
            return new ItemStack(Material.AIR);
        }
        return SmeltMaterial.material(new ItemStack(material));
    }
}

