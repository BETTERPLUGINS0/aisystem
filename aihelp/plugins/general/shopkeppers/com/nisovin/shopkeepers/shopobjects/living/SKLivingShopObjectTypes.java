package com.nisovin.shopkeepers.shopobjects.living;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopObjectTypes;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShops;
import com.nisovin.shopkeepers.shopobjects.living.types.AbstractHorseShop;
import com.nisovin.shopkeepers.shopobjects.living.types.ArmorStandShop;
import com.nisovin.shopkeepers.shopobjects.living.types.AxolotlShop;
import com.nisovin.shopkeepers.shopobjects.living.types.BabyableShop;
import com.nisovin.shopkeepers.shopobjects.living.types.BatShop;
import com.nisovin.shopkeepers.shopobjects.living.types.CatShop;
import com.nisovin.shopkeepers.shopobjects.living.types.ChestedHorseShop;
import com.nisovin.shopkeepers.shopobjects.living.types.ChickenShop;
import com.nisovin.shopkeepers.shopobjects.living.types.CopperGolemShop;
import com.nisovin.shopkeepers.shopobjects.living.types.CowShop;
import com.nisovin.shopkeepers.shopobjects.living.types.CreeperShop;
import com.nisovin.shopkeepers.shopobjects.living.types.EndermanShop;
import com.nisovin.shopkeepers.shopobjects.living.types.FoxShop;
import com.nisovin.shopkeepers.shopobjects.living.types.FrogShop;
import com.nisovin.shopkeepers.shopobjects.living.types.GlowSquidShop;
import com.nisovin.shopkeepers.shopobjects.living.types.GoatShop;
import com.nisovin.shopkeepers.shopobjects.living.types.HorseShop;
import com.nisovin.shopkeepers.shopobjects.living.types.LlamaShop;
import com.nisovin.shopkeepers.shopobjects.living.types.MagmaCubeShop;
import com.nisovin.shopkeepers.shopobjects.living.types.MannequinShop;
import com.nisovin.shopkeepers.shopobjects.living.types.MooshroomShop;
import com.nisovin.shopkeepers.shopobjects.living.types.NautilusShop;
import com.nisovin.shopkeepers.shopobjects.living.types.PandaShop;
import com.nisovin.shopkeepers.shopobjects.living.types.ParrotShop;
import com.nisovin.shopkeepers.shopobjects.living.types.PigShop;
import com.nisovin.shopkeepers.shopobjects.living.types.PufferFishShop;
import com.nisovin.shopkeepers.shopobjects.living.types.RabbitShop;
import com.nisovin.shopkeepers.shopobjects.living.types.SalmonShop;
import com.nisovin.shopkeepers.shopobjects.living.types.SheepShop;
import com.nisovin.shopkeepers.shopobjects.living.types.ShulkerShop;
import com.nisovin.shopkeepers.shopobjects.living.types.SlimeShop;
import com.nisovin.shopkeepers.shopobjects.living.types.SnowmanShop;
import com.nisovin.shopkeepers.shopobjects.living.types.StriderShop;
import com.nisovin.shopkeepers.shopobjects.living.types.TropicalFishShop;
import com.nisovin.shopkeepers.shopobjects.living.types.VillagerShop;
import com.nisovin.shopkeepers.shopobjects.living.types.WanderingTraderShop;
import com.nisovin.shopkeepers.shopobjects.living.types.WolfShop;
import com.nisovin.shopkeepers.shopobjects.living.types.ZombieNautilusShop;
import com.nisovin.shopkeepers.shopobjects.living.types.ZombieShop;
import com.nisovin.shopkeepers.shopobjects.living.types.ZombieVillagerShop;
import com.nisovin.shopkeepers.util.java.ClassUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SKLivingShopObjectTypes implements LivingShopObjectTypes {
   private final Map<? extends EntityType, ? extends SKLivingShopObjectType<?>> objectTypes;
   private final List<SKLivingShopObjectType<?>> orderedObjectTypes = new ArrayList();
   private final List<? extends SKLivingShopObjectType<?>> orderedObjectTypesView;

   SKLivingShopObjectTypes(BaseEntityShops baseEntityShops, LivingShops livingShops) {
      this.orderedObjectTypesView = Collections.unmodifiableList(this.orderedObjectTypes);
      LivingShopObjectCreationContext context = new LivingShopObjectCreationContext(baseEntityShops, livingShops);
      this.objectTypes = createShopObjectTypes(context);
   }

   private static Map<? extends EntityType, ? extends SKLivingShopObjectType<?>> createShopObjectTypes(LivingShopObjectCreationContext context) {
      Map<EntityType, SKLivingShopObjectType<?>> objectTypes = new HashMap();
      EntityType[] var2 = EntityType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EntityType entityType = var2[var4];
         if (entityType.isAlive() && entityType.isSpawnable()) {
            objectTypes.put(entityType, createLivingShopObjectType(context, entityType));
         }
      }

      return Collections.unmodifiableMap(objectTypes);
   }

   public void onRegisterDefaults() {
      this.reorderShopObjectTypes();
      this.registerPermissions();
   }

   private void reorderShopObjectTypes() {
      this.orderedObjectTypes.clear();
      Iterator var1 = Settings.DerivedSettings.enabledLivingShops.iterator();

      while(var1.hasNext()) {
         EntityType entityType = (EntityType)var1.next();

         assert entityType != null && entityType.isAlive() && entityType.isSpawnable();

         SKLivingShopObjectType<?> objectType = (SKLivingShopObjectType)Unsafe.assertNonNull(this.get(entityType));
         this.orderedObjectTypes.add(objectType);
      }

      this.objectTypes.values().forEach((objectTypex) -> {
         if (!Settings.DerivedSettings.enabledLivingShops.contains(objectTypex.getEntityType())) {
            this.orderedObjectTypes.add(objectTypex);
         }

      });
   }

   private void registerPermissions() {
      this.orderedObjectTypesView.forEach(BaseEntityShopObjectType::registerPermission);
   }

   public Collection<? extends SKLivingShopObjectType<?>> getAll() {
      return this.orderedObjectTypesView;
   }

   @Nullable
   public SKLivingShopObjectType<?> get(EntityType entityType) {
      return (SKLivingShopObjectType)this.objectTypes.get(entityType);
   }

   private static SKLivingShopObjectType<?> createLivingShopObjectType(LivingShopObjectCreationContext context, EntityType entityType) {
      assert entityType.isAlive() && entityType.isSpawnable();

      SKLivingShopObjectType<?> objectType = null;
      switch(entityType) {
      case VILLAGER:
         objectType = new SKLivingShopObjectType(context, entityType, VillagerShop.class, VillagerShop::new);
         break;
      case WANDERING_TRADER:
         objectType = new SKLivingShopObjectType(context, entityType, WanderingTraderShop.class, WanderingTraderShop::new);
         break;
      case PIG:
         objectType = new SKLivingShopObjectType(context, entityType, PigShop.class, PigShop::new);
         break;
      case CHICKEN:
         objectType = new SKLivingShopObjectType(context, entityType, ChickenShop.class, ChickenShop::new);
         break;
      case CREEPER:
         objectType = new SKLivingShopObjectType(context, entityType, CreeperShop.class, CreeperShop::new);
         break;
      case CAT:
         objectType = new SKLivingShopObjectType(context, entityType, CatShop.class, CatShop::new);
         break;
      case ENDERMAN:
         objectType = new SKLivingShopObjectType(context, entityType, EndermanShop.class, EndermanShop::new);
         break;
      case RABBIT:
         objectType = new SKLivingShopObjectType(context, entityType, RabbitShop.class, RabbitShop::new);
         break;
      case SHEEP:
         objectType = new SKLivingShopObjectType(context, entityType, SheepShop.class, SheepShop::new);
         break;
      case ZOMBIE:
         objectType = new SKLivingShopObjectType(context, entityType, ClassUtils.parameterized(ZombieShop.class), ZombieShop::new);
         break;
      case ZOMBIE_VILLAGER:
         objectType = new SKLivingShopObjectType(context, entityType, ZombieVillagerShop.class, ZombieVillagerShop::new);
         break;
      case FOX:
         objectType = new SKLivingShopObjectType(context, entityType, FoxShop.class, FoxShop::new);
         break;
      case PARROT:
         objectType = new SKLivingShopObjectType(context, entityType, ParrotShop.class, ParrotShop::new);
         break;
      case WOLF:
         objectType = new SKLivingShopObjectType(context, entityType, WolfShop.class, WolfShop::new);
         break;
      case HORSE:
         objectType = new SKLivingShopObjectType(context, entityType, HorseShop.class, HorseShop::new);
         break;
      case LLAMA:
         objectType = new SKLivingShopObjectType(context, entityType, ClassUtils.parameterized(LlamaShop.class), LlamaShop::new);
         break;
      case TRADER_LLAMA:
         objectType = new SKLivingShopObjectType(context, entityType, ClassUtils.parameterized(LlamaShop.class), LlamaShop::new);
         break;
      case PANDA:
         objectType = new SKLivingShopObjectType(context, entityType, PandaShop.class, PandaShop::new);
         break;
      case COW:
         objectType = new SKLivingShopObjectType(context, entityType, CowShop.class, CowShop::new);
         break;
      case MOOSHROOM:
         objectType = new SKLivingShopObjectType(context, entityType, MooshroomShop.class, MooshroomShop::new);
         break;
      case SLIME:
         objectType = new SKLivingShopObjectType(context, entityType, SlimeShop.class, SlimeShop::new);
         break;
      case MAGMA_CUBE:
         objectType = new SKLivingShopObjectType(context, entityType, MagmaCubeShop.class, MagmaCubeShop::new);
         break;
      case SNOW_GOLEM:
         objectType = new SKLivingShopObjectType(context, entityType, SnowmanShop.class, SnowmanShop::new);
         break;
      case SHULKER:
         objectType = new SKLivingShopObjectType(context, entityType, ShulkerShop.class, ShulkerShop::new);
         break;
      case TROPICAL_FISH:
         objectType = new SKLivingShopObjectType(context, entityType, TropicalFishShop.class, TropicalFishShop::new);
         break;
      case PUFFERFISH:
         objectType = new SKLivingShopObjectType(context, entityType, PufferFishShop.class, PufferFishShop::new);
         break;
      case AXOLOTL:
         objectType = new SKLivingShopObjectType(context, entityType, AxolotlShop.class, AxolotlShop::new);
         break;
      case GLOW_SQUID:
         objectType = new SKLivingShopObjectType(context, entityType, GlowSquidShop.class, GlowSquidShop::new);
         break;
      case GOAT:
         objectType = new SKLivingShopObjectType(context, entityType, GoatShop.class, GoatShop::new);
         break;
      case FROG:
         objectType = new SKLivingShopObjectType(context, entityType, FrogShop.class, FrogShop::new);
         break;
      case SALMON:
         objectType = new SKLivingShopObjectType(context, entityType, SalmonShop.class, SalmonShop::new);
         break;
      case STRIDER:
         objectType = new SKLivingShopObjectType(context, entityType, StriderShop.class, StriderShop::new);
         break;
      case ARMOR_STAND:
         objectType = new SKLivingShopObjectType(context, entityType, ArmorStandShop.class, ArmorStandShop::new);
         break;
      case BAT:
         objectType = new SKLivingShopObjectType(context, entityType, BatShop.class, BatShop::new);
      }

      if (objectType == null) {
         String var3 = entityType.name();
         byte var4 = -1;
         switch(var3.hashCode()) {
         case -1611381190:
            if (var3.equals("MANNEQUIN")) {
               var4 = 1;
            }
            break;
         case -1428246125:
            if (var3.equals("NAUTILUS")) {
               var4 = 2;
            }
            break;
         case 17859468:
            if (var3.equals("ZOMBIE_NAUTILUS")) {
               var4 = 3;
            }
            break;
         case 973178214:
            if (var3.equals("COPPER_GOLEM")) {
               var4 = 0;
            }
         }

         switch(var4) {
         case 0:
            objectType = new SKLivingShopObjectType(context, entityType, CopperGolemShop.class, CopperGolemShop::new);
            break;
         case 1:
            objectType = new SKLivingShopObjectType(context, entityType, MannequinShop.class, MannequinShop::new);
            break;
         case 2:
            objectType = new SKLivingShopObjectType(context, entityType, NautilusShop.class, NautilusShop::new);
            break;
         case 3:
            objectType = new SKLivingShopObjectType(context, entityType, ZombieNautilusShop.class, ZombieNautilusShop::new);
         }
      }

      if (objectType == null) {
         Class<? extends Entity> entityClass = (Class)Unsafe.assertNonNull(entityType.getEntityClass());
         if (ChestedHorse.class.isAssignableFrom(entityClass)) {
            objectType = new SKLivingShopObjectType(context, entityType, ClassUtils.parameterized(ChestedHorseShop.class), ChestedHorseShop::new);
         } else if (AbstractHorse.class.isAssignableFrom(entityClass)) {
            objectType = new SKLivingShopObjectType(context, entityType, ClassUtils.parameterized(AbstractHorseShop.class), AbstractHorseShop::new);
         } else if (Zombie.class.isAssignableFrom(entityClass)) {
            objectType = new SKLivingShopObjectType(context, entityType, ClassUtils.parameterized(ZombieShop.class), ZombieShop::new);
         } else if (Ageable.class.isAssignableFrom(entityClass)) {
            objectType = new SKLivingShopObjectType(context, entityType, ClassUtils.parameterized(BabyableShop.class), BabyableShop::new);
         } else {
            objectType = new SKLivingShopObjectType(context, entityType, ClassUtils.parameterized(SKLivingShopObject.class), SKLivingShopObject::new);
         }
      }

      assert objectType != null;

      return objectType;
   }
}
