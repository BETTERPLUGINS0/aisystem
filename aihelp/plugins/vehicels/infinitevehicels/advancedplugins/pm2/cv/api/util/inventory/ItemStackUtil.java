package advancedplugins.pm2.cv.api.util.inventory;

import advancedplugins.pm2.cv.api.util.ColorUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ItemStackUtil {
   public static void saveItem(@NotNull ConfigurationSection var0, ItemStack var1) {
   }

   @NotNull
   public static ItemStack loadItem(@NotNull ConfigurationSection var0) {
      String var1 = var0.getString("name");
      List var2 = var0.getStringList("lore");
      int var3 = var0.getInt("custom-model-data");
      String var4 = var0.getString("item-model");
      String var5 = var0.getString("material");
      if (var5 == null) {
         throw new IllegalArgumentException("material cannot be null");
      } else {
         Material var6 = Material.matchMaterial(var5);
         if (var6 == null) {
            throw new IllegalArgumentException("material " + var5 + " is not a valid material");
         } else {
            int var7 = var0.getInt("item-amount", 1);
            ItemStack var8 = new ItemStack(var6, var7);
            ItemMeta var9 = var8.getItemMeta();
            if (var9 == null) {
               var9 = Bukkit.getItemFactory().getItemMeta(var8.getType());
            }

            if (var9 == null) {
               throw new IllegalArgumentException(String.valueOf(var8.getType()) + " doesn't have a ItemMeta!");
            } else {
               if (var3 > 0) {
                  var9.setCustomModelData(var3);
               }

               if (var4 != null) {
                  try {
                     var9.setItemModel(NamespacedKey.fromString(var4));
                  } catch (NoSuchMethodError var11) {
                  } catch (Exception var12) {
                     throw new IllegalArgumentException("Failed to set ItemModel for " + String.valueOf(var8.getType()), var12);
                  }
               }

               if (var1 != null) {
                  var9.setDisplayName(ColorUtil.translate(var1));
               }

               var9.setLore(ColorUtil.translate(var2));
               var8.setItemMeta(var9);
               return var8;
            }
         }
      }
   }

   public static boolean isBanner(@NotNull Material var0) {
      return var0.isBlock() && var0.name().endsWith("_BANNER") ? true : Bukkit.getItemFactory().getItemMeta(var0) instanceof BannerMeta;
   }

   public static boolean isHead(@NotNull Material var0) {
      return var0.isBlock() && var0.name().endsWith("_HEAD") && var0 != Material.PISTON_HEAD ? true : Bukkit.getItemFactory().getItemMeta(var0) instanceof SkullMeta;
   }

   @NotNull
   public static ItemStack setDisplayName(@NotNull ItemStack var0, @NotNull String var1) {
      ItemMeta var2 = var0.getItemMeta();
      if (var2 != null) {
         var2.setDisplayName(colorize(var1));
         var0.setItemMeta(var2);
      }

      return var0;
   }

   @NotNull
   public static ItemStack setLore(@NotNull ItemStack var0, @NotNull List<String> var1) {
      ItemMeta var2 = var0.getItemMeta();
      if (var2 != null) {
         var2.setLore(colorize(var1));
         var0.setItemMeta(var2);
      }

      return var0;
   }

   @NotNull
   public static ItemStack buildCustomItem(@NotNull Material var0, @Nullable Integer var1) {
      ItemStack var2 = new ItemStack(var0);
      if (var1 != null) {
         ItemMeta var3 = var2.getItemMeta();
         if (var3 == null) {
            return var2;
         }

         try {
            Method var4 = ItemMeta.class.getMethod("setCustomModelData", Integer.class);

            try {
               var4.setAccessible(true);
               var4.invoke(var3, var1);
            } catch (InvocationTargetException | IllegalAccessException var6) {
               var6.printStackTrace();
            }
         } catch (NoSuchMethodException var7) {
            var2.setDurability((short)var1);
         }

         var2.setItemMeta(var3);
      }

      return var2;
   }

   @NotNull
   public static ItemStack buildCustomItem(@NotNull Material var0, @Nullable Integer var1, @Nullable String var2, @Nullable List<String> var3, @Nullable ItemFlag... var4) {
      ItemStack var5 = buildCustomItem(var0, var1);
      ItemMeta var6 = var5.getItemMeta();
      if (var6 != null) {
         var6.setDisplayName(colorize(var2));
         var6.setLore(colorize(var3));
         if (var4 != null) {
            var6.addItemFlags(var4);
         }

         var5.setItemMeta(var6);
      }

      return var5;
   }

   public static boolean hasPersistentData(@NotNull ItemStack var0, @NotNull NamespacedKey var1, @NotNull PersistentDataType var2) {
      ItemMeta var3 = var0.getItemMeta();
      PersistentDataContainer var4 = var3 != null ? var3.getPersistentDataContainer() : null;
      return var4 != null && var4.has(var1, var2);
   }

   @Nullable
   public static <T, Z> Z getPersistentData(@NotNull ItemStack var0, @NotNull NamespacedKey var1, @NotNull PersistentDataType<T, Z> var2) {
      ItemMeta var3 = var0.getItemMeta();
      PersistentDataContainer var4 = var3 != null ? var3.getPersistentDataContainer() : null;
      return var4 != null ? var4.get(var1, var2) : null;
   }

   @NotNull
   public static <T, Z> ItemStack setPersistentData(@NotNull ItemStack var0, @NotNull NamespacedKey var1, @NotNull PersistentDataType<T, Z> var2, @NotNull Z var3) {
      ItemMeta var4 = var0.getItemMeta();
      if (var4 != null || (var4 = Bukkit.getItemFactory().getItemMeta(var0.getType())) != null) {
         var4.getPersistentDataContainer().set(var1, var2, var3);
         var0.setItemMeta(var4);
      }

      return var0;
   }

   public static <T, Z> boolean hasNamespacedKey(ItemStack var0, NamespacedKey var1, @NotNull PersistentDataType<T, Z> var2) {
      if (var0 == null) {
         return false;
      } else {
         ItemMeta var3 = var0.getItemMeta();
         if (var3 == null) {
            return false;
         } else {
            PersistentDataContainer var4 = var3.getPersistentDataContainer();
            return var4.has(var1, var2);
         }
      }
   }

   @Nullable
   public static String colorize(@Nullable String var0) {
      return var0 != null ? ChatColor.translateAlternateColorCodes('&', var0) : null;
   }

   @Nullable
   public static List<String> colorize(@Nullable List<String> var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.add(colorize(var3));
         }

         return var1;
      }
   }
}
