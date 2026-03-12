package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.MappingHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class EnchantmentTypes {
   private static final Map<String, String> STRING_UPDATER = new HashMap();
   private static final Map<ResourceLocation, NBTCompound> ENCHANTMENT_DATA;
   private static final VersionedRegistry<EnchantmentType> REGISTRY;
   public static final EnchantmentType ALL_DAMAGE_PROTECTION;
   public static final EnchantmentType FIRE_PROTECTION;
   public static final EnchantmentType FALL_PROTECTION;
   public static final EnchantmentType BLAST_PROTECTION;
   public static final EnchantmentType PROJECTILE_PROTECTION;
   public static final EnchantmentType RESPIRATION;
   public static final EnchantmentType AQUA_AFFINITY;
   public static final EnchantmentType THORNS;
   public static final EnchantmentType DEPTH_STRIDER;
   public static final EnchantmentType FROST_WALKER;
   public static final EnchantmentType BINDING_CURSE;
   public static final EnchantmentType SOUL_SPEED;
   public static final EnchantmentType SWIFT_SNEAK;
   public static final EnchantmentType SHARPNESS;
   public static final EnchantmentType SMITE;
   public static final EnchantmentType BANE_OF_ARTHROPODS;
   public static final EnchantmentType KNOCKBACK;
   public static final EnchantmentType FIRE_ASPECT;
   public static final EnchantmentType MOB_LOOTING;
   public static final EnchantmentType SWEEPING_EDGE;
   public static final EnchantmentType BLOCK_EFFICIENCY;
   public static final EnchantmentType SILK_TOUCH;
   public static final EnchantmentType UNBREAKING;
   public static final EnchantmentType BLOCK_FORTUNE;
   public static final EnchantmentType POWER_ARROWS;
   public static final EnchantmentType PUNCH_ARROWS;
   public static final EnchantmentType FLAMING_ARROWS;
   public static final EnchantmentType INFINITY_ARROWS;
   public static final EnchantmentType FISHING_LUCK;
   public static final EnchantmentType FISHING_SPEED;
   public static final EnchantmentType LOYALTY;
   public static final EnchantmentType IMPALING;
   public static final EnchantmentType RIPTIDE;
   public static final EnchantmentType CHANNELING;
   public static final EnchantmentType MULTISHOT;
   public static final EnchantmentType QUICK_CHARGE;
   public static final EnchantmentType PIERCING;
   public static final EnchantmentType MENDING;
   public static final EnchantmentType VANISHING_CURSE;
   public static final EnchantmentType DENSITY;
   public static final EnchantmentType BREACH;
   public static final EnchantmentType WIND_BURST;
   public static final EnchantmentType LUNGE;

   private EnchantmentTypes() {
   }

   @ApiStatus.Internal
   public static EnchantmentType define(String key) {
      PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(ClientVersion.getLatest());
      return (EnchantmentType)REGISTRY.define(key, (data) -> {
         NBTCompound dataTag = (NBTCompound)ENCHANTMENT_DATA.get(data.getName());
         if (dataTag == null) {
            throw new IllegalArgumentException("Can't define enchantment " + data.getName() + ", no data found");
         } else {
            return EnchantmentType.decode(dataTag, (PacketWrapper)wrapper, data);
         }
      });
   }

   public static VersionedRegistry<EnchantmentType> getRegistry() {
      return REGISTRY;
   }

   @Nullable
   public static EnchantmentType getByName(String name) {
      String fixedName = (String)STRING_UPDATER.getOrDefault(name, name);
      return (EnchantmentType)REGISTRY.getByName(fixedName);
   }

   @Nullable
   public static EnchantmentType getById(ClientVersion version, int id) {
      return (EnchantmentType)REGISTRY.getById(version, id);
   }

   static {
      STRING_UPDATER.put("minecraft:sweeping", "minecraft:sweeping_edge");
      ENCHANTMENT_DATA = new HashMap();

      try {
         SequentialNBTReader.Compound dataTag = MappingHelper.decompress("mappings/data/enchantment");

         try {
            dataTag.skipOne();
            Iterator var1 = ((SequentialNBTReader.Compound)dataTag.next().getValue()).iterator();

            while(var1.hasNext()) {
               Entry<String, NBT> entry = (Entry)var1.next();
               ResourceLocation enchantKey = new ResourceLocation((String)entry.getKey());
               ENCHANTMENT_DATA.put(enchantKey, ((SequentialNBTReader.Compound)entry.getValue()).readFully());
            }
         } catch (Throwable var5) {
            if (dataTag != null) {
               try {
                  dataTag.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (dataTag != null) {
            dataTag.close();
         }
      } catch (IOException var6) {
         throw new RuntimeException("Error while reading enchantment type data", var6);
      }

      REGISTRY = new VersionedRegistry("enchantment");
      ALL_DAMAGE_PROTECTION = define("protection");
      FIRE_PROTECTION = define("fire_protection");
      FALL_PROTECTION = define("feather_falling");
      BLAST_PROTECTION = define("blast_protection");
      PROJECTILE_PROTECTION = define("projectile_protection");
      RESPIRATION = define("respiration");
      AQUA_AFFINITY = define("aqua_affinity");
      THORNS = define("thorns");
      DEPTH_STRIDER = define("depth_strider");
      FROST_WALKER = define("frost_walker");
      BINDING_CURSE = define("binding_curse");
      SOUL_SPEED = define("soul_speed");
      SWIFT_SNEAK = define("swift_sneak");
      SHARPNESS = define("sharpness");
      SMITE = define("smite");
      BANE_OF_ARTHROPODS = define("bane_of_arthropods");
      KNOCKBACK = define("knockback");
      FIRE_ASPECT = define("fire_aspect");
      MOB_LOOTING = define("looting");
      SWEEPING_EDGE = define("sweeping_edge");
      BLOCK_EFFICIENCY = define("efficiency");
      SILK_TOUCH = define("silk_touch");
      UNBREAKING = define("unbreaking");
      BLOCK_FORTUNE = define("fortune");
      POWER_ARROWS = define("power");
      PUNCH_ARROWS = define("punch");
      FLAMING_ARROWS = define("flame");
      INFINITY_ARROWS = define("infinity");
      FISHING_LUCK = define("luck_of_the_sea");
      FISHING_SPEED = define("lure");
      LOYALTY = define("loyalty");
      IMPALING = define("impaling");
      RIPTIDE = define("riptide");
      CHANNELING = define("channeling");
      MULTISHOT = define("multishot");
      QUICK_CHARGE = define("quick_charge");
      PIERCING = define("piercing");
      MENDING = define("mending");
      VANISHING_CURSE = define("vanishing_curse");
      DENSITY = define("density");
      BREACH = define("breach");
      WIND_BURST = define("wind_burst");
      LUNGE = define("lunge");
      ENCHANTMENT_DATA.clear();
      REGISTRY.unloadMappings();
   }
}
