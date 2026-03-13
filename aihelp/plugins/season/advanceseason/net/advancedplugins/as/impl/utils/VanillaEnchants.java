/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.enchantments.Enchantment
 */
package net.advancedplugins.as.impl.utils;

import java.util.Locale;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.enchantments.Enchantment;

public class VanillaEnchants {
    public static Enchantment displayNameToEnchant(String string) {
        return VanillaEnchants.displayNameToEnchant(string, true);
    }

    public static Enchantment displayNameToEnchant(String string, boolean bl) {
        Enchantment enchantment = VanillaEnchants.getEnchant(string = string.toLowerCase(Locale.ROOT));
        if (enchantment != null) {
            return enchantment;
        }
        enchantment = VanillaEnchants.getEnchant(string.replaceAll(" ", "_"));
        if (enchantment != null) {
            return enchantment;
        }
        enchantment = VanillaEnchants.getEnchant(string.replaceAll("_", "").replaceAll(" ", ""));
        if (enchantment != null) {
            return enchantment;
        }
        if (bl) {
            ASManager.getInstance().getLogger().warning("Invalid vanilla enchantment: " + string + ". Enchantment names can be found here: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html");
        }
        return null;
    }

    private static Enchantment getEnchant(String string) {
        switch (string.toLowerCase(Locale.ROOT)) {
            case "protection": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"PROTECTION") : Enchantment.getByName((String)"PROTECTION_ENVIRONMENTAL");
            }
            case "fire_protection": 
            case "fireprotection": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"FIRE_PROTECTION") : Enchantment.getByName((String)"PROTECTION_FIRE");
            }
            case "feather_falling": 
            case "featherfalling": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"FEATHER_FALLING") : Enchantment.getByName((String)"PROTECTION_FALL");
            }
            case "blast_protection": 
            case "blastprotection": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"BLAST_PROTECTION") : Enchantment.getByName((String)"PROTECTION_EXPLOSIONS");
            }
            case "projectile_protection": 
            case "projectileprotection": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"PROJECTILE_PROTECTION") : Enchantment.getByName((String)"PROTECTION_PROJECTILE");
            }
            case "respiration": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"RESPIRATION") : Enchantment.getByName((String)"OXYGEN");
            }
            case "aqua_affinity": 
            case "aquaaffinity": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"AQUA_AFFINITY") : Enchantment.getByName((String)"WATER_WORKER");
            }
            case "throns": 
            case "thorns": {
                return Enchantment.THORNS;
            }
            case "depth_strider": 
            case "depthstrider": {
                return Enchantment.DEPTH_STRIDER;
            }
            case "frost_walker": 
            case "frostwalker": {
                return MinecraftVersion.getVersionNumber() >= 190 ? Enchantment.FROST_WALKER : null;
            }
            case "curse_of_binding": 
            case "curseofbinding": {
                return MinecraftVersion.getVersionNumber() >= 1110 ? Enchantment.BINDING_CURSE : null;
            }
            case "soul_speed": 
            case "soulspeed": {
                return MinecraftVersion.getVersionNumber() >= 1160 ? Enchantment.SOUL_SPEED : null;
            }
            case "sharpness": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"SHARPNESS") : Enchantment.getByName((String)"DAMAGE_ALL");
            }
            case "smite": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"SMITE") : Enchantment.getByName((String)"DAMAGE_UNDEAD");
            }
            case "bane_of_arthropods": 
            case "baneofarthropods": 
            case "bane": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"BANE_OF_ARTHROPODS") : Enchantment.getByName((String)"DAMAGE_ARTHROPODS");
            }
            case "knockback": {
                return Enchantment.KNOCKBACK;
            }
            case "fire_aspect": 
            case "fireaspect": {
                return Enchantment.FIRE_ASPECT;
            }
            case "looting": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"LOOTING") : Enchantment.getByName((String)"LOOT_BONUS_MOBS");
            }
            case "sweeping_edge": 
            case "sweepingedge": {
                return MinecraftVersion.getVersionNumber() >= 1111 ? Enchantment.getByName((String)"SWEEPING_EDGE") : null;
            }
            case "efficiency": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"EFFICIENCY") : Enchantment.getByName((String)"DIG_SPEED");
            }
            case "silk_touch": 
            case "silktouch": {
                return Enchantment.SILK_TOUCH;
            }
            case "unbreaking": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"UNBREAKING") : Enchantment.getByName((String)"DURABILITY");
            }
            case "fortune": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"FORTUNE") : Enchantment.getByName((String)"LOOT_BONUS_BLOCKS");
            }
            case "power": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"POWER") : Enchantment.getByName((String)"ARROW_DAMAGE");
            }
            case "punch": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"PUNCH") : Enchantment.getByName((String)"ARROW_KNOCKBACK");
            }
            case "flame": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"FLAME") : Enchantment.getByName((String)"ARROW_FIRE");
            }
            case "infinity": {
                return MinecraftVersion.getVersionNumber() >= 1205 ? Enchantment.getByName((String)"INFINITY") : Enchantment.getByName((String)"ARROW_INFINITE");
            }
            case "luck_of_the_sea": 
            case "luckofthesea": {
                return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? Enchantment.getByName((String)"LUCK_OF_THE_SEA") : Enchantment.getByName((String)"LUCK");
            }
            case "lure": {
                return Enchantment.LURE;
            }
            case "curse_of_vanishing": {
                return MinecraftVersion.getVersionNumber() >= 1140 ? Enchantment.VANISHING_CURSE : null;
            }
            case "loyalty": {
                return MinecraftVersion.getVersionNumber() >= 1130 ? Enchantment.LOYALTY : null;
            }
            case "impaling": {
                return MinecraftVersion.getVersionNumber() >= 1130 ? Enchantment.IMPALING : null;
            }
            case "riptide": {
                return MinecraftVersion.getVersionNumber() >= 1130 ? Enchantment.RIPTIDE : null;
            }
            case "channeling": {
                return MinecraftVersion.getVersionNumber() >= 1130 ? Enchantment.CHANNELING : null;
            }
            case "multishot": {
                return MinecraftVersion.getVersionNumber() >= 1140 ? Enchantment.MULTISHOT : null;
            }
            case "quick_charge": 
            case "quickcharge": {
                return MinecraftVersion.getVersionNumber() >= 1140 ? Enchantment.QUICK_CHARGE : null;
            }
            case "piercing": {
                return MinecraftVersion.getVersionNumber() >= 1140 ? Enchantment.PIERCING : null;
            }
            case "mending": {
                return MinecraftVersion.getVersionNumber() >= 190 ? Enchantment.MENDING : null;
            }
            case "vanishing_curse": 
            case "vanishingcurse": {
                return Enchantment.VANISHING_CURSE;
            }
        }
        try {
            return Enchantment.getByName((String)string.toUpperCase(Locale.ROOT));
        } catch (Exception exception) {
            try {
                return Enchantment.getByName((String)string.toUpperCase(Locale.ROOT).replace(" ", "").replace("_", ""));
            } catch (Exception exception2) {
                return null;
            }
        }
    }

    public static String enchantToDisplayName(Enchantment enchantment) {
        switch (enchantment.getName()) {
            case "PROTECTION_ENVIRONMENTAL": {
                return "protection";
            }
            case "PROTECTION_FIRE": {
                return "fire_protection";
            }
            case "PROTECTION_FALL": {
                return "feather_falling";
            }
            case "PROTECTION_EXPLOSIONS": {
                return "blast_protection";
            }
            case "PROTECTION_PROJECTILE": {
                return "projectile_protection";
            }
            case "OXYGEN": {
                return "respiration";
            }
            case "WATER_WORKER": {
                return "aqua_affinity";
            }
            case "THORNS": {
                return "thorns";
            }
            case "DEPTH_STRIDER": {
                return "depth_strider";
            }
            case "FROST_WALKER": {
                return "frost_walker";
            }
            case "BINDING_CURSE": {
                return "curse_of_binding";
            }
            case "SOUL_SPEED": {
                return "soul_speed";
            }
            case "DAMAGE_ALL": {
                return "sharpness";
            }
            case "DAMAGE_UNDEAD": {
                return "smite";
            }
            case "DAMAGE_ARTHROPODS": {
                return "bane_of_arthropods";
            }
            case "KNOCKBACK": {
                return "knockback";
            }
            case "FIRE_ASPECT": {
                return "fire_aspect";
            }
            case "LOOT_BONUS_MOBS": {
                return "looting";
            }
            case "SWEEPING_EDGE": {
                return "sweeping_edge";
            }
            case "DIG_SPEED": {
                return "efficiency";
            }
            case "SILK_TOUCH": {
                return "silk_touch";
            }
            case "DURABILITY": {
                return "unbreaking";
            }
            case "LOOT_BONUS_BLOCKS": {
                return "fortune";
            }
            case "ARROW_DAMAGE": {
                return "power";
            }
            case "ARROW_KNOCKBACK": {
                return "punch";
            }
            case "ARROW_FIRE": {
                return "flame";
            }
            case "ARROW_INFINITE": {
                return "infinity";
            }
            case "LUCK": {
                return "luck_of_the_sea";
            }
            case "LURE": {
                return "lure";
            }
            case "LOYALTY": {
                return "loyalty";
            }
            case "IMPALING": {
                return "impaling";
            }
            case "RIPTIDE": {
                return "riptide";
            }
            case "CHANNELING": {
                return "channeling";
            }
            case "MULTISHOT": {
                return "multishot";
            }
            case "QUICK_CHARGE": {
                return "quick_charge";
            }
            case "PIERCING": {
                return "piercing";
            }
            case "MENDING": {
                return "mending";
            }
            case "VANISHING_CURSE": {
                return "vanishing_curse";
            }
        }
        return enchantment.getName();
    }
}

