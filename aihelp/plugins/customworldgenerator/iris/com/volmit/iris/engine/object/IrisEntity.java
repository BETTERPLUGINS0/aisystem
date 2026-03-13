package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.service.ExternalDataSVC;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.RegistryListSpecialEntity;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.data.registry.Particles;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.plugin.Chunks;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attributable;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Panda.Gene;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.bukkit.util.Vector;

@Desc("Represents an iris entity.")
public class IrisEntity extends IrisRegistrant {
   @Required
   @Desc("The type of entity to spawn. To spawn a mythic mob, set this type to unknown and define mythic type.")
   private EntityType type;
   @Desc("The SpawnReason to spawn the entity with.")
   private SpawnReason reason;
   @Desc("The custom name of this entity")
   private String customName;
   @Desc("Should the name on this entity be visible even if you arent looking at it.")
   private boolean customNameVisible;
   @Desc("If this entity type is a mob, should it be aware of it's surroundings & interact with the world.")
   private boolean aware;
   @Desc("If this entity type is a creature, should it have ai goals.")
   private boolean ai;
   @Desc("Should this entity be glowing")
   private boolean glowing;
   @Desc("Should gravity apply to this entity")
   private boolean gravity;
   @Desc("When an entity is invulnerable it can only be damaged by players increative mode.")
   private boolean invulnerable;
   @Desc("When an entity is silent it will not produce any sound.")
   private boolean silent;
   @Desc("Should this entity be allowed to pickup items")
   private boolean pickupItems;
   @Desc("Should this entity be removed when far away")
   private boolean removable;
   @Desc("Entity helmet equipment")
   private IrisLoot helmet;
   @Desc("Entity chestplate equipment")
   private IrisLoot chestplate;
   @Desc("Entity boots equipment")
   private IrisLoot boots;
   @Desc("Entity leggings equipment")
   private IrisLoot leggings;
   @Desc("Entity main hand equipment")
   private IrisLoot mainHand;
   @Desc("Entity off hand equipment")
   private IrisLoot offHand;
   @Desc("Make other entities ride this entity")
   @ArrayType(
      min = 1,
      type = IrisEntity.class
   )
   private KList<IrisEntity> passengers;
   @Desc("Attribute modifiers for this entity")
   @ArrayType(
      min = 1,
      type = IrisAttributeModifier.class
   )
   private KList<IrisAttributeModifier> attributes;
   @Desc("Loot tables for drops")
   private IrisLootReference loot;
   @Desc("If specified, this entity will be leashed by this entity. I.e. THIS ENTITY Leashed by SPECIFIED. This has no effect on EnderDragons, Withers, Players, or Bats.Non-living entities excluding leashes will not persist as leashholders.")
   private IrisEntity leashHolder;
   @Desc("If specified, this entity will spawn with an effect")
   private IrisEffect spawnEffect;
   @Desc("Simply moves the entity from below the surface slowly out of the ground as a spawn-in effect")
   private boolean spawnEffectRiseOutOfGround;
   @Desc("The main gene for a panda if the entity type is a panda")
   private Gene pandaMainGene;
   @Desc("The hidden gene for a panda if the entity type is a panda")
   private Gene pandaHiddenGene;
   @Desc("The this entity is ageable, set it's baby status")
   private boolean baby;
   @Desc("If the entity should never be culled. Useful for Jigsaws")
   private boolean keepEntity;
   @Desc("The surface type to spawn this mob on")
   private IrisSurface surface;
   @RegistryListSpecialEntity
   @Desc("Create a mob from another plugin, such as Mythic Mobs. Should be in the format of a namespace of PluginName:MobName")
   private String specialType;
   @Desc("Set to true if you want to apply all of the settings here to the mob, even though an external plugin has already done so. Scripts are always applied.")
   private boolean applySettingsToCustomMobAnyways;
   @Desc("Set the entity type to UNKNOWN, then define a script here which ends with the entity variable (the result). You can use location to find the target location. You can spawn any entity this way.\nFile extension: .spawn.kts")
   @RegistryListResource(IrisScript.class)
   private String spawnerScript;
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("Executed post spawn you can modify the entity however you want with it\nFile extension: .postspawn.kts")
   @RegistryListResource(IrisScript.class)
   private KList<String> postSpawnScripts;
   @ArrayType(
      min = 1,
      type = IrisCommand.class
   )
   @Desc("Run raw commands when this entity is spawned. Use {x}, {y}, and {z} for location. /summon pig {x} {y} {z}")
   private KList<IrisCommand> rawCommands;

   public Entity spawn(Engine gen, Location at) {
      return this.spawn(var1, var2, new RNG((long)var2.hashCode()));
   }

