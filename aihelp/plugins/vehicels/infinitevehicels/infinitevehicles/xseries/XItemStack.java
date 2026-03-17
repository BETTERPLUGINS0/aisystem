package me.PM2.infinitevehicles.xseries;

import com.google.common.base.Enums;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.PM2.infinitevehicles.xseries.profiles.builder.XSkull;
import me.PM2.infinitevehicles.xseries.profiles.objects.Profileable;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Axolotl.Variant;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.bukkit.material.MaterialData;
import org.bukkit.material.SpawnEgg;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class XItemStack {
   private static final XMaterial DEFAULT_MATERIAL;
   private static final boolean SUPPORTS_UNBREAKABLE;
   private static final boolean SUPPORTS_POTION_COLOR;
   private static final boolean SUPPORTS_Inventory_getStorageContents;
   private static final boolean SUPPORTS_CUSTOM_MODEL_DATA;
   private static final boolean SUPPORTS_ADVANCED_CUSTOM_MODEL_DATA;
   private static final boolean SUPPORTS_ITEM_MODEL;
   private static final Map<Class<? extends ItemMeta>, Optional<Function<XItemStack.Deserializer, XItemStack.MetaHandler<ItemMeta>>>> DESERIALIZE_META_HANDLERS;
   private static final Map<Class<? extends ItemMeta>, Optional<Function<XItemStack.Serializer, XItemStack.MetaHandler<ItemMeta>>>> SERIALIZE_META_HANDLERS;

   private static <M extends ItemMeta> void meta(Class<? extends M> var0, Function<XItemStack.Deserializer, XItemStack.MetaHandler<M>> var1, Function<XItemStack.Serializer, XItemStack.MetaHandler<M>> var2) {
      DESERIALIZE_META_HANDLERS.put(var0, Optional.of((Function)cast(var1)));
      SERIALIZE_META_HANDLERS.put(var0, Optional.of((Function)cast(var2)));
   }

   private static void onlyIf(String var0, Runnable var1) {
      try {
         Class.forName("org.bukkit.inventory.meta." + var0);
         var1.run();
      } catch (ClassNotFoundException var3) {
      }

   }

   private static <T extends XItemStack.SerialObject> void recursiveMetaHandle(T var0, Class<?> var1, ItemMeta var2, Map<Class<? extends ItemMeta>, Optional<Function<T, XItemStack.MetaHandler<ItemMeta>>>> var3, List<Function<T, XItemStack.MetaHandler<ItemMeta>>> var4) {
      Optional var5 = (Optional)var3.get(var1);
      if (var5 != null) {
         if (var5.isPresent()) {
            if (var4 != null) {
               var4.add((Function)var5.get());
            }

            ((XItemStack.MetaHandler)((Function)var5.get()).apply(var0)).handle(var2);
         }

      } else {
         ArrayList var6 = new ArrayList();
         Class var7 = var1.getSuperclass();
         if (var7 != null) {
            recursiveMetaHandle(var0, var7, var2, var3, var6);
         }

         Class[] var8 = var1.getInterfaces();
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            Class var11 = var8[var10];
            recursiveMetaHandle(var0, var11, var2, var3, var6);
         }

         if (var6.isEmpty()) {
            var3.put(var1, Optional.empty());
         } else {
            var3.put(var1, Optional.of((var1x) -> {
               return (var2) -> {
                  XItemStack.SerialObject var3 = (XItemStack.SerialObject)cast(var1x);
                  Iterator var4 = var6.iterator();

                  while(var4.hasNext()) {
                     Function var5 = (Function)var4.next();
                     ((XItemStack.MetaHandler)var5.apply(var3)).handle(var2);
                  }

               };
            }));
            if (var4 != null) {
               var4.addAll(var6);
            }
         }

      }
   }

   private static <T> T cast(Object var0) {
      return var0;
   }

   private XItemStack() {
   }

   private static BlockState safeBlockState(BlockStateMeta var0) {
      try {
         return var0.getBlockState();
      } catch (IllegalStateException var2) {
         if (var2.getMessage().toLowerCase(Locale.ENGLISH).contains("missing blockstate")) {
            return null;
         } else {
            throw var2;
         }
      } catch (ClassCastException var3) {
         return null;
      }
   }

   public static void serialize(@NotNull ItemStack var0, @NotNull ConfigurationSection var1) {
      serialize(var0, var1, Function.identity());
   }

   public static void serialize(@NotNull ItemStack var0, @NotNull ConfigurationSection var1, @NotNull Function<String, String> var2) {
      (new XItemStack.Serializer(var0, var1, var2)).handle();
   }

   public static Map<String, Object> serialize(@NotNull ItemStack var0) {
      Objects.requireNonNull(var0, "Cannot serialize a null item");
      MemoryConfiguration var1 = new MemoryConfiguration();
      serialize(var0, var1);
      return configSectionToMap(var1);
   }

   @NotNull
   public static ItemStack deserialize(@NotNull ConfigurationSection var0) {
      return edit(DEFAULT_MATERIAL.parseItem(), var0, Function.identity(), (Consumer)null);
   }

   @NotNull
   public static ItemStack deserialize(@NotNull Map<String, Object> var0) {
      Objects.requireNonNull(var0, "serializedItem cannot be null.");
      return deserialize(mapToConfigSection(var0));
   }

   @NotNull
   public static ItemStack deserialize(@NotNull ConfigurationSection var0, @NotNull Function<String, String> var1) {
      return deserialize(var0, var1, (Consumer)null);
   }

   @NotNull
   public static ItemStack deserialize(@NotNull ConfigurationSection var0, @NotNull Function<String, String> var1, @Nullable Consumer<Exception> var2) {
      return edit(DEFAULT_MATERIAL.parseItem(), var0, var1, var2);
   }

   @NotNull
   public static ItemStack deserialize(@NotNull Map<String, Object> var0, @NotNull Function<String, String> var1) {
      Objects.requireNonNull(var0, "serializedItem cannot be null.");
      Objects.requireNonNull(var1, "translator cannot be null.");
      return deserialize(mapToConfigSection(var0), var1);
   }

   private static int toInt(String var0, int var1) {
      try {
         return Integer.parseInt(var0);
      } catch (NumberFormatException var3) {
         return var1;
      }
   }

   private static List<String> split(@NotNull String var0, char var1) {
      ArrayList var2 = new ArrayList(5);
      boolean var3 = false;
      boolean var4 = false;
      int var5 = var0.length();
      int var6 = 0;

      for(int var7 = 0; var7 < var5; ++var7) {
         if (var0.charAt(var7) == var1) {
            if (var3) {
               var2.add(var0.substring(var6, var7));
               var3 = false;
               var4 = true;
            }

            var6 = var7 + 1;
         } else {
            var4 = false;
            var3 = true;
         }
      }

      if (var3 || var4) {
         var2.add(var0.substring(var6, var5));
      }

      return var2;
   }

   private static List<String> splitNewLine(String var0) {
      int var1 = var0.length();
      ArrayList var2 = new ArrayList();
      int var3 = 0;
      int var4 = 0;
      boolean var5 = false;
      boolean var6 = false;

      while(var3 < var1) {
         if (var0.charAt(var3) == '\n') {
            if (var5) {
               var2.add(var0.substring(var4, var3));
               var5 = false;
               var6 = true;
            }

            ++var3;
            var4 = var3;
         } else {
            var6 = false;
            var5 = true;
            ++var3;
         }
      }

      if (var5 || var6) {
         var2.add(var0.substring(var4, var3));
      }

      return var2;
   }

   @NotNull
   public static ItemStack edit(@NotNull ItemStack var0, @NotNull ConfigurationSection var1, @NotNull Function<String, String> var2, @Nullable Consumer<Exception> var3) {
      return (new XItemStack.Deserializer(var0, var1, var2, var3)).parse();
   }

   @NotNull
   private static ConfigurationSection mapToConfigSection(@NotNull Map<?, ?> var0) {
      MemoryConfiguration var1 = new MemoryConfiguration();
      Iterator var2 = var0.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         String var4 = var3.getKey().toString();
         Object var5 = var3.getValue();
         if (var5 != null) {
            if (var5 instanceof Map) {
               var5 = mapToConfigSection((Map)var5);
            }

            var1.set(var4, var5);
         }
      }

      return var1;
   }

   @NotNull
   private static Map<String, Object> configSectionToMap(@NotNull ConfigurationSection var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      Iterator var2 = var0.getKeys(false).iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Object var4 = var0.get(var3);
         if (var4 != null) {
            if (var4 instanceof ConfigurationSection) {
               var4 = configSectionToMap((ConfigurationSection)var4);
            }

            var1.put(var3, var4);
         }
      }

      return var1;
   }

   @NotNull
   @Internal
   public static Optional<Color> parseColor(@Nullable String var0) {
      if (Strings.isNullOrEmpty(var0)) {
         return Optional.empty();
      } else {
         List var1 = split(var0.replace(" ", ""), ',');
         if (var1.size() == 3) {
            return Optional.of(Color.fromRGB(toInt((String)var1.get(0), 0), toInt((String)var1.get(1), 0), toInt((String)var1.get(2), 0)));
         } else {
            try {
               return Optional.of(Color.fromRGB(Integer.parseInt(var0)));
            } catch (NumberFormatException var4) {
               if (var0.startsWith("#")) {
                  var0 = var0.substring(1);
               }

               try {
                  return Optional.of(Color.fromRGB(Integer.parseInt(var0, 16)));
               } catch (NumberFormatException var3) {
                  return Optional.empty();
               }
            }
         }
      }
   }

   private static <T> List<T> parseRawOrList(String var0, String var1, ConfigurationSection var2, Function<String, T> var3) {
      List var4 = var2.getStringList(var1);
      if (!var4.isEmpty()) {
         return (List)var4.stream().map(var3).collect(Collectors.toList());
      } else {
         var4 = var2.getStringList(var0);
         if (!var4.isEmpty()) {
            return (List)var4.stream().map(var3).collect(Collectors.toList());
         } else {
            String var5 = var2.getString(var0);
            if (var5 != null && !var5.isEmpty()) {
               return Collections.singletonList(var3.apply(var5));
            } else {
               var5 = var2.getString(var1);
               return var5 != null && !var5.isEmpty() ? Collections.singletonList(var3.apply(var5)) : Collections.emptyList();
            }
         }
      }
   }

   private static <T> T tryNumber(String var0, Function<String, T> var1) {
      try {
         return var1.apply(var0);
      } catch (NumberFormatException var3) {
         return null;
      }
   }

   @NotNull
   @Contract(
      mutates = "param1"
   )
   public static List<ItemStack> giveOrDrop(@NotNull Player var0, @Nullable ItemStack... var1) {
      return giveOrDrop(var0, true, var1);
   }

   @NotNull
   @Contract(
      mutates = "param1"
   )
   public static List<ItemStack> giveOrDrop(@NotNull Player var0, boolean var1, @Nullable ItemStack... var2) {
      if (var2 != null && var2.length != 0) {
         List var3 = addItems(var0.getInventory(), var1, var2);
         World var4 = var0.getWorld();
         Location var5 = var0.getLocation();
         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            ItemStack var7 = (ItemStack)var6.next();
            var4.dropItemNaturally(var5, var7);
         }

         return var3;
      } else {
         return new ArrayList();
      }
   }

   @Contract(
      mutates = "param1"
   )
   public static List<ItemStack> addItems(@NotNull Inventory var0, boolean var1, @NotNull ItemStack... var2) {
      return addItems(var0, var1, (Predicate)null, var2);
   }

   @NotNull
   @Contract(
      mutates = "param1"
   )
   public static List<ItemStack> addItems(@NotNull Inventory var0, boolean var1, @Nullable Predicate<Integer> var2, @NotNull ItemStack... var3) {
      Objects.requireNonNull(var0, "Cannot add items to null inventory");
      Objects.requireNonNull(var3, "Cannot add null items to inventory");
      ArrayList var4 = new ArrayList(var3.length);
      int var5 = getStorageContents(var0).length;
      int var6 = 0;
      ItemStack[] var7 = var3;
      int var8 = var3.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         ItemStack var10 = var7[var9];
         int var11 = 0;
         int var12 = var1 ? var10.getMaxStackSize() : var0.getMaxStackSize();

         while(true) {
            int var13 = var11 >= var5 ? -1 : firstPartial(var0, var10, var11, var2);
            if (var13 == -1) {
               if (var6 != -1) {
                  var6 = firstEmpty(var0, var6, var2);
               }

               if (var6 == -1) {
                  var4.add(var10);
                  break;
               }

               var11 = Integer.MAX_VALUE;
               int var14 = var10.getAmount();
               if (var14 <= var12) {
                  var0.setItem(var6, var10);
                  break;
               }

               ItemStack var15 = var10.clone();
               var15.setAmount(var12);
               var0.setItem(var6, var15);
               var10.setAmount(var14 - var12);
               ++var6;
               if (var6 == var5) {
                  var6 = -1;
               }
            } else {
               ItemStack var16 = var0.getItem(var13);
               int var17 = var10.getAmount() + var16.getAmount();
               if (var17 <= var12) {
                  var16.setAmount(var17);
                  var0.setItem(var13, var16);
                  break;
               }

               var16.setAmount(var12);
               var0.setItem(var13, var16);
               var10.setAmount(var17 - var12);
               var11 = var13 + 1;
            }
         }
      }

      return var4;
   }

   @NotNull
   @Contract(
      pure = true
   )
   @Range(
      from = -1L,
      to = 2147483647L
   )
   public static int firstPartial(@NotNull Inventory var0, @Nullable ItemStack var1, int var2) {
      return firstPartial(var0, var1, var2, (Predicate)null);
   }

   @NotNull
   @Contract(
      pure = true
   )
   @Range(
      from = -1L,
      to = 2147483647L
   )
   public static int firstPartial(@NotNull Inventory var0, @Nullable ItemStack var1, int var2, @Nullable Predicate<Integer> var3) {
      if (var1 != null) {
         ItemStack[] var4 = getStorageContents(var0);
         int var5 = var4.length;
         if (var2 < 0 || var2 >= var5) {
            throw new IndexOutOfBoundsException("Begin Index: " + var2 + ", Inventory storage content size: " + var5);
         }

         for(; var2 < var5; ++var2) {
            if (var3 == null || var3.test(var2)) {
               ItemStack var6 = var4[var2];
               if (var6 != null && var6.getAmount() < var6.getMaxStackSize() && var6.isSimilar(var1)) {
                  return var2;
               }
            }
         }
      }

      return -1;
   }

   @NotNull
   @Contract(
      pure = true
   )
   public static List<ItemStack> stack(@NotNull Collection<ItemStack> var0) {
      return stack(var0, ItemStack::isSimilar);
   }

   @NotNull
   @Contract(
      pure = true
   )
   public static List<ItemStack> stack(@NotNull Collection<ItemStack> var0, @NotNull BiPredicate<ItemStack, ItemStack> var1) {
      Objects.requireNonNull(var0, "Cannot stack null items");
      Objects.requireNonNull(var1, "Similarity check cannot be null");
      ArrayList var2 = new ArrayList(var0.size());
      Iterator var3 = var0.iterator();

      while(true) {
         ItemStack var4;
         do {
            do {
               if (!var3.hasNext()) {
                  return var2;
               }

               var4 = (ItemStack)var3.next();
            } while(var4 == null);
         } while(var4.getType() == Material.AIR);

         boolean var5 = true;
         Iterator var6 = var2.iterator();

         while(var6.hasNext()) {
            ItemStack var7 = (ItemStack)var6.next();
            if (var1.test(var4, var7)) {
               var7.setAmount(var7.getAmount() + var4.getAmount());
               var5 = false;
               break;
            }
         }

         if (var5) {
            var2.add(var4.clone());
         }
      }
   }

   @Contract(
      pure = true
   )
   @Range(
      from = -1L,
      to = 2147483647L
   )
   public static int firstEmpty(@NotNull Inventory var0, int var1) {
      return firstEmpty(var0, var1, (Predicate)null);
   }

   @Contract(
      pure = true
   )
   @Range(
      from = -1L,
      to = 2147483647L
   )
   public static int firstEmpty(@NotNull Inventory var0, int var1, @Nullable Predicate<Integer> var2) {
      ItemStack[] var3 = getStorageContents(var0);
      int var4 = var3.length;
      if (var1 >= 0 && var1 < var4) {
         while(var1 < var4) {
            if ((var2 == null || var2.test(var1)) && var3[var1] == null) {
               return var1;
            }

            ++var1;
         }

         return -1;
      } else {
         throw new IndexOutOfBoundsException("Begin Index: " + var1 + ", Inventory storage content size: " + var4);
      }
   }

   @Contract(
      pure = true
   )
   @Range(
      from = -1L,
      to = 2147483647L
   )
   public static int firstPartialOrEmpty(@NotNull Inventory var0, @Nullable ItemStack var1, int var2) {
      if (var1 != null) {
         ItemStack[] var3 = getStorageContents(var0);
         int var4 = var3.length;
         if (var2 < 0 || var2 >= var4) {
            throw new IndexOutOfBoundsException("Begin Index: " + var2 + ", Size: " + var4);
         }

         while(var2 < var4) {
            ItemStack var5 = var3[var2];
            if (var5 == null || var5.getAmount() < var5.getMaxStackSize() && var5.isSimilar(var1)) {
               return var2;
            }

            ++var2;
         }
      }

      return -1;
   }

   @Contract(
      pure = true
   )
   public static ItemStack[] getStorageContents(Inventory var0) {
      return SUPPORTS_Inventory_getStorageContents ? var0.getStorageContents() : (ItemStack[])Arrays.copyOfRange(var0.getContents(), 0, 36);
   }

   @Contract(
      pure = true
   )
   public static boolean notEmpty(@Nullable ItemStack var0) {
      return !isEmpty(var0);
   }

   @Contract(
      pure = true
   )
   public static boolean isEmpty(@Nullable ItemStack var0) {
      return var0 == null || var0.getType() == Material.AIR;
   }

   static {
      DEFAULT_MATERIAL = XMaterial.BARRIER;
      boolean var0 = false;
      boolean var1 = false;
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;

      try {
         ItemMeta.class.getDeclaredMethod("setUnbreakable", Boolean.TYPE);
         var1 = true;
      } catch (NoSuchMethodException var12) {
      }

      try {
         ItemMeta.class.getDeclaredMethod("hasCustomModelData");
         var3 = true;
      } catch (NoSuchMethodException var11) {
      }

      try {
         Class.forName("org.bukkit.inventory.meta.components.CustomModelDataComponent");
         var4 = true;
      } catch (ClassNotFoundException var10) {
      }

      try {
         ItemMeta.class.getDeclaredMethod("getItemModel");
         var5 = true;
      } catch (NoSuchMethodException var9) {
      }

      try {
         Class.forName("org.bukkit.inventory.meta.PotionMeta").getMethod("setColor", Color.class);
         var0 = true;
      } catch (Throwable var8) {
      }

      try {
         Inventory.class.getDeclaredMethod("getStorageContents");
         var2 = true;
      } catch (NoSuchMethodException var7) {
      }

      SUPPORTS_POTION_COLOR = var0;
      SUPPORTS_UNBREAKABLE = var1;
      SUPPORTS_Inventory_getStorageContents = var2;
      SUPPORTS_CUSTOM_MODEL_DATA = var3;
      SUPPORTS_ADVANCED_CUSTOM_MODEL_DATA = var4;
      SUPPORTS_ITEM_MODEL = var5;
      DESERIALIZE_META_HANDLERS = new IdentityHashMap();
      SERIALIZE_META_HANDLERS = new IdentityHashMap();
      meta(SkullMeta.class, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleSkullMeta(var1);
         };
      }, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleSkullMeta(var1);
         };
      });
      meta(LeatherArmorMeta.class, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleLeatherArmorMeta(var1);
         };
      }, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleLeatherArmorMeta(var1);
         };
      });
      meta(PotionMeta.class, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handlePotionMeta(var1);
         };
      }, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handlePotionMeta(var1);
         };
      });
      meta(BlockStateMeta.class, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleBlockStateMeta(var1);
         };
      }, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleBlockStateMeta(var1);
         };
      });
      meta(FireworkMeta.class, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleFireworkMeta(var1);
         };
      }, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleFireworkMeta(var1);
         };
      });
      meta(BookMeta.class, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleBookMeta(var1);
         };
      }, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleBookMeta(var1);
         };
      });
      meta(BannerMeta.class, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleBannerMeta(var1);
         };
      }, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleBannerMeta(var1);
         };
      });
      meta(MapMeta.class, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleMapMeta(var1);
         };
      }, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleMapMeta(var1);
         };
      });
      meta(EnchantmentStorageMeta.class, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleEnchantmentStorageMeta(var1);
         };
      }, (var0x) -> {
         Objects.requireNonNull(var0x);
         return (var1) -> {
            var0x.handleEnchantmentStorageMeta(var1);
         };
      });
      onlyIf("SpawnEggMeta", () -> {
         meta(SpawnEggMeta.class, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleSpawnEggMeta(var1);
            };
         }, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleSpawnEggMeta(var1);
            };
         });
      });
      onlyIf("ArmorMeta", () -> {
         meta(ArmorMeta.class, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleArmorMeta(var1);
            };
         }, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleArmorMeta(var1);
            };
         });
      });
      onlyIf("AxolotlBucketMeta", () -> {
         meta(AxolotlBucketMeta.class, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleAxolotlBucketMeta(var1);
            };
         }, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleAxolotlBucketMeta(var1);
            };
         });
      });
      onlyIf("CompassMeta", () -> {
         meta(CompassMeta.class, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleCompassMeta(var1);
            };
         }, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleCompassMeta(var1);
            };
         });
      });
      onlyIf("SuspiciousStewMeta", () -> {
         meta(SuspiciousStewMeta.class, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleSuspiciousStewMeta(var1);
            };
         }, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleSuspiciousStewMeta(var1);
            };
         });
      });
      onlyIf("CrossbowMeta", () -> {
         meta(CrossbowMeta.class, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleCrossbowMeta(var1);
            };
         }, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleCrossbowMeta(var1);
            };
         });
      });
      onlyIf("TropicalFishBucketMeta", () -> {
         meta(TropicalFishBucketMeta.class, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleTropicalFishBucketMeta(var1);
            };
         }, (var0) -> {
            Objects.requireNonNull(var0);
            return (var1) -> {
               var0.handleTropicalFishBucketMeta(var1);
            };
         });
      });
   }

   private abstract static class SerialObject {
      @NotNull
      protected final ItemStack item;
      @NotNull
      protected final ConfigurationSection config;
      @NotNull
      protected final Function<String, String> translator;
      protected ItemMeta meta;

      private SerialObject(ItemStack var1, @NotNull ConfigurationSection var2, @NotNull Function<String, String> var3) {
         this.item = (ItemStack)Objects.requireNonNull(var1, "Cannot operate on null ItemStack, considering using an AIR ItemStack instead");
         this.config = (ConfigurationSection)Objects.requireNonNull(var2, "Cannot deserialize item to a null configuration section.");
         this.translator = (Function)Objects.requireNonNull(var3, "Translator function cannot be null");
      }

      // $FF: synthetic method
      SerialObject(ItemStack var1, ConfigurationSection var2, Function var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private interface MetaHandler<M extends ItemMeta> {
      void handle(M var1);
   }

   private static final class Serializer extends XItemStack.SerialObject {
      private Serializer(ItemStack var1, @NotNull ConfigurationSection var2, @NotNull Function<String, String> var3) {
         super(var1, var2, var3, null);
      }

      public void handle() {
         this.config.set("material", XMaterial.matchXMaterial(this.item).name());
         if (this.item.getAmount() > 1) {
            this.config.set("amount", this.item.getAmount());
         }

         if (this.item.hasItemMeta()) {
            this.meta = this.item.getItemMeta();
            if (this.meta != null) {
               this.handleDurability(this.meta);
               if (this.meta.hasDisplayName()) {
                  this.config.set("name", this.translator.apply(this.meta.getDisplayName()));
               }

               if (this.meta.hasLore()) {
                  this.config.set("lore", this.meta.getLore().stream().map(this.translator).collect(Collectors.toList()));
               }

               this.customModelData();
               if (XItemStack.SUPPORTS_UNBREAKABLE && this.meta.isUnbreakable()) {
                  this.config.set("unbreakable", true);
               }

               this.handleEnchants();
               this.handleItemFlags(this.meta);
               this.handleAttributes(this.meta);
               this.legacySpawnEgg();
               XItemStack.recursiveMetaHandle(this, this.meta.getClass(), this.meta, XItemStack.SERIALIZE_META_HANDLERS, (List)null);
            }
         }
      }

      private void customModelData() {
         if (XItemStack.SUPPORTS_ITEM_MODEL) {
            String var1 = this.config.getString("item-model");
            if (var1 != null && !var1.isEmpty()) {
               this.meta.setItemModel(NamespacedKey.fromString(var1));
            }
         }

         if (XItemStack.SUPPORTS_CUSTOM_MODEL_DATA) {
            if (XItemStack.SUPPORTS_ADVANCED_CUSTOM_MODEL_DATA && this.meta.hasCustomModelDataComponent()) {
               CustomModelDataComponent var8 = this.meta.getCustomModelDataComponent();
               List var2 = var8.getStrings();
               List var3 = var8.getFloats();
               List var4 = var8.getFlags();
               List var5 = var8.getColors();
               int var6 = (int)Stream.of(var2, var3, var4, var5).filter((var0) -> {
                  return !var0.isEmpty();
               }).count();
               if (var6 == 0) {
                  return;
               }

               if (var6 == 1) {
                  if (!var2.isEmpty()) {
                     this.config.set("custom-model-data", singleOrList(var2));
                  }

                  if (!var3.isEmpty()) {
                     this.config.set("custom-model-data", singleOrList(var3));
                  }

                  if (!var4.isEmpty()) {
                     this.config.set("custom-model-data", singleOrList(var4));
                  }

                  if (!var5.isEmpty()) {
                     this.config.set("custom-model-data", singleOrList((List)var5.stream().map(XItemStack.Serializer::colorString).collect(Collectors.toList())));
                  }
               } else {
                  ConfigurationSection var7 = this.config.createSection("custom-model-data");
                  if (!var2.isEmpty()) {
                     var7.set("strings", var2);
                  }

                  if (!var3.isEmpty()) {
                     var7.set("floats", var3);
                  }

                  if (!var4.isEmpty()) {
                     var7.set("flags", var4);
                  }

                  if (!var5.isEmpty()) {
                     var7.set("colors", var5.stream().map(XItemStack.Serializer::colorString).collect(Collectors.toList()));
                  }
               }
            } else if (this.meta.hasCustomModelData()) {
               this.config.set("custom-model-data", this.meta.getCustomModelData());
            }
         }

      }

      private static <T> Object singleOrList(List<T> var0) {
         return var0.size() == 1 ? var0.get(0) : var0;
      }

      private void legacySpawnEgg() {
         if (!XMaterial.supports(11)) {
            MaterialData var1 = this.item.getData();
            if (var1 instanceof SpawnEgg) {
               SpawnEgg var2 = (SpawnEgg)var1;
               this.config.set("creature", var2.getSpawnedType().getName());
            }
         }

      }

      private void handleSpawnEggMeta(SpawnEggMeta var1) {
         this.config.set("creature", var1.getSpawnedType().getName());
      }

      private void handleSuspiciousStewMeta(SuspiciousStewMeta var1) {
         List var2 = var1.getCustomEffects();
         ArrayList var3 = new ArrayList(var2.size());
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            PotionEffect var5 = (PotionEffect)var4.next();
            var3.add(XPotion.of(var5.getType()).name() + ", " + var5.getDuration() + ", " + var5.getAmplifier());
         }

         this.config.set("effects", var3);
      }

      private void handleTropicalFishBucketMeta(TropicalFishBucketMeta var1) {
         this.config.set("pattern", var1.getPattern().name());
         this.config.set("color", var1.getBodyColor().name());
         this.config.set("pattern-color", var1.getPatternColor().name());
      }

      private void handleCrossbowMeta(CrossbowMeta var1) {
         int var2 = 0;

         for(Iterator var3 = var1.getChargedProjectiles().iterator(); var3.hasNext(); ++var2) {
            ItemStack var4 = (ItemStack)var3.next();
            XItemStack.serialize(var4, this.config.getConfigurationSection("projectiles." + var2), this.translator);
         }

      }

      private void handleCompassMeta(CompassMeta var1) {
         ConfigurationSection var2 = this.config.createSection("lodestone");
         var2.set("tracked", var1.isLodestoneTracked());
         if (var1.hasLodestone()) {
            Location var3 = var1.getLodestone();
            var2.set("location.world", var3.getWorld().getName());
            var2.set("location.x", var3.getX());
            var2.set("location.y", var3.getY());
            var2.set("location.z", var3.getZ());
         }

      }

      private void handleAxolotlBucketMeta(AxolotlBucketMeta var1) {
         if (var1.hasVariant()) {
            this.config.set("color", var1.getVariant().toString());
         }

      }

      private void handleArmorMeta(ArmorMeta var1) {
         if (var1.hasTrim()) {
            ArmorTrim var2 = var1.getTrim();
            ConfigurationSection var3 = this.config.createSection("trim");
            var3.set("material", var2.getMaterial().getKey().getNamespace() + ':' + var2.getMaterial().getKey().getKey());
            var3.set("pattern", var2.getPattern().getKey().getNamespace() + ':' + var2.getPattern().getKey().getKey());
         }

      }

      private void handleMapMeta(MapMeta var1) {
         ConfigurationSection var2 = this.config.createSection("map");
         var2.set("scaling", var1.isScaling());
         if (XMaterial.supports(11)) {
            if (var1.hasLocationName()) {
               var2.set("location", var1.getLocationName());
            }

            if (var1.hasColor()) {
               Color var3 = var1.getColor();
               var2.set("color", colorString(var3));
            }
         }

         if (XMaterial.supports(14) && var1.hasMapView()) {
            MapView var6 = var1.getMapView();
            ConfigurationSection var4 = var2.createSection("view");
            var4.set("scale", var6.getScale().toString());
            var4.set("world", var6.getWorld().getName());
            ConfigurationSection var5 = var4.createSection("center");
            var5.set("x", var6.getCenterX());
            var5.set("z", var6.getCenterZ());
            var4.set("locked", var6.isLocked());
            var4.set("tracking-position", var6.isTrackingPosition());
            var4.set("unlimited-tracking", var6.isUnlimitedTracking());
         }

      }

      private void handleBookMeta(BookMeta var1) {
         if (var1.getTitle() != null || var1.getAuthor() != null || var1.getGeneration() != null || !var1.getPages().isEmpty()) {
            ConfigurationSection var2 = this.config.createSection("book");
            if (var1.getTitle() != null) {
               var2.set("title", var1.getTitle());
            }

            if (var1.getAuthor() != null) {
               var2.set("author", var1.getAuthor());
            }

            if (XMaterial.supports(9)) {
               Generation var3 = var1.getGeneration();
               if (var3 != null) {
                  var2.set("generation", var1.getGeneration().toString());
               }
            }

            if (!var1.getPages().isEmpty()) {
               var2.set("pages", var1.getPages());
            }
         }

      }

      private void handleFireworkMeta(FireworkMeta var1) {
         this.config.set("power", var1.getPower());
         int var2 = 0;

         for(Iterator var3 = var1.getEffects().iterator(); var3.hasNext(); ++var2) {
            FireworkEffect var4 = (FireworkEffect)var3.next();
            this.config.set("firework." + var2 + ".type", var4.getType().name());
            ConfigurationSection var5 = this.config.getConfigurationSection("firework." + var2);
            var5.set("flicker", var4.hasFlicker());
            var5.set("trail", var4.hasTrail());
            List var6 = var4.getColors();
            List var7 = var4.getFadeColors();
            ArrayList var8 = new ArrayList(var6.size());
            ArrayList var9 = new ArrayList(var7.size());
            ConfigurationSection var10 = var5.createSection("colors");
            Iterator var11 = var6.iterator();

            Color var12;
            while(var11.hasNext()) {
               var12 = (Color)var11.next();
               var8.add(colorString(var12));
            }

            var10.set("base", var8);
            var11 = var7.iterator();

            while(var11.hasNext()) {
               var12 = (Color)var11.next();
               var9.add(colorString(var12));
            }

            var10.set("fade", var9);
         }

      }

      @NotNull
      private static String colorString(Color var0) {
         return var0.getRed() + ", " + var0.getGreen() + ", " + var0.getBlue();
      }

      private void handlePotionMeta(PotionMeta var1) {
         if (XMaterial.supports(9)) {
            List var2 = var1.getCustomEffects();
            ArrayList var3 = new ArrayList(var2.size());
            Iterator var4 = var2.iterator();

            while(var4.hasNext()) {
               PotionEffect var5 = (PotionEffect)var4.next();
               var3.add(var5.getType().getName() + ", " + var5.getDuration() + ", " + var5.getAmplifier());
            }

            if (!var3.isEmpty()) {
               this.config.set("effects", var3);
            }

            PotionType var6 = var1.getBasePotionType();
            this.config.set("base-type", var6.name());
            this.config.set("effects", var1.getCustomEffects().stream().map((var0) -> {
               NamespacedKey var1 = var0.getType().getKey();
               String var2 = var1.getNamespace() + ':' + var1.getKey();
               return var2 + ", " + var0.getDuration() + ", " + var0.getAmplifier();
            }).collect(Collectors.toList()));
            if (XItemStack.SUPPORTS_POTION_COLOR && var1.hasColor()) {
               this.config.set("color", var1.getColor().asRGB());
            }
         }

      }

      private void handleLeatherArmorMeta(LeatherArmorMeta var1) {
         Color var2 = var1.getColor();
         this.config.set("color", colorString(var2));
      }

      private void handleBannerMeta(BannerMeta var1) {
         ConfigurationSection var2 = this.config.createSection("patterns");
         Iterator var3 = var1.getPatterns().iterator();

         while(var3.hasNext()) {
            Pattern var4 = (Pattern)var3.next();
            var2.set(XPatternType.of(var4.getPattern()).name(), var4.getColor().name());
         }

      }

      private void handleSkullMeta(ItemMeta var1) {
         String var2 = XSkull.of(var1).getProfileValue();
         if (var2 != null) {
            this.config.set("skull", var2);
         }

      }

      private void handleEnchantmentStorageMeta(EnchantmentStorageMeta var1) {
         Iterator var2 = var1.getStoredEnchants().entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            String var4 = "stored-enchants." + XEnchantment.of((Enchantment)var3.getKey()).name();
            this.config.set(var4, var3.getValue());
         }

      }

      private void handleBlockStateMeta(BlockStateMeta var1) {
         BlockState var2 = XItemStack.safeBlockState(var1);
         if (XMaterial.supports(11) && var2 instanceof ShulkerBox) {
            ShulkerBox var10 = (ShulkerBox)var2;
            ConfigurationSection var4 = this.config.createSection("contents");
            int var5 = 0;
            ItemStack[] var6 = var10.getInventory().getContents();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               ItemStack var9 = var6[var8];
               if (var9 != null) {
                  XItemStack.serialize(var9, var4.createSection(Integer.toString(var5)), this.translator);
               }

               ++var5;
            }
         } else if (var2 instanceof CreatureSpawner) {
            CreatureSpawner var3 = (CreatureSpawner)var2;
            if (var3.getSpawnedType() != null) {
               this.config.set("spawner", var3.getSpawnedType().name());
            }
         }

      }

      private void handleAttributes(ItemMeta var1) {
         if (XMaterial.supports(13)) {
            Multimap var2 = var1.getAttributeModifiers();
            if (var2 != null) {
               Iterator var3 = var2.entries().iterator();

               while(var3.hasNext()) {
                  Entry var4 = (Entry)var3.next();
                  String var5 = "attributes." + XAttribute.of((Attribute)var4.getKey()).name() + '.';
                  AttributeModifier var6 = (AttributeModifier)var4.getValue();
                  this.config.set(var5 + "name", var6.getName());
                  this.config.set(var5 + "amount", var6.getAmount());
                  this.config.set(var5 + "operation", var6.getOperation().name());
                  if (var6.getSlot() != null) {
                     this.config.set(var5 + "slot", var6.getSlot().name());
                  }
               }
            }
         }

      }

      private void handleItemFlags(ItemMeta var1) {
         if (!var1.getItemFlags().isEmpty()) {
            Set var2 = var1.getItemFlags();
            ArrayList var3 = new ArrayList(var2.size());
            Iterator var4 = var2.iterator();

            while(var4.hasNext()) {
               ItemFlag var5 = (ItemFlag)var4.next();
               var3.add(var5.name());
            }

            this.config.set("flags", var3);
         }

      }

      private void handleEnchants() {
         Iterator var1 = this.meta.getEnchants().entrySet().iterator();

         while(var1.hasNext()) {
            Entry var2 = (Entry)var1.next();
            String var3 = "enchants." + XEnchantment.of((Enchantment)var2.getKey()).name();
            this.config.set(var3, var2.getValue());
         }

      }

      private void handleDurability(ItemMeta var1) {
         if (XMaterial.supports(13)) {
            if (var1 instanceof Damageable) {
               Damageable var2 = (Damageable)var1;
               if (var2.hasDamage()) {
                  this.config.set("damage", var2.getDamage());
               }
            }
         } else {
            this.config.set("damage", this.item.getDurability());
         }

      }

      // $FF: synthetic method
      Serializer(ItemStack var1, ConfigurationSection var2, Function var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static final class Deserializer extends XItemStack.SerialObject {
      @Nullable
      private final Consumer<Exception> restart;
      private static final boolean SPACE_EMPTY_LORE_LINES = true;

      private Deserializer(ItemStack var1, @NotNull ConfigurationSection var2, @NotNull Function<String, String> var3, @Nullable Consumer<Exception> var4) {
         super(var1, var2, var3, null);
         this.restart = var4;
      }

      public ItemStack parse() {
         this.handleMaterial();
         this.handleDamage();
         this.getOrCreateMeta();
         this.handleDurability();
         this.displayName();
         this.unbreakable();
         this.customModelData();
         this.lore();
         this.enchants();
         this.itemFlags();
         this.attributes();
         this.legacySpawnEgg();
         XItemStack.recursiveMetaHandle(this, this.meta.getClass(), this.meta, XItemStack.DESERIALIZE_META_HANDLERS, (List)null);
         this.item.setItemMeta(this.meta);
         return this.item;
      }

      private void attributes() {
         if (XMaterial.supports(13)) {
            ConfigurationSection var1 = this.config.getConfigurationSection("attributes");
            if (var1 != null) {
               Iterator var2 = var1.getKeys(false).iterator();

               while(var2.hasNext()) {
                  String var3 = (String)var2.next();
                  Optional var4 = XAttribute.of(var3);
                  if (var4.isPresent() && ((XAttribute)var4.get()).isSupported()) {
                     ConfigurationSection var5 = var1.getConfigurationSection(var3);
                     if (var5 != null) {
                        EquipmentSlot var6 = var5.getString("slot") != null ? (EquipmentSlot)Enums.getIfPresent(EquipmentSlot.class, var5.getString("slot")).or(EquipmentSlot.HAND) : null;
                        String var7 = var5.getString("name");
                        if (var7 == null) {
                           var7 = UUID.randomUUID().toString().toLowerCase(Locale.ENGLISH);
                        }

                        AttributeModifier var8 = XAttribute.createModifier(var7, var5.getDouble("amount"), (Operation)Enums.getIfPresent(Operation.class, var5.getString("operation")).or(Operation.ADD_NUMBER), var6);
                        this.meta.addAttributeModifier((Attribute)((XAttribute)var4.get()).get(), var8);
                     }
                  }
               }
            }

            if (!this.meta.getItemFlags().isEmpty() && XReflection.supports(1, 20, 6) && !this.meta.hasAttributeModifiers()) {
               this.meta.addAttributeModifier((Attribute)XAttribute.ATTACK_DAMAGE.get(), XAttribute.createModifier("xseries:itemflagdummy", 0.0D, Operation.MULTIPLY_SCALAR_1, (EquipmentSlot)null));
            }

         }
      }

      private void legacySpawnEgg() {
         if (!XMaterial.supports(11)) {
            MaterialData var1 = this.item.getData();
            if (var1 instanceof SpawnEgg) {
               String var2 = this.config.getString("creature");
               if (!Strings.isNullOrEmpty(var2)) {
                  SpawnEgg var3 = (SpawnEgg)var1;
                  com.google.common.base.Optional var4 = Enums.getIfPresent(EntityType.class, var2.toUpperCase(Locale.ENGLISH));
                  if (var4.isPresent()) {
                     var3.setSpawnedType((EntityType)var4.get());
                  }

                  this.item.setData(var1);
               }
            }
         }

      }

      private void unbreakable() {
         if (XItemStack.SUPPORTS_UNBREAKABLE && this.config.isSet("unbreakable")) {
            this.meta.setUnbreakable(this.config.getBoolean("unbreakable"));
         }

      }

      private void customModelData() {
         if (XItemStack.SUPPORTS_ITEM_MODEL && this.meta.hasItemModel()) {
            this.config.set("item-model", this.meta.getItemModel().toString());
         }

         if (XItemStack.SUPPORTS_ADVANCED_CUSTOM_MODEL_DATA) {
            CustomModelDataComponent var1 = this.meta.getCustomModelDataComponent();
            ConfigurationSection var2 = this.config.getConfigurationSection("custom-model-data");
            if (var2 != null) {
               var1.setStrings(XItemStack.parseRawOrList("string", "strings", var2, (var0) -> {
                  return var0;
               }));
               var1.setFlags(XItemStack.parseRawOrList("flag", "flags", var2, Boolean::parseBoolean));
               var1.setFloats(XItemStack.parseRawOrList("float", "floats", var2, Float::parseFloat));
               var1.setColors(XItemStack.parseRawOrList("color", "colors", var2, (var0) -> {
                  return (Color)XItemStack.parseColor(var0).orElseThrow(() -> {
                     return new IllegalArgumentException("Unknown color for custom model data: " + var0);
                  });
               }));
            } else {
               List var3 = this.config.getStringList("custom-model-data");
               String var4;
               if (!var3.isEmpty()) {
                  var4 = (String)var3.get(0);
                  if (var4 != null && !var4.isEmpty()) {
                     if (XItemStack.tryNumber(var4, Float::parseFloat) != null) {
                        var1.setFloats((List)var3.stream().map(Float::parseFloat).collect(Collectors.toList()));
                     } else {
                        Optional var5 = XItemStack.parseColor(var4);
                        if (var5.isPresent()) {
                           var1.setColors((List)var3.stream().map(XItemStack::parseColor).map((var0) -> {
                              return (Color)var0.orElseThrow(() -> {
                                 return new IllegalArgumentException("Unknown color for custom model data: " + var0);
                              });
                           }).collect(Collectors.toList()));
                        } else {
                           var1.setStrings(var3);
                        }
                     }
                  }
               } else {
                  var4 = this.config.getString("custom-model-data");
                  if (var4 != null && !var4.isEmpty()) {
                     Float var9 = (Float)XItemStack.tryNumber(var4, Float::parseFloat);
                     if (var9 != null) {
                        var1.setFloats(Collections.singletonList(var9));
                     } else {
                        Optional var6 = XItemStack.parseColor(var4);
                        if (var6.isPresent()) {
                           var1.setColors(Collections.singletonList((Color)var6.get()));
                        } else {
                           var1.setStrings(Collections.singletonList(var4));
                        }
                     }
                  }
               }
            }

            if (!var1.getColors().isEmpty() || !var1.getStrings().isEmpty() || !var1.getFlags().isEmpty() || !var1.getFloats().isEmpty()) {
               this.meta.setCustomModelDataComponent(var1);
            }
         } else if (XItemStack.SUPPORTS_CUSTOM_MODEL_DATA) {
            String var7 = this.config.getString("custom-model-data");
            if (var7 != null && !var7.isEmpty()) {
               Integer var8 = (Integer)XItemStack.tryNumber(var7, Integer::parseInt);
               if (var8 != null) {
                  this.meta.setCustomModelData(var8);
               }
            }
         }

      }

      private void displayName() {
         String var1 = this.config.getString("name");
         if (!Strings.isNullOrEmpty(var1)) {
            String var2 = (String)this.translator.apply(var1);
            this.meta.setDisplayName(var2);
         } else if (var1 != null && var1.isEmpty()) {
            this.meta.setDisplayName(" ");
         }

      }

      private void itemFlags() {
         List var1 = this.config.getStringList("flags");
         if (!var1.isEmpty()) {
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               var3 = var3.toUpperCase(Locale.ENGLISH);
               if (var3.equals("ALL")) {
                  XItemFlag.decorationOnly(this.meta);
                  break;
               }

               XItemFlag.of(var3).ifPresent((var1x) -> {
                  var1x.set(this.meta);
               });
            }
         } else {
            String var4 = this.config.getString("flags");
            if (!Strings.isNullOrEmpty(var4) && var4.equalsIgnoreCase("ALL")) {
               XItemFlag.decorationOnly(this.meta);
            }
         }

      }

      private void handleEnchantmentStorageMeta(EnchantmentStorageMeta var1) {
         ConfigurationSection var2 = this.config.getConfigurationSection("stored-enchants");
         if (var2 != null) {
            Iterator var3 = var2.getKeys(false).iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               Optional var5 = XEnchantment.of(var4);
               var5.ifPresent((var3x) -> {
                  var1.addStoredEnchant((Enchantment)var3x.get(), var2.getInt(var4), true);
               });
            }
         }

      }

      private void enchants() {
         ConfigurationSection var1 = this.config.getConfigurationSection("enchants");
         if (var1 != null) {
            Iterator var2 = var1.getKeys(false).iterator();

            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               Optional var4 = XEnchantment.of(var3);
               var4.ifPresent((var3x) -> {
                  this.meta.addEnchant((Enchantment)var3x.get(), var1.getInt(var3), true);
               });
            }
         } else if (this.config.getBoolean("glow")) {
            this.meta.addEnchant((Enchantment)XEnchantment.UNBREAKING.get(), 1, false);
            XItemFlag.HIDE_ENCHANTS.set(this.meta);
         }

      }

      private void lore() {
         if (this.config.isSet("lore")) {
            List var2 = this.config.getStringList("lore");
            ArrayList var1;
            if (!var2.isEmpty()) {
               var1 = new ArrayList(var2.size());
               Iterator var3 = var2.iterator();

               label46:
               while(true) {
                  while(true) {
                     if (!var3.hasNext()) {
                        break label46;
                     }

                     String var4 = (String)var3.next();
                     if (var4.isEmpty()) {
                        var1.add(" ");
                     } else {
                        Iterator var5 = XItemStack.splitNewLine(var4).iterator();

                        while(var5.hasNext()) {
                           String var6 = (String)var5.next();
                           if (var6.isEmpty()) {
                              var1.add(" ");
                           } else {
                              var1.add((String)this.translator.apply(var6));
                           }
                        }
                     }
                  }
               }
            } else {
               String var7 = this.config.getString("lore");
               var1 = new ArrayList(10);
               if (!Strings.isNullOrEmpty(var7)) {
                  Iterator var8 = XItemStack.splitNewLine(var7).iterator();

                  while(var8.hasNext()) {
                     String var9 = (String)var8.next();
                     if (var9.isEmpty()) {
                        var1.add(" ");
                     } else {
                        var1.add((String)this.translator.apply(var9));
                     }
                  }
               }
            }

            this.meta.setLore(var1);
         }
      }

      private void handleSpawnEggMeta(SpawnEggMeta var1) {
         String var2 = this.config.getString("creature");
         if (!Strings.isNullOrEmpty(var2)) {
            com.google.common.base.Optional var3 = Enums.getIfPresent(EntityType.class, var2.toUpperCase(Locale.ENGLISH));
            if (var3.isPresent()) {
               var1.setSpawnedType((EntityType)var3.get());
            }
         }

      }

      private void handleTropicalFishBucketMeta(TropicalFishBucketMeta var1) {
         DyeColor var2 = (DyeColor)Enums.getIfPresent(DyeColor.class, this.config.getString("color")).or(DyeColor.WHITE);
         DyeColor var3 = (DyeColor)Enums.getIfPresent(DyeColor.class, this.config.getString("pattern-color")).or(DyeColor.WHITE);
         org.bukkit.entity.TropicalFish.Pattern var4 = (org.bukkit.entity.TropicalFish.Pattern)Enums.getIfPresent(org.bukkit.entity.TropicalFish.Pattern.class, this.config.getString("pattern")).or(org.bukkit.entity.TropicalFish.Pattern.BETTY);
         var1.setBodyColor(var2);
         var1.setPatternColor(var3);
         var1.setPattern(var4);
      }

      private void handleCrossbowMeta(CrossbowMeta var1) {
         ConfigurationSection var2 = this.config.getConfigurationSection("projectiles");
         if (var2 != null) {
            Iterator var3 = var2.getKeys(false).iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               ItemStack var5 = XItemStack.deserialize(this.config.getConfigurationSection("projectiles." + var4));
               var1.addChargedProjectile(var5);
            }
         }

      }

      private void handleSuspiciousStewMeta(SuspiciousStewMeta var1) {
         Iterator var2 = this.config.getStringList("effects").iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            XPotion.Effect var4 = XPotion.parseEffect(var3);
            if (var4.hasChance()) {
               var1.addCustomEffect(var4.getEffect(), true);
            }
         }

      }

      private void handleCompassMeta(CompassMeta var1) {
         var1.setLodestoneTracked(this.config.getBoolean("tracked"));
         ConfigurationSection var2 = this.config.getConfigurationSection("lodestone");
         if (var2 != null) {
            World var3 = Bukkit.getWorld(var2.getString("world"));
            double var4 = var2.getDouble("x");
            double var6 = var2.getDouble("y");
            double var8 = var2.getDouble("z");
            var1.setLodestone(new Location(var3, var4, var6, var8));
         }

      }

      private void handleAxolotlBucketMeta(AxolotlBucketMeta var1) {
         String var2 = this.config.getString("color");
         if (var2 != null) {
            Variant var3 = (Variant)Enums.getIfPresent(Variant.class, var2.toUpperCase(Locale.ENGLISH)).or(Variant.BLUE);
            var1.setVariant(var3);
         }

      }

      private void handleArmorMeta(ArmorMeta var1) {
         ConfigurationSection var2 = this.config.getConfigurationSection("trim");
         if (var2 != null) {
            TrimMaterial var3 = (TrimMaterial)Registry.TRIM_MATERIAL.get(NamespacedKey.fromString(var2.getString("material")));
            TrimPattern var4 = (TrimPattern)Registry.TRIM_PATTERN.get(NamespacedKey.fromString(var2.getString("pattern")));
            var1.setTrim(new ArmorTrim(var3, var4));
         }

      }

      private void handleMapMeta(MapMeta var1) {
         ConfigurationSection var2 = this.config.getConfigurationSection("map");
         if (var2 != null) {
            var1.setScaling(var2.getBoolean("scaling"));
            if (XMaterial.supports(11)) {
               if (var2.isSet("location")) {
                  var1.setLocationName(var2.getString("location"));
               }

               if (var2.isSet("color")) {
                  Optional var10000 = XItemStack.parseColor(var2.getString("color"));
                  Objects.requireNonNull(var1);
                  var10000.ifPresent(var1::setColor);
               }
            }

            if (XMaterial.supports(14)) {
               ConfigurationSection var3 = var2.getConfigurationSection("view");
               if (var3 != null) {
                  World var4 = Bukkit.getWorld(var3.getString("world"));
                  if (var4 != null) {
                     MapView var5 = Bukkit.createMap(var4);
                     var5.setWorld(var4);
                     var5.setScale((Scale)Enums.getIfPresent(Scale.class, var3.getString("scale")).or(Scale.NORMAL));
                     var5.setLocked(var3.getBoolean("locked"));
                     var5.setTrackingPosition(var3.getBoolean("tracking-position"));
                     var5.setUnlimitedTracking(var3.getBoolean("unlimited-tracking"));
                     ConfigurationSection var6 = var3.getConfigurationSection("center");
                     if (var6 != null) {
                        var5.setCenterX(var6.getInt("x"));
                        var5.setCenterZ(var6.getInt("z"));
                     }

                     var1.setMapView(var5);
                  }
               }
            }

         }
      }

      private void handleBookMeta(BookMeta var1) {
         ConfigurationSection var2 = this.config.getConfigurationSection("book");
         if (var2 != null) {
            var1.setTitle(var2.getString("title"));
            var1.setAuthor(var2.getString("author"));
            var1.setPages(var2.getStringList("pages"));
            if (XMaterial.supports(9)) {
               String var3 = var2.getString("generation");
               if (var3 != null) {
                  Generation var4 = (Generation)Enums.getIfPresent(Generation.class, var3).orNull();
                  var1.setGeneration(var4);
               }
            }

         }
      }

      private void handleFireworkMeta(FireworkMeta var1) {
         var1.setPower(this.config.getInt("power"));
         ConfigurationSection var2 = this.config.getConfigurationSection("firework");
         if (var2 != null) {
            Builder var3 = FireworkEffect.builder();

            for(Iterator var4 = var2.getKeys(false).iterator(); var4.hasNext(); var1.addEffect(var3.build())) {
               String var5 = (String)var4.next();
               ConfigurationSection var6 = this.config.getConfigurationSection("firework." + var5);
               var3.flicker(var6.getBoolean("flicker"));
               var3.trail(var6.getBoolean("trail"));
               var3.with((Type)Enums.getIfPresent(Type.class, var6.getString("type").toUpperCase(Locale.ENGLISH)).or(Type.STAR));
               ConfigurationSection var7 = var6.getConfigurationSection("colors");
               if (var7 != null) {
                  List var8 = var7.getStringList("base");
                  ArrayList var9 = new ArrayList(var8.size());
                  Iterator var10 = var8.iterator();

                  String var11;
                  Optional var12;
                  while(var10.hasNext()) {
                     var11 = (String)var10.next();
                     var12 = XItemStack.parseColor(var11);
                     if (var12.isPresent()) {
                        var9.add((Color)var12.get());
                     }
                  }

                  var3.withColor(var9);
                  var8 = var7.getStringList("fade");
                  var9 = new ArrayList(var8.size());
                  var10 = var8.iterator();

                  while(var10.hasNext()) {
                     var11 = (String)var10.next();
                     var12 = XItemStack.parseColor(var11);
                     if (var12.isPresent()) {
                        var9.add((Color)var12.get());
                     }
                  }

                  var3.withFade(var9);
               }
            }

         }
      }

      private void handleBlockStateMeta(BlockStateMeta var1) {
         BlockState var2 = XItemStack.safeBlockState(var1);
         if (var2 instanceof CreatureSpawner) {
            CreatureSpawner var3 = (CreatureSpawner)var2;
            String var4 = this.config.getString("spawner");
            if (!Strings.isNullOrEmpty(var4)) {
               var3.setSpawnedType((EntityType)Enums.getIfPresent(EntityType.class, var4.toUpperCase(Locale.ENGLISH)).orNull());
               var3.update(true);
               var1.setBlockState(var3);
            }
         } else {
            Iterator var5;
            String var6;
            if (XMaterial.supports(11) && var2 instanceof ShulkerBox) {
               ConfigurationSection var10 = this.config.getConfigurationSection("contents");
               if (var10 != null) {
                  ShulkerBox var12 = (ShulkerBox)var2;
                  var5 = var10.getKeys(false).iterator();

                  while(var5.hasNext()) {
                     var6 = (String)var5.next();
                     ItemStack var13 = XItemStack.deserialize(var10.getConfigurationSection(var6));
                     int var14 = XItemStack.toInt(var6, 0);
                     var12.getInventory().setItem(var14, var13);
                  }

                  var12.update(true);
                  var1.setBlockState(var12);
               }
            } else if (var2 instanceof Banner) {
               Banner var9 = (Banner)var2;
               ConfigurationSection var11 = this.config.getConfigurationSection("patterns");
               if (!XMaterial.supports(14)) {
                  var9.setBaseColor(DyeColor.WHITE);
               }

               if (var11 != null) {
                  var5 = var11.getKeys(false).iterator();

                  while(var5.hasNext()) {
                     var6 = (String)var5.next();
                     Optional var7 = XPatternType.of(var6);
                     if (var7.isPresent() && ((XPatternType)var7.get()).isSupported()) {
                        DyeColor var8 = (DyeColor)Enums.getIfPresent(DyeColor.class, var11.getString(var6).toUpperCase(Locale.ENGLISH)).or(DyeColor.WHITE);
                        var9.addPattern(new Pattern(var8, (PatternType)((XPatternType)var7.get()).get()));
                     }
                  }

                  var9.update(true);
                  var1.setBlockState(var9);
               }
            }
         }

      }

      private void handlePotionMeta(ItemMeta var1) {
         if (XMaterial.supports(9)) {
            PotionMeta var2 = (PotionMeta)var1;
            Iterator var3 = this.config.getStringList("effects").iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               XPotion.Effect var5 = XPotion.parseEffect(var4);
               if (var5.hasChance()) {
                  var2.addCustomEffect(var5.getEffect(), true);
               }
            }

            String var7 = this.config.getString("base-type");
            if (!Strings.isNullOrEmpty(var7)) {
               PotionType var8;
               try {
                  var8 = PotionType.valueOf(var7);
               } catch (IllegalArgumentException var6) {
                  var8 = PotionType.HEALING;
               }

               var2.setBasePotionType(var8);
            }

            if (XItemStack.SUPPORTS_POTION_COLOR && this.config.contains("color")) {
               var2.setColor(Color.fromRGB(this.config.getInt("color")));
            }
         }

      }

      private void handleLeatherArmorMeta(LeatherArmorMeta var1) {
         String var2 = this.config.getString("color");
         if (var2 != null) {
            Optional var10000 = XItemStack.parseColor(var2);
            Objects.requireNonNull(var1);
            var10000.ifPresent(var1::setColor);
         }

      }

      private void handleBannerMeta(BannerMeta var1) {
         ConfigurationSection var2 = this.config.getConfigurationSection("patterns");
         if (var2 != null) {
            Iterator var3 = var2.getKeys(false).iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               Optional var5 = XPatternType.of(var4);
               if (var5.isPresent() && ((XPatternType)var5.get()).isSupported()) {
                  DyeColor var6 = (DyeColor)Enums.getIfPresent(DyeColor.class, var2.getString(var4).toUpperCase(Locale.ENGLISH)).or(DyeColor.WHITE);
                  var1.addPattern(new Pattern(var6, (PatternType)((XPatternType)var5.get()).get()));
               }
            }
         }

      }

      private void handleSkullMeta(SkullMeta var1) {
         String var2 = this.config.getString("skull");
         if (var2 != null) {
            if (var2.isEmpty()) {
               XSkull.of((ItemMeta)var1).profile(Profileable.detect(var2)).removeProfile();
            } else {
               XSkull.of((ItemMeta)var1).profile(Profileable.detect(var2)).lenient().apply();
            }
         }

      }

      private void handleDurability() {
         int var1;
         if (XMaterial.supports(13)) {
            if (this.meta instanceof Damageable) {
               var1 = this.config.getInt("damage");
               if (var1 > 0) {
                  ((Damageable)this.meta).setDamage(var1);
               }
            }
         } else {
            var1 = this.config.getInt("damage");
            if (var1 > 0) {
               this.item.setDurability((short)var1);
            }
         }

      }

      private void handleDamage() {
         int var1 = this.config.getInt("amount");
         if (var1 > 1) {
            this.item.setAmount(var1);
         }

      }

      private void getOrCreateMeta() {
         this.meta = this.item.getItemMeta();
         if (this.meta == null) {
            this.meta = Bukkit.getItemFactory().getItemMeta(XMaterial.STONE.get());
         }

      }

      private void handleMaterial() {
         String var1 = this.config.getString("material");
         if (!Strings.isNullOrEmpty(var1)) {
            Optional var2 = XMaterial.matchXMaterial(var1);
            XMaterial var3;
            if (var2.isPresent()) {
               var3 = (XMaterial)var2.get();
            } else {
               XItemStack.UnknownMaterialCondition var4 = new XItemStack.UnknownMaterialCondition(var1);
               if (this.restart == null) {
                  throw var4;
               }

               this.restart.accept(var4);
               if (!var4.hasSolution()) {
                  throw var4;
               }

               var3 = var4.solution;
            }

            XItemStack.UnAcceptableMaterialCondition var6;
            if (!var3.isSupported()) {
               var6 = new XItemStack.UnAcceptableMaterialCondition(var3, XItemStack.UnAcceptableMaterialCondition.Reason.UNSUPPORTED);
               if (this.restart == null) {
                  throw var6;
               }

               this.restart.accept(var6);
               if (!var6.hasSolution()) {
                  throw var6;
               }

               var3 = var6.solution;
            }

            if (XTag.INVENTORY_NOT_DISPLAYABLE.isTagged(var3)) {
               var6 = new XItemStack.UnAcceptableMaterialCondition(var3, XItemStack.UnAcceptableMaterialCondition.Reason.NOT_DISPLAYABLE);
               if (this.restart == null) {
                  throw var6;
               }

               this.restart.accept(var6);
               if (!var6.hasSolution()) {
                  throw var6;
               }

               var3 = var6.solution;
            }

            var3.setType(this.item);
         } else {
            String var5 = this.config.getString("skull");
            if (var5 != null) {
               XMaterial.PLAYER_HEAD.setType(this.item);
            }
         }

      }

      // $FF: synthetic method
      Deserializer(ItemStack var1, ConfigurationSection var2, Function var3, Consumer var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }

   public static final class UnAcceptableMaterialCondition extends XItemStack.MaterialCondition {
      private final XMaterial material;
      private final XItemStack.UnAcceptableMaterialCondition.Reason reason;

      public UnAcceptableMaterialCondition(XMaterial var1, XItemStack.UnAcceptableMaterialCondition.Reason var2) {
         super("Unacceptable material: " + var1.name() + " (" + var2.name() + ')');
         this.material = var1;
         this.reason = var2;
      }

      public XItemStack.UnAcceptableMaterialCondition.Reason getReason() {
         return this.reason;
      }

      public XMaterial getMaterial() {
         return this.material;
      }

      public static enum Reason {
         UNSUPPORTED,
         NOT_DISPLAYABLE;

         // $FF: synthetic method
         private static XItemStack.UnAcceptableMaterialCondition.Reason[] $values() {
            return new XItemStack.UnAcceptableMaterialCondition.Reason[]{UNSUPPORTED, NOT_DISPLAYABLE};
         }
      }
   }

   public static final class UnknownMaterialCondition extends XItemStack.MaterialCondition {
      private final String material;

      public UnknownMaterialCondition(String var1) {
         super("Unknown material: " + var1);
         this.material = var1;
      }

      public String getMaterial() {
         return this.material;
      }
   }

   public static class MaterialCondition extends RuntimeException {
      protected XMaterial solution;

      public MaterialCondition(String var1) {
         super(var1);
      }

      public void setSolution(XMaterial var1) {
         this.solution = var1;
      }

      public boolean hasSolution() {
         return this.solution != null;
      }
   }
}
