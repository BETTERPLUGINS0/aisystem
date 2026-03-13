package com.nisovin.shopkeepers.compat.v1_21_R9;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.compat.CompatProvider;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.bukkit.RegistryUtils;
import com.nisovin.shopkeepers.util.bukkit.ServerUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.inventory.ItemStackComponentsData;
import com.nisovin.shopkeepers.util.inventory.ItemStackMetaTag;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.ClassUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.GameProfileSerializer;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.util.datafix.fixes.DataConverterTypes;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.IMerchant;
import net.minecraft.world.item.trading.MerchantRecipeList;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_21_R7.CraftRegistry;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftAbstractVillager;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftMerchant;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.CopperGolem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Golem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieNautilus;
import org.bukkit.entity.CopperGolem.CopperWeatherState;
import org.bukkit.entity.ZombieNautilus.Variant;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.bukkit.potion.PotionType;
import org.bukkit.profile.PlayerProfile;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class CompatProviderImpl implements CompatProvider {
   public static final String VERSION_ID = "1_21_R9";
   private final CopperChestProtectionListener copperChestProtectionListener;
   private final MojangsonParser<NBTBase> tagParser;
   private final Field craftItemStackHandleField;

   public CompatProviderImpl() throws Exception {
      this.tagParser = (MojangsonParser)Unsafe.castNonNull(MojangsonParser.a(DynamicOpsNBT.a));
      this.copperChestProtectionListener = new CopperChestProtectionListener(SKShopkeepersPlugin.getInstance());
      this.craftItemStackHandleField = CraftItemStack.class.getDeclaredField("handle");
      this.craftItemStackHandleField.setAccessible(true);
   }

   public String getVersionId() {
      return "1_21_R9";
   }

   public void onEnable() {
      SKShopkeepersPlugin plugin = SKShopkeepersPlugin.getInstance();
      if (Settings.protectContainers && Settings.preventCopperGolemAccess && ClassUtils.getClassOrNull("org.bukkit.event.entity.EntityTargetBlockEvent") != null) {
         Bukkit.getPluginManager().registerEvents(this.copperChestProtectionListener, plugin);
      }

   }

   public void onDisable() {
      HandlerList.unregisterAll(this.copperChestProtectionListener);
   }

   public void overwriteLivingEntityAI(LivingEntity entity) {
      if (entity instanceof Mob) {
         try {
            EntityInsentient mcMob = ((CraftMob)entity).getHandle();
            PathfinderGoalSelector goalSelector = mcMob.cs;
            goalSelector.a((goal) -> {
               return true;
            });
            goalSelector.a(0, new PathfinderGoalLookAtPlayer(mcMob, EntityHuman.class, 6.0F, 1.0F));
            PathfinderGoalSelector targetSelector = mcMob.ct;
            targetSelector.a((goal) -> {
               return true;
            });
         } catch (Exception var5) {
            Log.severe((String)"Failed to override mob AI!", (Throwable)var5);
         }

      }
   }

   public void tickAI(LivingEntity entity, int ticks) {
      EntityLiving mcLivingEntity = ((CraftLivingEntity)entity).getHandle();
      if (mcLivingEntity instanceof EntityInsentient) {
         EntityInsentient mcMob = (EntityInsentient)mcLivingEntity;
         mcMob.P().a();

         for(int i = 0; i < ticks; ++i) {
            mcMob.cs.a();
            if (!mcMob.J().d()) {
               mcMob.s(mcMob.ec());
            }

            mcMob.J().a();
         }

         mcMob.P().a();
      }
   }

   public void setOnGround(Entity entity, boolean onGround) {
      net.minecraft.world.entity.Entity mcEntity = ((CraftEntity)entity).getHandle();
      mcEntity.e(onGround);
   }

   public boolean isNoAIDisablingGravity() {
      return true;
   }

   public void setNoclip(Entity entity) {
      net.minecraft.world.entity.Entity mcEntity = ((CraftEntity)entity).getHandle();
      mcEntity.ar = true;
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

   private NBTTagCompound getItemStackTag(ItemStack nmsItem) {
      NBTTagCompound itemTag = (NBTTagCompound)ItemStack.b.encodeStart(CraftRegistry.getMinecraftRegistry().a(DynamicOpsNBT.a), nmsItem).getOrThrow();

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
            DataComponentMap requiredComponents = PatchedDataComponentMap.a(DataComponentMap.a, nmsRequired.d());
            return DataComponentExactPredicate.a(requiredComponents).a(nmsProvided);
         }
      }
   }

   public void setInventoryViewTitle(InventoryViewBuilder<?> builder, String title) {
      builder.title(title);
   }

   public void updateTrades(Player player) {
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

         MerchantRecipeList merchantRecipeList = ((IMerchant)nmsMerchant).b();
         if (merchantRecipeList == null) {
            merchantRecipeList = new MerchantRecipeList();
         }

         EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
         nmsPlayer.a(nmsPlayer.cn.l, merchantRecipeList, merchantLevel, merchantExperience, regularVillager, canRestock);
      }
   }

   public ItemStackMetaTag getItemStackMetaTag(@Nullable @ReadOnly org.bukkit.inventory.ItemStack itemStack) {
      if (ItemUtils.isEmpty(itemStack)) {
         return new ItemStackMetaTag((Object)null);
      } else {
         assert itemStack != null;

         ItemStack nmsItem = this.asNMSItemStack(itemStack);
         NBTTagCompound itemTag = this.getItemStackTag(nmsItem);
         NBTTagCompound componentsTag = (NBTTagCompound)itemTag.a("components");
         return new ItemStackMetaTag(componentsTag);
      }
   }

   public boolean matches(ItemStackMetaTag provided, ItemStackMetaTag required, boolean matchPartialLists) {
      Validate.notNull(provided, (String)"provided is null");
      Validate.notNull(required, (String)"required is null");
      NBTBase providedTag = (NBTBase)provided.getNmsTag();
      NBTBase requiredTag = (NBTBase)required.getNmsTag();
      return GameProfileSerializer.a(requiredTag, providedTag, matchPartialLists);
   }

   @Nullable
   public ItemStackComponentsData getItemStackComponentsData(@ReadOnly org.bukkit.inventory.ItemStack itemStack) {
      Validate.notNull(itemStack, (String)"itemStack is null!");
      if (ItemUtils.isEmpty(itemStack)) {
         return null;
      } else {
         ItemStack nmsItem = this.asNMSItemStack(itemStack);
         NBTTagCompound itemTag = this.getItemStackTag(nmsItem);
         NBTTagCompound componentsTag = (NBTTagCompound)itemTag.a("components");
         if (componentsTag == null) {
            return null;
         } else {
            ItemStackComponentsData componentsData = ItemStackComponentsData.ofNonNull(DataContainer.create());
            componentsTag.a((componentKey, componentValue) -> {
               assert componentKey != null;

               componentsData.set(componentKey, componentValue.toString());
            });
            return componentsData;
         }
      }
   }

   public org.bukkit.inventory.ItemStack deserializeItemStack(int dataVersion, NamespacedKey id, int count, @Nullable ItemStackComponentsData componentsData) {
      Validate.notNull(id, (String)"id is null!");
      NBTTagCompound itemTag = new NBTTagCompound();
      itemTag.a("id", id.toString());
      itemTag.a("count", count);
      Map<? extends String, ?> componentValues = componentsData != null ? componentsData.getValues() : null;
      if (componentValues != null && !componentValues.isEmpty()) {
         NBTTagCompound componentsTag = new NBTTagCompound();
         componentValues.forEach((componentKey, componentValue) -> {
            assert componentKey != null;

            assert componentValue != null;

            String componentSnbt = componentValue.toString();

            NBTBase componentTag;
            try {
               componentTag = (NBTBase)Unsafe.assertNonNull((NBTBase)this.tagParser.b(componentSnbt));
            } catch (CommandSyntaxException var7) {
               throw new IllegalArgumentException("Error parsing item stack component: '" + componentSnbt + "'", var7);
            }

            componentsTag.a(componentKey.toString(), componentTag);
         });
         itemTag.a("components", componentsTag);
      }

      int currentDataVersion = ServerUtils.getDataVersion();
      NBTTagCompound convertedItemTag = (NBTTagCompound)DataConverterRegistry.a().update(DataConverterTypes.x, new Dynamic((DynamicOps)Unsafe.castNonNull(DynamicOpsNBT.a), itemTag), dataVersion, currentDataVersion).getValue();
      if (convertedItemTag.b("id", "minecraft:air").equals("minecraft:air")) {
         return new org.bukkit.inventory.ItemStack(Material.AIR);
      } else {
         ItemStack nmsItem = (ItemStack)ItemStack.b.parse(CraftRegistry.getMinecraftRegistry().a(DynamicOpsNBT.a), convertedItemTag).getOrThrow();
         return (org.bukkit.inventory.ItemStack)Unsafe.assertNonNull(CraftItemStack.asCraftMirror(nmsItem));
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

   public void setCopperGolemWeatherState(Golem golem, String weatherState) {
      CopperWeatherState weatherStateValue = (CopperWeatherState)EnumUtils.valueOf(CopperWeatherState.class, weatherState);
      if (weatherStateValue == null) {
         weatherStateValue = CopperWeatherState.UNAFFECTED;
      }

      ((CopperGolem)golem).setWeatherState(weatherStateValue);
   }

   public void setCopperGolemNextWeatheringTick(Golem golem, int tick) {
      ((CopperGolem)golem).setNextWeatheringTick((long)tick);
   }

   public void setMannequinHideDescription(LivingEntity mannequin, boolean hideDescription) {
      ((Mannequin)mannequin).setHideDescription(hideDescription);
   }

   public void setMannequinDescription(LivingEntity mannequin, @Nullable String description) {
      ((Mannequin)mannequin).setDescription(description);
   }

   public void setMannequinMainHand(LivingEntity mannequin, MainHand mainHand) {
      ((Mannequin)mannequin).setMainHand(mainHand);
   }

   public void setMannequinPose(LivingEntity mannequin, Pose pose) {
      ((Mannequin)mannequin).setPose(pose);
   }

   public void setMannequinProfile(LivingEntity mannequin, @Nullable PlayerProfile profile) {
      ((Mannequin)mannequin).setPlayerProfile(profile);
   }

   public void setZombieNautilusVariant(LivingEntity zombieNautilus, NamespacedKey variant) {
      Registry<Variant> registry = this.getRegistry(Variant.class);
      Variant variantValue = (Variant)registry.get(variant);
      if (variantValue == null) {
         variantValue = Variant.TEMPERATE;
      }

      assert variantValue != null;

      ((ZombieNautilus)zombieNautilus).setVariant(variantValue);
   }

   public NamespacedKey cycleZombieNautilusVariant(NamespacedKey variant, boolean backwards) {
      Registry<Variant> registry = this.getRegistry(Variant.class);
      Variant variantValue = (Variant)registry.get(variant);
      if (variantValue == null) {
         variantValue = Variant.TEMPERATE;
      }

      assert variantValue != null;

      return ((Variant)RegistryUtils.cycleKeyed(Variant.class, variantValue, backwards)).getKeyOrThrow();
   }
}
