package com.nisovin.shopkeepers.shopobjects;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.ShopkeeperAddedEvent;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.ShopObject;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperPropertyValuesHolder;
import com.nisovin.shopkeepers.shopkeeper.registry.ShopObjectRegistry;
import com.nisovin.shopkeepers.shopkeeper.spawning.ShopkeeperSpawnState;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.bukkit.BlockFaceUtils;
import com.nisovin.shopkeepers.util.bukkit.LocationUtils;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.validation.java.StringValidators;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValuesHolder;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.BooleanSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.StringSerializers;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractShopObject implements ShopObject {
   private static final String DATA_KEY_SHOP_OBJECT_TYPE = "type";
   public static final Property<String> SHOP_OBJECT_TYPE_ID;
   public static final Property<AbstractShopObjectType<?>> SHOP_OBJECT_TYPE;
   public static final Property<Boolean> SPAWN_FAILED;
   protected final AbstractShopkeeper shopkeeper;
   protected final PropertyValuesHolder properties;
   private final PropertyValue<Boolean> lastSpawnFailedProperty;
   @Nullable
   private Object lastId = null;
   private boolean tickActivity = false;

   protected AbstractShopObject(AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      assert shopkeeper != null;

      this.shopkeeper = shopkeeper;
      this.properties = new ShopkeeperPropertyValuesHolder(shopkeeper);
      this.lastSpawnFailedProperty = (new PropertyValue(SPAWN_FAILED)).build(this.properties);
   }

   public abstract AbstractShopObjectType<?> getType();

   public final AbstractShopkeeper getShopkeeper() {
      return this.shopkeeper;
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      Validate.notNull(shopObjectData, (String)"shopObjectData is null");
      ShopObjectType<?> shopObjectType = (ShopObjectType)shopObjectData.get(SHOP_OBJECT_TYPE);

      assert shopObjectType != null;

      if (shopObjectType != this.getType()) {
         String var10002 = this.getType().getIdentifier();
         throw new InvalidDataException("Shop object data is for a different shop object type (expected: " + var10002 + ", got: " + shopObjectType.getIdentifier() + ")!");
      } else {
         this.lastSpawnFailedProperty.load(shopObjectData);
      }
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      Validate.notNull(shopObjectData, (String)"shopObjectData is null");
      shopObjectData.set(SHOP_OBJECT_TYPE, this.getType());
      this.lastSpawnFailedProperty.save(shopObjectData);
   }

   public void setup() {
   }

   public int updateItems(String logPrefix, @ReadWrite ShopObjectData shopObjectData) {
      return 0;
   }

   public void onShopkeeperAdded(ShopkeeperAddedEvent.Cause cause) {
   }

   public void remove() {
   }

   public void delete() {
   }

   public void setAttachedBlockFace(BlockFace attachedBlockFace) {
      Validate.notNull(attachedBlockFace, (String)"attachedBlockFace is null");
      Validate.isTrue(BlockFaceUtils.isBlockSide(attachedBlockFace), "attachedBlockFace is not a block side");
   }

   @Nullable
   public final Object getLastId() {
      return this.lastId;
   }

   public final void setLastId(@Nullable Object lastId) {
      this.lastId = lastId;
   }

   @Nullable
   public abstract Object getId();

   protected final void onIdChanged() {
      ShopObjectRegistry shopObjectRegistry = SKShopkeepersPlugin.getInstance().getShopkeeperRegistry().getShopObjectRegistry();
      shopObjectRegistry.updateShopObjectRegistration(this.shopkeeper);
   }

   protected final boolean isSpawningScheduled() {
      return ((ShopkeeperSpawnState)this.shopkeeper.getComponents().getOrAdd(ShopkeeperSpawnState.class)).isSpawningScheduled();
   }

   public abstract boolean isActive();

   public abstract boolean spawn();

   public boolean isLastSpawnFailed() {
      return (Boolean)this.lastSpawnFailedProperty.getValue();
   }

   protected final void onSpawnFailed() {
      this.lastSpawnFailedProperty.setValue(true);
   }

   protected final void onSpawnSucceeded() {
      this.lastSpawnFailedProperty.setValue(false);
   }

   public abstract void despawn();

   public final boolean respawn() {
      if (!this.isSpawned()) {
         return false;
      } else {
         this.despawn();
         return this.spawn();
      }
   }

   @Nullable
   public abstract Location getLocation();

   public abstract boolean move();

   public void onStartTicking() {
   }

   public void onStopTicking() {
   }

   public void onTickStart() {
      this.tickActivity = false;
   }

   public void onTick() {
   }

   public void onTickEnd() {
   }

   protected void indicateTickActivity() {
      this.tickActivity = true;
   }

   @Nullable
   public abstract Location getTickVisualizationParticleLocation();

   public void visualizeLastTick() {
      if (this.tickActivity) {
         Location particleLocation = this.getTickVisualizationParticleLocation();
         if (particleLocation != null) {
            World world = LocationUtils.getWorld(particleLocation);
            world.spawnParticle(Particle.ANGRY_VILLAGER, particleLocation, 1);
         }
      }
   }

   public int getNameLengthLimit() {
      return 256;
   }

   @Nullable
   public String prepareName(@Nullable String name) {
      if (name == null) {
         return null;
      } else {
         String prepared = name;
         int lengthLimit = this.getNameLengthLimit();
         if (name.length() > lengthLimit) {
            prepared = name.substring(0, lengthLimit);
         }

         return prepared;
      }
   }

   public abstract void setName(@Nullable String var1);

   @Nullable
   public abstract String getName();

   public void onShopOwnerChanged() {
   }

   public List<Button> createEditorButtons() {
      return new ArrayList();
   }

   static {
      SHOP_OBJECT_TYPE_ID = (new BasicProperty()).name("type-id").dataKeyAccessor("type", StringSerializers.STRICT).validator(StringValidators.NON_EMPTY).build();
      SHOP_OBJECT_TYPE = (new BasicProperty()).dataKeyAccessor("type", new DataSerializer<AbstractShopObjectType<?>>() {
         @Nullable
         public Object serialize(AbstractShopObjectType<?> value) {
            Validate.notNull(value, (String)"value is null");
            return value.getIdentifier();
         }

         public AbstractShopObjectType<?> deserialize(Object data) throws InvalidDataException {
            String shopObjectTypeId = (String)StringSerializers.STRICT_NON_EMPTY.deserialize(data);
            SKShopObjectTypesRegistry shopObjectTypeRegistry = SKShopkeepersPlugin.getInstance().getShopObjectTypeRegistry();
            AbstractShopObjectType<?> shopObjectType = (AbstractShopObjectType)shopObjectTypeRegistry.get(shopObjectTypeId);
            if (shopObjectType == null) {
               throw new InvalidDataException("Unknown shop object type: " + shopObjectTypeId);
            } else {
               return shopObjectType;
            }
         }
      }).build();
      SPAWN_FAILED = (new BasicProperty()).dataKeyAccessor("spawn-failed", BooleanSerializers.STRICT).defaultValue(false).omitIfDefault().build();
   }
}
