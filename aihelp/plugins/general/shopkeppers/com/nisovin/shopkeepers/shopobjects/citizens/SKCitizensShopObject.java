package com.nisovin.shopkeepers.shopobjects.citizens;

import com.nisovin.shopkeepers.api.events.ShopkeeperAddedEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.citizens.CitizensShopObject;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.dependencies.citizens.CitizensUtils;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.shopkeeper.migration.Migration;
import com.nisovin.shopkeepers.shopkeeper.migration.MigrationPhase;
import com.nisovin.shopkeepers.shopkeeper.migration.ShopkeeperDataMigrator;
import com.nisovin.shopkeepers.shopobjects.SKDefaultShopObjectTypes;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.AbstractEntityShopObject;
import com.nisovin.shopkeepers.util.bukkit.LocationUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.DataContainerSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.UUIDSerializers;
import com.nisovin.shopkeepers.util.java.CyclicCounter;
import com.nisovin.shopkeepers.util.java.RateLimiter;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Collections;
import java.util.UUID;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPC.Metadata;
import net.citizensnpcs.api.trait.trait.MobType;
import net.citizensnpcs.api.trait.trait.Owner;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKCitizensShopObject extends AbstractEntityShopObject implements CitizensShopObject {
   public static final Property<UUID> NPC_UNIQUE_ID;
   public static final Property<DataContainer> NPC_DATA;
   public static final String CREATION_DATA_NPC_UUID_KEY = "CitizensNpcUUID";
   private static final int CHECK_PERIOD_SECONDS = 10;
   private static final CyclicCounter nextCheckingOffset;
   protected final CitizensShops citizensShops;
   private final PropertyValue<UUID> npcUniqueIdProperty;
   @Nullable
   private DataContainer npcData;
   @Nullable
   private String creatorName;
   private boolean destroyNPC;
   private final RateLimiter checkLimiter;
   @Nullable
   private Entity entity;

   protected SKCitizensShopObject(CitizensShops citizensShops, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(shopkeeper, creationData);
      this.npcUniqueIdProperty = (new PropertyValue(NPC_UNIQUE_ID)).onValueChanged((property, oldValue, newValue, updateFlags) -> {
         ((SKCitizensShopObject)Unsafe.initialized(this)).onNPCUniqueIdChanged(oldValue, newValue);
      }).build(this.properties);
      this.npcData = null;
      this.creatorName = null;
      this.destroyNPC = true;
      this.checkLimiter = new RateLimiter(10, nextCheckingOffset.getAndIncrement());
      this.entity = null;
      this.citizensShops = citizensShops;
      if (creationData != null) {
         UUID npcId = (UUID)creationData.getValue("CitizensNpcUUID");
         this.npcUniqueIdProperty.setValue(npcId, Collections.emptySet());
         Player creator = creationData.getCreator();
         this.creatorName = creator != null ? creator.getName() : null;
      }

   }

   public SKCitizensShopObjectType getType() {
      return SKDefaultShopObjectTypes.CITIZEN();
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.npcUniqueIdProperty.load(shopObjectData);
      this.loadNpcData(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.npcUniqueIdProperty.save(shopObjectData);
      this.saveNpcData(shopObjectData, saveAll);
   }

   @Nullable
   public UUID getNPCUniqueId() {
      return (UUID)this.npcUniqueIdProperty.getValue();
   }

   private void setNPCUniqueId(@Nullable UUID npcId) {
      this.npcUniqueIdProperty.setValue(npcId);
   }

   private void onNPCUniqueIdChanged(@Nullable UUID oldValue, @Nullable UUID newValue) {
      if (this.shopkeeper.isValid()) {
         if (oldValue != null) {
            this.citizensShops.unregisterCitizensShopkeeper(this, oldValue);
         }

         if (newValue != null) {
            this.citizensShops.registerCitizensShopkeeper(this, newValue);
         }
      }

   }

   @Nullable
   public NPC getNPC() {
      UUID npcUniqueId = this.getNPCUniqueId();
      if (npcUniqueId == null) {
         return null;
      } else {
         return !this.citizensShops.isEnabled() ? null : CitizensAPI.getNPCRegistry().getByUniqueId(npcUniqueId);
      }
   }

   @Nullable
   private EntityType getEntityType() {
      Entity entity = this.getEntity();
      if (entity != null) {
         return entity.getType();
      } else {
         NPC npc = this.getNPC();
         if (npc == null) {
            return null;
         } else {
            MobType mobType = (MobType)Unsafe.assertNonNull((MobType)npc.getOrAddTrait(MobType.class));
            return mobType.getType();
         }
      }
   }

   private NPC createNpcIfNotYetCreated() {
      if (this.getNPCUniqueId() != null) {
         return null;
      } else {
         assert this.getNPC() == null;

         if (!this.citizensShops.isEnabled()) {
            return null;
         } else {
            Log.debug(() -> {
               return this.shopkeeper.getLogPrefix() + "Creating Citizens NPC.";
            });
            EntityType entityType = Settings.defaultCitizenNpcType;
            Location spawnLocation = this.getSpawnLocation();
            NPC npc = this.citizensShops.createNPC(spawnLocation, entityType, "");
            if (npc == null) {
               Log.debug(() -> {
                  return this.shopkeeper.getLogPrefix() + "Failed to create Citizens NPC!";
               });
               return null;
            } else {
               this.setNPCUniqueId(npc.getUniqueId());
               String name = "";
               if (entityType == EntityType.PLAYER) {
                  if (this.shopkeeper instanceof PlayerShopkeeper) {
                     name = ((PlayerShopkeeper)this.shopkeeper).getOwnerName();
                  } else {
                     name = this.creatorName != null ? this.creatorName : "";
                  }
               }

               assert name != null;

               this.setNpcName(npc, name);
               return npc;
            }
         }
      }
   }

   private void synchronizeNpc() {
      boolean npcHasChanged = false;
      NPC npc = this.getNPC();
      if (npc == null) {
         npc = this.createNpcIfNotYetCreated();
         if (npc == null) {
            return;
         }

         npcHasChanged = true;
      }

      assert npc != null;

      npcHasChanged |= this.applyNpcData(npc);
      npcHasChanged |= this.updateNpcOwner(npc);
      npcHasChanged |= this.updateNpcFluidPushable(npc);
      this.setEntity(npc.getEntity());
      if (!this.isSpawned()) {
         this.updateShopkeeperLocation(npc);
      }

      if (npcHasChanged) {
         this.citizensShops.onNPCEdited(npc);
      }

   }

   private boolean updateNpcOwner(NPC npc) {
      assert npc != null;

      if (!(this.shopkeeper instanceof PlayerShopkeeper)) {
         return false;
      } else {
         PlayerShopkeeper playerShop = (PlayerShopkeeper)this.shopkeeper;
         boolean npcHasChanged = false;
         if (Settings.setCitizenNpcOwnerOfPlayerShops) {
            UUID ownerId = playerShop.getOwnerUUID();
            Owner ownerTrait = (Owner)Unsafe.assertNonNull((Owner)npc.getOrAddTrait(Owner.class));
            if (!ownerId.equals(ownerTrait.getOwnerId())) {
               ownerTrait.setOwner(ownerId);
               npcHasChanged = true;
               Log.debug(() -> {
                  return this.shopkeeper.getLogPrefix() + "Citizens NPC owner set.";
               });
            }
         } else if (npc.hasTrait(Owner.class)) {
            npc.removeTrait(Owner.class);
            npcHasChanged = true;
            Log.debug(() -> {
               return this.shopkeeper.getLogPrefix() + "Citizens NPC owner removed.";
            });
         }

         return npcHasChanged;
      }
   }

   private boolean updateNpcFluidPushable(NPC npc) {
      switch(Settings.citizenNpcFluidPushable) {
      case TRUE:
         if (!npc.isPushableByFluids()) {
            npc.data().set(Metadata.FLUID_PUSHABLE, true);
            Log.debug(() -> {
               return this.shopkeeper.getLogPrefix() + "Made Citizens NPC fluid pushable.";
            });
            return true;
         }

         return false;
      case FALSE:
         if (npc.isPushableByFluids()) {
            npc.data().set(Metadata.FLUID_PUSHABLE, false);
            Log.debug(() -> {
               return this.shopkeeper.getLogPrefix() + "Made Citizens NPC fluid unpushable.";
            });
            return true;
         }

         return false;
      default:
         return false;
      }
   }

   private void loadNpcData(ShopObjectData shopObjectData) throws InvalidDataException {
      DataContainer previousNpcData = this.npcData;
      this.npcData = (DataContainer)shopObjectData.get(NPC_DATA);
      if (this.npcData == null && previousNpcData != null) {
         Log.warning(this.shopkeeper.getLogPrefix() + "Prevented previously restored but not yet applied Citizens NPC data from being cleared!");
         this.npcData = previousNpcData;
         this.shopkeeper.markDirty();
      }

      if (this.npcData != null) {
         assert Settings.snapshotsSaveCitizenNpcData;

         if (this.shopkeeper.isValid()) {
            this.synchronizeNpc();
         }
      }

   }

   private void saveNpcData(ShopObjectData shopObjectData, boolean saveAll) {
      if (Settings.snapshotsSaveCitizenNpcData) {
         DataContainer npcData = this.npcData;
         if (saveAll && npcData == null) {
            NPC npc = this.getNPC();
            if (npc == null) {
               UUID npcId = this.getNPCUniqueId();
               if (npcId != null) {
                  String var10000 = this.shopkeeper.getLogPrefix();
                  Log.warning(var10000 + "Could not save the data of the corresponding Citizens NPC! Citizens NPC not found (uuid: " + String.valueOf(npcId) + ")! Is the Citizens plugin enabled?");
               }

               return;
            }

            npcData = CitizensUtils.Internal.saveNpc(npc);
         }

         shopObjectData.set(NPC_DATA, npcData);
      }
   }

   private boolean applyNpcData(NPC npc) {
      assert npc != null;

      DataContainer npcData = this.npcData;
      if (npcData == null) {
         return false;
      } else {
         Log.debug(() -> {
            String var10000 = this.shopkeeper.getLogPrefix();
            return var10000 + "Applying stored Citizens NPC state to NPC " + npc.getId();
         });
         CitizensUtils.Internal.loadNpc(npc, npcData);
         this.npcData = null;
         this.shopkeeper.markDirty();
         return true;
      }
   }

   protected void setKeepNPCOnDeletion() {
      this.destroyNPC = false;
   }

   public void onShopkeeperAdded(ShopkeeperAddedEvent.Cause cause) {
      super.onShopkeeperAdded(cause);
      this.synchronizeNpc();
      UUID npcId = this.getNPCUniqueId();
      if (npcId != null) {
         this.citizensShops.registerCitizensShopkeeper(this, npcId);
      }

   }

   public void remove() {
      super.remove();
      this.setEntity((Entity)null);
      UUID npcId = this.getNPCUniqueId();
      if (npcId != null) {
         this.citizensShops.unregisterCitizensShopkeeper(this, npcId);
      }

   }

   public void delete() {
      super.delete();

      assert this.entity == null;

      if (this.getNPCUniqueId() != null) {
         if (this.destroyNPC) {
            NPC npc = this.getNPC();
            if (npc != null) {
               CitizensShopkeeperTrait shopkeeperTrait = (CitizensShopkeeperTrait)Unsafe.cast(npc.getTraitNullable(CitizensShopkeeperTrait.class));
               if (shopkeeperTrait != null) {
                  shopkeeperTrait.onShopkeeperDeletion(this.shopkeeper);
               } else {
                  Log.debug(() -> {
                     String var10000 = this.shopkeeper.getUniqueIdLogPrefix();
                     return var10000 + "Deleting Citizens NPC " + CitizensShops.getNPCIdString(npc) + " due to shopkeeper deletion.";
                  });
                  npc.destroy();
                  this.citizensShops.onNPCEdited(npc);
               }
            }
         }

         this.setNPCUniqueId((UUID)null);
      }
   }

   void onNPCDeleted(@Nullable Player player) {
      if (this.shopkeeper.isValid()) {
         NPC npc = (NPC)Unsafe.assertNonNull(this.getNPC());
         Log.debug(() -> {
            String var10000 = this.shopkeeper.getUniqueIdLogPrefix();
            return var10000 + "Deletion due to the deletion of Citizens NPC " + CitizensShops.getNPCIdString(npc) + (player != null ? " by player " + TextUtils.getPlayerString(player) : "");
         });
         this.setKeepNPCOnDeletion();
         this.shopkeeper.delete(player);
      }
   }

   void onCitizensShopsEnabled() {
      this.synchronizeNpc();
   }

   void onCitizensShopsDisabled() {
   }

   void onCitizensReloaded() {
      this.synchronizeNpc();
   }

   @Nullable
   public Entity getEntity() {
      if (this.entity != null) {
         assert this.getNPC() != null;

         assert ((NPC)Unsafe.assertNonNull(this.getNPC())).getEntity() == this.entity;
      }

      return this.entity;
   }

   public boolean isActive() {
      Entity entity = this.getEntity();
      if (entity == null) {
         return false;
      } else {
         return !entity.isDead();
      }
   }

   @Nullable
   private Location getSpawnLocation() {
      Location spawnLocation = this.shopkeeper.getLocation();
      if (spawnLocation == null) {
         return null;
      } else {
         spawnLocation.add(0.5D, 0.5D, 0.5D);
         return spawnLocation;
      }
   }

   public boolean spawn() {
      NPC npc = this.getNPC();
      if (npc == null) {
         this.onSpawnFailed();
         return false;
      } else {
         Location spawnLocation = this.getSpawnLocation();
         if (spawnLocation == null) {
            this.onSpawnFailed();
            return false;
         } else if (!npc.spawn(spawnLocation, SpawnReason.PLUGIN)) {
            this.onSpawnFailed();
            return false;
         } else {
            this.onSpawnSucceeded();
            return true;
         }
      }
   }

   public void despawn() {
      NPC npc = this.getNPC();
      if (npc != null) {
         npc.despawn(DespawnReason.PLUGIN);
      }
   }

   public boolean move() {
      NPC npc = this.getNPC();
      if (npc == null) {
         return false;
      } else {
         Location spawnLocation = this.getSpawnLocation();
         if (spawnLocation == null) {
            return false;
         } else if (npc.isSpawned()) {
            npc.teleport(spawnLocation, TeleportCause.PLUGIN);
            return true;
         } else {
            return npc.spawn(spawnLocation, SpawnReason.PLUGIN);
         }
      }
   }

   void setEntity(@Nullable Entity entity) {
      if (entity != null) {
         NPC npc = (NPC)Unsafe.assertNonNull(this.getNPC());

         assert npc.getEntity() == entity;

         this.onSpawnSucceeded();
         this.updateShopkeeperLocation();
      }

      this.entity = entity;
      this.onIdChanged();
   }

   public void onStopTicking() {
      super.onStopTicking();
      this.updateShopkeeperLocation();
   }

   public void onTick() {
      super.onTick();
      if (this.checkLimiter.request()) {
         NPC npc = this.getNPC();
         if (npc != null) {
            this.indicateTickActivity();
            this.respawnNpcIfMissing(npc);
            this.updateShopkeeperLocation(npc);
         }
      }
   }

   private void respawnNpcIfMissing(NPC npc) {
      assert npc != null;

      Location currentLocation = npc.getStoredLocation();
      if (currentLocation == null) {
         assert !npc.isSpawned();

         Location expectedLocation = this.getSpawnLocation();
         if (expectedLocation != null) {
            assert expectedLocation.getWorld() != null;

            Log.debug(() -> {
               return this.shopkeeper.getLocatedLogPrefix() + "Citizens NPC has no stored location. Attempting spawn.";
            });
            npc.spawn(expectedLocation);
         }
      } else {
         assert currentLocation != null && currentLocation.getWorld() != null;

         Entity entity = npc.getEntity();
         if (entity != null && entity.isDead()) {
            Log.debug(() -> {
               return this.shopkeeper.getLocatedLogPrefix() + "Citizens NPC is missing. Attempting respawn.";
            });
            npc.spawn(currentLocation);
         }
      }
   }

   void onNpcTeleport(Location toLocation) {
      assert toLocation != null;

      this.shopkeeper.setLocation(toLocation);
   }

   private void updateShopkeeperLocation() {
      NPC npc = this.getNPC();
      if (npc != null) {
         this.updateShopkeeperLocation(npc);
      }
   }

   private void updateShopkeeperLocation(NPC npc) {
      assert npc != null;

      Location currentLocation = npc.getStoredLocation();
      if (currentLocation != null) {
         assert currentLocation.getWorld() != null;

         Location expectedLocation = this.getSpawnLocation();

         assert expectedLocation == null || expectedLocation.getWorld() != null;

         if (expectedLocation == null || LocationUtils.getDistanceSquared(expectedLocation, currentLocation) > 1.0D) {
            Log.debug(DebugOptions.regularTickActivities, () -> {
               return this.shopkeeper.getLocatedLogPrefix() + "Citizens NPC moved. Updating shopkeeper location.";
            });
            this.shopkeeper.setLocation(currentLocation);
         }

      }
   }

   public int getNameLengthLimit() {
      return 256;
   }

   public void setName(@Nullable String name) {
      NPC npc = this.getNPC();
      if (npc != null) {
         if (this.setNpcName(npc, name)) {
            this.citizensShops.onNPCEdited(npc);
         }

      }
   }

   private boolean setNpcName(NPC npc, @Nullable String name) {
      assert npc != null;

      if (Settings.showNameplates && name != null && !name.isEmpty()) {
         boolean isPlayerNPC = this.getEntityType() == EntityType.PLAYER;
         String preparedName;
         if (!isPlayerNPC) {
            preparedName = Messages.nameplatePrefix + name;
         } else {
            preparedName = name;
         }

         preparedName = (String)Unsafe.assertNonNull(this.prepareName(preparedName));
         npc.setName(preparedName);
         npc.data().setPersistent(Metadata.NAMEPLATE_VISIBLE, !Settings.alwaysShowNameplates && !isPlayerNPC ? "hover" : "true");
      } else {
         npc.setName("");
         npc.data().setPersistent(Metadata.NAMEPLATE_VISIBLE, "false");
      }

      return true;
   }

   @Nullable
   public String getName() {
      NPC npc = this.getNPC();
      return npc == null ? null : npc.getName();
   }

   public void onShopOwnerChanged() {
      super.onShopOwnerChanged();

      assert this.shopkeeper instanceof PlayerShopkeeper;

      NPC npc = this.getNPC();
      if (npc != null) {
         boolean npcHasChanged = this.updateNpcOwner(npc);
         if (!Settings.allowRenamingOfPlayerNpcShops) {
            String ownerName = ((PlayerShopkeeper)this.shopkeeper).getOwnerName();
            npcHasChanged |= this.setNpcName(npc, ownerName);
         }

         if (npcHasChanged) {
            this.citizensShops.onNPCEdited(npc);
         }

      }
   }

   static {
      NPC_UNIQUE_ID = (new BasicProperty()).dataKeyAccessor("npcId", UUIDSerializers.LENIENT).nullable().defaultValue((Object)null).build();
      NPC_DATA = (new BasicProperty()).dataKeyAccessor("npc-data", DataContainerSerializers.DEFAULT).nullable().defaultValue((Object)null).build();
      nextCheckingOffset = new CyclicCounter(1, 11);
      ShopkeeperDataMigrator.registerMigration(new Migration("citizens-npc-data-cleanup", MigrationPhase.ofShopObjectClass(SKCitizensShopObject.class)) {
         public boolean migrate(ShopkeeperData shopkeeperData, String logPrefix) throws InvalidDataException {
            if (Settings.snapshotsSaveCitizenNpcData) {
               return false;
            } else {
               ShopObjectData shopObjectData = (ShopObjectData)shopkeeperData.get(AbstractShopkeeper.SHOP_OBJECT_DATA);
               DataContainer npcData = (DataContainer)shopObjectData.get(SKCitizensShopObject.NPC_DATA);
               if (npcData != null) {
                  Log.warning(logPrefix + "Deleted previously saved Citizens NPC data!");
                  shopObjectData.set(SKCitizensShopObject.NPC_DATA, (Object)null);
                  return true;
               } else {
                  return false;
               }
            }
         }
      });
   }
}
