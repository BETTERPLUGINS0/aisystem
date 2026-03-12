package libs.com.ryderbelserion.vital.paper.util;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.util.Base64;
import libs.com.ryderbelserion.vital.common.VitalAPI;
import libs.com.ryderbelserion.vital.common.api.Provider;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUtil {
   private static final VitalAPI api = Provider.getApi();
   private static final ComponentLogger logger;
   private static final boolean isVerbose;

   private ItemUtil() {
      throw new AssertionError();
   }

   @Nullable
   public static Material getMaterial(@NotNull String value) {
      return getMaterial(value, isVerbose);
   }

   @Nullable
   public static Material getMaterial(@NotNull String value, boolean isVerbose) {
      if (value.isEmpty()) {
         if (isVerbose) {
            logger.error("{} cannot be blank!", value);
         }

         return null;
      } else {
         try {
            return (Material)Registry.MATERIAL.get(getKey(value));
         } catch (Exception var3) {
            if (isVerbose) {
               logger.error("{} is an invalid material.", value);
            }

            return null;
         }
      }
   }

   @Nullable
   public static Sound getSound(@NotNull String value) {
      return getSound(value, isVerbose);
   }

   @Nullable
   public static Sound getSound(@NotNull String value, boolean isVerbose) {
      if (value.isEmpty()) {
         if (isVerbose) {
            logger.error("{} cannot be blank!", value);
         }

         return null;
      } else {
         try {
            return (Sound)Registry.SOUNDS.get(getKey(value));
         } catch (Exception var3) {
            if (isVerbose) {
               logger.error("{} is an invalid sound.", value);
            }

            return null;
         }
      }
   }

   @Nullable
   public static Enchantment getEnchantment(@NotNull String value) {
      return getEnchantment(value, isVerbose);
   }

   @Nullable
   public static Enchantment getEnchantment(@NotNull String value, boolean isVerbose) {
      if (value.isEmpty()) {
         if (isVerbose) {
            logger.error("{} cannot be blank!", value);
         }

         return null;
      } else {
         try {
            return (Enchantment)RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(getKey(value));
         } catch (Exception var3) {
            if (isVerbose) {
               logger.error("{} is an invalid enchantment.", value);
            }

            return null;
         }
      }
   }

   @Nullable
   public static TrimPattern getTrimPattern(@NotNull String value) {
      return getTrimPattern(value, isVerbose);
   }

   @Nullable
   public static TrimPattern getTrimPattern(@NotNull String value, boolean isVerbose) {
      if (value.isEmpty()) {
         if (isVerbose) {
            logger.error("{} cannot be blank!", value);
         }

         return null;
      } else {
         try {
            return (TrimPattern)RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).get(getKey(value));
         } catch (Exception var3) {
            if (isVerbose) {
               logger.error("{} is an invalid trim pattern.", value);
            }

            return null;
         }
      }
   }

   @Nullable
   public static TrimMaterial getTrimMaterial(@NotNull String value) {
      return getTrimMaterial(value, isVerbose);
   }

   @Nullable
   public static TrimMaterial getTrimMaterial(@NotNull String value, boolean isVerbose) {
      if (value.isEmpty()) {
         if (isVerbose) {
            logger.error("{} cannot be blank!", value);
         }

         return null;
      } else {
         try {
            return (TrimMaterial)RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(getKey(value));
         } catch (Exception var3) {
            if (isVerbose) {
               logger.error("{} is an invalid trim material.", value);
            }

            return null;
         }
      }
   }

   @Nullable
   public static PotionType getPotionType(@NotNull String value) {
      return getPotionType(value, isVerbose);
   }

   @Nullable
   public static PotionType getPotionType(@NotNull String value, boolean isVerbose) {
      if (value.isEmpty()) {
         if (isVerbose) {
            logger.error("{} cannot be blank!", value);
         }

         return null;
      } else {
         try {
            return (PotionType)Registry.POTION.get(getKey(value));
         } catch (Exception var3) {
            if (isVerbose) {
               logger.error("{} is an invalid potion type.", value);
            }

            return null;
         }
      }
   }

   @Nullable
   public static PotionEffectType getPotionEffect(@NotNull String value) {
      return getPotionEffect(value, isVerbose);
   }

   @Nullable
   public static PotionEffectType getPotionEffect(@NotNull String value, boolean isVerbose) {
      if (value.isEmpty()) {
         if (isVerbose) {
            logger.error("{} cannot be blank!", value);
         }

         return null;
      } else {
         try {
            return (PotionEffectType)Registry.POTION_EFFECT_TYPE.get(getKey(value));
         } catch (Exception var3) {
            if (isVerbose) {
               logger.error("{} is an invalid potion effect type.", value);
            }

            return null;
         }
      }
   }

   @Nullable
   public static Particle getParticleType(@NotNull String value) {
      return getParticleType(value, isVerbose);
   }

   @Nullable
   public static Particle getParticleType(@NotNull String value, boolean isVerbose) {
      if (value.isEmpty()) {
         if (isVerbose) {
            logger.error("{} cannot be blank!", value);
         }

         return null;
      } else {
         try {
            return (Particle)Registry.PARTICLE_TYPE.get(getKey(value));
         } catch (Exception var3) {
            if (isVerbose) {
               logger.error("{} is an invalid particle type.", value);
            }

            return null;
         }
      }
   }

   @Nullable
   public static PatternType getPatternType(@NotNull String value) {
      return getPatternType(value, isVerbose);
   }

   @Nullable
   public static PatternType getPatternType(@NotNull String value, boolean isVerbose) {
      if (value.isEmpty()) {
         if (isVerbose) {
            logger.error("{} cannot be blank!", value);
         }

         return null;
      } else {
         try {
            return (PatternType)Registry.BANNER_PATTERN.get(getKey(value));
         } catch (Exception var3) {
            if (isVerbose) {
               logger.error("{} is an invalid banner type.", value);
            }

            return null;
         }
      }
   }

   @Nullable
   public static EntityType getEntity(@NotNull String value) {
      return getEntity(value, isVerbose);
   }

   @Nullable
   public static EntityType getEntity(@NotNull String value, boolean isVerbose) {
      if (value.isEmpty()) {
         if (isVerbose) {
            logger.error("{} cannot be blank!", value);
         }

         return null;
      } else {
         try {
            return (EntityType)Registry.ENTITY_TYPE.get(getKey(value));
         } catch (Exception var3) {
            if (isVerbose) {
               logger.error("{} is an invalid entity type.", value);
            }

            return null;
         }
      }
   }

   @Nullable
   public static Attribute getAttribute(@NotNull String value) {
      return getAttribute(value, isVerbose);
   }

   @Nullable
   public static Attribute getAttribute(@NotNull String value, boolean isVerbose) {
      if (value.isEmpty()) {
         if (isVerbose) {
            logger.error("{} cannot be blank!", value);
         }

         return null;
      } else {
         try {
            return (Attribute)Registry.ATTRIBUTE.get(getKey(value));
         } catch (Exception var3) {
            if (isVerbose) {
               logger.error("{} is an invalid attribute.", value);
            }

            return null;
         }
      }
   }

   @NotNull
   private static NamespacedKey getKey(@NotNull String value) {
      return NamespacedKey.minecraft(value);
   }

   public static byte[] toBytes(@NotNull ItemStack itemStack) {
      return itemStack.serializeAsBytes();
   }

   @NotNull
   public static ItemStack fromBytes(@NotNull byte[] bytes) {
      return ItemStack.deserializeBytes(bytes);
   }

   public static String toBase64(@NotNull ItemStack itemStack) {
      return Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());
   }

   @NotNull
   public static ItemStack fromBase64(@NotNull String base64) {
      return ItemStack.deserializeBytes(Base64.getDecoder().decode(base64));
   }

   static {
      logger = api.getComponentLogger();
      isVerbose = api.isVerbose();
   }
}
