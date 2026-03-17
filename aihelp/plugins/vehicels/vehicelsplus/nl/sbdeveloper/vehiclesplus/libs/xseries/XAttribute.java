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
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.XModule;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.XRegistry;
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
    public static final XRegistry<XAttribute, Attribute> REGISTRY = new XRegistry<XAttribute, Attribute>(Attribute.class, XAttribute.class, () -> Registry.ATTRIBUTE, XAttribute::new, XAttribute[]::new);
    public static final XAttribute MAX_HEALTH = XAttribute.std("max_health", "GENERIC_MAX_HEALTH");
    public static final XAttribute FOLLOW_RANGE = XAttribute.std("follow_range", "GENERIC_FOLLOW_RANGE");
    public static final XAttribute KNOCKBACK_RESISTANCE = XAttribute.std("knockback_resistance", "GENERIC_KNOCKBACK_RESISTANCE");
    public static final XAttribute MOVEMENT_SPEED = XAttribute.std("movement_speed", "GENERIC_MOVEMENT_SPEED");
    public static final XAttribute FLYING_SPEED = XAttribute.std("flying_speed", "GENERIC_FLYING_SPEED");
    public static final XAttribute ATTACK_DAMAGE = XAttribute.std("attack_damage", "GENERIC_ATTACK_DAMAGE");
    public static final XAttribute ATTACK_KNOCKBACK = XAttribute.std("attack_knockback", "GENERIC_ATTACK_KNOCKBACK");
    public static final XAttribute ATTACK_SPEED = XAttribute.std("attack_speed", "GENERIC_ATTACK_SPEED");
    public static final XAttribute ARMOR = XAttribute.std("armor", "GENERIC_ARMOR");
    public static final XAttribute ARMOR_TOUGHNESS = XAttribute.std("armor_toughness", "GENERIC_ARMOR_TOUGHNESS");
    public static final XAttribute FALL_DAMAGE_MULTIPLIER = XAttribute.std("fall_damage_multiplier", "GENERIC_FALL_DAMAGE_MULTIPLIER");
    public static final XAttribute LUCK = XAttribute.std("luck", "GENERIC_LUCK");
    public static final XAttribute MAX_ABSORPTION = XAttribute.std("max_absorption", "GENERIC_MAX_ABSORPTION");
    public static final XAttribute SAFE_FALL_DISTANCE = XAttribute.std("safe_fall_distance", "GENERIC_SAFE_FALL_DISTANCE");
    public static final XAttribute SCALE = XAttribute.std("scale", "GENERIC_SCALE");
    public static final XAttribute STEP_HEIGHT = XAttribute.std("step_height", "GENERIC_STEP_HEIGHT");
    public static final XAttribute GRAVITY = XAttribute.std("gravity", "GENERIC_GRAVITY");
    public static final XAttribute JUMP_STRENGTH = XAttribute.std("jump_strength", "GENERIC_JUMP_STRENGTH");
    public static final XAttribute BURNING_TIME = XAttribute.std("burning_time", "GENERIC_BURNING_TIME");
    public static final XAttribute EXPLOSION_KNOCKBACK_RESISTANCE = XAttribute.std("explosion_knockback_resistance", "GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE");
    public static final XAttribute MOVEMENT_EFFICIENCY = XAttribute.std("movement_efficiency", "GENERIC_MOVEMENT_EFFICIENCY");
    public static final XAttribute OXYGEN_BONUS = XAttribute.std("oxygen_bonus", "GENERIC_OXYGEN_BONUS");
    public static final XAttribute WATER_MOVEMENT_EFFICIENCY = XAttribute.std("water_movement_efficiency", "GENERIC_WATER_MOVEMENT_EFFICIENCY");
    public static final XAttribute TEMPT_RANGE = XAttribute.std("tempt_range", "GENERIC_TEMPT_RANGE");
    public static final XAttribute BLOCK_INTERACTION_RANGE = XAttribute.std("block_interaction_range", "PLAYER_BLOCK_INTERACTION_RANGE");
    public static final XAttribute ENTITY_INTERACTION_RANGE = XAttribute.std("entity_interaction_range", "PLAYER_ENTITY_INTERACTION_RANGE");
    public static final XAttribute BLOCK_BREAK_SPEED = XAttribute.std("block_break_speed", "PLAYER_BLOCK_BREAK_SPEED");
    public static final XAttribute MINING_EFFICIENCY = XAttribute.std("mining_efficiency", "PLAYER_MINING_EFFICIENCY");
    public static final XAttribute SNEAKING_SPEED = XAttribute.std("sneaking_speed", "PLAYER_SNEAKING_SPEED");
    public static final XAttribute SUBMERGED_MINING_SPEED = XAttribute.std("submerged_mining_speed", "PLAYER_SUBMERGED_MINING_SPEED");
    public static final XAttribute SWEEPING_DAMAGE_RATIO = XAttribute.std("sweeping_damage_ratio", "PLAYER_SWEEPING_DAMAGE_RATIO");
    public static final XAttribute SPAWN_REINFORCEMENTS = XAttribute.std("spawn_reinforcements", "ZOMBIE_SPAWN_REINFORCEMENTS");
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
        boolean bl = false;
        try {
            AttributeModifier.class.getConstructor(NamespacedKey.class, Double.TYPE, AttributeModifier.Operation.class, EquipmentSlotGroup.class);
            bl = true;
        } catch (NoClassDefFoundError | NoSuchMethodException throwable) {
            // empty catch block
        }
        SUPPORTS_MODERN_MODIFIERS = bl;
    }
}

