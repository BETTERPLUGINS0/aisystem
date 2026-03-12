package xyz.xenondevs.particle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.data.ParticleData;
import xyz.xenondevs.particle.data.VibrationData;
import xyz.xenondevs.particle.data.color.DustColorTransitionData;
import xyz.xenondevs.particle.data.color.DustData;
import xyz.xenondevs.particle.data.color.NoteColor;
import xyz.xenondevs.particle.data.color.ParticleColor;
import xyz.xenondevs.particle.data.color.RegularColor;
import xyz.xenondevs.particle.data.texture.BlockTexture;
import xyz.xenondevs.particle.data.texture.ItemTexture;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public enum ParticleEffect {
   ASH((version) -> {
      return version < 16.0D ? "NONE" : "ash";
   }, new PropertyType[0]),
   BARRIER((version) -> {
      return !(version < 8.0D) && !(version > 17.0D) ? (version < 13.0D ? "BARRIER" : "barrier") : "NONE";
   }, new PropertyType[0]),
   BLOCK_CRACK((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "BLOCK_CRACK" : "block");
   }, new PropertyType[]{PropertyType.REQUIRES_BLOCK}),
   BLOCK_DUST((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "BLOCK_DUST" : "falling_dust");
   }, new PropertyType[]{PropertyType.DIRECTIONAL, PropertyType.REQUIRES_BLOCK}),
   BUBBLE_COLUMN_UP((version) -> {
      return version < 13.0D ? "NONE" : "bubble_column_up";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   BLOCK_MARKER((version) -> {
      return version < 18.0D ? "NONE" : "block_marker";
   }, new PropertyType[]{PropertyType.REQUIRES_BLOCK}),
   BUBBLE_POP((version) -> {
      return version < 13.0D ? "NONE" : "bubble_pop";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   CAMPFIRE_COSY_SMOKE((version) -> {
      return version < 14.0D ? "NONE" : "campfire_cosy_smoke";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   CAMPFIRE_SIGNAL_SMOKE((version) -> {
      return version < 14.0D ? "NONE" : "campfire_signal_smoke";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   CLOUD((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "CLOUD" : "cloud");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   COMPOSTER((version) -> {
      return version < 14.0D ? "NONE" : "composter";
   }, new PropertyType[0]),
   CRIMSON_SPORE((version) -> {
      return version < 16.0D ? "NONE" : "crimson_spore";
   }, new PropertyType[0]),
   CRIT((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "CRIT" : "crit");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   CRIT_MAGIC((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "CRIT_MAGIC" : "enchanted_hit");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   CURRENT_DOWN((version) -> {
      return version < 13.0D ? "NONE" : "current_down";
   }, new PropertyType[0]),
   DAMAGE_INDICATOR((version) -> {
      return version < 9.0D ? "NONE" : (version < 13.0D ? "DAMAGE_INDICATOR" : "damage_indicator");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   DOLPHIN((version) -> {
      return version < 13.0D ? "NONE" : "dolphin";
   }, new PropertyType[0]),
   DRAGON_BREATH((version) -> {
      return version < 9.0D ? "NONE" : (version < 13.0D ? "DRAGON_BREATH" : "dragon_breath");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   DRIP_LAVA((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "DRIP_LAVA" : "dripping_lava");
   }, new PropertyType[0]),
   DRIP_WATER((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "DRIP_WATER" : "dripping_water");
   }, new PropertyType[0]),
   DRIPPING_DRIPSTONE_LAVA((version) -> {
      return version < 17.0D ? "NONE" : "dripping_dripstone_lava";
   }, new PropertyType[0]),
   DRIPPING_DRIPSTONE_WATER((version) -> {
      return version < 17.0D ? "NONE" : "dripping_dripstone_water";
   }, new PropertyType[0]),
   DRIPPING_HONEY((version) -> {
      return version < 15.0D ? "NONE" : "dripping_honey";
   }, new PropertyType[0]),
   DRIPPING_OBSIDIAN_TEAR((version) -> {
      return version < 16.0D ? "NONE" : "dripping_obsidian_tear";
   }, new PropertyType[0]),
   DUST_COLOR_TRANSITION((version) -> {
      return version < 17.0D ? "NONE" : "dust_color_transition";
   }, new PropertyType[]{PropertyType.COLORABLE, PropertyType.DUST}),
   ELECTRIC_SPARK((version) -> {
      return version < 17.0D ? "NONE" : "electric_spark";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   ENCHANTMENT_TABLE((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "ENCHANTMENT_TABLE" : "enchant");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   END_ROD((version) -> {
      return version < 9.0D ? "NONE" : (version < 13.0D ? "END_ROD" : "end_rod");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   EXPLOSION_HUGE((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "EXPLOSION_HUGE" : "explosion_emitter");
   }, new PropertyType[0]),
   EXPLOSION_LARGE((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "EXPLOSION_LARGE" : "explosion");
   }, new PropertyType[0]),
   EXPLOSION_NORMAL((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "EXPLOSION_NORMAL" : "poof");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   FALLING_DRIPSTONE_LAVA((version) -> {
      return version < 17.0D ? "NONE" : "falling_dripstone_lava";
   }, new PropertyType[0]),
   FALLING_DRIPSTONE_WATER((version) -> {
      return version < 17.0D ? "NONE" : "falling_dripstone_water";
   }, new PropertyType[0]),
   FALLING_DUST((version) -> {
      return version < 10.0D ? "NONE" : (version < 13.0D ? "FALLING_DUST" : "falling_dust");
   }, new PropertyType[]{PropertyType.REQUIRES_BLOCK}),
   FALLING_HONEY((version) -> {
      return version < 15.0D ? "NONE" : "falling_honey";
   }, new PropertyType[0]),
   FALLING_NECTAR((version) -> {
      return version < 15.0D ? "NONE" : "falling_nectar";
   }, new PropertyType[0]),
   FALLING_OBSIDIAN_TEAR((version) -> {
      return version < 16.0D ? "NONE" : "falling_obsidian_tear";
   }, new PropertyType[0]),
   FALLING_SPORE_BLOSSOM((version) -> {
      return version < 17.0D ? "NONE" : "falling_spore_blossom";
   }, new PropertyType[0]),
   FIREWORKS_SPARK((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "FIREWORKS_SPARK" : "firework");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   FLAME((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "FLAME" : "flame");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   FLASH((version) -> {
      return version < 14.0D ? "NONE" : "flash";
   }, new PropertyType[0]),
   FOOTSTEP((version) -> {
      return version > 8.0D && version < 13.0D ? "FOOTSTEP" : "NONE";
   }, new PropertyType[0]),
   GLOW((version) -> {
      return version < 17.0D ? "NONE" : "glow";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   GLOW_SQUID_INK((version) -> {
      return version < 17.0D ? "NONE" : "glow_squid_ink";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   HEART((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "HEART" : "heart");
   }, new PropertyType[0]),
   ITEM_CRACK((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "ITEM_CRACK" : "item");
   }, new PropertyType[]{PropertyType.DIRECTIONAL, PropertyType.REQUIRES_ITEM}),
   LANDING_HONEY((version) -> {
      return version < 15.0D ? "NONE" : "landing_honey";
   }, new PropertyType[0]),
   LANDING_OBSIDIAN_TEAR((version) -> {
      return version < 16.0D ? "NONE" : "landing_obsidian_tear";
   }, new PropertyType[0]),
   LAVA((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "LAVA" : "lava");
   }, new PropertyType[0]),
   LIGHT((version) -> {
      return version != 17.0D ? "NONE" : "light";
   }, new PropertyType[0]),
   MOB_APPEARANCE((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "MOB_APPEARANCE" : "elder_guardian");
   }, new PropertyType[0]),
   NAUTILUS((version) -> {
      return version < 13.0D ? "NONE" : "nautilus";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   NOTE((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "NOTE" : "note");
   }, new PropertyType[]{PropertyType.COLORABLE}),
   PORTAL((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "PORTAL" : "portal");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   REDSTONE((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "REDSTONE" : "dust");
   }, new PropertyType[]{PropertyType.COLORABLE, PropertyType.DUST}),
   REVERSE_PORTAL((version) -> {
      return version < 16.0D ? "NONE" : "reverse_portal";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SCRAPE((version) -> {
      return version < 17.0D ? "NONE" : "scrape";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SCULK_CHARGE((version) -> {
      return version < 19.0D ? "NONE" : "sculk_charge";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SCULK_CHARGE_POP((version) -> {
      return version < 19.0D ? "NONE" : "sculk_charge_pop";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SCULK_SOUL((version) -> {
      return version < 19.0D ? "NONE" : "sculk_soul";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SHRIEK((version) -> {
      return version < 19.0D ? "NONE" : "shriek";
   }, new PropertyType[0]),
   SLIME((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "SLIME" : "item_slime");
   }, new PropertyType[0]),
   SMALL_FLAME((version) -> {
      return version < 17.0D ? "NONE" : "small_flame";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SMOKE_LARGE((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "SMOKE_LARGE" : "large_smoke");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SMOKE_NORMAL((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "SMOKE_NORMAL" : "smoke");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SNEEZE((version) -> {
      return version < 14.0D ? "NONE" : "sneeze";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SNOWBALL((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "SNOWBALL" : "item_snowball");
   }, new PropertyType[0]),
   SNOWFLAKE((version) -> {
      return version < 17.0D ? "NONE" : "snowflake";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SNOW_SHOVEL((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "SNOW_SHOVEL" : "poof");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SONIC_BOOM((version) -> {
      return version < 19.0D ? "NONE" : "sonic_boom";
   }, new PropertyType[0]),
   SOUL((version) -> {
      return version < 16.0D ? "NONE" : "soul";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SOUL_FIRE_FLAME((version) -> {
      return version < 16.0D ? "NONE" : "soul_fire_flame";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SPELL((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "SPELL" : "effect");
   }, new PropertyType[0]),
   SPELL_INSTANT((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "SPELL_INSTANT" : "instant_effect");
   }, new PropertyType[0]),
   SPELL_MOB((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "SPELL_MOB" : "entity_effect");
   }, new PropertyType[]{PropertyType.COLORABLE}),
   SPELL_MOB_AMBIENT((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "SPELL_MOB_AMBIENT" : "ambient_entity_effect");
   }, new PropertyType[]{PropertyType.COLORABLE}),
   SPELL_WITCH((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "SPELL_WITCH" : "witch");
   }, new PropertyType[0]),
   SPIT((version) -> {
      return version < 11.0D ? "NONE" : (version < 13.0D ? "SPIT" : "spit");
   }, new PropertyType[0]),
   SPORE_BLOSSOM_AIR((version) -> {
      return version < 17.0D ? "NONE" : "spore_blossom_air";
   }, new PropertyType[0]),
   SQUID_INK((version) -> {
      return version < 13.0D ? "NONE" : "squid_ink";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SUSPENDED((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "SUSPENDED" : "underwater");
   }, new PropertyType[]{PropertyType.REQUIRES_WATER}),
   SUSPENDED_DEPTH((version) -> {
      return version > 8.0D && version < 13.0D ? "SUSPENDED_DEPTH" : "NONE";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   SWEEP_ATTACK((version) -> {
      return version < 9.0D ? "NONE" : (version < 13.0D ? "SWEEP_ATTACK" : "sweep_attack");
   }, new PropertyType[]{PropertyType.RESIZEABLE}),
   TOTEM((version) -> {
      return version < 11.0D ? "NONE" : (version < 13.0D ? "TOTEM" : "totem_of_undying");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   TOWN_AURA((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "TOWN_AURA" : "mycelium");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   VIBRATION((version) -> {
      return version < 17.0D ? "NONE" : "vibration";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   VILLAGER_ANGRY((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "VILLAGER_ANGRY" : "angry_villager");
   }, new PropertyType[0]),
   VILLAGER_HAPPY((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "VILLAGER_HAPPY" : "happy_villager");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   WARPED_SPORE((version) -> {
      return version < 16.0D ? "NONE" : "warped_spore";
   }, new PropertyType[0]),
   WATER_BUBBLE((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "WATER_BUBBLE" : "bubble");
   }, new PropertyType[]{PropertyType.DIRECTIONAL, PropertyType.REQUIRES_WATER}),
   WATER_DROP((version) -> {
      return version > 8.0D && version < 13.0D ? "WATER_DROP" : "NONE";
   }, new PropertyType[0]),
   WATER_SPLASH((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "WATER_SPLASH" : "splash");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   WATER_WAKE((version) -> {
      return version < 8.0D ? "NONE" : (version < 13.0D ? "WATER_WAKE" : "fishing");
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   WAX_OFF((version) -> {
      return version < 17.0D ? "NONE" : "wax_off";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   WAX_ON((version) -> {
      return version < 17.0D ? "NONE" : "wax_on";
   }, new PropertyType[]{PropertyType.DIRECTIONAL}),
   WHITE_ASH((version) -> {
      return version < 16.0D ? "NONE" : "white_ash";
   }, new PropertyType[0]);

   private final DoubleFunction<String> fieldNameMapper;
   private final List<PropertyType> properties;
   public static final List<ParticleEffect> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
   public static final Map<ParticleEffect, Object> NMS_EFFECTS = Collections.unmodifiableMap((Map)VALUES.stream().filter((effect) -> {
      return !"NONE".equals(effect.getFieldName());
   }).collect(Collectors.toMap(Function.identity(), ParticleEffect::getNMSObject)));

   public static Set<ParticleEffect> getAvailableEffects() {
      return NMS_EFFECTS.keySet();
   }

   private ParticleEffect(DoubleFunction<String> fieldNameMapper, PropertyType... properties) {
      this.fieldNameMapper = fieldNameMapper;
      this.properties = Collections.unmodifiableList(Arrays.asList(properties));
   }

   public String getFieldName() {
      return (String)this.fieldNameMapper.apply(ReflectionUtils.MINECRAFT_VERSION);
   }

   public List<PropertyType> getProperties() {
      return this.properties;
   }

   public boolean hasProperty(PropertyType propertyType) {
      return propertyType != null && this.properties.contains(propertyType);
   }

   public boolean isCorrectData(ParticleData data) {
      if (data == null) {
         return true;
      } else if (data instanceof ParticleColor) {
         return this.isCorrectColor((ParticleColor)data);
      } else if (data instanceof BlockTexture) {
         return this.hasProperty(PropertyType.REQUIRES_BLOCK);
      } else if (data instanceof ItemTexture) {
         return this.hasProperty(PropertyType.REQUIRES_ITEM);
      } else {
         return data instanceof VibrationData && this == VIBRATION;
      }
   }

   public boolean isCorrectColor(ParticleColor color) {
      if (color instanceof DustColorTransitionData) {
         return this == DUST_COLOR_TRANSITION;
      } else if (color instanceof DustData) {
         return this.hasProperty(PropertyType.DUST);
      } else {
         boolean var10000;
         label43: {
            if (this.hasProperty(PropertyType.COLORABLE)) {
               if (this.equals(NOTE)) {
                  if (color instanceof NoteColor) {
                     break label43;
                  }
               } else if (color instanceof RegularColor) {
                  break label43;
               }
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      }
   }

   public Object getNMSObject() {
      if (NMS_EFFECTS != null && NMS_EFFECTS.containsKey(this)) {
         return NMS_EFFECTS.get(this);
      } else {
         String fieldName = this.getFieldName();
         if ("NONE".equals(fieldName)) {
            return null;
         } else if (ReflectionUtils.MINECRAFT_VERSION < 13.0D) {
            return Arrays.stream(ParticleConstants.PARTICLE_ENUM.getEnumConstants()).filter((effect) -> {
               return effect.toString().equals(fieldName);
            }).findFirst().orElse((Object)null);
         } else {
            try {
               return ParticleConstants.REGISTRY_GET_METHOD.invoke(ParticleConstants.PARTICLE_TYPE_REGISTRY, ReflectionUtils.getMinecraftKey(fieldName));
            } catch (Exception var3) {
               return null;
            }
         }
      }
   }

   public void display(Location location, ParticleColor color, Player... players) {
      this.display(location, 0.0F, 0.0F, 0.0F, 1.0F, 0, color, (Player[])players);
   }

   public void display(Location location, Color color, Player... players) {
      this.display(location, (ParticleColor)(new RegularColor(color)), (Player[])players);
   }

   public void display(Location location, ParticleColor color, Predicate filter) {
      this.display(location, 0.0F, 0.0F, 0.0F, 1.0F, 0, color, (Predicate)filter);
   }

   public void display(Location location, Color color, Predicate filter) {
      this.display(location, (ParticleColor)(new RegularColor(color)), (Predicate)filter);
   }

   public void display(Location location, ParticleColor color, Collection<? extends Player> players) {
      this.display(location, 0.0F, 0.0F, 0.0F, 1.0F, 0, color, (Collection)players);
   }

   public void display(Location location, Color color, Collection<? extends Player> players) {
      this.display(location, (ParticleColor)(new RegularColor(color)), (Collection)players);
   }

   public void display(Location location, ParticleColor color) {
      this.display(location, 0.0F, 0.0F, 0.0F, 1.0F, 0, color);
   }

   public void display(Location location, Color color) {
      this.display(location, (ParticleColor)(new RegularColor(color)));
   }

   public void display(Location location, Player... players) {
      this.display(location, 0.0F, 0.0F, 0.0F, 0.0F, 1, (ParticleData)null, (Player[])players);
   }

   public void display(Location location, Predicate filter) {
      this.display(location, 0.0F, 0.0F, 0.0F, 0.0F, 1, (ParticleData)null, (Predicate)filter);
   }

   public void display(Location location, Collection<? extends Player> players) {
      this.display(location, 0.0F, 0.0F, 0.0F, 0.0F, 1, (ParticleData)null, (Collection)players);
   }

   public void display(Location location) {
      this.display(location, 0.0F, 0.0F, 0.0F, 0.0F, 1, (ParticleData)null, (Collection)Bukkit.getOnlinePlayers());
   }

   public void display(Location location, Vector vector, float speed, int amount, ParticleData data, Player... players) {
      this.display(location, (float)vector.getX(), (float)vector.getY(), (float)vector.getZ(), speed, amount, data, players);
   }

   public void display(Location location, Vector vector, float speed, int amount, ParticleData data, Predicate filter) {
      this.display(location, (float)vector.getX(), (float)vector.getY(), (float)vector.getZ(), speed, amount, data, filter);
   }

   public void display(Location location, Vector vector, float speed, int amount, ParticleData data, Collection<? extends Player> players) {
      this.display(location, (float)vector.getX(), (float)vector.getY(), (float)vector.getZ(), speed, amount, data, players);
   }

   public void display(Location location, Vector vector, float speed, int amount, ParticleData data) {
      this.display(location, (float)vector.getX(), (float)vector.getY(), (float)vector.getZ(), speed, amount, data);
   }

   public void display(Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData data, Player... players) {
      ArrayList<Player> playerList = (ArrayList)Arrays.stream(players).collect(Collectors.toCollection(ArrayList::new));
      this.display(location, offsetX, offsetY, offsetZ, speed, amount, data, (Collection)playerList);
   }

   public void display(Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData data, Predicate<Player> filter) {
      ArrayList<Player> players = (ArrayList)Bukkit.getOnlinePlayers().stream().filter(filter).collect(Collectors.toCollection(ArrayList::new));
      this.display(location, offsetX, offsetY, offsetZ, speed, amount, data, (Collection)players);
   }

   public void display(Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData data) {
      this.display(location, offsetX, offsetY, offsetZ, speed, amount, data, Bukkit.getOnlinePlayers());
   }

   public void display(Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData data, Collection<? extends Player> players) {
      if (this.isCorrectData(data)) {
         if (data != null) {
            data.setEffect(this);
         }

         ParticlePacket packet = new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, data);
         Object nmsPacket = packet.createPacket(location);
         players.stream().filter((p) -> {
            return p.getWorld().equals(location.getWorld());
         }).forEach((p) -> {
            ReflectionUtils.sendPacket(p, nmsPacket);
         });
      }
   }

   // $FF: synthetic method
   private static ParticleEffect[] $values() {
      return new ParticleEffect[]{ASH, BARRIER, BLOCK_CRACK, BLOCK_DUST, BUBBLE_COLUMN_UP, BLOCK_MARKER, BUBBLE_POP, CAMPFIRE_COSY_SMOKE, CAMPFIRE_SIGNAL_SMOKE, CLOUD, COMPOSTER, CRIMSON_SPORE, CRIT, CRIT_MAGIC, CURRENT_DOWN, DAMAGE_INDICATOR, DOLPHIN, DRAGON_BREATH, DRIP_LAVA, DRIP_WATER, DRIPPING_DRIPSTONE_LAVA, DRIPPING_DRIPSTONE_WATER, DRIPPING_HONEY, DRIPPING_OBSIDIAN_TEAR, DUST_COLOR_TRANSITION, ELECTRIC_SPARK, ENCHANTMENT_TABLE, END_ROD, EXPLOSION_HUGE, EXPLOSION_LARGE, EXPLOSION_NORMAL, FALLING_DRIPSTONE_LAVA, FALLING_DRIPSTONE_WATER, FALLING_DUST, FALLING_HONEY, FALLING_NECTAR, FALLING_OBSIDIAN_TEAR, FALLING_SPORE_BLOSSOM, FIREWORKS_SPARK, FLAME, FLASH, FOOTSTEP, GLOW, GLOW_SQUID_INK, HEART, ITEM_CRACK, LANDING_HONEY, LANDING_OBSIDIAN_TEAR, LAVA, LIGHT, MOB_APPEARANCE, NAUTILUS, NOTE, PORTAL, REDSTONE, REVERSE_PORTAL, SCRAPE, SCULK_CHARGE, SCULK_CHARGE_POP, SCULK_SOUL, SHRIEK, SLIME, SMALL_FLAME, SMOKE_LARGE, SMOKE_NORMAL, SNEEZE, SNOWBALL, SNOWFLAKE, SNOW_SHOVEL, SONIC_BOOM, SOUL, SOUL_FIRE_FLAME, SPELL, SPELL_INSTANT, SPELL_MOB, SPELL_MOB_AMBIENT, SPELL_WITCH, SPIT, SPORE_BLOSSOM_AIR, SQUID_INK, SUSPENDED, SUSPENDED_DEPTH, SWEEP_ATTACK, TOTEM, TOWN_AURA, VIBRATION, VILLAGER_ANGRY, VILLAGER_HAPPY, WARPED_SPORE, WATER_BUBBLE, WATER_DROP, WATER_SPLASH, WATER_WAKE, WAX_OFF, WAX_ON, WHITE_ASH};
   }
}
