package ac.grim.grimac.utils.latency;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.Equipment;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.UserProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Direction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectOpenHashMap;
import ac.grim.grimac.shaded.fastutil.ints.IntArraySet;
import ac.grim.grimac.shaded.fastutil.objects.Object2ObjectOpenHashMap;
import ac.grim.grimac.shaded.fastutil.objects.ObjectIterator;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.ShulkerData;
import ac.grim.grimac.utils.data.TrackerData;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityArmorStand;
import ac.grim.grimac.utils.data.packetentity.PacketEntityCamel;
import ac.grim.grimac.utils.data.packetentity.PacketEntityGuardian;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHappyGhast;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHook;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHorse;
import ac.grim.grimac.utils.data.packetentity.PacketEntityNautilus;
import ac.grim.grimac.utils.data.packetentity.PacketEntityPainting;
import ac.grim.grimac.utils.data.packetentity.PacketEntityRideable;
import ac.grim.grimac.utils.data.packetentity.PacketEntitySelf;
import ac.grim.grimac.utils.data.packetentity.PacketEntityShulker;
import ac.grim.grimac.utils.data.packetentity.PacketEntitySizeable;
import ac.grim.grimac.utils.data.packetentity.PacketEntityStrider;
import ac.grim.grimac.utils.data.packetentity.PacketEntityTrackXRot;
import ac.grim.grimac.utils.data.packetentity.PacketEntityUnHittable;
import ac.grim.grimac.utils.data.packetentity.dragon.PacketEntityEnderDragon;
import ac.grim.grimac.utils.nmsutil.BoundingBoxSize;
import ac.grim.grimac.utils.nmsutil.WatchableIndexUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.Map.Entry;

public class CompensatedEntities {
   public static final UUID SPRINTING_MODIFIER_UUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
   public static final UUID SNOW_MODIFIER_UUID = UUID.fromString("1eaf83ff-7207-4596-b37a-d7a07b3ec4ce");
   public final Int2ObjectOpenHashMap<PacketEntity> entityMap = new Int2ObjectOpenHashMap(40, 0.7F);
   public final IntArraySet entitiesRemovedThisTick = new IntArraySet();
   public final Int2ObjectOpenHashMap<TrackerData> serverPositionsMap = new Int2ObjectOpenHashMap(40, 0.7F);
   public final Object2ObjectOpenHashMap<UUID, UserProfile> profiles = new Object2ObjectOpenHashMap();
   public Integer serverPlayerVehicle = null;
   public boolean hasSprintingAttributeEnabled = false;
   public TrackerData selfTrackedEntity;
   public PacketEntitySelf self;
   private final GrimPlayer player;

   public CompensatedEntities(GrimPlayer player) {
      this.player = player;
      this.self = new PacketEntitySelf(player);
      this.selfTrackedEntity = new TrackerData(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, EntityTypes.PLAYER, player.lastTransactionSent.get());
   }

   public int getPacketEntityID(PacketEntity entity) {
      ObjectIterator var2 = this.entityMap.int2ObjectEntrySet().iterator();

      Entry entry;
      do {
         if (!var2.hasNext()) {
            return Integer.MIN_VALUE;
         }

         entry = (Entry)var2.next();
      } while(entry.getValue() != entity);

      return (Integer)entry.getKey();
   }

   public void tick() {
      this.self.setPositionRaw(this.player, new SimpleCollisionBox(this.player.x, this.player.y, this.player.z, this.player.x, this.player.y, this.player.z));
      ObjectIterator var1 = this.entityMap.values().iterator();

      while(var1.hasNext()) {
         PacketEntity vehicle = (PacketEntity)var1.next();
         Iterator var3 = vehicle.passengers.iterator();

         while(var3.hasNext()) {
            PacketEntity passenger = (PacketEntity)var3.next();
            this.tickPassenger(vehicle, passenger);
         }
      }

   }

   public void removeEntity(int entityID) {
      PacketEntity entity = (PacketEntity)this.entityMap.remove(entityID);
      if (entity != null) {
         if (entity instanceof PacketEntityEnderDragon) {
            PacketEntityEnderDragon dragon = (PacketEntityEnderDragon)entity;

            for(int i = 1; i < dragon.getParts().size() + 1; ++i) {
               this.entityMap.remove(entityID + i);
            }
         }

         Iterator var5 = (new ArrayList(entity.passengers)).iterator();

         while(var5.hasNext()) {
            PacketEntity passenger = (PacketEntity)var5.next();
            passenger.eject();
         }

      }
   }

