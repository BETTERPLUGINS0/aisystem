package me.PM2.infinitevehicles.xseries;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import me.PM2.infinitevehicles.xseries.base.XModule;
import me.PM2.infinitevehicles.xseries.base.XRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class XAttribute extends XModule<XAttribute, Attribute> {
   public static final XRegistry<XAttribute, Attribute> REGISTRY = new XRegistry(Attribute.class, XAttribute.class, () -> {
      return Registry.ATTRIBUTE;
   }, XAttribute::new, (var0x) -> {
      return new XAttribute[var0x];
   });
   public static final XAttribute MAX_HEALTH = std("max_health", "generic.max_health");
   public static final XAttribute FOLLOW_RANGE = std("follow_range", "generic.follow_range");
   public static final XAttribute KNOCKBACK_RESISTANCE = std("knockback_resistance", "generic.knockback_resistance");
   public static final XAttribute MOVEMENT_SPEED = std("movement_speed", "generic.movement_speed");
   public static final XAttribute FLYING_SPEED = std("flying_speed", "generic.flying_speed");
   public static final XAttribute ATTACK_DAMAGE = std("attack_damage", "generic.attack_damage");
   public static final XAttribute ATTACK_KNOCKBACK = std("attack_knockback", "generic.attack_knockback");
   public static final XAttribute ATTACK_SPEED = std("attack_speed", "generic.attack_speed");
   public static final XAttribute ARMOR = std("armor", "generic.armor");
   public static final XAttribute ARMOR_TOUGHNESS = std("armor_toughness", "generic.armor_toughness");
   public static final XAttribute FALL_DAMAGE_MULTIPLIER = std("fall_damage_multiplier", "generic.fall_damage_multiplier");
   public static final XAttribute LUCK = std("luck", "generic.luck");
   public static final XAttribute MAX_ABSORPTION = std("max_absorption", "generic.max_absorption");
   public static final XAttribute SAFE_FALL_DISTANCE = std("safe_fall_distance", "generic.safe_fall_distance");
   public static final XAttribute SCALE = std("scale", "generic.scale");
   public static final XAttribute STEP_HEIGHT = std("step_height", "generic.step_height");
   public static final XAttribute GRAVITY = std("gravity", "generic.gravity");
   public static final XAttribute JUMP_STRENGTH = std("jump_strength", "generic.jump_strength", "horse.jump_strength");
   public static final XAttribute BURNING_TIME = std("burning_time", "generic.burning_time");
   public static final XAttribute EXPLOSION_KNOCKBACK_RESISTANCE = std("explosion_knockback_resistance", "generic.explosion_knockback_resistance");
   public static final XAttribute MOVEMENT_EFFICIENCY = std("movement_efficiency", "generic.movement_efficiency");
   public static final XAttribute OXYGEN_BONUS = std("oxygen_bonus", "generic.oxygen_bonus");
   public static final XAttribute WATER_MOVEMENT_EFFICIENCY = std("water_movement_efficiency", "generic.water_movement_efficiency");
   public static final XAttribute TEMPT_RANGE = std("tempt_range", "generic.tempt_range");
   public static final XAttribute BLOCK_INTERACTION_RANGE = std("block_interaction_range", "player.block_interaction_range");
   public static final XAttribute ENTITY_INTERACTION_RANGE = std("entity_interaction_range", "player.entity_interaction_range");
   public static final XAttribute BLOCK_BREAK_SPEED = std("block_break_speed", "player.block_break_speed");
   public static final XAttribute MINING_EFFICIENCY = std("mining_efficiency", "player.mining_efficiency");
   public static final XAttribute SNEAKING_SPEED = std("sneaking_speed", "player.sneaking_speed");
   public static final XAttribute SUBMERGED_MINING_SPEED = std("submerged_mining_speed", "player.submerged_mining_speed");
   public static final XAttribute SWEEPING_DAMAGE_RATIO = std("sweeping_damage_ratio", "player.sweeping_damage_ratio");
   public static final XAttribute SPAWN_REINFORCEMENTS = std("spawn_reinforcements", "zombie.spawn_reinforcements");
   public static final XAttribute CAMERA_DISTANCE = std("camera_distance");
   public static final XAttribute WAYPOINT_TRANSMIT_RANGE = std("waypoint_transmit_range");
   public static final XAttribute WAYPOINT_RECEIVE_RANGE = std("waypoint_receive_range");
   private static final boolean SUPPORTS_MODERN_MODIFIERS;

   private XAttribute(Attribute var1, String[] var2) {
      super(var1, var2);
   }

   public static AttributeModifier createModifier(@NotNull String var0, double var1, @NotNull Operation var3, @Nullable EquipmentSlot var4) {
      Objects.requireNonNull(var0, "Key is null");
      Objects.requireNonNull(var3, "Operation is null");
      if (SUPPORTS_MODERN_MODIFIERS) {
         NamespacedKey var5 = (NamespacedKey)Objects.requireNonNull(NamespacedKey.fromString(var0), () -> {
            return "Invalid namespace: " + var0;
         });
         return new AttributeModifier(var5, var1, var3, var4 == null ? EquipmentSlotGroup.ANY : var4.getGroup());
      } else {
         return new AttributeModifier(UUID.randomUUID(), var0, var1, var3, var4);
      }
   }

   public static XAttribute of(Attribute var0) {
      return (XAttribute)REGISTRY.getByBukkitForm(var0);
   }

   public static Optional<XAttribute> of(String var0) {
      return REGISTRY.getByName(var0);
   }

   /** @deprecated */
   @Deprecated
   public static XAttribute[] values() {
      return (XAttribute[])REGISTRY.values();
   }

   @NotNull
   @Unmodifiable
   public static Collection<XAttribute> getValues() {
      return REGISTRY.getValues();
   }

   private static XAttribute std(String... var0) {
      return (XAttribute)REGISTRY.std(var0);
   }

   static {
      boolean var0;
      try {
         AttributeModifier.class.getConstructor(NamespacedKey.class, Double.TYPE, Operation.class, EquipmentSlotGroup.class);
         var0 = true;
      } catch (NoClassDefFoundError | NoSuchMethodException var2) {
         var0 = false;
      }

      SUPPORTS_MODERN_MODIFIERS = var0;
      REGISTRY.discardMetadata();
   }
}