   public Entity spawn(Engine gen, Location at, RNG rng) {
      if (!Chunks.isSafe(var2)) {
         return null;
      } else {
         if (this.isSpawnEffectRiseOutOfGround()) {
            AtomicReference var4 = new AtomicReference(var2);

            try {
               J.sfut(() -> {
                  if (Chunks.hasPlayersNearby((Location)var4.get())) {
                     Location var1 = ((Location)var4.get()).clone();
                     Location var2 = new Location(var1.getWorld(), var1.getX(), var1.getY() - 5.0D, var1.getZ());
                     var4.set(var2);
                  }

               }).get();
            } catch (InterruptedException var13) {
               var13.printStackTrace();
            } catch (ExecutionException var14) {
               var14.printStackTrace();
            }

            var2 = (Location)var4.get();
         }

         Entity var16 = this.doSpawn(var2);
         if (var16 == null && !Chunks.isSafe(var2)) {
            return null;
         } else {
            if (!this.spawnerScript.isEmpty() && var16 == null) {
               synchronized(this) {
                  try {
                     var16 = (Entity)var1.getExecution().spawnMob(this.spawnerScript, var2);
                  } catch (Throwable var11) {
                     Iris.error("You must return an Entity in your scripts to use entity scripts!");
                     var11.printStackTrace();
                  }
               }
            }

            if (this.isSpecialType() && !this.applySettingsToCustomMobAnyways) {
               return var16;
            } else if (var16 == null) {
               return null;
            } else {
               Entity var5 = var16;
               var16.setCustomName(this.getCustomName() != null ? C.translateAlternateColorCodes('&', this.getCustomName()) : null);
               var16.setCustomNameVisible(this.isCustomNameVisible());
               var16.setGlowing(this.isGlowing());
               var16.setGravity(this.isGravity());
               var16.setInvulnerable(this.isInvulnerable());
               var16.setSilent(this.isSilent());
               var16.setPersistent(this.isKeepEntity() || IrisSettings.get().getWorld().isForcePersistEntities());
               int var6 = 0;
               Iterator var7 = this.passengers.iterator();

               while(var7.hasNext()) {
                  IrisEntity var8 = (IrisEntity)var7.next();
                  Entity var9 = var8.spawn(var1, var2, var3.nextParallelRNG(234858 + var6++));
                  if (!Bukkit.isPrimaryThread()) {
                     J.s(() -> {
                        var5.addPassenger(var9);
                     });
                  }
               }

               Iterator var20;
               if (var5 instanceof Attributable) {
                  Attributable var17 = (Attributable)var5;
                  var20 = this.getAttributes().iterator();

                  while(var20.hasNext()) {
                     IrisAttributeModifier var23 = (IrisAttributeModifier)var20.next();
                     var23.apply(var3, var17);
                  }
               }

               if (var5 instanceof Lootable) {
                  Lootable var18 = (Lootable)var5;
                  if (this.getLoot().getTables().isNotEmpty()) {
                     var18.setLootTable(new LootTable() {
                        public NamespacedKey getKey() {
                           return new NamespacedKey(Iris.instance, "loot-" + IrisEntity.this.hashCode());
                        }

                        public Collection<ItemStack> populateLoot(Random random, LootContext context) {
                           KList var3x = new KList();
                           Iterator var4 = IrisEntity.this.getLoot().getTables().iterator();

                           while(var4.hasNext()) {
                              String var5 = (String)var4.next();
                              IrisLootTable var6 = (IrisLootTable)var1.getData().getLootLoader().load(var5);
                              var3x.addAll(var6.getLoot(var1.isStudio(), var3.nextParallelRNG(345911), InventorySlotType.STORAGE, var2.getWorld(), var2.getBlockX(), var2.getBlockY(), var2.getBlockZ()));
                           }

                           return var3x;
                        }

                        public void fillInventory(Inventory inventory, Random random, LootContext context) {
                           Iterator var4 = this.populateLoot(var2x, var3x).iterator();

                           while(var4.hasNext()) {
                              ItemStack var5 = (ItemStack)var4.next();
                              var1x.addItem(new ItemStack[]{var5});
                           }

                           var1.scramble(var1x, var3);
                        }
                     });
                  }
               }

               if (var5 instanceof LivingEntity) {
                  LivingEntity var19 = (LivingEntity)var5;
                  var19.setAI(this.isAi());
                  var19.setCanPickupItems(this.isPickupItems());
                  if (this.getLeashHolder() != null) {
                     var19.setLeashHolder(this.getLeashHolder().spawn(var1, var2, var3.nextParallelRNG(234548)));
                  }

                  var19.setRemoveWhenFarAway(this.isRemovable());
                  if (this.getHelmet() != null && var3.i(1, this.getHelmet().getRarity()) == 1) {
                     var19.getEquipment().setHelmet(this.getHelmet().get(var1.isStudio(), var3));
                  }

                  if (this.getChestplate() != null && var3.i(1, this.getChestplate().getRarity()) == 1) {
                     var19.getEquipment().setChestplate(this.getChestplate().get(var1.isStudio(), var3));
                  }

                  if (this.getLeggings() != null && var3.i(1, this.getLeggings().getRarity()) == 1) {
                     var19.getEquipment().setLeggings(this.getLeggings().get(var1.isStudio(), var3));
                  }

                  if (this.getBoots() != null && var3.i(1, this.getBoots().getRarity()) == 1) {
                     var19.getEquipment().setBoots(this.getBoots().get(var1.isStudio(), var3));
                  }

                  if (this.getMainHand() != null && var3.i(1, this.getMainHand().getRarity()) == 1) {
                     var19.getEquipment().setItemInMainHand(this.getMainHand().get(var1.isStudio(), var3));
                  }

                  if (this.getOffHand() != null && var3.i(1, this.getOffHand().getRarity()) == 1) {
                     var19.getEquipment().setItemInOffHand(this.getOffHand().get(var1.isStudio(), var3));
                  }
               }

               if (var5 instanceof Ageable && this.isBaby()) {
                  ((Ageable)var5).setBaby();
               }

               if (var5 instanceof Panda) {
                  ((Panda)var5).setMainGene(this.getPandaMainGene());
                  ((Panda)var5).setMainGene(this.getPandaHiddenGene());
               }

               if (var5 instanceof Villager) {
                  Villager var21 = (Villager)var5;
                  var21.setRemoveWhenFarAway(false);
                  Bukkit.getScheduler().scheduleSyncDelayedTask(Iris.instance, () -> {
                     var21.setPersistent(true);
                  }, 1L);
               }

               if (var5 instanceof Mob) {
                  Mob var22 = (Mob)var5;
                  var22.setAware(this.isAware());
               }

               if (this.spawnEffect != null) {
                  this.spawnEffect.apply(var5);
               }

               if (this.postSpawnScripts.isNotEmpty()) {
                  synchronized(this) {
                     var20 = this.postSpawnScripts.iterator();

                     while(var20.hasNext()) {
                        String var24 = (String)var20.next();
                        var1.getExecution().postSpawnMob(var24, var2, var16);
                     }
                  }
               }

               if (this.rawCommands.isNotEmpty()) {
                  this.rawCommands.forEach((var1x) -> {
                     var1x.run(var2);
                  });
               }

               J.s(() -> {
                  if (this.isSpawnEffectRiseOutOfGround() && var5 instanceof LivingEntity && Chunks.hasPlayersNearby(var2)) {
                     Location var3 = var2.clone();
                     var5.setInvulnerable(true);
                     ((LivingEntity)var5).setAI(false);
                     ((LivingEntity)var5).setCollidable(false);
                     ((LivingEntity)var5).setNoDamageTicks(100000);
                     AtomicInteger var4 = new AtomicInteger(0);
                     AtomicInteger var5x = new AtomicInteger(0);
                     var5x.set(J.sr(() -> {
                        if (var4.get() > 100) {
                           J.csr(var5x.get());
                        } else {
                           var4.incrementAndGet();
                           if (!var5.getLocation().getBlock().getType().isSolid() && !((LivingEntity)var5).getEyeLocation().getBlock().getType().isSolid()) {
                              J.csr(var5x.get());
                              ((LivingEntity)var5).setNoDamageTicks(0);
                              ((LivingEntity)var5).setCollidable(true);
                              ((LivingEntity)var5).setAI(true);
                              var5.setInvulnerable(false);
                           } else {
                              var5.teleport(var3.add(new Vector(0.0D, 0.1D, 0.0D)));
                              ItemStack var4x = new ItemStack(((LivingEntity)var5).getEyeLocation().clone().subtract(0.0D, 2.0D, 0.0D).getBlock().getBlockData().getMaterial());
                              var5.getWorld().spawnParticle(Particles.ITEM, ((LivingEntity)var5).getEyeLocation(), 6, 0.2D, 0.4D, 0.2D, 0.05999999865889549D, var4x);
                              if (M.r(0.2D)) {
                                 var5.getWorld().playSound(var5.getLocation(), Sound.BLOCK_CHORUS_FLOWER_GROW, 0.8F, 0.1F);
                              }
                           }

                        }
                     }, 0));
                  }

               });
               return var5;
            }
         }
      }
   }

