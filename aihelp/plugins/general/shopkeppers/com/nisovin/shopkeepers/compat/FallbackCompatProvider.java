package com.nisovin.shopkeepers.compat;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.bukkit.ServerUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.inventory.ItemStackComponentsData;
import com.nisovin.shopkeepers.util.inventory.ItemStackMetaTag;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.BiConsumer;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.bukkit.potion.PotionType;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class FallbackCompatProvider implements CompatProvider {
   public static final String VERSION_ID = "fallback";
   private final Class<?> nmsEntityClass;
   private final Method nmsEntitySetOnGroundMethod;
   private final Object nmsMinecraftRegistry;
   private final Class<?> nmsTagClass;
   private final Class<?> nmsCompoundTagClass;
   private final Method nmsCompoundTagGetMethod;
   private final Method nmsCompoundTagGetStringOrMethod;
   private final Method nmsCompoundTagPutStringMethod;
   private final Method nmsCompoundTagPutIntMethod;
   private final Method nmsCompoundTagPutMethod;
   private final Method nmsCompoundTagForEachMethod;
   private final Method nmsHolderLookupProviderCreateSerializationContextMethod;
   private final Class<?> nmsItemStackClass;
   private final Object nmsItemStackCodec;
   private final Method nmsCodecEncodeStartMethod;
   private final Method nmsCodecParseMethod;
   private final Method nmsDataResultGetOrThrowMethod;
   private final Object nmsNbtOps;
   private final Object nmsTagParser;
   private final Method nmsTagParserParseFullyMethod;
   private final Object nmsDataFixer;
   private final Method nmsDataFixerUpdateMethod;
   private final Object nmsDataFixerTypeItemStack;
   private final Constructor<?> nmsDynamicConstructor;
   private final Method nmsDynamicGetValueMethod;
   private final Method nmsNbtUtilsCompareNbtMethod;
   private final Class<?> obcCraftItemStackClass;
   private final Field obcCraftItemStackHandleField;
   private final Method obcCraftItemStackAsNMSCopyMethod;
   private final Method obcCraftItemStackAsCraftMirrorMethod;
   private final Class<?> obcCraftEntityClass;
   private final Method obcGetHandleMethod;
   @Nullable
   private final Method inventoryViewBuilderTitleMethod;

   public FallbackCompatProvider() throws Exception {
      String cbPackage = ServerUtils.getCraftBukkitPackage();
      this.nmsEntityClass = Class.forName("net.minecraft.world.entity.Entity");
      this.nmsEntitySetOnGroundMethod = this.nmsEntityClass.getDeclaredMethod("e", Boolean.TYPE);
      Class<?> obcCraftRegistryClass = Class.forName(cbPackage + ".CraftRegistry");
      Method obcGetMinecraftRegistryMethod = obcCraftRegistryClass.getMethod("getMinecraftRegistry");
      this.nmsMinecraftRegistry = Unsafe.assertNonNull(obcGetMinecraftRegistryMethod.invoke((Object)null));
      this.nmsTagClass = Class.forName("net.minecraft.nbt.NBTBase");
      this.nmsCompoundTagClass = Class.forName("net.minecraft.nbt.NBTTagCompound");
      this.nmsCompoundTagGetMethod = this.nmsCompoundTagClass.getDeclaredMethod("a", String.class);
      this.nmsCompoundTagGetStringOrMethod = this.nmsCompoundTagClass.getDeclaredMethod("b", String.class, String.class);
      this.nmsCompoundTagPutStringMethod = this.nmsCompoundTagClass.getDeclaredMethod("a", String.class, String.class);
      this.nmsCompoundTagPutIntMethod = this.nmsCompoundTagClass.getDeclaredMethod("a", String.class, Integer.TYPE);
      this.nmsCompoundTagPutMethod = this.nmsCompoundTagClass.getMethod("a", String.class, this.nmsTagClass);
      this.nmsCompoundTagForEachMethod = this.nmsCompoundTagClass.getDeclaredMethod("a", BiConsumer.class);
      Class<?> dynamicOpsClass = Class.forName("com.mojang.serialization.DynamicOps");
      Class<?> nmsHolderLookupProviderClass = Class.forName("net.minecraft.core.HolderLookup$a");
      this.nmsHolderLookupProviderCreateSerializationContextMethod = nmsHolderLookupProviderClass.getDeclaredMethod("a", dynamicOpsClass);
      this.nmsItemStackClass = Class.forName("net.minecraft.world.item.ItemStack");
      Field nmsItemStackCodecField = this.nmsItemStackClass.getDeclaredField("b");
      this.nmsItemStackCodec = Unsafe.assertNonNull(nmsItemStackCodecField.get((Object)null));
      this.nmsCodecEncodeStartMethod = this.nmsItemStackCodec.getClass().getMethod("encodeStart", dynamicOpsClass, Object.class);
      this.nmsCodecParseMethod = this.nmsItemStackCodec.getClass().getMethod("parse", dynamicOpsClass, Object.class);
      Class<?> nmsDataResultClass = Class.forName("com.mojang.serialization.DataResult");
      this.nmsDataResultGetOrThrowMethod = nmsDataResultClass.getDeclaredMethod("getOrThrow");
      Class<?> nmsNbtOpsClass = Class.forName("net.minecraft.nbt.DynamicOpsNBT");
      Field nmsNbtOpsInstanceField = nmsNbtOpsClass.getField("a");
      this.nmsNbtOps = Unsafe.assertNonNull(nmsNbtOpsInstanceField.get((Object)null));
      Class<?> nmsTagParserClass = Class.forName("net.minecraft.nbt.MojangsonParser");
      Method nmsTagParserCreateMethod = nmsTagParserClass.getDeclaredMethod("a", dynamicOpsClass);
      this.nmsTagParser = Unsafe.assertNonNull(nmsTagParserCreateMethod.invoke((Object)null, this.nmsNbtOps));
      this.nmsTagParserParseFullyMethod = nmsTagParserClass.getDeclaredMethod("b", String.class);
      Class<?> nmsDynamicClass = Class.forName("com.mojang.serialization.Dynamic");
      Class<?> nmsDataFixersClass = Class.forName("net.minecraft.util.datafix.DataConverterRegistry");
      Method nmsDataFixerGetDataFixerMethod = nmsDataFixersClass.getDeclaredMethod("a");
      this.nmsDataFixer = Unsafe.assertNonNull(nmsDataFixerGetDataFixerMethod.invoke((Object)null));
      Class<?> nmsDataFixerTypeReferenceClass = Class.forName("com.mojang.datafixers.DSL$TypeReference");
      Class<?> nmsDataFixerClass = Class.forName("com.mojang.datafixers.DataFixer");
      this.nmsDataFixerUpdateMethod = nmsDataFixerClass.getDeclaredMethod("update", nmsDataFixerTypeReferenceClass, nmsDynamicClass, Integer.TYPE, Integer.TYPE);
      Object nmsDataFixerTypeItemStackResult = findDataFixerConverterType("item_stack");
      if (nmsDataFixerTypeItemStackResult == null) {
         throw new IllegalStateException("Failed to retrieve the item stack datafixer type reference!");
      } else {
         this.nmsDataFixerTypeItemStack = nmsDataFixerTypeItemStackResult;
         this.nmsDynamicConstructor = nmsDynamicClass.getConstructor(dynamicOpsClass, Object.class);
         this.nmsDynamicGetValueMethod = nmsDynamicClass.getDeclaredMethod("getValue");
         Class<?> nmsNbtUtilsClass = Class.forName("net.minecraft.nbt.GameProfileSerializer");
         this.nmsNbtUtilsCompareNbtMethod = nmsNbtUtilsClass.getDeclaredMethod("a", this.nmsTagClass, this.nmsTagClass, Boolean.TYPE);
         this.obcCraftItemStackClass = Class.forName(cbPackage + ".inventory.CraftItemStack");
         this.obcCraftItemStackHandleField = this.obcCraftItemStackClass.getDeclaredField("handle");
         this.obcCraftItemStackHandleField.setAccessible(true);
         this.obcCraftItemStackAsNMSCopyMethod = this.obcCraftItemStackClass.getDeclaredMethod("asNMSCopy", ItemStack.class);
         this.obcCraftItemStackAsCraftMirrorMethod = this.obcCraftItemStackClass.getDeclaredMethod("asCraftMirror", this.nmsItemStackClass);
         this.obcCraftEntityClass = Class.forName(cbPackage + ".entity.CraftEntity");
         this.obcGetHandleMethod = this.obcCraftEntityClass.getDeclaredMethod("getHandle");
         Method localInventoryViewBuilderTitleMethod = null;

         try {
            localInventoryViewBuilderTitleMethod = InventoryViewBuilder.class.getMethod("title", String.class);
         } catch (NoSuchMethodException var21) {
         }

         this.inventoryViewBuilderTitleMethod = localInventoryViewBuilderTitleMethod;
      }
   }

   @Nullable
   private static Object findDataFixerConverterType(String typeName) throws Exception {
      Class<?> nmsDataFixerReferencesClass = Class.forName("net.minecraft.util.datafix.fixes.DataConverterTypes");
      Class<?> nmsDataFixerTypeReferenceClass = Class.forName("com.mojang.datafixers.DSL$TypeReference");
      Method nmsDataFixerTypeReferenceTypeNameMethod = nmsDataFixerTypeReferenceClass.getDeclaredMethod("typeName");
      Field[] var4 = nmsDataFixerReferencesClass.getDeclaredFields();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Field field = var4[var6];
         if (Modifier.isStatic(field.getModifiers())) {
            Object value = field.get((Object)null);
            if (value != null) {
               Object typeNameResult = nmsDataFixerTypeReferenceTypeNameMethod.invoke(value);
               if (typeName.equals(typeNameResult)) {
                  return value;
               }
            }
         }
      }

      return null;
   }

   public String getVersionId() {
      return "fallback";
   }

   public void overwriteLivingEntityAI(LivingEntity entity) {
   }

   public boolean supportsCustomMobAI() {
      return false;
   }

   public void tickAI(LivingEntity entity, int ticks) {
   }

   public void setOnGround(Entity entity, boolean onGround) {
      try {
         Object mcEntity = Unsafe.assertNonNull(this.obcGetHandleMethod.invoke(entity));
         this.nmsEntitySetOnGroundMethod.invoke(mcEntity, onGround);
      } catch (Exception var4) {
      }

   }

   public void setNoclip(Entity entity) {
   }

   private Object asNMSItemStack(ItemStack itemStack) {
      assert itemStack != null;

      if (this.obcCraftItemStackClass.isInstance(itemStack)) {
         try {
            return Unsafe.castNonNull(this.obcCraftItemStackHandleField.get(itemStack));
         } catch (Exception var4) {
            Log.severe((String)"Failed to retrieve the underlying Minecraft ItemStack!", (Throwable)var4);
         }
      }

      try {
         return Unsafe.assertNonNull(this.obcCraftItemStackAsNMSCopyMethod.invoke((Object)null, itemStack));
      } catch (InvocationTargetException | IllegalAccessException var3) {
         throw new RuntimeException("Failed to convert item stack to NMS item stack!", var3);
      }
   }

   public boolean matches(@Nullable ItemStack provided, @Nullable ItemStack required) {
      if (provided == required) {
         return true;
      } else if (ItemUtils.isEmpty(required)) {
         return ItemUtils.isEmpty(provided);
      } else if (ItemUtils.isEmpty(provided)) {
         return false;
      } else {
         assert required != null && provided != null;

         return provided.getType() != required.getType() ? false : required.isSimilar(provided);
      }
   }

   public void setInventoryViewTitle(InventoryViewBuilder<?> builder, String title) {
      if (this.inventoryViewBuilderTitleMethod != null) {
         assert this.inventoryViewBuilderTitleMethod != null;

         try {
            this.inventoryViewBuilderTitleMethod.invoke(builder, title);
         } catch (InvocationTargetException | IllegalAccessException var4) {
         }

      }
   }

   public void updateTrades(Player player) {
   }

   private Object getItemStackTag(Object nmsItemStack) throws Exception {
      Object serializationContext = this.nmsHolderLookupProviderCreateSerializationContextMethod.invoke(this.nmsMinecraftRegistry, this.nmsNbtOps);
      Object itemTagResult = this.nmsCodecEncodeStartMethod.invoke(this.nmsItemStackCodec, serializationContext, nmsItemStack);
      Object itemTag = this.nmsDataResultGetOrThrowMethod.invoke(itemTagResult);
      return Unsafe.assertNonNull(itemTag);
   }

   public ItemStackMetaTag getItemStackMetaTag(@Nullable @ReadOnly ItemStack itemStack) {
      if (ItemUtils.isEmpty(itemStack)) {
         return new ItemStackMetaTag((Object)null);
      } else {
         assert itemStack != null;

         try {
            Object nmsItem = this.asNMSItemStack(itemStack);
            Object itemTag = this.getItemStackTag(nmsItem);
            Object componentsTag = this.nmsCompoundTagGetMethod.invoke(itemTag, "components");
            return new ItemStackMetaTag(componentsTag);
         } catch (Exception var5) {
            throw new RuntimeException("Failed to get item stack meta tag!", var5);
         }
      }
   }

   public boolean matches(ItemStackMetaTag provided, ItemStackMetaTag required, boolean matchPartialLists) {
      Validate.notNull(provided, (String)"provided is null");
      Validate.notNull(required, (String)"required is null");
      Object providedTag = provided.getNmsTag();
      Object requiredTag = required.getNmsTag();

      try {
         return (Boolean)Unsafe.castNonNull(this.nmsNbtUtilsCompareNbtMethod.invoke((Object)null, requiredTag, providedTag, matchPartialLists));
      } catch (Exception var7) {
         throw new RuntimeException("Failed to match item stack meta tags!", var7);
      }
   }

   @Nullable
   public ItemStackComponentsData getItemStackComponentsData(@ReadOnly ItemStack itemStack) {
      Validate.notNull(itemStack, (String)"itemStack is null!");
      if (ItemUtils.isEmpty(itemStack)) {
         return null;
      } else {
         try {
            Object nmsItem = this.asNMSItemStack(itemStack);
            Object itemTag = this.getItemStackTag(nmsItem);
            Object componentsTag = this.nmsCompoundTagGetMethod.invoke(itemTag, "components");
            if (componentsTag == null) {
               return null;
            } else {
               final ItemStackComponentsData componentsData = ItemStackComponentsData.ofNonNull(DataContainer.create());
               <undefinedtype> consumer = new BiConsumer<String, Object>(this) {
                  public void accept(String componentKey, Object componentValue) {
                     assert componentKey != null;

                     componentsData.set(componentKey, componentValue.toString());
                  }
               };
               this.nmsCompoundTagForEachMethod.invoke(componentsTag, consumer);
               return componentsData;
            }
         } catch (Exception var7) {
            throw new RuntimeException("Failed to serialize item stack!", var7);
         }
      }
   }

   public ItemStack deserializeItemStack(int dataVersion, NamespacedKey id, int count, @Nullable ItemStackComponentsData componentsData) {
      Validate.notNull(id, (String)"id is null!");

      try {
         Object itemTag = this.nmsCompoundTagClass.getConstructor().newInstance();
         this.nmsCompoundTagPutStringMethod.invoke(itemTag, "id", id.toString());
         this.nmsCompoundTagPutIntMethod.invoke(itemTag, "count", count);
         Map<? extends String, ?> componentValues = componentsData != null ? componentsData.getValues() : null;
         if (componentValues != null && !componentValues.isEmpty()) {
            Object componentsTag = this.nmsCompoundTagClass.getConstructor().newInstance();
            componentValues.forEach((componentKey, componentValue) -> {
               assert componentKey != null;

               assert componentValue != null;

               String componentSnbt = componentValue.toString();

               Object componentTag;
               try {
                  componentTag = Unsafe.assertNonNull(this.nmsTagParserParseFullyMethod.invoke(this.nmsTagParser, componentSnbt));
               } catch (Exception var8) {
                  throw new IllegalArgumentException("Error parsing item stack component: '" + componentSnbt + "'", var8);
               }

               try {
                  this.nmsCompoundTagPutMethod.invoke(componentsTag, componentKey.toString(), componentTag);
               } catch (InvocationTargetException | IllegalAccessException var7) {
                  throw new RuntimeException(var7);
               }
            });
            this.nmsCompoundTagPutMethod.invoke(itemTag, "components", componentsTag);
         }

         int currentDataVersion = ServerUtils.getDataVersion();
         Object convertedItemTagDynamic = this.nmsDataFixerUpdateMethod.invoke(this.nmsDataFixer, this.nmsDataFixerTypeItemStack, this.nmsDynamicConstructor.newInstance(this.nmsNbtOps, itemTag), dataVersion, currentDataVersion);
         Object convertedItemTag = this.nmsDynamicGetValueMethod.invoke(convertedItemTagDynamic);
         String idString = (String)this.nmsCompoundTagGetStringOrMethod.invoke(convertedItemTag, "id", "minecraft:air");

         assert idString != null;

         if (idString.equals("minecraft:air")) {
            return new ItemStack(Material.AIR);
         } else {
            Object serializationContext = this.nmsHolderLookupProviderCreateSerializationContextMethod.invoke(this.nmsMinecraftRegistry, this.nmsNbtOps);
            Object nmsItemResult = this.nmsCodecParseMethod.invoke(this.nmsItemStackCodec, serializationContext, convertedItemTag);
            Object nmsItem = this.nmsDataResultGetOrThrowMethod.invoke(nmsItemResult);
            return (ItemStack)Unsafe.assertNonNull((ItemStack)this.obcCraftItemStackAsCraftMirrorMethod.invoke((Object)null, nmsItem));
         }
      } catch (Exception var14) {
         throw new RuntimeException("Failed to deserialize item stack!", var14);
      }
   }

   public <T extends Keyed> Registry<T> getRegistry(Class<T> clazz) {
      if (clazz == EntityType.class) {
         return (Registry)Unsafe.castNonNull(Registry.ENTITY_TYPE);
      } else if (clazz == Particle.class) {
         return (Registry)Unsafe.castNonNull(Registry.PARTICLE_TYPE);
      } else if (clazz == PotionType.class) {
         return (Registry)Unsafe.castNonNull(Registry.POTION);
      } else {
         return clazz == MemoryKey.class ? (Registry)Unsafe.castNonNull(Registry.MEMORY_MODULE_TYPE) : (Registry)Unsafe.assertNonNull(Bukkit.getRegistry(clazz));
      }
   }
}
