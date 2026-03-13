package com.nisovin.shopkeepers.shopkeeper;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.ShopkeeperAddedEvent;
import com.nisovin.shopkeepers.api.events.ShopkeeperRemoveEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopType;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperCreateException;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperLoadException;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperSnapshot;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.api.shopobjects.virtual.VirtualShopObject;
import com.nisovin.shopkeepers.api.shopobjects.virtual.VirtualShopObjectType;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.api.ui.UISession;
import com.nisovin.shopkeepers.api.ui.UIType;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.debug.Debug;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.migration.Migration;
import com.nisovin.shopkeepers.shopkeeper.migration.MigrationPhase;
import com.nisovin.shopkeepers.shopkeeper.migration.ShopkeeperDataMigrator;
import com.nisovin.shopkeepers.shopkeeper.ticking.ShopkeeperTicker;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObject;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObjectType;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.lib.UISessionManager;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.ViewProvider;
import com.nisovin.shopkeepers.ui.trading.TradingViewProvider;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.bukkit.BlockLocation;
import com.nisovin.shopkeepers.util.bukkit.ColorUtils;
import com.nisovin.shopkeepers.util.bukkit.LocationUtils;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.DataKeyAccessor;
import com.nisovin.shopkeepers.util.data.property.EmptyDataPredicates;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.validation.java.IntegerValidators;
import com.nisovin.shopkeepers.util.data.property.validation.java.StringValidators;
import com.nisovin.shopkeepers.util.data.serialization.DataAccessor;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.ColoredStringSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.BooleanSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.DataContainerSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.NumberSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.StringSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.UUIDSerializers;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import com.nisovin.shopkeepers.util.text.MessageArguments;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractShopkeeper implements Shopkeeper {
   private static final String VIRTUAL_SHOPKEEPER_MARKER = "[virtual]";
   public static final int MAX_NAME_LENGTH = 256;
   private static final int SNAPSHOTS_WARNING_LIMIT = 10;
   private static final DustOptions[] TICK_VISUALIZATION_DUSTS = new DustOptions[4];
   private boolean initialized = false;
   private int id;
   private UUID uniqueId = (UUID)Unsafe.uncheckedNull();
   private AbstractShopObject shopObject = (AbstractShopObject)Unsafe.uncheckedNull();
   @Nullable
   private BlockLocation location;
   private float yaw;
   @Nullable
   private ChunkCoords chunkCoords;
   @Nullable
   private ChunkCoords lastChunkCoords = null;
   private boolean open = true;
   private String name = "";
   private final List<SKShopkeeperSnapshot> snapshots = new ArrayList();
   private final List<? extends SKShopkeeperSnapshot> snapshotsView;
   private final ShopkeeperComponentHolder components;
   private final Map<String, Supplier<?>> messageArgumentsMap;
   private final MessageArguments messageArguments;
   private boolean dirty;
   private boolean valid;
   private boolean active;
   private boolean ticking;
   private final Map<String, ViewProvider> viewProviders;
   private final int tickingGroup;
   private static final String DATA_KEY_SHOP_TYPE = "type";
   public static final Property<String> SHOP_TYPE_ID;
   public static final Property<AbstractShopType<?>> SHOP_TYPE;
   public static final Property<Integer> ID;
   public static final Property<UUID> UNIQUE_ID;
   public static final Property<String> WORLD_NAME;
   public static final Property<Integer> LOCATION_X;
   public static final Property<Integer> LOCATION_Y;
   public static final Property<Integer> LOCATION_Z;
   public static final Property<Float> YAW;
   public static final Property<BlockLocation> LOCATION;
   public static final Property<Boolean> OPEN;
   public static final Property<String> NAME;
   public static final Property<ShopObjectData> SHOP_OBJECT_DATA;
   public static final Property<List<? extends SKShopkeeperSnapshot>> SNAPSHOTS;

   public static String getLogPrefix(int shopkeeperId) {
      return "Shopkeeper " + shopkeeperId + ": ";
   }

   protected AbstractShopkeeper() {
      this.snapshotsView = Collections.unmodifiableList(this.snapshots);
      this.components = new ShopkeeperComponentHolder((AbstractShopkeeper)Unsafe.initialized(this));
      this.messageArgumentsMap = new HashMap();
      this.messageArguments = MessageArguments.ofMap(this.messageArgumentsMap);
      this.dirty = false;
      this.valid = false;
      this.active = false;
      this.ticking = false;
      this.viewProviders = new HashMap();
      this.tickingGroup = ShopkeeperTicker.nextTickingGroup();
   }

   final void initOnCreation(int id, ShopCreationData shopCreationData) throws ShopkeeperCreateException {
      this.loadFromCreationData(id, shopCreationData);
      this.commonSetup();
   }

   final void initOnLoad(ShopkeeperData shopkeeperData) throws InvalidDataException {
      this.loadFromSaveData(shopkeeperData);
      this.commonSetup();
   }

   private void initialize() {
      Validate.State.isTrue(!this.initialized, "The shopkeeper has already been initialized!");
      this.initialized = true;
   }

   final boolean isInitialized() {
      return this.initialized;
   }

   private void commonSetup() {
      this.setup();
      this.postSetup();
   }

   protected void loadFromCreationData(int id, ShopCreationData shopCreationData) throws ShopkeeperCreateException {
      this.getType().validateCreationData(shopCreationData);
      this.initialize();
      this.id = id;
      this.uniqueId = UUID.randomUUID();
      if (shopCreationData.getShopType() != this.getType()) {
         String var10002 = this.getType().getIdentifier();
         throw new ShopkeeperCreateException("The shopCreationData is for a different shop type (expected: " + var10002 + ", got: " + shopCreationData.getShopType().getIdentifier() + ")!");
      } else {
         ShopObjectType<?> shopObjectType = shopCreationData.getShopObjectType();
         Validate.isTrue(shopObjectType instanceof AbstractShopObjectType, "ShopObjectType of shopCreationData is not of type AbstractShopObjectType, but: " + shopObjectType.getClass().getName());
         if (shopObjectType instanceof VirtualShopObjectType) {
            this.location = null;
            this.yaw = 0.0F;
         } else {
            Location spawnLocation = (Location)Unsafe.assertNonNull(shopCreationData.getSpawnLocation());

            assert spawnLocation.getWorld() != null;

            this.location = BlockLocation.of(spawnLocation);

            assert this.location.hasWorldName();

            this.yaw = spawnLocation.getYaw();
         }

         this.updateChunkCoords();
         this.shopObject = this.createShopObject((AbstractShopObjectType)shopObjectType, shopCreationData);
         this.markDirty();
      }
   }

   protected void setup() {
      this.registerViewProviderIfMissing(DefaultUITypes.TRADING(), () -> {
         return new TradingViewProvider(this);
      });
   }

   protected void postSetup() {
      this.getShopObject().setup();
   }

   protected void loadFromSaveData(ShopkeeperData shopkeeperData) throws InvalidDataException {
      Validate.notNull(shopkeeperData, (String)"shopkeeperData is null");
      this.initialize();
      this.id = (Integer)shopkeeperData.get(ID);
      this.uniqueId = (UUID)shopkeeperData.get(UNIQUE_ID);
      AbstractShopType<?> shopType = this.getAndValidateShopType(shopkeeperData);

      assert shopType != null;

      ShopObjectData shopObjectData = (ShopObjectData)shopkeeperData.get(SHOP_OBJECT_DATA);

      assert shopObjectData != null;

      AbstractShopObjectType<?> objectType = (AbstractShopObjectType)shopObjectData.get(AbstractShopObject.SHOP_OBJECT_TYPE);

      assert objectType != null;

      BlockLocation location = (BlockLocation)shopkeeperData.get(LOCATION);

      assert location != null;

      this.location = location;
      this.yaw = (Float)shopkeeperData.get(YAW);
      if (objectType instanceof VirtualShopObjectType) {
         if (!location.isEmpty() || this.yaw != 0.0F) {
            String var10002 = TextUtils.getLocationString(location, (double)this.yaw);
            throw new InvalidDataException("Shopkeeper is virtual, but stores a non-empty location: " + var10002);
         }

         this.location = null;
      } else if (!location.hasWorldName()) {
         throw new InvalidDataException("Missing world name!");
      }

      this.updateChunkCoords();
      this.shopObject = this.createShopObject(objectType, (ShopCreationData)null);
      this.loadSnapshots(shopkeeperData);
      this.loadDynamicState(shopkeeperData);
   }

   private AbstractShopType<?> getAndValidateShopType(ShopkeeperData shopkeeperData) throws InvalidDataException {
      assert shopkeeperData != null;

      AbstractShopType<?> shopType = (AbstractShopType)shopkeeperData.get(SHOP_TYPE);

      assert shopType != null;

      if (shopType != this.getType()) {
         String var10002 = this.getType().getIdentifier();
         throw new InvalidDataException("The shopkeeper data is for a different shop type (expected: " + var10002 + ", got: " + shopType.getIdentifier() + ")!");
      } else {
         return shopType;
      }
   }

   private AbstractShopObject createShopObject(AbstractShopObjectType<?> objectType, @Nullable ShopCreationData shopCreationData) {
      assert objectType != null;

      AbstractShopObject shopObject = objectType.createObject(this, shopCreationData);
      String var10001 = this.getLogPrefix();
      Validate.State.notNull(shopObject, (String)(var10001 + "Shop object type '" + objectType.getIdentifier() + "' created null shop object!"));
      return shopObject;
   }

   public void loadDynamicState(ShopkeeperData shopkeeperData) throws InvalidDataException {
      Validate.notNull(shopkeeperData, (String)"shopkeeperData is null");
      ShopType<?> shopType = this.getAndValidateShopType(shopkeeperData);

      assert shopType != null;

      this._setOpen((Boolean)shopkeeperData.get(OPEN));
      this._setName((String)shopkeeperData.get(NAME));
      ShopObjectData shopObjectData = (ShopObjectData)shopkeeperData.getOrNullIfMissing(SHOP_OBJECT_DATA);
      if (shopObjectData != null) {
         AbstractShopObjectType<?> objectType = (AbstractShopObjectType)shopObjectData.get(AbstractShopObject.SHOP_OBJECT_TYPE);

         assert objectType != null;

         if (objectType == this.shopObject.getType()) {
            this.shopObject.load(shopObjectData);
         } else {
            Log.debug(() -> {
               String var10000 = this.getLogPrefix();
               return var10000 + "Ignoring shop object data of different type (expected: " + this.shopObject.getType().getIdentifier() + ", got: " + objectType.getIdentifier() + ")!";
            });
         }
      }

   }

   public void save(ShopkeeperData shopkeeperData, boolean saveAll) {
      Validate.notNull(shopkeeperData, (String)"shopkeeperData is null");
      shopkeeperData.set(ID, this.id);
      shopkeeperData.set(UNIQUE_ID, this.uniqueId);
      if (!this.isVirtual()) {
         shopkeeperData.set(LOCATION, this.location);
         shopkeeperData.set(YAW, this.yaw);
      }

      this.saveDynamicState(shopkeeperData, saveAll);
      this.saveSnapshots(shopkeeperData);
   }

   public void saveDynamicState(ShopkeeperData shopkeeperData, boolean saveAll) {
      Validate.notNull(shopkeeperData, (String)"shopkeeperData is null");
      shopkeeperData.set(SHOP_TYPE, this.getType());
      shopkeeperData.set(OPEN, this.open);
      shopkeeperData.set(NAME, this.name);
      ShopObjectData shopObjectData = ShopObjectData.ofNonNull(DataContainer.create());
      this.shopObject.save(shopObjectData, saveAll);
      shopkeeperData.set(SHOP_OBJECT_DATA, shopObjectData);
   }

   public final void save() {
      this.markDirty();
      ShopkeepersPlugin.getInstance().getShopkeeperStorage().save();
   }

   public final void saveDelayed() {
      this.markDirty();
      ShopkeepersPlugin.getInstance().getShopkeeperStorage().saveDelayed();
   }

   public final void markDirty() {
      this.dirty = true;
      if (this.isValid()) {
         SKShopkeepersPlugin.getInstance().getShopkeeperStorage().markDirty(this);
      }

   }

   public final boolean isDirty() {
      return this.dirty;
   }

   public final void onSave() {
      this.dirty = false;
   }

   public final int updateItems() {
      this.abortUISessionsDelayed();
      ShopkeeperData dynamicShopkeeperData = ShopkeeperData.ofNonNull(DataContainer.create());
      this.saveDynamicState(dynamicShopkeeperData, false);
      int updatedItems = this.updateItems(this.getLogPrefix(), dynamicShopkeeperData);
      if (updatedItems > 0) {
         try {
            this.loadDynamicState(dynamicShopkeeperData);
         } catch (InvalidDataException var10) {
            Log.warning((String)(this.getLogPrefix() + "Failed re-apply the updated shopkeeper data!"), (Throwable)var10);
            return 0;
         }
      }

      int snapshotId = 0;
      ListIterator snapshotIterator = this.snapshots.listIterator();

      while(snapshotIterator.hasNext()) {
         SKShopkeeperSnapshot snapshot = (SKShopkeeperSnapshot)Unsafe.assertNonNull((SKShopkeeperSnapshot)snapshotIterator.next());
         ++snapshotId;
         String snapshotLogPrefix = this.getLogPrefix(snapshotId, snapshot);
         ShopkeeperData updatedSnaphotShopkeeperData = ShopkeeperData.ofNonNull(DataContainer.ofNonNull(snapshot.getShopkeeperData().getValuesCopy()));
         int snapshotUpdatedItems = this.updateItems(snapshotLogPrefix, updatedSnaphotShopkeeperData);
         if (snapshotUpdatedItems > 0) {
            SKShopkeeperSnapshot updatedSnapshot = new SKShopkeeperSnapshot(snapshot.getName(), snapshot.getTimestamp(), updatedSnaphotShopkeeperData);
            snapshotIterator.set(updatedSnapshot);
            updatedItems += snapshotUpdatedItems;
         }
      }

      if (updatedItems > 0) {
         this.markDirty();
      }

      return updatedItems;
   }

   protected int updateItems(String logPrefix, @ReadWrite ShopkeeperData shopkeeperData) {
      int updatedItems = 0;
      int updatedItems = updatedItems + this.updateShopObjectItems(logPrefix, shopkeeperData);
      return updatedItems;
   }

   private int updateShopObjectItems(String logPrefix, @ReadWrite ShopkeeperData shopkeeperData) {
      ShopObjectData shopObjectData;
      try {
         shopObjectData = (ShopObjectData)shopkeeperData.getOrNullIfMissing(SHOP_OBJECT_DATA);
      } catch (InvalidDataException var7) {
         Log.warning((String)(logPrefix + "Failed to load '" + SHOP_OBJECT_DATA.getName() + "'!"), (Throwable)var7);
         return 0;
      }

      if (shopObjectData == null) {
         return 0;
      } else {
         AbstractShopObjectType objectType;
         try {
            objectType = (AbstractShopObjectType)shopObjectData.get(AbstractShopObject.SHOP_OBJECT_TYPE);

            assert objectType != null;
         } catch (InvalidDataException var8) {
            Log.warning((String)(logPrefix + "Failed to load '" + AbstractShopObject.SHOP_OBJECT_TYPE.getName() + "'!"), (Throwable)var8);
            return 0;
         }

         if (objectType != this.shopObject.getType()) {
            Log.debug(() -> {
               String var10000 = this.getLogPrefix();
               return var10000 + "Ignoring shop object data of different type (expected: " + this.shopObject.getType().getIdentifier() + ", got: " + objectType.getIdentifier() + ")!";
            });
            return 0;
         } else {
            ShopObjectData updatedShopObjectData = ShopObjectData.ofNonNull(DataContainer.ofNonNull(shopObjectData.getValuesCopy()));
            int updatedItems = this.shopObject.updateItems(logPrefix, updatedShopObjectData);
            if (updatedItems > 0) {
               shopkeeperData.set(SHOP_OBJECT_DATA, updatedShopObjectData);
            }

            return updatedItems;
         }
      }
   }

   public final ShopkeeperComponentHolder getComponents() {
      return this.components;
   }

   public final boolean isValid() {
      return this.valid;
   }

   public final void informAdded(ShopkeeperAddedEvent.Cause cause) {
      assert !this.valid;

      this.valid = true;
      if (this.isDirty()) {
         this.markDirty();
      }

      this.onAdded(cause);
   }

   protected void onAdded(ShopkeeperAddedEvent.Cause cause) {
      this.shopObject.onShopkeeperAdded(cause);
   }

   public final void informRemoval(ShopkeeperRemoveEvent.Cause cause) {
      assert this.valid;

      this.valid = false;
      this.onRemoval(cause);
      if (cause == ShopkeeperRemoveEvent.Cause.DELETE) {
         this.onDeletion();
      }

   }

   protected void onRemoval(ShopkeeperRemoveEvent.Cause cause) {
      this.shopObject.remove();
   }

   public final void delete() {
      this.delete((Player)null);
   }

   public void delete(@Nullable Player player) {
      SKShopkeepersPlugin.getInstance().getShopkeeperRegistry().deleteShopkeeper(this);
   }

   protected void onDeletion() {
      this.shopObject.delete();
   }

   public final void setActive(boolean active) {
      this.active = active;
   }

   public final boolean isActive() {
      return this.active;
   }

   public abstract AbstractShopType<?> getType();

   public final int getId() {
      return this.id;
   }

   public final UUID getUniqueId() {
      return this.uniqueId;
   }

   public final String getIdString() {
      int var10000 = this.id;
      return var10000 + " (" + this.uniqueId.toString() + ")";
   }

   public final String getLogPrefix() {
      return getLogPrefix(this.id);
   }

   public final String getUniqueIdLogPrefix() {
      return "Shopkeeper " + this.getIdString() + ": ";
   }

   public final String getLocatedLogPrefix() {
      if (this.isVirtual()) {
         return "Shopkeeper " + this.id + " [virtual]: ";
      } else {
         int var10000 = this.id;
         return "Shopkeeper " + var10000 + " at " + this.getPositionString() + ": ";
      }
   }

   public final String getDisplayName() {
      String name = this.getName();
      return !name.isEmpty() ? name : String.valueOf(this.getId());
   }

   public final boolean isVirtual() {
      assert (this.location != null && this.location.hasWorldName()) ^ (this.location == null && this.shopObject instanceof VirtualShopObject);

      return this.location == null;
   }

   @Nullable
   public final String getWorldName() {
      return this.location != null ? this.location.getWorldName() : null;
   }

   public final int getX() {
      return this.location != null ? this.location.getX() : 0;
   }

   public final int getY() {
      return this.location != null ? this.location.getY() : 0;
   }

   public final int getZ() {
      return this.location != null ? this.location.getZ() : 0;
   }

   public final String getPositionString() {
      return this.isVirtual() ? "[virtual]" : TextUtils.getLocationString((BlockLocation)Unsafe.assertNonNull(this.location));
   }

   @Nullable
   public final Location getLocation() {
      if (this.isVirtual()) {
         return null;
      } else {
         BlockLocation location = (BlockLocation)Unsafe.assertNonNull(this.location);

         assert location != null && location.hasWorldName();

         World world = location.getWorld();
         return world == null ? null : new Location(world, (double)location.getX(), (double)location.getY(), (double)location.getZ(), this.yaw, 0.0F);
      }
   }

   @Nullable
   public final BlockLocation getBlockLocation() {
      return this.location;
   }

   public final void setLocation(Location location) {
      this.setLocation(location, (BlockFace)null);
   }

   public final void setLocation(Location location, @Nullable BlockFace attachedBlockFace) {
      this.setLocation(BlockLocation.of(location));

      assert location != null;

      this.setYaw(location.getYaw());
      if (attachedBlockFace != null) {
         this.shopObject.setAttachedBlockFace(attachedBlockFace);
      }

   }

   public final void setLocation(BlockLocation location) {
      Validate.State.isTrue(!this.isVirtual(), "Cannot set location of virtual shopkeeper!");
      Validate.notNull(location, (String)"location is null");
      Validate.isTrue(location.hasWorldName(), "location has no world name");
      this.location = location.immutable();
      this.updateChunkCoords();
      this.markDirty();
      if (this.isValid()) {
         SKShopkeepersPlugin.getInstance().getShopkeeperRegistry().onShopkeeperMoved(this);
      }

      this.onShopkeeperMoved();
   }

   protected void onShopkeeperMoved() {
   }

   public final void teleport(Location location, @Nullable BlockFace attachedBlockFace) {
      Validate.notNull(location, (String)"location is null");
      boolean spawned = this.shopObject.isSpawned();
      this.setLocation(location, attachedBlockFace);
      if (spawned || !this.shopObject.getType().mustBeSpawned()) {
         this.shopObject.move();
      }

   }

   public final float getYaw() {
      return this.yaw;
   }

   public final void setYaw(float yaw) {
      Validate.State.isTrue(!this.isVirtual(), "Cannot set yaw of virtual shopkeeper!");
      this.yaw = yaw;
      this.markDirty();
   }

   @Nullable
   public final ChunkCoords getChunkCoords() {
      return this.chunkCoords;
   }

   private void updateChunkCoords() {
      if (this.isVirtual()) {
         this.chunkCoords = null;
      } else {
         this.chunkCoords = ((BlockLocation)Unsafe.assertNonNull(this.location)).getChunkCoords();
      }

   }

   @Nullable
   public final ChunkCoords getLastChunkCoords() {
      return this.lastChunkCoords;
   }

   public final void setLastChunkCoords(@Nullable ChunkCoords chunkCoords) {
      this.lastChunkCoords = chunkCoords;
   }

   public final MessageArguments getMessageArguments(String contextPrefix) {
      if (this.messageArgumentsMap.isEmpty()) {
         this.populateMessageArguments(this.messageArgumentsMap);

         assert !this.messageArgumentsMap.isEmpty();
      }

      return this.messageArguments.prefixed(contextPrefix);
   }

   protected void populateMessageArguments(Map<String, Supplier<?>> messageArguments) {
      messageArguments.put("id", this::getId);
      messageArguments.put("uuid", this::getUniqueId);
      messageArguments.put("name", () -> {
         return Text.parse(this.getName());
      });
      messageArguments.put("world", () -> {
         return StringUtils.getOrEmpty(this.getWorldName());
      });
      messageArguments.put("x", this::getX);
      messageArguments.put("y", this::getY);
      messageArguments.put("z", this::getZ);
      messageArguments.put("yaw", () -> {
         return TextUtils.format((double)this.getYaw());
      });
      messageArguments.put("location", this::getPositionString);
      messageArguments.put("type", () -> {
         return this.getType().getIdentifier();
      });
      messageArguments.put("object_type", () -> {
         return this.getShopObject().getType().getIdentifier();
      });
   }

   public boolean isOpen() {
      return this.open;
   }

   public void setOpen(boolean open) {
      this._setOpen(open);
      this.markDirty();
   }

   private void _setOpen(boolean open) {
      this.open = open;
      if (!open) {
         this.onClosed();
      }

   }

   protected void onClosed() {
      UISessionManager.getInstance().abortUISessionsForContextDelayed(this, DefaultUITypes.TRADING());
      UISessionManager.getInstance().abortUISessionsForContextDelayed(this, DefaultUITypes.HIRING());
   }

   public final String getName() {
      return this.name;
   }

   public final void setName(@Nullable String newName) {
      this._setName(newName);
      this.markDirty();
   }

   private void _setName(@Nullable String newName) {
      String preparedName = this.prepareName(newName);
      this.name = preparedName;
      this.shopObject.setName(preparedName);
   }

   private String prepareName(@Nullable String name) {
      String preparedName = name != null ? name : "";
      preparedName = TextUtils.convertHexColorsToBukkit(preparedName);
      preparedName = TextUtils.colorize(preparedName);
      preparedName = this.trimName(preparedName);
      return preparedName;
   }

   private String trimName(String name) {
      assert name != null;

      if (name.length() <= 256) {
         return name;
      } else {
         String trimmedName = name.substring(0, 256);
         Log.warning(this.getLogPrefix() + "Name is more than 256 characters long ('" + name + "'). Name is trimmed to '" + trimmedName + "'.");
         return trimmedName;
      }
   }

   public boolean isValidName(@Nullable String name) {
      return name != null && name.length() <= 256 && Settings.DerivedSettings.shopNamePattern.matcher(name).matches();
   }

   public AbstractShopObject getShopObject() {
      return this.shopObject;
   }

   public static String getLogPrefix(String shopkeeperPrefix, int snapshotId, ShopkeeperSnapshot snapshot) {
      return shopkeeperPrefix + "Snapshot " + snapshotId + " ('" + snapshot.getName() + "'): ";
   }

   public final String getLogPrefix(int snapshotId, ShopkeeperSnapshot snapshot) {
      return getLogPrefix(this.getLogPrefix(), snapshotId, snapshot);
   }

   private void loadSnapshots(ShopkeeperData shopkeeperData) throws InvalidDataException {
      assert shopkeeperData != null;

      List<? extends SKShopkeeperSnapshot> loadedSnapshots = (List)shopkeeperData.get(SNAPSHOTS);
      this.snapshots.clear();

      try {
         loadedSnapshots.forEach(this::_addSnapshot);
      } catch (IllegalArgumentException var7) {
         int snapshotId = this.getSnapshots().size() + 1;
         ShopkeeperSnapshot snapshot = (ShopkeeperSnapshot)loadedSnapshots.get(snapshotId - 1);
         String snapshotLogPrefix = "Snapshot " + snapshotId + " ('" + snapshot.getName() + "'): ";
         throw new InvalidDataException(snapshotLogPrefix + var7.getMessage(), var7);
      }

      this.checkSnapshotsCountLimit();
   }

   private void checkSnapshotsCountLimit() {
      int snapshotsCount = this.getSnapshots().size();
      if (snapshotsCount > 10) {
         String var10000 = this.getLogPrefix();
         Log.warning(var10000 + "This shopkeeper has has more than 10 snapshots (" + snapshotsCount + ")! Consider deleting no longer needed snapshots to save memory and storage space.");
      }

   }

   private void saveSnapshots(ShopkeeperData shopkeeperData) {
      assert shopkeeperData != null;

      shopkeeperData.set(SNAPSHOTS, this.snapshotsView);
   }

   public final List<? extends SKShopkeeperSnapshot> getSnapshots() {
      return this.snapshotsView;
   }

   public final SKShopkeeperSnapshot getSnapshot(int index) {
      return (SKShopkeeperSnapshot)this.snapshotsView.get(index);
   }

   public final int getSnapshotIndex(String name) {
      String normalizedName = StringUtils.normalize(name);
      if (StringUtils.isEmpty(normalizedName)) {
         return -1;
      } else {
         for(int index = 0; index < this.snapshotsView.size(); ++index) {
            ShopkeeperSnapshot snapshot = (ShopkeeperSnapshot)this.snapshotsView.get(index);
            String normalizedSnapshotName = StringUtils.normalize(snapshot.getName());
            if (normalizedSnapshotName.equals(normalizedName)) {
               return index;
            }
         }

         return -1;
      }
   }

   @Nullable
   public final SKShopkeeperSnapshot getSnapshot(String name) {
      int index = this.getSnapshotIndex(name);
      return index != -1 ? this.getSnapshot(index) : null;
   }

   public final SKShopkeeperSnapshot createSnapshot(String name) {
      Instant timestamp = Instant.now();
      ShopkeeperData dynamicShopkeeperData = ShopkeeperData.ofNonNull(DataContainer.create());
      this.saveDynamicState(dynamicShopkeeperData, true);
      return new SKShopkeeperSnapshot(name, timestamp, dynamicShopkeeperData);
   }

   public final void addSnapshot(ShopkeeperSnapshot snapshot) {
      this._addSnapshot(snapshot);
      this.checkSnapshotsCountLimit();
      this.markDirty();
   }

   private void _addSnapshot(ShopkeeperSnapshot snapshot) {
      Validate.notNull(snapshot, (String)"snapshot is null");
      Validate.isTrue(snapshot instanceof SKShopkeeperSnapshot, () -> {
         String var10000 = SKShopkeeperSnapshot.class.getName();
         return "snapshot is not of type " + var10000 + ", but " + snapshot.getClass().getName();
      });
      SKShopkeeperSnapshot skSnapshot = (SKShopkeeperSnapshot)snapshot;

      try {
         this.getAndValidateShopType(skSnapshot.getShopkeeperData());
      } catch (InvalidDataException var4) {
         Validate.error("Invalid snapshot shop type: " + var4.getMessage());
      }

      String snapshotName = snapshot.getName();
      Validate.isTrue(this.getSnapshot(snapshotName) == null, () -> {
         return "There already exists a snapshot with this name: " + snapshotName;
      });
      this.snapshots.add(skSnapshot);
   }

   public final SKShopkeeperSnapshot removeSnapshot(int index) {
      SKShopkeeperSnapshot snapshot = (SKShopkeeperSnapshot)this.snapshots.remove(index);
      this.markDirty();
      return snapshot;
   }

   public final void removeAllSnapshots() {
      this.snapshots.clear();
      this.markDirty();
   }

   public final void applySnapshot(ShopkeeperSnapshot snapshot) throws ShopkeeperLoadException {
      Validate.notNull(snapshot, (String)"snapshot is null");
      Validate.isTrue(snapshot instanceof SKShopkeeperSnapshot, () -> {
         String var10000 = SKShopkeeperSnapshot.class.getName();
         return "snapshot is not of type " + var10000 + ", but " + snapshot.getClass().getName();
      });
      SKShopkeepersPlugin.getInstance().getUIRegistry().abortUISessions(this);

      try {
         this.loadDynamicState(((SKShopkeeperSnapshot)snapshot).getShopkeeperData());
      } catch (InvalidDataException var3) {
         throw new ShopkeeperLoadException(var3.getMessage(), var3);
      }

      this.markDirty();
   }

   public abstract boolean hasTradingRecipes(@Nullable Player var1);

   public abstract List<? extends TradingRecipe> getTradingRecipes(@Nullable Player var1);

   public final Collection<? extends UISession> getUISessions() {
      return ShopkeepersPlugin.getInstance().getUIRegistry().getUISessions((Shopkeeper)this);
   }

   public final Collection<? extends UISession> getUISessions(UIType uiType) {
      return ShopkeepersPlugin.getInstance().getUIRegistry().getUISessions(this, uiType);
   }

   public final void abortUISessionsDelayed() {
      ShopkeepersPlugin.getInstance().getUIRegistry().abortUISessionsDelayed(this);
   }

   public final void registerViewProvider(ViewProvider viewProvider) {
      Validate.notNull(viewProvider, (String)"viewProvider is null");
      this.viewProviders.put(viewProvider.getUIType().getIdentifier(), viewProvider);
   }

   public final void registerViewProviderIfMissing(UIType uiType, Supplier<ViewProvider> viewProviderSupplier) {
      Validate.notNull(viewProviderSupplier, (String)"viewProviderSupplier is null");
      if (this.getViewProvider(uiType) == null) {
         this.registerViewProvider((ViewProvider)viewProviderSupplier.get());
      }

   }

   @Nullable
   public final ViewProvider getViewProvider(UIType uiType) {
      Validate.notNull(uiType, (String)"uiType is null");
      return (ViewProvider)this.viewProviders.get(uiType.getIdentifier());
   }

   public final boolean openWindow(UIType uiType, Player player) {
      return this.openWindow(uiType, player, UIState.EMPTY);
   }

   public boolean openWindow(UIType uiType, Player player, UIState uiState) {
      Validate.notNull(uiType, (String)"uiType is null");
      Validate.notNull(player, (String)"player is null");
      String uiIdentifier = uiType.getIdentifier();
      if (!this.isValid()) {
         Log.debug(() -> {
            return this.getLogPrefix() + "Cannot open UI '" + uiIdentifier + "' for player " + player.getName() + ": Shopkeeper not valid.";
         });
         return false;
      } else {
         ViewProvider viewProvider = this.getViewProvider(uiType);
         if (viewProvider == null) {
            Log.debug(() -> {
               return this.getLogPrefix() + "Cannot open UI '" + uiIdentifier + "' for player " + player.getName() + ": This shopkeeper does not support this type of UI.";
            });
            return false;
         } else {
            return UISessionManager.getInstance().requestUI(viewProvider, player, uiState);
         }
      }
   }

   public final boolean openEditorWindow(Player player) {
      return this.openWindow(DefaultUITypes.EDITOR(), player);
   }

   public final boolean openTradingWindow(Player player) {
      return this.openWindow(DefaultUITypes.TRADING(), player);
   }

   public final boolean canEdit(CommandSender sender, boolean silent) {
      return this.canAccess(sender, DefaultUITypes.EDITOR(), silent);
   }

   public final boolean canAccess(CommandSender sender, UIType uiType, boolean silent) {
      ViewProvider viewProvider = this.getViewProvider(uiType);
      if (viewProvider == null) {
         return false;
      } else if (sender instanceof Player) {
         Player player = (Player)sender;
         return viewProvider.canAccess(player, silent);
      } else {
         return PermissionUtils.hasPermission(sender, "shopkeeper.bypass");
      }
   }

   public List<String> getInformation() {
      MessageArguments messageArguments = this.getMessageArguments("shop_");
      return StringUtils.replaceArguments((Collection)Messages.shopInformation, (MessageArguments)messageArguments);
   }

   public void onPlayerInteraction(Player player) {
      Validate.notNull(player, (String)"player is null");
      if (player.isSneaking()) {
         this.openEditorWindow(player);
      } else {
         this.openTradingWindow(player);
      }

   }

   public final int getTickingGroup() {
      return this.tickingGroup;
   }

   public final void informStartTicking() {
      this.ticking = true;
      this.onStartTicking();
   }

   protected void onStartTicking() {
      this.shopObject.onStartTicking();
   }

   public final void informStopTicking() {
      this.ticking = false;
      this.onStopTicking();
   }

   protected void onStopTicking() {
      this.shopObject.onStopTicking();
   }

   public final boolean isTicking() {
      return this.ticking;
   }

   public final void tick() {
      assert this.isTicking();

      this.onTickStart();
      if (this.isTicking()) {
         try {
            this.onTick();
         } finally {
            this.onTickEnd();
         }

      }
   }

   protected void onTickStart() {
      this.shopObject.onTickStart();
   }

   protected void onTick() {
      if (this.isTicking()) {
         this.shopObject.onTick();
      }
   }

   protected void onTickEnd() {
      this.shopObject.onTickEnd();
      if (Debug.isDebugging(DebugOptions.visualizeShopkeeperTicks)) {
         this.visualizeLastTick();
      }

   }

   protected void visualizeLastTick() {
      this.visualizeLastShopkeeperTick();
      this.shopObject.visualizeLastTick();
   }

   protected void visualizeLastShopkeeperTick() {
      Location particleLocation = this.shopObject.getTickVisualizationParticleLocation();
      if (particleLocation != null) {
         assert particleLocation.isWorldLoaded() && particleLocation.getWorld() != null;

         this.spawnTickVisualizationParticle(particleLocation);
      }
   }

   private void spawnTickVisualizationParticle(Location location) {
      assert location != null && location.isWorldLoaded() && location.getWorld() != null;

      World world = LocationUtils.getWorld(location);
      world.spawnParticle(Particle.DUST, location, 1, TICK_VISUALIZATION_DUSTS[this.tickingGroup]);
   }

   public String toString() {
      return "Shopkeeper " + this.getIdString();
   }

   public final int hashCode() {
      return System.identityHashCode(this);
   }

   public final boolean equals(@Nullable Object obj) {
      return this == obj;
   }

   static {
      float hueStep = 0.25F;

      for(int i = 0; i < 4; ++i) {
         float hue = (float)i * hueStep;
         int rgb = ColorUtils.HSBtoRGB(hue, 1.0F, 1.0F);
         Color color = Color.fromRGB(rgb);
         TICK_VISUALIZATION_DUSTS[i] = new DustOptions(color, 1.0F);
      }

      SHOP_TYPE_ID = (new BasicProperty()).name("shop-type-id").dataKeyAccessor("type", StringSerializers.STRICT).validator(StringValidators.NON_EMPTY).build();
      SHOP_TYPE = (new BasicProperty()).dataKeyAccessor("type", new DataSerializer<AbstractShopType<?>>() {
         @Nullable
         public Object serialize(AbstractShopType<?> value) {
            Validate.notNull(value, (String)"value is null");
            return value.getIdentifier();
         }

         public AbstractShopType<?> deserialize(Object data) throws InvalidDataException {
            String shopTypeId = (String)StringSerializers.STRICT_NON_EMPTY.deserialize(data);
            SKShopTypesRegistry shopTypeRegistry = SKShopkeepersPlugin.getInstance().getShopTypeRegistry();
            AbstractShopType<?> shopType = (AbstractShopType)shopTypeRegistry.get(shopTypeId);
            if (shopType == null) {
               throw new InvalidDataException("Unknown shop type: " + shopTypeId);
            } else {
               return shopType;
            }
         }
      }).build();
      ID = (new BasicProperty()).dataKeyAccessor("id", NumberSerializers.INTEGER).validator(IntegerValidators.POSITIVE).build();
      UNIQUE_ID = (new BasicProperty()).dataKeyAccessor("uniqueId", UUIDSerializers.LENIENT).build();
      WORLD_NAME = (new BasicProperty()).dataAccessor((new DataKeyAccessor("world", StringSerializers.SCALAR)).emptyDataPredicate(EmptyDataPredicates.EMPTY_STRING)).nullable().build();
      LOCATION_X = (new BasicProperty()).dataKeyAccessor("x", NumberSerializers.INTEGER).useDefaultIfMissing().defaultValue(0).build();
      LOCATION_Y = (new BasicProperty()).dataKeyAccessor("y", NumberSerializers.INTEGER).useDefaultIfMissing().defaultValue(0).build();
      LOCATION_Z = (new BasicProperty()).dataKeyAccessor("z", NumberSerializers.INTEGER).useDefaultIfMissing().defaultValue(0).build();
      YAW = (new BasicProperty()).dataKeyAccessor("yaw", NumberSerializers.FLOAT).useDefaultIfMissing().defaultValue(0.0F).build();
      LOCATION = (new BasicProperty()).name("location").dataAccessor(new DataAccessor<BlockLocation>() {
         public void save(DataContainer dataContainer, @Nullable BlockLocation value) {
            if (value != null) {
               dataContainer.set((DataSaver)AbstractShopkeeper.WORLD_NAME, value.getWorldName());
               dataContainer.set((DataSaver)AbstractShopkeeper.LOCATION_X, value.getX());
               dataContainer.set((DataSaver)AbstractShopkeeper.LOCATION_Y, value.getY());
               dataContainer.set((DataSaver)AbstractShopkeeper.LOCATION_Z, value.getZ());
            } else {
               dataContainer.set((DataSaver)AbstractShopkeeper.WORLD_NAME, (Object)null);
               dataContainer.set((DataSaver)AbstractShopkeeper.LOCATION_X, (Object)null);
               dataContainer.set((DataSaver)AbstractShopkeeper.LOCATION_Y, (Object)null);
               dataContainer.set((DataSaver)AbstractShopkeeper.LOCATION_Z, (Object)null);
            }

         }

         public BlockLocation load(DataContainer dataContainer) throws InvalidDataException {
            String worldName = (String)dataContainer.get((DataLoader)AbstractShopkeeper.WORLD_NAME);
            int x = (Integer)dataContainer.get((DataLoader)AbstractShopkeeper.LOCATION_X);
            int y = (Integer)dataContainer.get((DataLoader)AbstractShopkeeper.LOCATION_Y);
            int z = (Integer)dataContainer.get((DataLoader)AbstractShopkeeper.LOCATION_Z);
            return new BlockLocation(worldName, x, y, z);
         }
      }).build();
      OPEN = (new BasicProperty()).dataKeyAccessor("open", BooleanSerializers.STRICT).useDefaultIfMissing().defaultValue(true).build();
      NAME = (new BasicProperty()).dataKeyAccessor("name", ColoredStringSerializers.SCALAR).useDefaultIfMissing().defaultValue("").build();
      SHOP_OBJECT_DATA = (new BasicProperty()).dataKeyAccessor("object", new DataSerializer<ShopObjectData>() {
         @Nullable
         public Object serialize(ShopObjectData value) {
            return DataContainerSerializers.DEFAULT.serialize(value);
         }

         public ShopObjectData deserialize(Object data) throws InvalidDataException {
            DataContainer dataContainer = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);
            return ShopObjectData.ofNonNull(dataContainer);
         }
      }).build();
      SNAPSHOTS = (new BasicProperty()).dataKeyAccessor("snapshots", SKShopkeeperSnapshot.LIST_SERIALIZER).useDefaultIfMissing().defaultValue(Collections.emptyList()).build();
      ShopkeeperDataMigrator.registerMigration(new Migration("snapshots", MigrationPhase.DEFAULT) {
         public boolean migrate(ShopkeeperData shopkeeperData, String logPrefix) throws InvalidDataException {
            List<? extends SKShopkeeperSnapshot> snapshots = (List)shopkeeperData.get(AbstractShopkeeper.SNAPSHOTS);
            if (snapshots.isEmpty()) {
               return false;
            } else {
               int shopkeeperId = (Integer)shopkeeperData.get(AbstractShopkeeper.ID);
               String shopkeeperPrefix = AbstractShopkeeper.getLogPrefix(shopkeeperId);
               boolean migrated = false;
               int snapshotId = 1;

               for(Iterator var8 = snapshots.iterator(); var8.hasNext(); ++snapshotId) {
                  SKShopkeeperSnapshot snapshot = (SKShopkeeperSnapshot)var8.next();
                  String snapshotLogPrefix = AbstractShopkeeper.getLogPrefix(shopkeeperPrefix, snapshotId, snapshot);
                  migrated |= snapshot.getShopkeeperData().migrate(snapshotLogPrefix);
               }

               return migrated;
            }
         }
      });
   }
}