   private int surfaceY(Location l) {
      int var2 = var1.getBlockY();

      Location var3;
      do {
         if (var2-- <= 0) {
            return 0;
         }

         var3 = var1.clone();
         var3.setY((double)var2);
      } while(!var3.getBlock().getType().isSolid());

      return var2;
   }

   private Entity doSpawn(Location at) {
      if (!Chunks.isSafe(var1)) {
         return null;
      } else if (this.type.equals(EntityType.UNKNOWN) && !this.isSpecialType()) {
         return null;
      } else if (!Bukkit.isPrimaryThread()) {
         AtomicReference var2 = new AtomicReference();

         try {
            J.s(() -> {
               var2.set(this.doSpawn(var1));
            });
         } catch (Throwable var4) {
            return null;
         }

         PrecisionStopwatch var3 = PrecisionStopwatch.start();

         do {
            if (var2.get() != null) {
               return (Entity)var2.get();
            }

            J.sleep(25L);
         } while(!(var3.getMilliseconds() > 500.0D));

         return null;
      } else {
         return this.isSpecialType() ? ((ExternalDataSVC)Iris.service(ExternalDataSVC.class)).spawnMob(var1, Identifier.fromString(this.specialType)) : INMS.get().spawnEntity(var1, this.getType(), this.getReason());
      }
   }

