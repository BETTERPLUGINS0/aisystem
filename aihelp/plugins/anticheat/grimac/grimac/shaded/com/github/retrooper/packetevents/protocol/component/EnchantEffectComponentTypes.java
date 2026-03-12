package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public final class EnchantEffectComponentTypes {
   private static final VersionedRegistry<ComponentType<?>> REGISTRY = new VersionedRegistry("enchantment_effect_component_type");
   public static ComponentType<NBT> DAMAGE_PROTECTION = define("damage_protection", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> DAMAGE_IMMUNITY = define("damage_immunity", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> DAMAGE = define("damage", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> SMASH_DAMAGE_PER_FALLEN_BLOCK = define("smash_damage_per_fallen_block", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> KNOCKBACK = define("knockback", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> ARMOR_EFFECTIVENESS = define("armor_effectiveness", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> POST_ATTACK = define("post_attack", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> HIT_BLOCK = define("hit_block", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> ITEM_DAMAGE = define("item_damage", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> ATTRIBUTES = define("attributes", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> EQUIPMENT_DROPS = define("equipment_drops", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> LOCATION_CHANGED = define("location_changed", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> TICK = define("tick", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> AMMO_USE = define("ammo_use", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> PROJECTILE_PIERCING = define("projectile_piercing", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> PROJECTILE_SPAWNED = define("projectile_spawned", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> PROJECTILE_SPREAD = define("projectile_spread", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> PROJECTILE_COUNT = define("projectile_count", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> TRIDENT_RETURN_ACCELERATION = define("trident_return_acceleration", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> FISHING_TIME_REDUCTION = define("fishing_time_reduction", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> FISHING_LUCK_BONUS = define("fishing_luck_bonus", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> BLOCK_EXPERIENCE = define("block_experience", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> MOB_EXPERIENCE = define("mob_experience", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> REPAIR_WITH_XP = define("repair_with_xp", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> CROSSBOW_CHARGE_TIME = define("crossbow_charge_time", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> CROSSBOW_CHARGING_SOUNDS = define("crossbow_charging_sounds", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> TRIDENT_SOUND = define("trident_sound", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> PREVENT_EQUIPMENT_DROP = define("prevent_equipment_drop", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> PREVENT_ARMOR_CHANGE = define("prevent_armor_change", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> TRIDENT_SPIN_ATTACK_STRENGTH = define("trident_spin_attack_strength", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });
   public static ComponentType<NBT> POST_PIERCING_ATTACK = define("post_piercing_attack", (nbt, version) -> {
      return nbt;
   }, (val, version) -> {
      return val;
   });

   private EnchantEffectComponentTypes() {
   }

   @ApiStatus.Internal
   public static <T> ComponentType<T> define(String key) {
      return define(key, (ComponentType.Decoder)null, (ComponentType.Encoder)null);
   }

   @ApiStatus.Internal
   public static <T> ComponentType<T> define(String key, @Nullable ComponentType.Decoder<T> reader, @Nullable ComponentType.Encoder<T> writer) {
      return (ComponentType)REGISTRY.define(key, (data) -> {
         return new StaticComponentType(data, reader, writer);
      });
   }

   public static VersionedRegistry<ComponentType<?>> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