   public OptionalInt getSlowFallingAmplifier() {
      return this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_12_2) ? OptionalInt.empty() : this.getPotionLevelForPlayer(PotionTypes.SLOW_FALLING);
   }

   public OptionalInt getPotionLevelForPlayer(PotionType type) {
      return this.getEntityInControl().getPotionEffectLevel(type);
   }

   public OptionalInt getPotionLevelForSelfPlayer(PotionType type) {
      return this.self.getPotionEffectLevel(type);
   }

   public boolean hasPotionEffect(PotionType type) {
      return this.getEntityInControl().hasPotionEffect(type);
   }

   public PacketEntity getEntityInControl() {
      return (PacketEntity)(this.self.getRiding() != null ? this.self.getRiding() : this.self);
   }

   public void updateAttributes(int entityID, List<WrapperPlayServerUpdateAttributes.Property> objects) {
      if (entityID == this.player.entityID) {
         Iterator var3 = objects.iterator();

         while(var3.hasNext()) {
            WrapperPlayServerUpdateAttributes.Property snapshotWrapper = (WrapperPlayServerUpdateAttributes.Property)var3.next();
            Attribute attribute = snapshotWrapper.getAttribute();
            if (attribute == Attributes.MOVEMENT_SPEED) {
               boolean found = false;
               List<WrapperPlayServerUpdateAttributes.PropertyModifier> modifiers = snapshotWrapper.getModifiers();
               Iterator var8 = modifiers.iterator();

               label46: {
                  ResourceLocation name;
                  do {
                     if (!var8.hasNext()) {
                        break label46;
                     }

                     WrapperPlayServerUpdateAttributes.PropertyModifier modifier = (WrapperPlayServerUpdateAttributes.PropertyModifier)var8.next();
                     name = modifier.getName();
                  } while(!name.getKey().equals(SPRINTING_MODIFIER_UUID.toString()) && !name.getKey().equals("sprinting"));

                  found = true;
               }

               this.hasSprintingAttributeEnabled = found;
               break;
            }
         }
      }

      PacketEntity entity = this.player.compensatedEntities.getEntity(entityID);
      if (entity != null) {
         Iterator var12 = objects.iterator();

         while(var12.hasNext()) {
            WrapperPlayServerUpdateAttributes.Property snapshotWrapper = (WrapperPlayServerUpdateAttributes.Property)var12.next();
            Attribute attribute = snapshotWrapper.getAttribute();
            if (attribute != null) {
               if (attribute == Attributes.HORSE_JUMP_STRENGTH) {
                  attribute = Attributes.JUMP_STRENGTH;
               }

               Optional<ValuedAttribute> valuedAttribute = entity.getAttribute(attribute);
               if (!valuedAttribute.isEmpty()) {
                  ((ValuedAttribute)valuedAttribute.get()).with(snapshotWrapper);
               }
            }
         }

      }
   }

   private void tickPassenger(PacketEntity riding, PacketEntity passenger) {
      if (riding != null && passenger != null) {
         passenger.setPositionRaw(this.player, riding.getPossibleLocationBoxes().offset(0.0D, BoundingBoxSize.getMyRidingOffset(riding) + BoundingBoxSize.getPassengerRidingOffset(this.player, passenger), 0.0D));
         Iterator var3 = riding.passengers.iterator();

         while(var3.hasNext()) {
            PacketEntity passengerPassenger = (PacketEntity)var3.next();
            this.tickPassenger(passenger, passengerPassenger);
         }

      }
   }

   public PacketEntity addEntity(int entityID, UUID uuid, EntityType entityType, Vector3d position, float xRot, int data) {
      if (entityType == EntityTypes.ITEM) {
         return null;
      } else {
         Object packetEntity;
         if (EntityTypes.isTypeInstanceOf(entityType, EntityTypes.ABSTRACT_NAUTILUS)) {
            packetEntity = new PacketEntityNautilus(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ());
         } else if (EntityTypes.HAPPY_GHAST.equals(entityType)) {
            packetEntity = new PacketEntityHappyGhast(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ(), xRot);
         } else if (EntityTypes.isTypeInstanceOf(entityType, EntityTypes.CAMEL)) {
            packetEntity = new PacketEntityCamel(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ(), xRot);
         } else if (EntityTypes.isTypeInstanceOf(entityType, EntityTypes.ABSTRACT_HORSE)) {
            packetEntity = new PacketEntityHorse(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ(), xRot);
         } else if (entityType != EntityTypes.SLIME && entityType != EntityTypes.MAGMA_CUBE && entityType != EntityTypes.PHANTOM) {
            if (EntityTypes.PIG.equals(entityType)) {
               packetEntity = new PacketEntityRideable(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ());
            } else if (EntityTypes.SHULKER.equals(entityType)) {
               packetEntity = new PacketEntityShulker(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ());
            } else if (EntityTypes.STRIDER.equals(entityType)) {
               packetEntity = new PacketEntityStrider(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ());
            } else if (!EntityTypes.isTypeInstanceOf(entityType, EntityTypes.BOAT) && !EntityTypes.CHICKEN.equals(entityType)) {
               if (EntityTypes.FISHING_BOBBER.equals(entityType)) {
                  packetEntity = new PacketEntityHook(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ(), data);
               } else if (EntityTypes.ENDER_DRAGON.equals(entityType)) {
                  packetEntity = new PacketEntityEnderDragon(this.player, uuid, entityID, position.getX(), position.getY(), position.getZ());
               } else if (!EntityTypes.isTypeInstanceOf(entityType, EntityTypes.ABSTRACT_ARROW) && !EntityTypes.FIREWORK_ROCKET.equals(entityType) && !EntityTypes.BLOCK_DISPLAY.equals(entityType) && !EntityTypes.TEXT_DISPLAY.equals(entityType) && !EntityTypes.LIGHTNING_BOLT.equals(entityType) && !EntityTypes.EXPERIENCE_BOTTLE.equals(entityType) && !EntityTypes.EXPERIENCE_ORB.equals(entityType) && !EntityTypes.EVOKER_FANGS.equals(entityType)) {
                  if (EntityTypes.ARMOR_STAND.equals(entityType)) {
                     packetEntity = new PacketEntityArmorStand(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ(), data);
                  } else if (EntityTypes.PAINTING.equals(entityType)) {
                     packetEntity = new PacketEntityPainting(this.player, uuid, position.x, position.y, position.z, Direction.values()[data]);
                  } else if (EntityTypes.GUARDIAN.equals(entityType)) {
                     packetEntity = new PacketEntityGuardian(this.player, uuid, entityType, position.x, position.y, position.z, false);
                  } else if (EntityTypes.ELDER_GUARDIAN.equals(entityType)) {
                     packetEntity = new PacketEntityGuardian(this.player, uuid, entityType, position.x, position.y, position.z, true);
                  } else {
                     packetEntity = new PacketEntity(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ());
                  }
               } else {
                  packetEntity = new PacketEntityUnHittable(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ());
               }
            } else {
               packetEntity = new PacketEntityTrackXRot(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ(), xRot);
            }
         } else {
            packetEntity = new PacketEntitySizeable(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ());
         }

         this.entityMap.put(entityID, packetEntity);
         return (PacketEntity)packetEntity;
      }
   }

   public PacketEntity getEntity(int entityID) {
      return (PacketEntity)(entityID == this.player.entityID ? this.self : (PacketEntity)this.entityMap.get(entityID));
   }

   public TrackerData getTrackedEntity(int id) {
      return id == this.player.entityID ? this.selfTrackedEntity : (TrackerData)this.serverPositionsMap.get(id);
   }

   public void updateEntityMetadata(int entityID, List<EntityData<?>> watchableObjects) {
      PacketEntity entity = this.player.compensatedEntities.getEntity(entityID);
      if (entity != null) {
         byte index;
         EntityData armorStandByte;
         if (entity.isAgeable) {
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
               index = 12;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4)) {
               index = 11;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
               index = 12;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4)) {
               index = 14;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
               index = 15;
            } else {
               index = 16;
            }

            armorStandByte = WatchableIndexUtil.getIndex(watchableObjects, index);
            if (armorStandByte != null) {
               Object value = armorStandByte.getValue();
               if (value instanceof Boolean) {
                  entity.isBaby = (Boolean)value;
               } else if (value instanceof Byte) {
                  entity.isBaby = (Byte)value < 0;
               }
            }
         }

         byte isElderlyBitMask;
         EntityData guardianByte;
         if (entity instanceof PacketEntitySizeable) {
            PacketEntitySizeable sizeable = (PacketEntitySizeable)entity;
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
               isElderlyBitMask = 16;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4)) {
               isElderlyBitMask = 11;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
               isElderlyBitMask = 12;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4)) {
               isElderlyBitMask = 14;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
               isElderlyBitMask = 15;
            } else {
               isElderlyBitMask = 16;
            }

            guardianByte = WatchableIndexUtil.getIndex(watchableObjects, isElderlyBitMask);
            if (guardianByte != null) {
               Object value = guardianByte.getValue();
               if (value instanceof Integer) {
                  sizeable.size = (Integer)value;
               } else if (value instanceof Byte) {
                  sizeable.size = (Byte)value;
               }
            }
         }

         EntityData pigBoost;
         if (entity instanceof PacketEntityShulker) {
            PacketEntityShulker shulker = (PacketEntityShulker)entity;
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4)) {
               isElderlyBitMask = 11;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
               isElderlyBitMask = 12;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4)) {
               isElderlyBitMask = 14;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
               isElderlyBitMask = 15;
            } else {
               isElderlyBitMask = 16;
            }

            guardianByte = WatchableIndexUtil.getIndex(watchableObjects, isElderlyBitMask);
            if (guardianByte != null) {
               shulker.facing = BlockFace.valueOf(guardianByte.getValue().toString().toUpperCase());
            }

            pigBoost = WatchableIndexUtil.getIndex(watchableObjects, isElderlyBitMask + 2);
            if (pigBoost != null) {
               ShulkerData data;
               if ((Byte)pigBoost.getValue() == 0) {
                  data = new ShulkerData(shulker, this.player.lastTransactionSent.get(), true);
                  this.player.compensatedWorld.openShulkerBoxes.remove(data);
                  this.player.compensatedWorld.openShulkerBoxes.add(data);
               } else {
                  data = new ShulkerData(shulker, this.player.lastTransactionSent.get(), false);
                  this.player.compensatedWorld.openShulkerBoxes.remove(data);
                  this.player.compensatedWorld.openShulkerBoxes.add(data);
               }
            }
         }

         EntityData striderSaddle;
         if (entity instanceof PacketEntityRideable) {
            PacketEntityRideable rideable = (PacketEntityRideable)entity;
            isElderlyBitMask = 0;
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
               if (entity.type == EntityTypes.PIG) {
                  guardianByte = WatchableIndexUtil.getIndex(watchableObjects, 16);
                  if (guardianByte != null) {
                     rideable.hasSaddle = (Byte)guardianByte.getValue() != 0;
                  }
               }
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4)) {
               isElderlyBitMask = 5;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
               isElderlyBitMask = 4;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4)) {
               isElderlyBitMask = 2;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
               isElderlyBitMask = 1;
            }

            if (entity.type == EntityTypes.PIG) {
               if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
                  isElderlyBitMask = 1;
               }

               guardianByte = WatchableIndexUtil.getIndex(watchableObjects, 17 - isElderlyBitMask);
               if (guardianByte != null) {
                  rideable.hasSaddle = (Boolean)guardianByte.getValue();
               }

               pigBoost = WatchableIndexUtil.getIndex(watchableObjects, 18 - isElderlyBitMask);
               if (pigBoost != null) {
                  rideable.boostTimeMax = (Integer)pigBoost.getValue();
                  rideable.currentBoostTime = 0;
               }
            } else if (entity instanceof PacketEntityStrider) {
               guardianByte = WatchableIndexUtil.getIndex(watchableObjects, 17 - isElderlyBitMask);
               if (guardianByte != null) {
                  rideable.boostTimeMax = (Integer)guardianByte.getValue();
                  rideable.currentBoostTime = 0;
               }

               pigBoost = WatchableIndexUtil.getIndex(watchableObjects, 18 - isElderlyBitMask);
               if (pigBoost != null) {
                  ((PacketEntityStrider)rideable).isShaking = (Boolean)pigBoost.getValue();
               }

               striderSaddle = WatchableIndexUtil.getIndex(watchableObjects, 19 - isElderlyBitMask);
               if (striderSaddle != null) {
                  rideable.hasSaddle = (Boolean)striderSaddle.getValue();
               }
            }
         }

         int attachedEntityID;
         if (entity instanceof PacketEntityHorse) {
            PacketEntityHorse horse = (PacketEntityHorse)entity;
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9_4)) {
               isElderlyBitMask = 0;
               if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4)) {
                  isElderlyBitMask = 5;
               } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
                  isElderlyBitMask = 4;
               } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4)) {
                  isElderlyBitMask = 2;
               } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                  isElderlyBitMask = 1;
               }

               guardianByte = WatchableIndexUtil.getIndex(watchableObjects, 17 - isElderlyBitMask);
               if (guardianByte != null) {
                  byte info = (Byte)guardianByte.getValue();
                  horse.isTame = (info & 2) != 0;
                  horse.hasSaddle = (info & 4) != 0;
                  horse.isRearing = (info & 32) != 0;
               }

               if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_20) && entity instanceof PacketEntityCamel) {
                  PacketEntityCamel camel = (PacketEntityCamel)entity;
                  striderSaddle = WatchableIndexUtil.getIndex(watchableObjects, 18);
                  if (striderSaddle != null) {
                     camel.setDashing((Boolean)striderSaddle.getValue());
                     camel.setDashCooldown(camel.getDashCooldown() == 0 ? 55 : camel.getDashCooldown());
                  }
               }
            } else {
               armorStandByte = WatchableIndexUtil.getIndex(watchableObjects, 16);
               if (armorStandByte != null) {
                  attachedEntityID = (Integer)armorStandByte.getValue();
                  horse.isTame = (attachedEntityID & 2) != 0;
                  horse.hasSaddle = (attachedEntityID & 4) != 0;
                  horse.isRearing = (attachedEntityID & 64) != 0;
               }
            }
         }

         if (entity instanceof PacketEntityNautilus) {
            PacketEntityNautilus nautilus = (PacketEntityNautilus)entity;
            armorStandByte = WatchableIndexUtil.getIndex(watchableObjects, 19);
            if (armorStandByte != null) {
               nautilus.setDashing((Boolean)armorStandByte.getValue());
               nautilus.setDashCooldown(nautilus.getDashCooldown() == 0 ? 40 : nautilus.getDashCooldown());
            }
         }

         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9_4)) {
            EntityData<?> gravity = WatchableIndexUtil.getIndex(watchableObjects, 5);
            if (gravity != null) {
               Object gravityObject = gravity.getValue();
               if (gravityObject instanceof Boolean) {
                  entity.hasGravity = !(Boolean)gravityObject;
               }
            }
         }

         if (entity.type == EntityTypes.FIREWORK_ROCKET) {
            index = 0;
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
               index = 2;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
               index = 1;
            }

            armorStandByte = WatchableIndexUtil.getIndex(watchableObjects, 9 - index);
            if (armorStandByte == null) {
               return;
            }

            if (armorStandByte.getValue() instanceof Integer) {
               attachedEntityID = (Integer)armorStandByte.getValue();
               if (attachedEntityID == this.player.entityID) {
                  this.player.fireworks.addNewFirework(entityID);
               }
            } else {
               Optional<Integer> attachedEntityID = (Optional)armorStandByte.getValue();
               if (attachedEntityID.isPresent() && ((Integer)attachedEntityID.get()).equals(this.player.entityID)) {
                  this.player.fireworks.addNewFirework(entityID);
               }
            }
         }

         if (entity instanceof PacketEntityHook) {
            PacketEntityHook hook = (PacketEntityHook)entity;
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4)) {
               isElderlyBitMask = 5;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4)) {
               isElderlyBitMask = 6;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
               isElderlyBitMask = 7;
            } else {
               isElderlyBitMask = 8;
            }

            guardianByte = WatchableIndexUtil.getIndex(watchableObjects, isElderlyBitMask);
            if (guardianByte == null) {
               return;
            }

            Integer attachedEntityID = (Integer)guardianByte.getValue();
            hook.attached = attachedEntityID - 1;
         }

         if (entity instanceof PacketEntityArmorStand) {
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4)) {
               index = 10;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
               index = 11;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4)) {
               index = 13;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
               index = 14;
            } else {
               index = 15;
            }

            armorStandByte = WatchableIndexUtil.getIndex(watchableObjects, index);
            if (armorStandByte != null) {
               byte info = (Byte)armorStandByte.getValue();
               entity.isBaby = (info & 1) != 0;
               ((PacketEntityArmorStand)entity).isMarker = (info & 16) != 0;
            }
         }

         if (entity instanceof PacketEntityGuardian && PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_11)) {
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_9)) {
               index = 16;
               isElderlyBitMask = 4;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_10)) {
               index = 11;
               isElderlyBitMask = 4;
            } else {
               index = 12;
               isElderlyBitMask = 4;
            }

            guardianByte = WatchableIndexUtil.getIndex(watchableObjects, index);
            if (guardianByte != null) {
               int info = (Integer)guardianByte.getValue();
               ((PacketEntityGuardian)entity).isElder = (info & isElderlyBitMask) != 0;
            }
         }

      }
   }

   public void updateEntityEquipment(int entityId, List<Equipment> equipment) {
      PacketEntity entity = this.player.compensatedEntities.getEntity(entityId);
      if (entity != null && entity.trackEntityEquipment) {
         Iterator var4 = equipment.iterator();

         while(var4.hasNext()) {
            Equipment equipmentItem = (Equipment)var4.next();
            entity.setItemBySlot(equipmentItem.getSlot(), equipmentItem.getItem());
         }

      }
   }
}
