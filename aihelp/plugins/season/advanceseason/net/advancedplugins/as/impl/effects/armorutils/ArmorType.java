/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.armorutils;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public enum ArmorType {
    HELMET(5, EquipmentSlot.HEAD),
    CHESTPLATE(6, EquipmentSlot.CHEST),
    LEGGINGS(7, EquipmentSlot.LEGS),
    BOOTS(8, EquipmentSlot.FEET);

    private final int slot;
    private final EquipmentSlot slotType;
    private static List<String> helmets;
    private static List<String> chestPlates;
    private static List<String> leggings;
    private static List<String> boots;

    private ArmorType(int n2, EquipmentSlot equipmentSlot) {
        this.slot = n2;
        this.slotType = equipmentSlot;
    }

    public static void setArmorItem(LivingEntity livingEntity, ItemStack itemStack) {
        ArmorType armorType = ArmorType.getClosest(livingEntity, itemStack);
        if (armorType == null) {
            return;
        }
        ArmorType.setArmorItem(livingEntity, itemStack, armorType);
    }

    public static void setArmorItem(LivingEntity livingEntity, ItemStack itemStack, ArmorType armorType) {
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        switch (armorType.ordinal()) {
            case 0: {
                entityEquipment.setHelmet(itemStack);
                break;
            }
            case 1: {
                entityEquipment.setChestplate(itemStack);
                break;
            }
            case 2: {
                entityEquipment.setLeggings(itemStack);
                break;
            }
            case 3: {
                entityEquipment.setBoots(itemStack);
            }
        }
    }

    public static ArmorType matchType(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        return ArmorType.matchType(itemStack.getType().name());
    }

    public static ArmorType matchType(String string) {
        if (string == null) {
            return null;
        }
        if (string.endsWith("HEAD")) {
            return HELMET;
        }
        for (String string2 : ArmorType.getHelmets()) {
            if (!string2.equalsIgnoreCase(string)) continue;
            return HELMET;
        }
        for (String string2 : ArmorType.getChestPlates()) {
            if (!string2.equalsIgnoreCase(string)) continue;
            return CHESTPLATE;
        }
        for (String string2 : ArmorType.getLeggings()) {
            if (!string2.equalsIgnoreCase(string)) continue;
            return LEGGINGS;
        }
        for (String string2 : ArmorType.getBoots()) {
            if (!string2.equalsIgnoreCase(string)) continue;
            return BOOTS;
        }
        return null;
    }

    public static ArmorType getClosest(LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        for (Map.Entry<ArmorType, ItemStack> entry : ArmorType.getArmorContents(livingEntity).entrySet()) {
            if (!itemStack.equals((Object)entry.getValue())) continue;
            return entry.getKey();
        }
        return ArmorType.matchType(itemStack);
    }

    public static Map<ArmorType, ItemStack> getArmorContents(LivingEntity livingEntity) {
        EnumMap<ArmorType, ItemStack> enumMap = new EnumMap<ArmorType, ItemStack>(ArmorType.class);
        enumMap.put(HELMET, HELMET.get(livingEntity));
        enumMap.put(CHESTPLATE, CHESTPLATE.get(livingEntity));
        enumMap.put(LEGGINGS, LEGGINGS.get(livingEntity));
        enumMap.put(BOOTS, BOOTS.get(livingEntity));
        return enumMap;
    }

    public static ArmorType fromSlot(int n) {
        return Arrays.stream(ArmorType.values()).filter(armorType -> armorType.slot == n).findFirst().orElse(null);
    }

    public ItemStack get(LivingEntity livingEntity) {
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        ItemStack itemStack = switch (this.ordinal()) {
            case 0 -> entityEquipment.getHelmet();
            case 1 -> entityEquipment.getChestplate();
            case 2 -> entityEquipment.getLeggings();
            case 3 -> entityEquipment.getBoots();
            default -> throw new IllegalArgumentException("Unknown ArmorType: " + this.name());
        };
        if (itemStack == null) {
            itemStack = new ItemStack(Material.AIR);
        }
        return itemStack;
    }

    public RollItemType getRollItemType() {
        return RollItemType.valueOf(this.name());
    }

    public EquipmentSlot getEquipmentSlot() {
        return this.slotType;
    }

    public int getSlot() {
        return this.slot;
    }

    public static void setHelmets(List<String> list) {
        helmets = list;
    }

    public static List<String> getHelmets() {
        return helmets;
    }

    public static void setChestPlates(List<String> list) {
        chestPlates = list;
    }

    public static List<String> getChestPlates() {
        return chestPlates;
    }

    public static void setLeggings(List<String> list) {
        leggings = list;
    }

    public static List<String> getLeggings() {
        return leggings;
    }

    public static void setBoots(List<String> list) {
        boots = list;
    }

    public static List<String> getBoots() {
        return boots;
    }

    static {
        helmets = Arrays.asList("HEAD", "SKULL_ITEM", "SKULL", "WITHER_SKELETON_SKULL", "SKELETON_SKULL", "PLAYER_HEAD", "CREEPER_HEAD", "DRAGON_HEAD", "ZOMBIE_HEAD", "NETHERITE_HELMET", "DIAMOND_HELMET", "GOLD_HELMET", "IRON_HELMET", "CHAINMAIL_HELMET", "LEATHER_HELMET", "TURTLE_SHELL", "CARVED_PUMPKIN", "TURTLE_HELMET");
        chestPlates = Arrays.asList("NETHERITE_CHESTPLATE", "DIAMOND_CHESTPLATE", "GOLD_CHESTPLATE", "IRON_CHESTPLATE", "CHAINMAIL_CHESTPLATE", "LEATHER_CHESTPLATE", "ELYTRA");
        leggings = Arrays.asList("LEGS", "DIAMOND_LEGGINGS", "NETHERITE_LEGGINGS", "GOLD_LEGGINGS", "IRON_LEGGINGS", "CHAINMAIL_LEGGINGS", "LEATHER_LEGGINGS");
        boots = Arrays.asList("FEET", "DIAMOND_BOOTS", "NETHERITE_BOOTS", "GOLD_BOOTS", "IRON_BOOTS", "CHAINMAIL_BOOTS", "LEATHER_BOOTS");
    }
}

