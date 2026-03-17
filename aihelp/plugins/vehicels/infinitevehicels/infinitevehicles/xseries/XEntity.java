package me.PM2.infinitevehicles.xseries;

import com.google.common.base.Enums;
import com.google.common.base.Strings;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.StaticClassHandle;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.TreeSpecies;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Cat;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Frog;
import org.bukkit.entity.GlowSquid;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Mob;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Spellcaster;
import org.bukkit.entity.Strider;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Cat.Type;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.entity.Llama.Color;
import org.bukkit.entity.Panda.Gene;
import org.bukkit.entity.Parrot.Variant;
import org.bukkit.entity.Spellcaster.Spell;
import org.bukkit.entity.TropicalFish.Pattern;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class XEntity {
   public static final Set<EntityType> UNDEAD;
   private static final boolean SUPPORTS_DELAYED_SPAWN;
   private static final MethodHandle DELAYED_SPAWN_1_17;
   private static final MethodHandle DELAYED_SPAWN_1_16_5;
   private static final MethodHandle DELAYED_SPAWN_1_11;
   private static final Object REGISTRY_CAT_VARIANT = supportsRegistry("CAT_VARIANT");
   private static Object REGISTRY_DEFAULT_CAT_VARIANT;
   private static final Map<Class<?>, BiConsumer<Entity, ConfigurationSection>> MAPPING;
   private static final boolean SUPPORTS_Villager_setVillagerLevel;
   private static final boolean SUPPORTS_Villager_setVillagerExperience;
   private static final boolean SUPPORTS_Villager_setVillagerType;

   private static <T extends Entity> void register(Class<T> var0, BiConsumer<T, ConfigurationSection> var1) {
      MAPPING.put(var0, (BiConsumer)cast(var1));
   }

   private static void mapObjectToConfig(Class<? extends Entity> var0) {
      ArrayList var1 = new ArrayList();
      Lookup var2 = MethodHandles.lookup();
      Method[] var3 = var0.getDeclaredMethods();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method var6 = var3[var5];
         String var7 = var6.getName();
         if (var7.startsWith("set")) {
            String var8 = var7.substring(3).replaceAll("[A-Z]", "-");
            if (var8.startsWith("-")) {
               var8 = var7.charAt(3) + var8.substring(1);
            }

            MethodHandle var9;
            try {
               var9 = var2.unreflect(var6);
            } catch (IllegalAccessException var11) {
               throw new IllegalStateException(var11);
            }

            var1.add(new XEntity.MappedConfigObject(var8, var9, (Function)null));
         }
      }

   }

   private XEntity() {
   }

   private static Object supportsRegistry(String var0) {
      try {
         Class var1 = XReflection.ofMinecraft().inPackage("org.bukkit").named("Registry").reflect();
         return XReflection.of(var1).field().asStatic().getter().named(var0).returns(var1).reflect().invoke();
      } catch (Throwable var2) {
         return null;
      }
   }

   private static <T> T getRegistryOrEnum(Class<T> var0, Object var1, String var2, Object var3) {
      if (Strings.isNullOrEmpty(var2)) {
         return var3;
      } else {
         Object var4;
         if (var1 != null) {
            var4 = cast(((Registry)var1).get(fromConfig(var2)));
         } else {
            var4 = cast(Enums.getIfPresent((Class)cast(var0), var2.toUpperCase(Locale.ENGLISH)).orNull());
         }

         return var4 == null ? var3 : var4;
      }
   }

   private static <T> T cast(Object var0) {
      return var0;
   }

   private static NamespacedKey fromConfig(String var0) {
      NamespacedKey var1;
      if (!var0.contains(":")) {
         var1 = NamespacedKey.minecraft(var0.toLowerCase(Locale.ENGLISH));
      } else {
         var1 = NamespacedKey.fromString(var0.toLowerCase(Locale.ENGLISH));
      }

      return (NamespacedKey)Objects.requireNonNull(var1, () -> {
         return "Invalid namespace key: " + var0;
      });
   }

   @NotNull
   private static Type getCatVariant(@Nullable String var0) {
      if (REGISTRY_DEFAULT_CAT_VARIANT == null) {
         REGISTRY_DEFAULT_CAT_VARIANT = getRegistryOrEnum(Type.class, REGISTRY_CAT_VARIANT, "TABBY", (Object)null);
      }

      return (Type)getRegistryOrEnum(Type.class, REGISTRY_CAT_VARIANT, var0, REGISTRY_DEFAULT_CAT_VARIANT);
   }

   public static boolean isUndead(@Nullable EntityType var0) {
      return var0 != null && UNDEAD.contains(var0);
   }

   @Nullable
   public static Entity spawn(@NotNull Location var0, @NotNull ConfigurationSection var1) {
      Objects.requireNonNull(var0, "Cannot spawn entity at a null location.");
      Objects.requireNonNull(var1, "Cannot spawn entity from a null configuration section");
      String var2 = var1.getString("type");
      if (var2 == null) {
         return null;
      } else {
         Optional var3 = XEntityType.of(var2);
         if (!var3.isPresent()) {
            return null;
         } else {
            XEntityType var4 = (XEntityType)((XEntityType)var3.get()).or(XEntityType.ZOMBIE);
            return !var4.isSupported() ? null : spawn(var0, var4.get().getEntityClass(), false, (var1x) -> {
               edit(var1x, var1);
            });
         }
      }
   }

   @NotNull
   public static <T extends Entity> T spawn(@NotNull Location var0, @NotNull Class<T> var1, boolean var2, @Nullable Consumer<? super T> var3) {
      if (SUPPORTS_DELAYED_SPAWN) {
         return var0.getWorld().spawn(var0, var1, var2, var3);
      } else if (DELAYED_SPAWN_1_17 != null) {
         try {
            return DELAYED_SPAWN_1_17.invoke(var0.getWorld(), var0, var1, var2, toBukkitConsumer(var3));
         } catch (Throwable var5) {
            throw new RuntimeException(var5);
         }
      } else if (DELAYED_SPAWN_1_11 != null) {
         try {
            return DELAYED_SPAWN_1_11.invoke(var0.getWorld(), var0, var1, toBukkitConsumer(var3));
         } catch (Throwable var6) {
            throw new RuntimeException(var6);
         }
      } else {
         Entity var4 = var0.getWorld().spawn(var0, var1);
         if (var3 != null) {
            var3.accept(var4);
         }

         return var4;
      }
   }

   @NotNull
   public static <T extends LivingEntity> T spawn(@NotNull Location var0, @NotNull Class<T> var1, @NotNull SpawnReason var2, boolean var3, @Nullable Consumer<? super T> var4) {
      if (SUPPORTS_DELAYED_SPAWN) {
         return var0.getWorld().spawn(var0, var1, var2, var3, var4);
      } else if (!var3 && DELAYED_SPAWN_1_17 != null) {
         try {
            return DELAYED_SPAWN_1_17.invoke(var0.getWorld(), var0, var1, var3, toBukkitConsumer(var4));
         } catch (Throwable var6) {
            throw new RuntimeException(var6);
         }
      } else if (DELAYED_SPAWN_1_16_5 != null) {
         try {
            return DELAYED_SPAWN_1_16_5.invoke(var0.getWorld(), var0, var1, var2, toBukkitConsumer(var4));
         } catch (Throwable var7) {
            throw new RuntimeException(var7);
         }
      } else if (DELAYED_SPAWN_1_11 != null) {
         try {
            return DELAYED_SPAWN_1_11.invoke(var0.getWorld(), var0, var1, toBukkitConsumer(var4));
         } catch (Throwable var8) {
            throw new RuntimeException(var8);
         }
      } else {
         LivingEntity var5 = (LivingEntity)var0.getWorld().spawn(var0, var1);
         if (var4 != null) {
            var4.accept(var5);
         }

         return var5;
      }
   }

   @Nullable
   @Contract("!null -> !null")
   private static <T> org.bukkit.util.Consumer<T> toBukkitConsumer(@Nullable final Consumer<T> var0) {
      return var0 == null ? null : new org.bukkit.util.Consumer<T>() {
         public void accept(T var1) {
            var0.accept(var1);
         }
      };
   }

   private static void map(Class<?> var0, Entity var1, ConfigurationSection var2) {
      if (var0 != Entity.class) {
         BiConsumer var3 = (BiConsumer)MAPPING.get(var0);
         if (var3 != null) {
            var3.accept(var1, var2);
         }

         Class var4 = var0.getSuperclass();
         if (var4 != null) {
            map(var4, var1, var2);
         }

         Class[] var5 = var0.getInterfaces();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Class var8 = var5[var7];
            map(var8, var1, var2);
         }

      }
   }

   @NotNull
   public static Entity edit(@NotNull Entity var0, @NotNull ConfigurationSection var1) {
      Objects.requireNonNull(var0, "Cannot edit properties of a null entity");
      Objects.requireNonNull(var1, "Cannot edit an entity from a null configuration section");
      String var2 = var1.getString("name");
      if (var2 != null) {
         var0.setCustomName(ChatColor.translateAlternateColorCodes('&', var2));
         var0.setCustomNameVisible(true);
      }

      if (var1.isSet("glowing")) {
         var0.setGlowing(var1.getBoolean("glowing"));
      }

      if (var1.isSet("gravity")) {
         var0.setGravity(var1.getBoolean("gravity"));
      }

      if (var1.isSet("silent")) {
         var0.setSilent(var1.getBoolean("silent"));
      }

      var0.setFireTicks(var1.getInt("fire-ticks"));
      var0.setFallDistance((float)var1.getInt("fall-distance"));
      if (var1.isSet("invulnerable")) {
         var0.setInvulnerable(var1.getBoolean("invulnerable"));
      }

      int var3 = var1.getInt("ticks-lived");
      if (var3 > 0) {
         var0.setTicksLived(var3);
      }

      if (var1.isSet("portal-cooldown")) {
         var0.setPortalCooldown(var1.getInt("portal-cooldown", -1));
      }

      ConfigurationSection var5;
      if (XReflection.supports(13) && var0 instanceof Boss) {
         Boss var4 = (Boss)var0;
         var5 = var1.getConfigurationSection("bossbar");
         if (var5 != null) {
            BossBar var6 = var4.getBossBar();
            editBossBar(var6, var5);
         }
      }

      if (var0 instanceof Vehicle && var0 instanceof Boat) {
         Boat var14 = (Boat)var0;
         String var17 = var1.getString("tree-species");
         if (var17 != null) {
            com.google.common.base.Optional var22 = Enums.getIfPresent(TreeSpecies.class, var17);
            if (var22.isPresent()) {
               var14.setWoodType((TreeSpecies)var22.get());
            }
         }
      }

      if (var0 instanceof LivingEntity) {
         LivingEntity var15 = (LivingEntity)var0;
         if (var1.isSet("health")) {
            double var19 = var1.getDouble("health");
            var15.getAttribute((Attribute)XAttribute.MAX_HEALTH.get()).setBaseValue(var19);
            var15.setHealth(var19);
         }

         if (XReflection.supports(14)) {
            var15.setAbsorptionAmount((double)var1.getInt("absorption"));
         }

         if (var1.isSet("AI")) {
            var15.setAI(var1.getBoolean("AI"));
         }

         if (var1.isSet("can-pickup-items")) {
            var15.setCanPickupItems(var1.getBoolean("can-pickup-items"));
         }

         if (var1.isSet("collidable")) {
            var15.setCollidable(var1.getBoolean("collidable"));
         }

         if (var1.isSet("gliding")) {
            var15.setGliding(var1.getBoolean("gliding"));
         }

         if (var1.isSet("remove-when-far-away")) {
            var15.setRemoveWhenFarAway(var1.getBoolean("remove-when-far-away"));
         }

         if (XReflection.supports(13) && var1.isSet("swimming")) {
            var15.setSwimming(var1.getBoolean("swimming"));
         }

         if (var1.isSet("max-air")) {
            var15.setMaximumAir(var1.getInt("max-air"));
         }

         if (var1.isSet("no-damage-ticks")) {
            var15.setNoDamageTicks(var1.getInt("no-damage-ticks"));
         }

         if (var1.isSet("remaining-air")) {
            var15.setRemainingAir(var1.getInt("remaining-air"));
         }

         XPotion.addEffects(var15, var1.getStringList("effects"));
         var5 = var1.getConfigurationSection("equipment");
         boolean var7;
         ConfigurationSection var11;
         if (var5 != null) {
            EntityEquipment var23 = var15.getEquipment();
            var7 = var0 instanceof Mob;
            ConfigurationSection var8 = var5.getConfigurationSection("helmet");
            if (var8 != null) {
               var23.setHelmet(XItemStack.deserialize(var8.getConfigurationSection("item")));
               if (var7) {
                  var23.setHelmetDropChance((float)var8.getInt("drop-chance"));
               }
            }

            ConfigurationSection var9 = var5.getConfigurationSection("chestplate");
            if (var9 != null) {
               var23.setChestplate(XItemStack.deserialize(var9.getConfigurationSection("item")));
               if (var7) {
                  var23.setChestplateDropChance((float)var9.getInt("drop-chance"));
               }
            }

            ConfigurationSection var10 = var5.getConfigurationSection("leggings");
            if (var10 != null) {
               var23.setLeggings(XItemStack.deserialize(var10.getConfigurationSection("item")));
               if (var7) {
                  var23.setLeggingsDropChance((float)var10.getInt("drop-chance"));
               }
            }

            var11 = var5.getConfigurationSection("boots");
            if (var11 != null) {
               var23.setBoots(XItemStack.deserialize(var11.getConfigurationSection("item")));
               if (var7) {
                  var23.setBootsDropChance((float)var11.getInt("drop-chance"));
               }
            }

            ConfigurationSection var12 = var5.getConfigurationSection("main-hand");
            if (var12 != null) {
               var23.setItemInMainHand(XItemStack.deserialize(var12.getConfigurationSection("item")));
               if (var7) {
                  var23.setItemInMainHandDropChance((float)var12.getInt("drop-chance"));
               }
            }

            ConfigurationSection var13 = var5.getConfigurationSection("off-hand");
            if (var13 != null) {
               var23.setItemInOffHand(XItemStack.deserialize(var13.getConfigurationSection("item")));
               if (var7) {
                  var23.setItemInOffHandDropChance((float)var13.getInt("drop-chance"));
               }
            }
         }

         if (var15 instanceof Ageable) {
            Ageable var24 = (Ageable)var15;
            if (var1.isSet("breed")) {
               var24.setBreed(var1.getBoolean("breed"));
            }

            if (var1.isSet("baby")) {
               if (var1.getBoolean("baby")) {
                  var24.setBaby();
               } else {
                  var24.setAdult();
               }
            }

            int var26 = var1.getInt("age", 0);
            if (var26 > 0) {
               var24.setAge(var26);
            }

            if (var1.isSet("age-lock")) {
               var24.setAgeLock(var1.getBoolean("age-lock"));
            }

            if (var15 instanceof Animals) {
               Animals var32 = (Animals)var15;
               int var38 = var1.getInt("love-mode");
               if (var38 != 0) {
                  var32.setLoveModeTicks(var38);
               }

               if (var15 instanceof Tameable) {
                  Tameable var45 = (Tameable)var15;
                  var45.setTamed(var1.getBoolean("tamed"));
               }
            }
         }

         if (var15 instanceof Sittable) {
            Sittable var25 = (Sittable)var15;
            var25.setSitting(var1.getBoolean("sitting"));
         }

         String var28;
         if (var15 instanceof Spellcaster) {
            Spellcaster var27 = (Spellcaster)var15;
            var28 = var1.getString("spell");
            if (var28 != null) {
               var27.setSpell((Spell)Enums.getIfPresent(Spell.class, var28).or(Spell.NONE));
            }
         }

         if (var15 instanceof AbstractHorse) {
            AbstractHorse var29 = (AbstractHorse)var15;
            if (var1.isSet("domestication")) {
               var29.setDomestication(var1.getInt("domestication"));
            }

            if (var1.isSet("jump-strength")) {
               var29.setJumpStrength(var1.getDouble("jump-strength"));
            }

            if (var1.isSet("max-domestication")) {
               var29.setMaxDomestication(var1.getInt("max-domestication"));
            }

            ConfigurationSection var30 = var1.getConfigurationSection("items");
            if (var30 != null) {
               AbstractHorseInventory var34 = var29.getInventory();
               Iterator var41 = var30.getKeys(false).iterator();

               while(var41.hasNext()) {
                  String var48 = (String)var41.next();
                  var11 = var30.getConfigurationSection(var48);
                  int var51 = var11.getInt("slot", -1);
                  if (var51 != -1) {
                     ItemStack var52 = XItemStack.deserialize(var11);
                     if (var52 != null) {
                        var34.setItem(var51, var52);
                     }
                  }
               }
            }

            if (var15 instanceof ChestedHorse) {
               ChestedHorse var36 = (ChestedHorse)var15;
               boolean var43 = var1.getBoolean("has-chest");
               if (var43) {
                  var36.setCarryingChest(true);
               }
            }
         }

         map(var0.getClass(), var0, var1);
         if (var15 instanceof Villager) {
            Villager var31 = (Villager)var15;
            if (SUPPORTS_Villager_setVillagerLevel) {
               var31.setVillagerLevel(var1.getInt("level"));
            }

            if (SUPPORTS_Villager_setVillagerExperience) {
               var31.setVillagerExperience(var1.getInt("xp"));
            }
         } else if (var15 instanceof Enderman) {
            Enderman var33 = (Enderman)var15;
            var28 = var1.getString("carrying");
            if (var28 != null) {
               Optional var39 = XMaterial.matchXMaterial(var28);
               if (var39.isPresent()) {
                  ItemStack var46 = ((XMaterial)var39.get()).parseItem();
                  if (var46 != null) {
                     var33.setCarriedMaterial(var46.getData());
                  }
               }
            }
         } else if (var15 instanceof Sheep) {
            Sheep var35 = (Sheep)var15;
            var7 = var1.getBoolean("sheared");
            if (var7) {
               var35.setSheared(true);
            }
         } else if (var15 instanceof Rabbit) {
            Rabbit var40 = (Rabbit)var15;
            var40.setRabbitType((org.bukkit.entity.Rabbit.Type)Enums.getIfPresent(org.bukkit.entity.Rabbit.Type.class, var1.getString("color")).or(org.bukkit.entity.Rabbit.Type.WHITE));
         } else if (var15 instanceof Bat) {
            Bat var42 = (Bat)var15;
            if (!var1.getBoolean("awake")) {
               var42.setAwake(false);
            }
         } else if (var15 instanceof Wolf) {
            Wolf var44 = (Wolf)var15;
            var44.setAngry(var1.getBoolean("angry"));
            var44.setCollarColor((DyeColor)Enums.getIfPresent(DyeColor.class, var1.getString("color")).or(DyeColor.GREEN));
         } else if (var15 instanceof Creeper) {
            Creeper var47 = (Creeper)var15;
            var47.setExplosionRadius(var1.getInt("explosion-radius"));
            var47.setMaxFuseTicks(var1.getInt("max-fuse-ticks"));
            var47.setPowered(var1.getBoolean("powered"));
         } else if (XReflection.supports(10) && XReflection.supports(11)) {
            if (var15 instanceof Llama) {
               Llama var49 = (Llama)var15;
               if (var1.isSet("strength")) {
                  var49.setStrength(var1.getInt("strength"));
               }

               com.google.common.base.Optional var37 = Enums.getIfPresent(Color.class, var1.getString("color"));
               if (var37.isPresent()) {
                  var49.setColor((Color)var37.get());
               }
            } else if (XReflection.supports(12)) {
               if (var15 instanceof Parrot) {
                  Parrot var50 = (Parrot)var15;
                  var50.setVariant((Variant)Enums.getIfPresent(Variant.class, var1.getString("color")).or(Variant.RED));
               }

               if (XReflection.supports(13)) {
                  thirteen(var0, var1);
               }

               if (XReflection.supports(14)) {
                  fourteen(var0, var1);
               }

               if (XReflection.supports(15)) {
                  fifteen(var0, var1);
               }

               if (XReflection.supports(16)) {
                  sixteen(var0, var1);
               }

               if (XReflection.supports(17)) {
                  seventeen(var0, var1);
               }
            }
         }
      } else if (var0 instanceof EnderSignal) {
         EnderSignal var16 = (EnderSignal)var0;
         var16.setDespawnTimer(var1.getInt("despawn-timer"));
         var16.setDropItem(var1.getBoolean("drop-item"));
      } else if (var0 instanceof ExperienceOrb) {
         ExperienceOrb var18 = (ExperienceOrb)var0;
         var18.setExperience(var1.getInt("exp"));
      } else if (var0 instanceof Explosive) {
         Explosive var20 = (Explosive)var0;
         var20.setYield((float)var1.getDouble("yield"));
         var20.setIsIncendiary(var1.getBoolean("incendiary"));
      } else if (var0 instanceof EnderCrystal) {
         EnderCrystal var21 = (EnderCrystal)var0;
         var21.setShowingBottom(var1.getBoolean("show-bottom"));
      }

      return var0;
   }

   private static void fourteen(Entity var0, ConfigurationSection var1) {
      if (var0 instanceof Raider) {
         Raider var2 = (Raider)var0;
         if (var1.isSet("can-join-raid")) {
            var2.setCanJoinRaid(var1.getBoolean("can-join-raid"));
         }

         if (var1.isSet("is-patrol-leader")) {
            var2.setCanJoinRaid(var1.getBoolean("is-patrol-leader"));
         }
      } else if (var0 instanceof Cat) {
         Cat var3 = (Cat)var0;
         var3.setCatType(getCatVariant(var1.getString("variant")));
         var3.setCollarColor((DyeColor)Enums.getIfPresent(DyeColor.class, var1.getString("color")).or(DyeColor.GREEN));
      } else if (var0 instanceof Fox) {
         Fox var4 = (Fox)var0;
         var4.setCrouching(var1.getBoolean("crouching"));
         var4.setSleeping(var1.getBoolean("sleeping"));
         var4.setFoxType((org.bukkit.entity.Fox.Type)Enums.getIfPresent(org.bukkit.entity.Fox.Type.class, var1.getString("color")).or(org.bukkit.entity.Fox.Type.RED));
      } else if (var0 instanceof Panda) {
         Panda var5 = (Panda)var0;
         var5.setHiddenGene((Gene)Enums.getIfPresent(Gene.class, var1.getString("hidden-gene")).or(Gene.PLAYFUL));
         var5.setMainGene((Gene)Enums.getIfPresent(Gene.class, var1.getString("main-gene")).or(Gene.NORMAL));
      } else if (var0 instanceof MushroomCow) {
         MushroomCow var6 = (MushroomCow)var0;
         var6.setVariant((org.bukkit.entity.MushroomCow.Variant)Enums.getIfPresent(org.bukkit.entity.MushroomCow.Variant.class, var1.getString("color")).or(org.bukkit.entity.MushroomCow.Variant.RED));
      }

   }

   private static void thirteen(Entity var0, ConfigurationSection var1) {
      if (var0 instanceof Husk) {
         Husk var2 = (Husk)var0;
         var2.setConversionTime(var1.getInt("conversion-time"));
      } else if (var0 instanceof Vex) {
         Vex var3 = (Vex)var0;
         var3.setCharging(var1.getBoolean("charging"));
      } else if (var0 instanceof PufferFish) {
         PufferFish var4 = (PufferFish)var0;
         var4.setPuffState(var1.getInt("puff-state"));
      } else if (var0 instanceof TropicalFish) {
         TropicalFish var5 = (TropicalFish)var0;
         var5.setBodyColor((DyeColor)Enums.getIfPresent(DyeColor.class, var1.getString("color")).or(DyeColor.WHITE));
         var5.setPattern((Pattern)Enums.getIfPresent(Pattern.class, var1.getString("pattern")).or(Pattern.BETTY));
         var5.setPatternColor((DyeColor)Enums.getIfPresent(DyeColor.class, var1.getString("pattern-color")).or(DyeColor.WHITE));
      } else if (var0 instanceof EnderDragon) {
         EnderDragon var6 = (EnderDragon)var0;
         var6.setPhase((Phase)Enums.getIfPresent(Phase.class, var1.getString("phase")).or(Phase.ROAR_BEFORE_ATTACK));
      } else if (var0 instanceof Phantom) {
         Phantom var7 = (Phantom)var0;
         var7.setSize(var1.getInt("size"));
      }

   }

   private static void fifteen(Entity var0, ConfigurationSection var1) {
      if (var0 instanceof Bee) {
         Bee var2 = (Bee)var0;
         var2.setAnger(var1.getInt("anger") * 20);
         var2.setHasNectar(var1.getBoolean("nectar"));
         var2.setHasStung(var1.getBoolean("stung"));
         var2.setCannotEnterHiveTicks(var1.getInt("disallow-hive") * 20);
      }

   }

   private static void sixteen(Entity var0, ConfigurationSection var1) {
      if (var0 instanceof Hoglin) {
         Hoglin var2 = (Hoglin)var0;
         var2.setConversionTime(var1.getInt("conversation") * 20);
         var2.setImmuneToZombification(var1.getBoolean("zombification-immunity"));
         var2.setIsAbleToBeHunted(var1.getBoolean("can-be-hunted"));
      } else if (var0 instanceof Piglin) {
         Piglin var3 = (Piglin)var0;
         var3.setConversionTime(var1.getInt("conversation") * 20);
         var3.setImmuneToZombification(var1.getBoolean("zombification-immunity"));
      } else if (var0 instanceof Strider) {
         Strider var4 = (Strider)var0;
         var4.setShivering(var1.getBoolean("shivering"));
      }

   }

   private static void frog(Entity var0, ConfigurationSection var1) {
      Frog var2 = (Frog)var0;
      var2.setVariant((org.bukkit.entity.Frog.Variant)Registry.FROG_VARIANT.get(fromConfig(var1.getString("variant"))));
   }

   private static boolean seventeen(Entity var0, ConfigurationSection var1) {
      if (var0 instanceof Axolotl) {
         Axolotl var6 = (Axolotl)var0;
         String var3 = var1.getString("variant");
         if (Strings.isNullOrEmpty(var3)) {
            com.google.common.base.Optional var4 = Enums.getIfPresent(org.bukkit.entity.Axolotl.Variant.class, var3);
            if (var4.isPresent()) {
               var6.setVariant((org.bukkit.entity.Axolotl.Variant)var4.get());
            }
         }

         if (var1.isSet("playing-dead")) {
            var6.setPlayingDead(var1.getBoolean("playing-dead"));
         }

         return true;
      } else if (var0 instanceof Goat) {
         Goat var5 = (Goat)var0;
         if (var1.isSet("screaming")) {
            var5.setScreaming(var1.getBoolean("screaming"));
         }

         return true;
      } else if (var0 instanceof GlowSquid) {
         GlowSquid var2 = (GlowSquid)var0;
         if (var1.isSet("dark-ticks-remaining")) {
            var2.setDarkTicksRemaining(var1.getInt("dark-ticks-remaining"));
         }

         return true;
      } else {
         return false;
      }
   }

   public static void editBossBar(BossBar var0, ConfigurationSection var1) {
      String var2 = var1.getString("title");
      if (var2 != null) {
         var0.setTitle(ChatColor.translateAlternateColorCodes('&', var2));
      }

      String var3 = var1.getString("color");
      if (var3 != null) {
         com.google.common.base.Optional var4 = Enums.getIfPresent(BarColor.class, var3.toUpperCase(Locale.ENGLISH));
         if (var4.isPresent()) {
            var0.setColor((BarColor)var4.get());
         }
      }

      String var11 = var1.getString("style");
      if (var11 != null) {
         com.google.common.base.Optional var5 = Enums.getIfPresent(BarStyle.class, var11.toUpperCase(Locale.ENGLISH));
         if (var5.isPresent()) {
            var0.setStyle((BarStyle)var5.get());
         }
      }

      List var12 = var1.getStringList("flags");
      if (!var12.isEmpty()) {
         EnumSet var6 = EnumSet.noneOf(BarFlag.class);
         Iterator var7 = var12.iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            BarFlag var9 = (BarFlag)Enums.getIfPresent(BarFlag.class, var8.toUpperCase(Locale.ENGLISH)).orNull();
            if (var9 != null) {
               var6.add(var9);
            }
         }

         BarFlag[] var13 = BarFlag.values();
         int var14 = var13.length;

         for(int var15 = 0; var15 < var14; ++var15) {
            BarFlag var10 = var13[var15];
            if (var6.contains(var10)) {
               var0.addFlag(var10);
            } else {
               var0.removeFlag(var10);
            }
         }
      }

   }

   static {
      MethodHandle var1 = null;
      MethodHandle var2 = null;
      MethodHandle var3 = null;

      boolean var0;
      try {
         World.class.getMethod("spawn", Location.class, Class.class, Boolean.TYPE, Consumer.class);
         var0 = true;
      } catch (NoSuchMethodException var9) {
         var0 = false;
      }

      Lookup var4 = MethodHandles.lookup();

      try {
         var1 = var4.unreflect(World.class.getMethod("spawn", Location.class, Class.class, Boolean.TYPE, org.bukkit.util.Consumer.class));
      } catch (Throwable var8) {
      }

      try {
         var2 = var4.unreflect(World.class.getMethod("spawn", Location.class, Class.class, SpawnReason.class, org.bukkit.util.Consumer.class));
      } catch (Throwable var7) {
      }

      try {
         var3 = var4.unreflect(World.class.getMethod("spawn", Location.class, Class.class, org.bukkit.util.Consumer.class));
      } catch (Throwable var6) {
      }

      SUPPORTS_DELAYED_SPAWN = var0;
      DELAYED_SPAWN_1_17 = var1;
      DELAYED_SPAWN_1_16_5 = var2;
      DELAYED_SPAWN_1_11 = var3;
      MAPPING = new HashMap(20);
      if (XReflection.supports(19)) {
         register(Frog.class, XEntity::frog);
      }

      StaticClassHandle var10 = XReflection.of(Villager.class);
      SUPPORTS_Villager_setVillagerLevel = var10.method("void setVillagerLevel(int var1);").exists();
      SUPPORTS_Villager_setVillagerExperience = var10.method("void setVillagerExperience(int xp);").exists();
      SUPPORTS_Villager_setVillagerType = var10.method().named("setVillagerType").returns(Void.TYPE).parameters(var10.inner(XReflection.ofMinecraft().named("Type"))).exists();
      EnumSet var11 = EnumSet.of(EntityType.SKELETON, EntityType.ZOMBIE, EntityType.GIANT, EntityType.ZOMBIE_VILLAGER, EntityType.WITHER, EntityType.WITHER_SKELETON, EntityType.ZOMBIE_HORSE);
      if (XReflection.supports(10)) {
         var11.add(EntityType.HUSK);
         var11.add(EntityType.STRAY);
         if (XReflection.supports(11)) {
            var11.add(EntityType.SKELETON_HORSE);
            if (XReflection.supports(13)) {
               var11.add(EntityType.DROWNED);
               var11.add(EntityType.PHANTOM);
               if (XReflection.supports(16)) {
                  var11.add(EntityType.ZOGLIN);
                  var11.add(EntityType.PIGLIN);
                  var11.add(EntityType.ZOMBIFIED_PIGLIN);
               }
            }
         }
      }

      if (!XReflection.supports(16)) {
         var11.add(EntityType.valueOf("PIG_ZOMBIE"));
      }

      UNDEAD = Collections.unmodifiableSet(var11);
   }

   private static final class MappedConfigObject {
      private final String configEntry;
      private final MethodHandle setter;
      private final Function<ConfigurationSection, Object> configurationValue;

      private MappedConfigObject(String var1, MethodHandle var2, Function<ConfigurationSection, Object> var3) {
         this.configEntry = var1;
         this.setter = var2;
         this.configurationValue = var3;
      }

      private void handle(Entity var1, ConfigurationSection var2) {
         if (var2.isSet(this.configEntry)) {
            try {
               this.setter.invoke(this.setter, this.configurationValue.apply(var2));
            } catch (Throwable var4) {
               throw new IllegalStateException(var4);
            }
         }

      }

      // $FF: synthetic method
      MappedConfigObject(String var1, MethodHandle var2, Function var3, Object var4) {
         this(var1, var2, var3);
      }
   }
}
