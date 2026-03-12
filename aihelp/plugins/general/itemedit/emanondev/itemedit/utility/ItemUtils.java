package emanondev.itemedit.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ItemUtils {
   private ItemUtils() {
      throw new UnsupportedOperationException();
   }

   @NotNull
   public static ItemMeta getMeta(@NotNull ItemStack item) {
      return (ItemMeta)Objects.requireNonNull(item.getItemMeta(), "ItemMeta cannot be null");
   }

   @NotNull
   public static ItemStack getHandItem(@NotNull Player player) {
      return player.getInventory().getItemInHand();
   }

   public static void setHandItem(@NotNull Player player, @Nullable ItemStack item) {
      player.getInventory().setItemInHand(item);
   }

   @Contract("null -> true")
   public static boolean isAirOrNull(@Nullable ItemStack item) {
      return item == null || item.getType() == Material.AIR;
   }

   public static boolean isUnbreakable(@NotNull ItemStack item) {
      return isUnbreakable(getMeta(item));
   }

   public static boolean isUnbreakable(@Nullable ItemMeta meta) {
      if (meta == null) {
         return false;
      } else if (VersionUtils.isVersionAfter(1, 11)) {
         return meta.isUnbreakable();
      } else {
         Object spigotMeta = Objects.requireNonNull(ReflectionUtils.invokeMethod(meta, "spigot"));
         Boolean result = (Boolean)ReflectionUtils.invokeMethod(spigotMeta, "isUnbreakable");
         return result != null && result;
      }
   }

   public static void setUnbreakable(@NotNull ItemStack item, boolean value) {
      ItemMeta meta = getMeta(item);
      setUnbreakable(meta, value);
      item.setItemMeta(meta);
   }

   public static void setUnbreakable(@Nullable ItemMeta meta, boolean value) {
      if (meta != null) {
         if (VersionUtils.isVersionAfter(1, 11)) {
            meta.setUnbreakable(value);
         } else {
            Object spigotMeta = Objects.requireNonNull(ReflectionUtils.invokeMethod(meta, "spigot"));
            ReflectionUtils.invokeMethod(spigotMeta, "setUnbreakable", (Class)Boolean.TYPE, (Object)value);
         }
      }
   }

   public static AttributeModifier createAttributeModifier(double amount, @NotNull Operation operation, @Nullable String slot) {
      if (VersionUtils.isVersionAfter(1, 20, 6)) {
         EquipmentSlotGroup group;
         if (slot == null) {
            group = EquipmentSlotGroup.ANY;
         } else {
            group = EquipmentSlotGroup.getByName(slot.toUpperCase(Locale.ENGLISH));
            if (group == null) {
               group = EquipmentSlot.valueOf(slot.toUpperCase(Locale.ENGLISH)).getGroup();
            }
         }

         if (VersionUtils.isVersionAfter(1, 21, 2)) {
            return new AttributeModifier((NamespacedKey)Objects.requireNonNull(NamespacedKey.fromString(UUID.randomUUID().toString())), amount, operation, group);
         } else {
            UUID uuid = UUID.randomUUID();
            return (AttributeModifier)ReflectionUtils.invokeConstructor(AttributeModifier.class, UUID.class, uuid, String.class, uuid.toString(), Double.TYPE, amount, Operation.class, operation, EquipmentSlotGroup.class, group);
         }
      } else {
         UUID uuid = UUID.randomUUID();
         return VersionUtils.isVersionAfter(1, 13, 2) ? (AttributeModifier)ReflectionUtils.invokeConstructor(AttributeModifier.class, UUID.class, uuid, String.class, uuid.toString(), Double.TYPE, amount, Operation.class, operation, EquipmentSlot.class, slot == null ? null : EquipmentSlot.valueOf(slot.toUpperCase(Locale.ENGLISH))) : (AttributeModifier)ReflectionUtils.invokeConstructor(AttributeModifier.class, UUID.class, uuid, String.class, uuid.toString(), Double.TYPE, amount, Operation.class, operation);
      }
   }

   public static PatternType[] getPatternTypes() {
      try {
         if (VersionUtils.isVersionAfter(1, 20, 6)) {
            List<PatternType> result = new ArrayList();
            Registry var10000 = Registry.BANNER_PATTERN;
            Objects.requireNonNull(result);
            var10000.forEach(result::add);
            return (PatternType[])result.toArray(new PatternType[0]);
         }
      } catch (Throwable var1) {
      }

      PatternType[] result = (PatternType[])Objects.requireNonNull((PatternType[])ReflectionUtils.invokeStaticMethod(PatternType.class, "values"));
      return (PatternType[])Arrays.copyOfRange(result, 1, result.length);
   }

   public static PatternType[] getPatternTypesFiltered() {
      PatternType[] val = getPatternTypes();
      ArrayList<PatternType> list = new ArrayList(Arrays.asList(val));
      list.remove(PatternType.BASE);
      if (VersionUtils.isVersionAfter(1, 20, 6) && !VersionUtils.isVersionAfter(1, 21)) {
         list.remove(PatternType.FLOW);
         list.remove(PatternType.GUSTER);
      }

      return (PatternType[])list.toArray(new PatternType[0]);
   }

   public static boolean isItem(@NotNull Material material) {
      if (VersionUtils.isVersionUpTo(1, 12, 99)) {
         return true;
      } else {
         return material.name().startsWith("LEGACY_") ? false : material.isItem();
      }
   }

   public static ItemMeta setColor(@NotNull ItemMeta meta, @NotNull Color color) {
      if (meta instanceof PotionMeta) {
         ((PotionMeta)meta).setColor(color);
         return meta;
      } else {
         try {
            if (meta instanceof LeatherArmorMeta) {
               LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)meta;
               leatherArmorMeta.setColor(color);
               return meta;
            }
         } catch (Throwable var3) {
         }

         return meta;
      }
   }

   public static Color getColor(@NotNull ItemMeta meta) {
      if (meta instanceof PotionMeta) {
         PotionMeta potionMeta = (PotionMeta)meta;
         return potionMeta.getColor() == null ? toColor(65, 85, 255) : potionMeta.getColor();
      } else {
         try {
            if (meta instanceof LeatherArmorMeta) {
               LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)meta;
               leatherArmorMeta.getColor();
               return leatherArmorMeta.getColor();
            }
         } catch (Throwable var2) {
         }

         return toColor(0, 0, 0);
      }
   }

   private static Color toColor(int red, int green, int blue) {
      return Color.fromRGB(limit(red), limit(green), limit(blue));
   }

   private static int limit(int color) {
      return Math.max(0, Math.min(255, color));
   }
}
