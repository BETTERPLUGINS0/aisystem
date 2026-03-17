/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Registry
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.attribute.AttributeModifier
 *  org.bukkit.attribute.AttributeModifier$Operation
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.EquipmentSlotGroup
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Unmodifiable
 */
package me.zombie_striker.qav.util.xseries;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import me.zombie_striker.qav.util.xseries.base.XModule;
import me.zombie_striker.qav.util.xseries.base.XRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class XAttribute
extends XModule<XAttribute, Attribute> {
    public static final XRegistry<XAttribute, Attribute> REGISTRY;
    public static final XAttribute MAX_HEALTH;
    public static final XAttribute FOLLOW_RANGE;
    public static final XAttribute KNOCKBACK_RESISTANCE;
    public static final XAttribute MOVEMENT_SPEED;
    public static final XAttribute FLYING_SPEED;
    public static final XAttribute ATTACK_DAMAGE;
    public static final XAttribute ATTACK_KNOCKBACK;
    public static final XAttribute ATTACK_SPEED;
    public static final XAttribute ARMOR;
    public static final XAttribute ARMOR_TOUGHNESS;
    public static final XAttribute FALL_DAMAGE_MULTIPLIER;
    public static final XAttribute LUCK;
    public static final XAttribute MAX_ABSORPTION;
    public static final XAttribute SAFE_FALL_DISTANCE;
    public static final XAttribute SCALE;
    public static final XAttribute STEP_HEIGHT;
    public static final XAttribute GRAVITY;
    public static final XAttribute JUMP_STRENGTH;
    public static final XAttribute BURNING_TIME;
    public static final XAttribute EXPLOSION_KNOCKBACK_RESISTANCE;
    public static final XAttribute MOVEMENT_EFFICIENCY;
    public static final XAttribute OXYGEN_BONUS;
    public static final XAttribute WATER_MOVEMENT_EFFICIENCY;
    public static final XAttribute TEMPT_RANGE;
    public static final XAttribute BLOCK_INTERACTION_RANGE;
    public static final XAttribute ENTITY_INTERACTION_RANGE;
    public static final XAttribute BLOCK_BREAK_SPEED;
    public static final XAttribute MINING_EFFICIENCY;
    public static final XAttribute SNEAKING_SPEED;
    public static final XAttribute SUBMERGED_MINING_SPEED;
    public static final XAttribute SWEEPING_DAMAGE_RATIO;
    public static final XAttribute SPAWN_REINFORCEMENTS;
    private static final boolean SUPPORTS_MODERN_MODIFIERS;

    private XAttribute(Attribute attribute, String[] stringArray) {
        super(attribute, stringArray);
    }

    public static AttributeModifier createModifier(@NotNull String string, double d, @NotNull AttributeModifier.Operation operation, @Nullable EquipmentSlot equipmentSlot) {
        Objects.requireNonNull(string, "Key is null");
        Objects.requireNonNull(operation, "Operation is null");
        if (SUPPORTS_MODERN_MODIFIERS) {
            NamespacedKey namespacedKey = Objects.requireNonNull(NamespacedKey.fromString((String)string), () -> "Invalid namespace: " + string);
            return new AttributeModifier(namespacedKey, d, operation, equipmentSlot == null ? EquipmentSlotGroup.ANY : equipmentSlot.getGroup());
        }
        return new AttributeModifier(UUID.randomUUID(), string, d, operation, equipmentSlot);
    }

    public static XAttribute of(Attribute attribute) {
        return REGISTRY.getByBukkitForm(attribute);
    }

    public static Optional<XAttribute> of(String string) {
        return REGISTRY.getByName(string);
    }

    @Deprecated
    public static XAttribute[] values() {
        return (XAttribute[])REGISTRY.values();
    }

    @NotNull
    public static @Unmodifiable Collection<XAttribute> getValues() {
        return REGISTRY.getValues();
    }

    private static XAttribute std(String ... stringArray) {
        return REGISTRY.std((XAttribute)stringArray);
    }

    static {
        boolean bl;
        REGISTRY = new XRegistry<XAttribute, Attribute>(Attribute.class, XAttribute.class, () -> Registry.ATTRIBUTE, XAttribute::new, XAttribute[]::new);
        MAX_HEALTH = XAttribute.std("max_health", "generic.max_health");
        FOLLOW_RANGE = XAttribute.std("follow_range", "generic.follow_range");
        KNOCKBACK_RESISTANCE = XAttribute.std("knockback_resistance", "generic.knockback_resistance");
        MOVEMENT_SPEED = XAttribute.std("movement_speed", "generic.movement_speed");
        FLYING_SPEED = XAttribute.std("flying_speed", "generic.flying_speed");
        ATTACK_DAMAGE = XAttribute.std("attack_damage", "generic.attack_damage");
        ATTACK_KNOCKBACK = XAttribute.std("attack_knockback", "generic.attack_knockback");
        ATTACK_SPEED = XAttribute.std("attack_speed", "generic.attack_speed");
        ARMOR = XAttribute.std("armor", "generic.armor");
        ARMOR_TOUGHNESS = XAttribute.std("armor_toughness", "generic.armor_toughness");
        FALL_DAMAGE_MULTIPLIER = XAttribute.std("fall_damage_multiplier", "generic.fall_damage_multiplier");
        LUCK = XAttribute.std("luck", "generic.luck");
        MAX_ABSORPTION = XAttribute.std("max_absorption", "generic.max_absorption");
        SAFE_FALL_DISTANCE = XAttribute.std("safe_fall_distance", "generic.safe_fall_distance");
        SCALE = XAttribute.std("scale", "generic.scale");
        STEP_HEIGHT = XAttribute.std("step_height", "generic.step_height");
        GRAVITY = XAttribute.std("gravity", "generic.gravity");
        JUMP_STRENGTH = XAttribute.std("jump_strength", "generic.jump_strength", "horse.jump_strength");
        BURNING_TIME = XAttribute.std("burning_time", "generic.burning_time");
        EXPLOSION_KNOCKBACK_RESISTANCE = XAttribute.std("explosion_knockback_resistance", "generic.explosion_knockback_resistance");
        MOVEMENT_EFFICIENCY = XAttribute.std("movement_efficiency", "generic.movement_efficiency");
        OXYGEN_BONUS = XAttribute.std("oxygen_bonus", "generic.oxygen_bonus");
        WATER_MOVEMENT_EFFICIENCY = XAttribute.std("water_movement_efficiency", "generic.water_movement_efficiency");
        TEMPT_RANGE = XAttribute.std("tempt_range", "generic.tempt_range");
        BLOCK_INTERACTION_RANGE = XAttribute.std("block_interaction_range", "player.block_interaction_range");
        ENTITY_INTERACTION_RANGE = XAttribute.std("entity_interaction_range", "player.entity_interaction_range");
        BLOCK_BREAK_SPEED = XAttribute.std("block_break_speed", "player.block_break_speed");
        MINING_EFFICIENCY = XAttribute.std("mining_efficiency", "player.mining_efficiency");
        SNEAKING_SPEED = XAttribute.std("sneaking_speed", "player.sneaking_speed");
        SUBMERGED_MINING_SPEED = XAttribute.std("submerged_mining_speed", "player.submerged_mining_speed");
        SWEEPING_DAMAGE_RATIO = XAttribute.std("sweeping_damage_ratio", "player.sweeping_damage_ratio");
        SPAWN_REINFORCEMENTS = XAttribute.std("spawn_reinforcements", "zombie.spawn_reinforcements");
        try {
            AttributeModifier.class.getConstructor(NamespacedKey.class, Double.TYPE, AttributeModifier.Operation.class, EquipmentSlotGroup.class);
            bl = true;
        } catch (NoClassDefFoundError | NoSuchMethodException throwable) {
            bl = false;
        }
        SUPPORTS_MODERN_MODIFIERS = bl;
    }
}

