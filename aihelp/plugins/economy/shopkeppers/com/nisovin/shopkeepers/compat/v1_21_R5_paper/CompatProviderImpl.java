package com.nisovin.shopkeepers.compat.v1_21_R5_paper;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.compat.CompatProvider;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.bukkit.ServerUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.inventory.ItemStackComponentsData;
import com.nisovin.shopkeepers.util.inventory.ItemStackMetaTag;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffers;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.entity.CraftAbstractVillager;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftMob;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftMerchant;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class CompatProviderImpl implements CompatProvider {
   private static final Map<Class<?>, RegistryKey<?>> CLASS_TO_REGISTRY_KEY = new HashMap();
   private final TagParser<Tag> tagParser;
   private final Field craftItemStackHandleField;

   public CompatProviderImpl() throws Exception {
      this.tagParser = (TagParser)Unsafe.castNonNull(TagParser.create(NbtOps.INSTANCE));
      this.craftItemStackHandleField = CraftItemStack.class.getDeclaredField("handle");
      this.craftItemStackHandleField.setAccessible(true);
   }

   public String getVersionId() {
      return "1_21_R5_paper";
   }

   public Class<?> getCraftMagicNumbersClass() {
      return CraftMagicNumbers.class;
   }

   public void overwriteLivingEntityAI(LivingEntity entity) {
      if (entity instanceof Mob) {
         try {
            net.minecraft.world.entity.Mob mcMob = ((CraftMob)entity).getHandle();
            GoalSelector goalSelector = mcMob.goalSelector;
            goalSelector.removeAllGoals((goal) -> {
               return true;
            });
            goalSelector.addGoal(0, new LookAtPlayerGoal(mcMob, Player.class, 6.0F, 1.0F));
            GoalSelector targetSelector = mcMob.targetSelector;
            targetSelector.removeAllGoals((goal) -> {
               return true;
            });
         } catch (Exception var5) {
            Log.severe((String)"Failed to override mob AI!", (Throwable)var5);
         }

      }
   }

   public void tickAI(LivingEntity entity, int ticks) {
      net.minecraft.world.entity.LivingEntity mcLivingEntity = ((CraftLivingEntity)entity).getHandle();
      if (mcLivingEntity instanceof net.minecraft.world.entity.Mob) {
         net.minecraft.world.entity.Mob mcMob = (net.minecraft.world.entity.Mob)mcLivingEntity;
         mcMob.getSensing().tick();

         for(int i = 0; i < ticks; ++i) {
            mcMob.goalSelector.tick();
            if (!mcMob.getLookControl().isLookingAtTarget()) {
               mcMob.setYBodyRot(mcMob.getYRot());
            }

            mcMob.getLookControl().tick();
         }

         mcMob.getSensing().tick();
      }
   }

   public void setOnGround(Entity entity, boolean onGround) {
      net.minecraft.world.entity.Entity mcEntity = ((CraftEntity)entity).getHandle();
      mcEntity.setOnGround(onGround);
   }

   public boolean isNoAIDisablingGravity() {
      return true;
   }

   public void setNoclip(Entity entity) {
      net.minecraft.world.entity.Entity mcEntity = ((CraftEntity)entity).getHandle();
      mcEntity.noPhysics = true;
   }

   private ItemStack asNMSItemStack(org.bukkit.inventory.ItemStack itemStack) {
      assert itemStack != null;

      if (itemStack instanceof CraftItemStack) {
         try {
            return (ItemStack)Unsafe.castNonNull(this.craftItemStackHandleField.get(itemStack));
         } catch (Exception var3) {
            Log.severe((String)"Failed to retrieve the underlying Minecraft ItemStack!", (Throwable)var3);
         }
      }

      return (ItemStack)Unsafe.assertNonNull(CraftItemStack.asNMSCopy(itemStack));
   }

   private CompoundTag getItemStackTag(ItemStack nmsItem) {
      CompoundTag itemTag = (CompoundTag)nmsItem.save(CraftRegistry.getMinecraftRegistry());

      assert itemTag != null;

      return itemTag;
   }

   public boolean matches(@Nullable org.bukkit.inventory.ItemStack provided, @Nullable org.bukkit.inventory.ItemStack required) {
      if (provided == required) {
         return true;
      } else if (ItemUtils.isEmpty(required)) {
         return ItemUtils.isEmpty(provided);
      } else if (ItemUtils.isEmpty(provided)) {
         return false;
      } else {
         assert required != null && provided != null;

         if (provided.getType() != required.getType()) {
            return false;
         } else {
            ItemStack nmsProvided = this.asNMSItemStack(provided);
            ItemStack nmsRequired = this.asNMSItemStack(required);
            DataComponentMap requiredComponents = PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, nmsRequired.getComponentsPatch());
            return DataComponentExactPredicate.allOf(requiredComponents).test(nmsProvided);
         }
      }
   }

   public void setInventoryViewTitle(InventoryViewBuilder<?> builder, String title) {
      TextComponent titleComponent = LegacyComponentSerializer.legacySection().deserialize(title);
      builder.title(titleComponent);
   }

   public void updateTrades(org.bukkit.entity.Player player) {
      Inventory openInventory = player.getOpenInventory().getTopInventory();
      if (openInventory instanceof MerchantInventory) {
         MerchantInventory merchantInventory = (MerchantInventory)openInventory;
         merchantInventory.setItem(0, merchantInventory.getItem(0));
         Merchant merchant = merchantInventory.getMerchant();
         boolean regularVillager = false;
         boolean canRestock = false;
         int merchantLevel = 1;
         int merchantExperience = 0;
         Object nmsMerchant;
         if (merchant instanceof Villager) {
            nmsMerchant = ((CraftVillager)merchant).getHandle();
            Villager villager = (Villager)merchant;
            regularVillager = true;
            canRestock = true;
            merchantLevel = villager.getVillagerLevel();
            merchantExperience = villager.getVillagerExperience();
         } else if (merchant instanceof AbstractVillager) {
            nmsMerchant = ((CraftAbstractVillager)merchant).getHandle();
         } else {
            nmsMerchant = ((CraftMerchant)merchant).getMerchant();
            merchantLevel = 0;
         }

         MerchantOffers merchantRecipeList = ((net.minecraft.world.item.trading.Merchant)nmsMerchant).getOffers();
         if (merchantRecipeList == null) {
            merchantRecipeList = new MerchantOffers();
         }

         ServerPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
         nmsPlayer.sendMerchantOffers(nmsPlayer.containerMenu.containerId, merchantRecipeList, merchantLevel, merchantExperience, regularVillager, canRestock);
      }
   }

   public ItemStackMetaTag getItemStackMetaTag(@Nullable @ReadOnly org.bukkit.inventory.ItemStack itemStack) {
      if (ItemUtils.isEmpty(itemStack)) {
         return new ItemStackMetaTag((Object)null);
      } else {
         assert itemStack != null;

         ItemStack nmsItem = this.asNMSItemStack(itemStack);
         CompoundTag itemTag = this.getItemStackTag(nmsItem);
         CompoundTag componentsTag = (CompoundTag)itemTag.get("components");
         return new ItemStackMetaTag(componentsTag);
      }
   }

   public boolean matches(ItemStackMetaTag provided, ItemStackMetaTag required, boolean matchPartialLists) {
      Validate.notNull(provided, (String)"provided is null");
      Validate.notNull(required, (String)"required is null");
      Tag providedTag = (Tag)provided.getNmsTag();
      Tag requiredTag = (Tag)required.getNmsTag();
      return NbtUtils.compareNbt(requiredTag, providedTag, matchPartialLists);
   }

   @Nullable
   public ItemStackComponentsData getItemStackComponentsData(@ReadOnly org.bukkit.inventory.ItemStack itemStack) {
      Validate.notNull(itemStack, (String)"itemStack is null!");
      if (ItemUtils.isEmpty(itemStack)) {
         return null;
      } else {
         ItemStack nmsItem = this.asNMSItemStack(itemStack);
         CompoundTag itemTag = this.getItemStackTag(nmsItem);
         CompoundTag componentsTag = (CompoundTag)itemTag.get("components");
         if (componentsTag == null) {
            return null;
         } else {
            ItemStackComponentsData componentsData = ItemStackComponentsData.ofNonNull(DataContainer.create());
            componentsTag.forEach((componentKey, componentValue) -> {
               assert componentKey != null;

               componentsData.set(componentKey, componentValue.toString());
            });
            return componentsData;
         }
      }
   }

   public org.bukkit.inventory.ItemStack deserializeItemStack(int dataVersion, NamespacedKey id, int count, @Nullable ItemStackComponentsData componentsData) {
      Validate.notNull(id, (String)"id is null!");
      CompoundTag itemTag = new CompoundTag();
      itemTag.putString("id", id.toString());
      itemTag.putInt("count", count);
      Map<? extends String, ?> componentValues = componentsData != null ? componentsData.getValues() : null;
      if (componentValues != null && !componentValues.isEmpty()) {
         CompoundTag componentsTag = new CompoundTag();
         componentValues.forEach((componentKey, componentValue) -> {
            assert componentKey != null;

            assert componentValue != null;

            String componentSnbt = componentValue.toString();

            Tag componentTag;
            try {
               componentTag = (Tag)Unsafe.assertNonNull((Tag)this.tagParser.parseFully(componentSnbt));
            } catch (CommandSyntaxException var7) {
               throw new IllegalArgumentException("Error parsing item stack component: '" + componentSnbt + "'", var7);
            }

            componentsTag.put(componentKey.toString(), componentTag);
         });
         itemTag.put("components", componentsTag);
      }

      int currentDataVersion = ServerUtils.getDataVersion();
      CompoundTag convertedItemTag = (CompoundTag)DataFixers.getDataFixer().update(References.ITEM_STACK, new Dynamic((DynamicOps)Unsafe.castNonNull(NbtOps.INSTANCE), itemTag), dataVersion, currentDataVersion).getValue();
      if (convertedItemTag.getStringOr("id", "minecraft:air").equals("minecraft:air")) {
         return new org.bukkit.inventory.ItemStack(Material.AIR);
      } else {
         ItemStack nmsItem = (ItemStack)ItemStack.parse(CraftRegistry.getMinecraftRegistry(), convertedItemTag).orElseThrow();
         return (org.bukkit.inventory.ItemStack)Unsafe.assertNonNull(CraftItemStack.asCraftMirror(nmsItem));
      }
   }

   public <T extends Keyed> Registry<T> getRegistry(Class<T> clazz) {
      RegistryKey<T> registryKey = (RegistryKey)Unsafe.castNonNull(CLASS_TO_REGISTRY_KEY.get(clazz));
      return (Registry)Unsafe.castNonNull(RegistryAccess.registryAccess().getRegistry(registryKey));
   }

   static {
      try {
         Field[] var0 = RegistryKey.class.getFields();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            Field field = var0[var2];
            if (field.getType() == RegistryKey.class) {
               ParameterizedType fieldType = (ParameterizedType)field.getGenericType();
               Type typeArgument = fieldType.getActualTypeArguments()[0];
               Class registryClass;
               if (typeArgument instanceof Class) {
                  Class<?> typeArgumentClass = (Class)typeArgument;
                  registryClass = typeArgumentClass;
               } else {
                  if (!(typeArgument instanceof ParameterizedType)) {
                     throw new RuntimeException("Unexpected RegistryKey type parameter for field: " + field.getName());
                  }

                  ParameterizedType typeArgumentParameterized = (ParameterizedType)typeArgument;
                  registryClass = (Class)Unsafe.castNonNull(typeArgumentParameterized.getRawType());
               }

               RegistryKey<?> registryKey = (RegistryKey)Unsafe.castNonNull(field.get((Object)null));
               CLASS_TO_REGISTRY_KEY.put(registryClass, registryKey);
            }
         }

      } catch (ReflectiveOperationException var9) {
         throw new RuntimeException(var9);
      }
   }
}
