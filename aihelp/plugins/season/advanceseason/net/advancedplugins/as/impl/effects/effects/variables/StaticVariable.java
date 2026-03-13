/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.ae.features.souls.SoulsAPI
 *  net.advancedplugins.items.api.AdvancedItemsAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.Keyed
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Enemy
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Mob
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffectType
 */
package net.advancedplugins.as.impl.effects.effects.variables;

import java.util.Arrays;
import java.util.stream.StreamSupport;
import net.advancedplugins.ae.features.souls.SoulsAPI;
import net.advancedplugins.as.impl.effects.armorutils.ArmorType;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecution;
import net.advancedplugins.as.impl.effects.effects.utils.Combo;
import net.advancedplugins.as.impl.effects.effects.variables.Variables;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.AreaUtils;
import net.advancedplugins.as.impl.utils.ColorUtils;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.ViaVersionHook;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.items.api.AdvancedItemsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public enum StaticVariable {
    ADVANCEDITEMS_TYPE("advanceditems type", true),
    ADVANCEDITEMS_HELMET_TYPE("advanceditems helmet type", true),
    ADVANCEDITEMS_CHESTPLATE_TYPE("advanceditems chestplate type", true),
    ADVANCEDITEMS_LEGGINGS_TYPE("advanceditems leggings type", true),
    ADVANCEDITEMS_BOOTS_TYPE("advanceditems boots type", true),
    ADVANCEDENCHANTMENTS_SOULS("souls on item", true),
    CAN_BREAK("can break", true),
    FACTION_LAND("faction land", true),
    FOOD("food", true),
    IS_BLEEDING("is bleeding", true),
    HELMET("helmet", true),
    CHESTPLATE("chestplate", true),
    LEGGINGS("leggings", true),
    BOOTS("boots", true),
    IS_BLOCKING("is blocking", true),
    IS_FLYING("is flying", false),
    IS_SNEAKING("is sneaking", true),
    PERMISSIONS("permissions", true),
    IS_OP("is op", true),
    CLIENT_VERSION("client version", true),
    TRIGGER_TYPE("trigger type", false),
    IS_RIDING("is riding", false),
    RIDING_ENTITY("passengers", false),
    BLOCK_BELOW("block below", false),
    COMBO("combo", false),
    CUSTOM_NAME("custom name", false),
    HEALTH("health", false),
    HEALTH_PERCENTAGE("health percentage", false),
    IS_CRITICAL("is critical", false),
    IS_GLIDING("is gliding", false),
    IS_CROUCHING("is crouching", false),
    IS_SPRINTING("is sprinting", false),
    IS_HOLDING("is holding", false),
    IS_HOSTILE("is hostile", false),
    IS_DAMAGEABLE("is damageable", false),
    IS_ON_FIRE("is on fire", false),
    IS_NIGHT("is night", false),
    IS_REMOVAL("is removal", false),
    IS_UNDER_WATER("is under water", false),
    ITEM_SLOT("item slot", false),
    MAX_HEALTH("max health", false),
    MOB_TYPE("mob type", false),
    IS_PLAYER("is player", false),
    ITEM_TYPE("item type", false),
    NAME("name", false),
    NEARBY_MOBS("nearby mobs", false),
    OFFHAND_ITEM("offhand item", false),
    PLAYERS("players", false),
    TIME("time", false),
    WORLD("world", false),
    X("x", false),
    X_DOUBLE("x double", false),
    Y("y", false),
    WEATHER("weather", false),
    Y_DOUBLE("y double", false),
    Z("z", false),
    Z_DOUBLE("z double", false),
    YAW("yaw", false),
    PITCH("pitch", false),
    BLOCK_TAGS("block tags", false),
    BLOCK_NATURAL("block natural", false),
    BLOCK_IS_INTERACTABLE("block is interactable", false),
    INVENTORY_FULL("inventory full", true);

    private final String name;
    private final boolean player;

    private StaticVariable(String string2, boolean bl) {
        this.name = string2;
        this.player = bl;
    }

    public boolean needsPlayer() {
        return this.player;
    }

    public static String getValue(Variables.VariableData variableData, StaticVariable staticVariable, String string2, ActionExecution actionExecution) {
        String[] stringArray;
        LivingEntity livingEntity = StaticVariable.getTarget(variableData, staticVariable);
        if (staticVariable.needsPlayer() && livingEntity instanceof Player) {
            stringArray = (String[])livingEntity;
            switch (staticVariable.ordinal()) {
                case 5: {
                    if (!Bukkit.getPluginManager().isPluginEnabled("AdvancedEnchantments")) break;
                    return "" + SoulsAPI.getSoulsOnItem((ItemStack)actionExecution.getBuilder().getItem());
                }
                case 0: 
                case 1: 
                case 2: 
                case 3: 
                case 4: {
                    ItemStack itemStack;
                    if (!Bukkit.getPluginManager().isPluginEnabled("AdvancedItems")) break;
                    ArmorType armorType = null;
                    if (staticVariable.equals((Object)ADVANCEDITEMS_BOOTS_TYPE)) {
                        armorType = ArmorType.BOOTS;
                    } else if (staticVariable.equals((Object)ADVANCEDITEMS_CHESTPLATE_TYPE)) {
                        armorType = ArmorType.CHESTPLATE;
                    } else if (staticVariable.equals((Object)ADVANCEDITEMS_HELMET_TYPE)) {
                        armorType = ArmorType.HELMET;
                    } else if (staticVariable.equals((Object)ADVANCEDITEMS_LEGGINGS_TYPE)) {
                        armorType = ArmorType.LEGGINGS;
                    }
                    ItemStack itemStack2 = itemStack = armorType == null ? actionExecution.getBuilder().getItem() : ((Player)livingEntity).getInventory().getItem(armorType.getEquipmentSlot());
                    if (itemStack == null) {
                        return "null";
                    }
                    return AdvancedItemsAPI.getCustomItemName((ItemStack)itemStack);
                }
                case 6: {
                    if (actionExecution.getBuilder().getBlock() == null) {
                        return "false";
                    }
                    return "" + EffectsHandler.getProtection().canBreak(actionExecution.getBuilder().getBlock().getLocation(), (Player)stringArray);
                }
                case 8: {
                    return "" + stringArray.getFoodLevel();
                }
                case 14: {
                    return "" + stringArray.isBlocking();
                }
                case 15: {
                    return "" + stringArray.isFlying();
                }
                case 17: {
                    return Arrays.toString(stringArray.getEffectivePermissions().stream().map(permissionAttachmentInfo -> permissionAttachmentInfo.getPermission()).toArray()).replaceAll("[|><=}{\\]\\[%:&\\-+!]", "");
                }
                case 18: {
                    return "" + stringArray.isOp();
                }
                case 19: {
                    return HooksHandler.getHook(HookPlugin.VIAVERSION) != null ? ((ViaVersionHook)HooksHandler.getHook(HookPlugin.VIAVERSION)).getPlayerVersion((Player)stringArray) : "0";
                }
                case 16: {
                    return "" + stringArray.isSneaking();
                }
                case 10: {
                    ItemStack itemStack = stringArray.getInventory().getHelmet();
                    return itemStack != null ? itemStack.getType().name() : "AIR";
                }
                case 11: {
                    ItemStack itemStack = stringArray.getInventory().getChestplate();
                    return itemStack != null ? itemStack.getType().name() : "AIR";
                }
                case 12: {
                    ItemStack itemStack = stringArray.getInventory().getLeggings();
                    return itemStack != null ? itemStack.getType().name() : "AIR";
                }
                case 13: {
                    ItemStack itemStack = stringArray.getInventory().getBoots();
                    return itemStack != null ? itemStack.getType().name() : "AIR";
                }
            }
        }
        switch (staticVariable.ordinal()) {
            case 20: {
                return actionExecution.getBuilder().getType();
            }
            case 43: {
                return actionExecution.getBuilder().getItem().getType().name();
            }
            case 21: {
                return "" + livingEntity.isInsideVehicle();
            }
            case 22: {
                stringArray = (String[])livingEntity.getPassengers().stream().map(entity -> entity.getType().name()).toArray(String[]::new);
                return "[" + ASManager.join(stringArray, ", ") + "]";
            }
            case 23: {
                return livingEntity.getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType().name();
            }
            case 24: {
                return "" + Combo.getCombos(livingEntity.getUniqueId());
            }
            case 25: {
                return livingEntity.isCustomNameVisible() ? ColorUtils.stripColor(livingEntity.getCustomName()) : "N/A";
            }
            case 26: {
                return "" + livingEntity.getHealth();
            }
            case 27: {
                return "" + (int)(livingEntity.getHealth() / livingEntity.getMaxHealth() * 100.0);
            }
            case 28: {
                Material material = livingEntity.getLocation().getBlock().getType();
                boolean bl = livingEntity.getFallDistance() > 0.0f && !livingEntity.isOnGround() && !livingEntity.isInsideVehicle() && !livingEntity.hasPotionEffect(PotionEffectType.BLINDNESS) && !livingEntity.getLocation().getBlock().isLiquid() && material != Material.LADDER && material != Material.VINE && (MinecraftVersion.getVersionNumber() < 1160 || material != Material.WEEPING_VINES_PLANT && material != Material.TWISTING_VINES_PLANT);
                return "" + bl;
            }
            case 29: {
                return MinecraftVersion.getVersionNumber() >= 190 ? "" + livingEntity.isGliding() : "false";
            }
            case 30: {
                return "" + livingEntity.isSneaking();
            }
            case 31: {
                return "" + ((Player)livingEntity).isSprinting();
            }
            case 32: {
                return livingEntity.getEquipment().getItemInHand().getType().name();
            }
            case 33: {
                Mob mob;
                return "" + (livingEntity instanceof Enemy || livingEntity instanceof Mob && (mob = (Mob)livingEntity).getTarget() instanceof Player);
            }
            case 34: {
                return "" + AreaUtils.isDamageable((Entity)livingEntity, (Entity)livingEntity);
            }
            case 35: {
                return "" + (livingEntity.getFireTicks() > 0);
            }
            case 37: {
                return "" + actionExecution.getBuilder().isRemoved();
            }
            case 36: {
                long l = livingEntity.getWorld().getTime();
                return "" + (l >= 12300L && l <= 23850L);
            }
            case 38: {
                return "" + livingEntity.getEyeLocation().getBlock().getType().name().contains("WATER");
            }
            case 39: {
                return actionExecution.getBuilder().getItemType().name();
            }
            case 40: {
                return "" + livingEntity.getMaxHealth();
            }
            case 41: {
                return livingEntity.getType().name();
            }
            case 42: {
                return String.valueOf(livingEntity instanceof Player);
            }
            case 44: {
                return livingEntity.getName();
            }
            case 45: {
                return "" + AreaUtils.getEntitiesInRadius(10, (Entity)livingEntity, AreaUtils.RadiusTarget.MOBS).size();
            }
            case 46: {
                return MinecraftVersion.getVersionNumber() >= 190 ? livingEntity.getEquipment().getItemInOffHand().getType().name() : "AIR";
            }
            case 47: {
                return "" + AreaUtils.getEntitiesInRadius(10, (Entity)livingEntity, AreaUtils.RadiusTarget.PLAYERS).size();
            }
            case 48: {
                return "" + livingEntity.getWorld().getTime();
            }
            case 49: {
                return livingEntity.getWorld().getName();
            }
            case 50: {
                return "" + livingEntity.getLocation().getBlockX();
            }
            case 52: {
                return "" + livingEntity.getLocation().getBlockY();
            }
            case 53: {
                return livingEntity.getLocation().getWorld().isClearWeather() ? "CLEAR" : "RAIN";
            }
            case 55: {
                return "" + livingEntity.getLocation().getBlockZ();
            }
            case 51: {
                return "" + livingEntity.getLocation().getX();
            }
            case 54: {
                return "" + livingEntity.getLocation().getY();
            }
            case 56: {
                return "" + livingEntity.getLocation().getZ();
            }
            case 57: {
                return String.valueOf(livingEntity.getLocation().getYaw());
            }
            case 58: {
                return String.valueOf(livingEntity.getLocation().getPitch());
            }
            case 59: {
                Block block = actionExecution.getBuilder().getBlock();
                if (block == null) {
                    return "[]";
                }
                String[] stringArray2 = (String[])StreamSupport.stream(Bukkit.getTags((String)"blocks", actionExecution.getBuilder().getBlock().getType().getClass()).spliterator(), false).map(tag -> tag).filter(tag -> tag.isTagged((Keyed)block.getType())).map(tag -> String.valueOf(tag.getKey())).map(string -> string.replace(":", "@")).toArray(String[]::new);
                return "[" + ASManager.join(stringArray2, ", ") + "]";
            }
            case 60: {
                Block block = actionExecution.getBuilder().getBlock();
                return block == null ? "none" : String.valueOf(!block.hasMetadata("non-natural"));
            }
            case 61: {
                Block block = actionExecution.getBuilder().getBlock();
                return block == null ? "false" : "" + block.getType().isInteractable();
            }
            case 62: {
                if (livingEntity instanceof Player) {
                    Player player = (Player)livingEntity;
                    return "" + (player.getInventory().firstEmpty() == -1);
                }
                return "false";
            }
        }
        return "";
    }

    public static LivingEntity getTarget(Variables.VariableData variableData, StaticVariable staticVariable) {
        return variableData.condition.contains("victim " + staticVariable.getName()) ? variableData.secondary : variableData.primary;
    }

    public String getName() {
        return this.name;
    }
}