   public boolean isSpecialType() {
      return this.specialType != null && !this.specialType.equals("");
   }

   public String getFolderName() {
      return "entities";
   }

   public String getTypeName() {
      return "Entity";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public IrisEntity() {
      this.type = EntityType.UNKNOWN;
      this.reason = SpawnReason.NATURAL;
      this.customName = "";
      this.customNameVisible = false;
      this.aware = true;
      this.ai = true;
      this.glowing = false;
      this.gravity = true;
      this.invulnerable = false;
      this.silent = false;
      this.pickupItems = false;
      this.removable = false;
      this.helmet = null;
      this.chestplate = null;
      this.boots = null;
      this.leggings = null;
      this.mainHand = null;
      this.offHand = null;
      this.passengers = new KList();
      this.attributes = new KList();
      this.loot = new IrisLootReference();
      this.leashHolder = null;
      this.spawnEffect = null;
      this.spawnEffectRiseOutOfGround = false;
      this.pandaMainGene = Gene.NORMAL;
      this.pandaHiddenGene = Gene.NORMAL;
      this.baby = false;
      this.keepEntity = false;
      this.surface = IrisSurface.LAND;
      this.specialType = "";
      this.applySettingsToCustomMobAnyways = false;
      this.spawnerScript = "";
      this.postSpawnScripts = new KList();
      this.rawCommands = new KList();
   }

   @Generated
   public IrisEntity(final EntityType type, final SpawnReason reason, final String customName, final boolean customNameVisible, final boolean aware, final boolean ai, final boolean glowing, final boolean gravity, final boolean invulnerable, final boolean silent, final boolean pickupItems, final boolean removable, final IrisLoot helmet, final IrisLoot chestplate, final IrisLoot boots, final IrisLoot leggings, final IrisLoot mainHand, final IrisLoot offHand, final KList<IrisEntity> passengers, final KList<IrisAttributeModifier> attributes, final IrisLootReference loot, final IrisEntity leashHolder, final IrisEffect spawnEffect, final boolean spawnEffectRiseOutOfGround, final Gene pandaMainGene, final Gene pandaHiddenGene, final boolean baby, final boolean keepEntity, final IrisSurface surface, final String specialType, final boolean applySettingsToCustomMobAnyways, final String spawnerScript, final KList<String> postSpawnScripts, final KList<IrisCommand> rawCommands) {
      this.type = EntityType.UNKNOWN;
      this.reason = SpawnReason.NATURAL;
      this.customName = "";
      this.customNameVisible = false;
      this.aware = true;
      this.ai = true;
      this.glowing = false;
      this.gravity = true;
      this.invulnerable = false;
      this.silent = false;
      this.pickupItems = false;
      this.removable = false;
      this.helmet = null;
      this.chestplate = null;
      this.boots = null;
      this.leggings = null;
      this.mainHand = null;
      this.offHand = null;
      this.passengers = new KList();
      this.attributes = new KList();
      this.loot = new IrisLootReference();
      this.leashHolder = null;
      this.spawnEffect = null;
      this.spawnEffectRiseOutOfGround = false;
      this.pandaMainGene = Gene.NORMAL;
      this.pandaHiddenGene = Gene.NORMAL;
      this.baby = false;
      this.keepEntity = false;
      this.surface = IrisSurface.LAND;
      this.specialType = "";
      this.applySettingsToCustomMobAnyways = false;
      this.spawnerScript = "";
      this.postSpawnScripts = new KList();
      this.rawCommands = new KList();
      this.type = var1;
      this.reason = var2;
      this.customName = var3;
      this.customNameVisible = var4;
      this.aware = var5;
      this.ai = var6;
      this.glowing = var7;
      this.gravity = var8;
      this.invulnerable = var9;
      this.silent = var10;
      this.pickupItems = var11;
      this.removable = var12;
      this.helmet = var13;
      this.chestplate = var14;
      this.boots = var15;
      this.leggings = var16;
      this.mainHand = var17;
      this.offHand = var18;
      this.passengers = var19;
      this.attributes = var20;
      this.loot = var21;
      this.leashHolder = var22;
      this.spawnEffect = var23;
      this.spawnEffectRiseOutOfGround = var24;
      this.pandaMainGene = var25;
      this.pandaHiddenGene = var26;
      this.baby = var27;
      this.keepEntity = var28;
      this.surface = var29;
      this.specialType = var30;
      this.applySettingsToCustomMobAnyways = var31;
      this.spawnerScript = var32;
      this.postSpawnScripts = var33;
      this.rawCommands = var34;
   }

   @Generated
   public EntityType getType() {
      return this.type;
   }

   @Generated
   public SpawnReason getReason() {
      return this.reason;
   }

   @Generated
   public String getCustomName() {
      return this.customName;
   }

   @Generated
   public boolean isCustomNameVisible() {
      return this.customNameVisible;
   }

   @Generated
   public boolean isAware() {
      return this.aware;
   }

   @Generated
   public boolean isAi() {
      return this.ai;
   }

   @Generated
   public boolean isGlowing() {
      return this.glowing;
   }

   @Generated
   public boolean isGravity() {
      return this.gravity;
   }

   @Generated
   public boolean isInvulnerable() {
      return this.invulnerable;
   }

   @Generated
   public boolean isSilent() {
      return this.silent;
   }

   @Generated
   public boolean isPickupItems() {
      return this.pickupItems;
   }

   @Generated
   public boolean isRemovable() {
      return this.removable;
   }

   @Generated
   public IrisLoot getHelmet() {
      return this.helmet;
   }

   @Generated
   public IrisLoot getChestplate() {
      return this.chestplate;
   }

   @Generated
   public IrisLoot getBoots() {
      return this.boots;
   }

   @Generated
   public IrisLoot getLeggings() {
      return this.leggings;
   }

   @Generated
   public IrisLoot getMainHand() {
      return this.mainHand;
   }

   @Generated
   public IrisLoot getOffHand() {
      return this.offHand;
   }

   @Generated
   public KList<IrisEntity> getPassengers() {
      return this.passengers;
   }

   @Generated
   public KList<IrisAttributeModifier> getAttributes() {
      return this.attributes;
   }

   @Generated
   public IrisLootReference getLoot() {
      return this.loot;
   }

   @Generated
   public IrisEntity getLeashHolder() {
      return this.leashHolder;
   }

   @Generated
   public IrisEffect getSpawnEffect() {
      return this.spawnEffect;
   }

   @Generated
   public boolean isSpawnEffectRiseOutOfGround() {
      return this.spawnEffectRiseOutOfGround;
   }

   @Generated
   public Gene getPandaMainGene() {
      return this.pandaMainGene;
   }

   @Generated
   public Gene getPandaHiddenGene() {
      return this.pandaHiddenGene;
   }

   @Generated
   public boolean isBaby() {
      return this.baby;
   }

   @Generated
   public boolean isKeepEntity() {
      return this.keepEntity;
   }

   @Generated
   public IrisSurface getSurface() {
      return this.surface;
   }

   @Generated
   public String getSpecialType() {
      return this.specialType;
   }

   @Generated
   public boolean isApplySettingsToCustomMobAnyways() {
      return this.applySettingsToCustomMobAnyways;
   }

   @Generated
   public String getSpawnerScript() {
      return this.spawnerScript;
   }

   @Generated
   public KList<String> getPostSpawnScripts() {
      return this.postSpawnScripts;
   }

   @Generated
   public KList<IrisCommand> getRawCommands() {
      return this.rawCommands;
   }

   @Generated
   public IrisEntity setType(final EntityType type) {
      this.type = var1;
      return this;
   }

   @Generated
   public IrisEntity setReason(final SpawnReason reason) {
      this.reason = var1;
      return this;
   }

   @Generated
   public IrisEntity setCustomName(final String customName) {
      this.customName = var1;
      return this;
   }

   @Generated
   public IrisEntity setCustomNameVisible(final boolean customNameVisible) {
      this.customNameVisible = var1;
      return this;
   }

   @Generated
   public IrisEntity setAware(final boolean aware) {
      this.aware = var1;
      return this;
   }

   @Generated
   public IrisEntity setAi(final boolean ai) {
      this.ai = var1;
      return this;
   }

   @Generated
   public IrisEntity setGlowing(final boolean glowing) {
      this.glowing = var1;
      return this;
   }

   @Generated
   public IrisEntity setGravity(final boolean gravity) {
      this.gravity = var1;
      return this;
   }

   @Generated
   public IrisEntity setInvulnerable(final boolean invulnerable) {
      this.invulnerable = var1;
      return this;
   }

   @Generated
   public IrisEntity setSilent(final boolean silent) {
      this.silent = var1;
      return this;
   }

   @Generated
   public IrisEntity setPickupItems(final boolean pickupItems) {
      this.pickupItems = var1;
      return this;
   }

   @Generated
   public IrisEntity setRemovable(final boolean removable) {
      this.removable = var1;
      return this;
   }

   @Generated
   public IrisEntity setHelmet(final IrisLoot helmet) {
      this.helmet = var1;
      return this;
   }

   @Generated
   public IrisEntity setChestplate(final IrisLoot chestplate) {
      this.chestplate = var1;
      return this;
   }

   @Generated
   public IrisEntity setBoots(final IrisLoot boots) {
      this.boots = var1;
      return this;
   }

   @Generated
   public IrisEntity setLeggings(final IrisLoot leggings) {
      this.leggings = var1;
      return this;
   }

   @Generated
   public IrisEntity setMainHand(final IrisLoot mainHand) {
      this.mainHand = var1;
      return this;
   }

   @Generated
   public IrisEntity setOffHand(final IrisLoot offHand) {
      this.offHand = var1;
      return this;
   }

   @Generated
   public IrisEntity setPassengers(final KList<IrisEntity> passengers) {
      this.passengers = var1;
      return this;
   }

   @Generated
   public IrisEntity setAttributes(final KList<IrisAttributeModifier> attributes) {
      this.attributes = var1;
      return this;
   }

   @Generated
   public IrisEntity setLoot(final IrisLootReference loot) {
      this.loot = var1;
      return this;
   }

   @Generated
   public IrisEntity setLeashHolder(final IrisEntity leashHolder) {
      this.leashHolder = var1;
      return this;
   }

   @Generated
   public IrisEntity setSpawnEffect(final IrisEffect spawnEffect) {
      this.spawnEffect = var1;
      return this;
   }

   @Generated
   public IrisEntity setSpawnEffectRiseOutOfGround(final boolean spawnEffectRiseOutOfGround) {
      this.spawnEffectRiseOutOfGround = var1;
      return this;
   }

   @Generated
   public IrisEntity setPandaMainGene(final Gene pandaMainGene) {
      this.pandaMainGene = var1;
      return this;
   }

   @Generated
   public IrisEntity setPandaHiddenGene(final Gene pandaHiddenGene) {
      this.pandaHiddenGene = var1;
      return this;
   }

   @Generated
   public IrisEntity setBaby(final boolean baby) {
      this.baby = var1;
      return this;
   }

   @Generated
   public IrisEntity setKeepEntity(final boolean keepEntity) {
      this.keepEntity = var1;
      return this;
   }

   @Generated
   public IrisEntity setSurface(final IrisSurface surface) {
      this.surface = var1;
      return this;
   }

   @Generated
   public IrisEntity setSpecialType(final String specialType) {
      this.specialType = var1;
      return this;
   }

   @Generated
   public IrisEntity setApplySettingsToCustomMobAnyways(final boolean applySettingsToCustomMobAnyways) {
      this.applySettingsToCustomMobAnyways = var1;
      return this;
   }

   @Generated
   public IrisEntity setSpawnerScript(final String spawnerScript) {
      this.spawnerScript = var1;
      return this;
   }

   @Generated
   public IrisEntity setPostSpawnScripts(final KList<String> postSpawnScripts) {
      this.postSpawnScripts = var1;
      return this;
   }

   @Generated
   public IrisEntity setRawCommands(final KList<IrisCommand> rawCommands) {
      this.rawCommands = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getType());
      return "IrisEntity(type=" + var10000 + ", reason=" + String.valueOf(this.getReason()) + ", customName=" + this.getCustomName() + ", customNameVisible=" + this.isCustomNameVisible() + ", aware=" + this.isAware() + ", ai=" + this.isAi() + ", glowing=" + this.isGlowing() + ", gravity=" + this.isGravity() + ", invulnerable=" + this.isInvulnerable() + ", silent=" + this.isSilent() + ", pickupItems=" + this.isPickupItems() + ", removable=" + this.isRemovable() + ", helmet=" + String.valueOf(this.getHelmet()) + ", chestplate=" + String.valueOf(this.getChestplate()) + ", boots=" + String.valueOf(this.getBoots()) + ", leggings=" + String.valueOf(this.getLeggings()) + ", mainHand=" + String.valueOf(this.getMainHand()) + ", offHand=" + String.valueOf(this.getOffHand()) + ", passengers=" + String.valueOf(this.getPassengers()) + ", attributes=" + String.valueOf(this.getAttributes()) + ", loot=" + String.valueOf(this.getLoot()) + ", leashHolder=" + String.valueOf(this.getLeashHolder()) + ", spawnEffect=" + String.valueOf(this.getSpawnEffect()) + ", spawnEffectRiseOutOfGround=" + this.isSpawnEffectRiseOutOfGround() + ", pandaMainGene=" + String.valueOf(this.getPandaMainGene()) + ", pandaHiddenGene=" + String.valueOf(this.getPandaHiddenGene()) + ", baby=" + this.isBaby() + ", keepEntity=" + this.isKeepEntity() + ", surface=" + String.valueOf(this.getSurface()) + ", specialType=" + this.getSpecialType() + ", applySettingsToCustomMobAnyways=" + this.isApplySettingsToCustomMobAnyways() + ", spawnerScript=" + this.getSpawnerScript() + ", postSpawnScripts=" + String.valueOf(this.getPostSpawnScripts()) + ", rawCommands=" + String.valueOf(this.getRawCommands()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisEntity)) {
         return false;
      } else {
         IrisEntity var2 = (IrisEntity)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isCustomNameVisible() != var2.isCustomNameVisible()) {
            return false;
         } else if (this.isAware() != var2.isAware()) {
            return false;
         } else if (this.isAi() != var2.isAi()) {
            return false;
         } else if (this.isGlowing() != var2.isGlowing()) {
            return false;
         } else if (this.isGravity() != var2.isGravity()) {
            return false;
         } else if (this.isInvulnerable() != var2.isInvulnerable()) {
            return false;
         } else if (this.isSilent() != var2.isSilent()) {
            return false;
         } else if (this.isPickupItems() != var2.isPickupItems()) {
            return false;
         } else if (this.isRemovable() != var2.isRemovable()) {
            return false;
         } else if (this.isSpawnEffectRiseOutOfGround() != var2.isSpawnEffectRiseOutOfGround()) {
            return false;
         } else if (this.isBaby() != var2.isBaby()) {
            return false;
         } else if (this.isKeepEntity() != var2.isKeepEntity()) {
            return false;
         } else if (this.isApplySettingsToCustomMobAnyways() != var2.isApplySettingsToCustomMobAnyways()) {
            return false;
         } else {
            label295: {
               EntityType var3 = this.getType();
               EntityType var4 = var2.getType();
               if (var3 == null) {
                  if (var4 == null) {
                     break label295;
                  }
               } else if (var3.equals(var4)) {
                  break label295;
               }

               return false;
            }

            SpawnReason var5 = this.getReason();
            SpawnReason var6 = var2.getReason();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label281: {
               String var7 = this.getCustomName();
               String var8 = var2.getCustomName();
               if (var7 == null) {
                  if (var8 == null) {
                     break label281;
                  }
               } else if (var7.equals(var8)) {
                  break label281;
               }

               return false;
            }

            IrisLoot var9 = this.getHelmet();
            IrisLoot var10 = var2.getHelmet();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            label267: {
               IrisLoot var11 = this.getChestplate();
               IrisLoot var12 = var2.getChestplate();
               if (var11 == null) {
                  if (var12 == null) {
                     break label267;
                  }
               } else if (var11.equals(var12)) {
                  break label267;
               }

               return false;
            }

            label260: {
               IrisLoot var13 = this.getBoots();
               IrisLoot var14 = var2.getBoots();
               if (var13 == null) {
                  if (var14 == null) {
                     break label260;
                  }
               } else if (var13.equals(var14)) {
                  break label260;
               }

               return false;
            }

            IrisLoot var15 = this.getLeggings();
            IrisLoot var16 = var2.getLeggings();
            if (var15 == null) {
               if (var16 != null) {
                  return false;
               }
            } else if (!var15.equals(var16)) {
               return false;
            }

            label246: {
               IrisLoot var17 = this.getMainHand();
               IrisLoot var18 = var2.getMainHand();
               if (var17 == null) {
                  if (var18 == null) {
                     break label246;
                  }
               } else if (var17.equals(var18)) {
                  break label246;
               }

               return false;
            }

            label239: {
               IrisLoot var19 = this.getOffHand();
               IrisLoot var20 = var2.getOffHand();
               if (var19 == null) {
                  if (var20 == null) {
                     break label239;
                  }
               } else if (var19.equals(var20)) {
                  break label239;
               }

               return false;
            }

            label232: {
               KList var21 = this.getPassengers();
               KList var22 = var2.getPassengers();
               if (var21 == null) {
                  if (var22 == null) {
                     break label232;
                  }
               } else if (var21.equals(var22)) {
                  break label232;
               }

               return false;
            }

            KList var23 = this.getAttributes();
            KList var24 = var2.getAttributes();
            if (var23 == null) {
               if (var24 != null) {
                  return false;
               }
            } else if (!var23.equals(var24)) {
               return false;
            }

            label218: {
               IrisLootReference var25 = this.getLoot();
               IrisLootReference var26 = var2.getLoot();
               if (var25 == null) {
                  if (var26 == null) {
                     break label218;
                  }
               } else if (var25.equals(var26)) {
                  break label218;
               }

               return false;
            }

            IrisEntity var27 = this.getLeashHolder();
            IrisEntity var28 = var2.getLeashHolder();
            if (var27 == null) {
               if (var28 != null) {
                  return false;
               }
            } else if (!var27.equals(var28)) {
               return false;
            }

            IrisEffect var29 = this.getSpawnEffect();
            IrisEffect var30 = var2.getSpawnEffect();
            if (var29 == null) {
               if (var30 != null) {
                  return false;
               }
            } else if (!var29.equals(var30)) {
               return false;
            }

            Gene var31 = this.getPandaMainGene();
            Gene var32 = var2.getPandaMainGene();
            if (var31 == null) {
               if (var32 != null) {
                  return false;
               }
            } else if (!var31.equals(var32)) {
               return false;
            }

            Gene var33 = this.getPandaHiddenGene();
            Gene var34 = var2.getPandaHiddenGene();
            if (var33 == null) {
               if (var34 != null) {
                  return false;
               }
            } else if (!var33.equals(var34)) {
               return false;
            }

            label183: {
               IrisSurface var35 = this.getSurface();
               IrisSurface var36 = var2.getSurface();
               if (var35 == null) {
                  if (var36 == null) {
                     break label183;
                  }
               } else if (var35.equals(var36)) {
                  break label183;
               }

               return false;
            }

            String var37 = this.getSpecialType();
            String var38 = var2.getSpecialType();
            if (var37 == null) {
               if (var38 != null) {
                  return false;
               }
            } else if (!var37.equals(var38)) {
               return false;
            }

            label169: {
               String var39 = this.getSpawnerScript();
               String var40 = var2.getSpawnerScript();
               if (var39 == null) {
                  if (var40 == null) {
                     break label169;
                  }
               } else if (var39.equals(var40)) {
                  break label169;
               }

               return false;
            }

            KList var41 = this.getPostSpawnScripts();
            KList var42 = var2.getPostSpawnScripts();
            if (var41 == null) {
               if (var42 != null) {
                  return false;
               }
            } else if (!var41.equals(var42)) {
               return false;
            }

            KList var43 = this.getRawCommands();
            KList var44 = var2.getRawCommands();
            if (var43 == null) {
               if (var44 != null) {
                  return false;
               }
            } else if (!var43.equals(var44)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisEntity;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var24 = var2 * 59 + (this.isCustomNameVisible() ? 79 : 97);
      var24 = var24 * 59 + (this.isAware() ? 79 : 97);
      var24 = var24 * 59 + (this.isAi() ? 79 : 97);
      var24 = var24 * 59 + (this.isGlowing() ? 79 : 97);
      var24 = var24 * 59 + (this.isGravity() ? 79 : 97);
      var24 = var24 * 59 + (this.isInvulnerable() ? 79 : 97);
      var24 = var24 * 59 + (this.isSilent() ? 79 : 97);
      var24 = var24 * 59 + (this.isPickupItems() ? 79 : 97);
      var24 = var24 * 59 + (this.isRemovable() ? 79 : 97);
      var24 = var24 * 59 + (this.isSpawnEffectRiseOutOfGround() ? 79 : 97);
      var24 = var24 * 59 + (this.isBaby() ? 79 : 97);
      var24 = var24 * 59 + (this.isKeepEntity() ? 79 : 97);
      var24 = var24 * 59 + (this.isApplySettingsToCustomMobAnyways() ? 79 : 97);
      EntityType var3 = this.getType();
      var24 = var24 * 59 + (var3 == null ? 43 : var3.hashCode());
      SpawnReason var4 = this.getReason();
      var24 = var24 * 59 + (var4 == null ? 43 : var4.hashCode());
      String var5 = this.getCustomName();
      var24 = var24 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisLoot var6 = this.getHelmet();
      var24 = var24 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisLoot var7 = this.getChestplate();
      var24 = var24 * 59 + (var7 == null ? 43 : var7.hashCode());
      IrisLoot var8 = this.getBoots();
      var24 = var24 * 59 + (var8 == null ? 43 : var8.hashCode());
      IrisLoot var9 = this.getLeggings();
      var24 = var24 * 59 + (var9 == null ? 43 : var9.hashCode());
      IrisLoot var10 = this.getMainHand();
      var24 = var24 * 59 + (var10 == null ? 43 : var10.hashCode());
      IrisLoot var11 = this.getOffHand();
      var24 = var24 * 59 + (var11 == null ? 43 : var11.hashCode());
      KList var12 = this.getPassengers();
      var24 = var24 * 59 + (var12 == null ? 43 : var12.hashCode());
      KList var13 = this.getAttributes();
      var24 = var24 * 59 + (var13 == null ? 43 : var13.hashCode());
      IrisLootReference var14 = this.getLoot();
      var24 = var24 * 59 + (var14 == null ? 43 : var14.hashCode());
      IrisEntity var15 = this.getLeashHolder();
      var24 = var24 * 59 + (var15 == null ? 43 : var15.hashCode());
      IrisEffect var16 = this.getSpawnEffect();
      var24 = var24 * 59 + (var16 == null ? 43 : var16.hashCode());
      Gene var17 = this.getPandaMainGene();
      var24 = var24 * 59 + (var17 == null ? 43 : var17.hashCode());
      Gene var18 = this.getPandaHiddenGene();
      var24 = var24 * 59 + (var18 == null ? 43 : var18.hashCode());
      IrisSurface var19 = this.getSurface();
      var24 = var24 * 59 + (var19 == null ? 43 : var19.hashCode());
      String var20 = this.getSpecialType();
      var24 = var24 * 59 + (var20 == null ? 43 : var20.hashCode());
      String var21 = this.getSpawnerScript();
      var24 = var24 * 59 + (var21 == null ? 43 : var21.hashCode());
      KList var22 = this.getPostSpawnScripts();
      var24 = var24 * 59 + (var22 == null ? 43 : var22.hashCode());
      KList var23 = this.getRawCommands();
      var24 = var24 * 59 + (var23 == null ? 43 : var23.hashCode());
      return var24;
   }
}
