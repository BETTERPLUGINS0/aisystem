package com.nisovin.shopkeepers.shopkeeper.player;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.ShopkeeperAddedEvent;
import com.nisovin.shopkeepers.api.events.ShopkeeperRemoveEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperCreateException;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.api.user.User;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.container.ShopContainers;
import com.nisovin.shopkeepers.container.protection.ProtectedContainers;
import com.nisovin.shopkeepers.currency.Currencies;
import com.nisovin.shopkeepers.currency.Currency;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.items.ItemUpdates;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.naming.ShopkeeperNaming;
import com.nisovin.shopkeepers.shopcreation.ShopCreationItem;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.SKTradingRecipe;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.shopkeeper.migration.Migration;
import com.nisovin.shopkeepers.shopkeeper.migration.MigrationPhase;
import com.nisovin.shopkeepers.shopkeeper.migration.ShopkeeperDataMigrator;
import com.nisovin.shopkeepers.user.SKUser;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.bukkit.BlockLocation;
import com.nisovin.shopkeepers.util.bukkit.LocationUtils;
import com.nisovin.shopkeepers.util.bukkit.MutableBlockLocation;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.validation.bukkit.ItemStackValidators;
import com.nisovin.shopkeepers.util.data.property.validation.java.StringValidators;
import com.nisovin.shopkeepers.util.data.serialization.DataAccessor;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.ItemStackSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.BooleanSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.NumberSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.StringSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.UUIDSerializers;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
import com.nisovin.shopkeepers.util.inventory.ItemMigration;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.CyclicCounter;
import com.nisovin.shopkeepers.util.java.RateLimiter;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import com.nisovin.shopkeepers.util.text.MessageArguments;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractPlayerShopkeeper extends AbstractShopkeeper implements PlayerShopkeeper {
   private static final int CHECK_CONTAINER_PERIOD_SECONDS = 5;
   private static final CyclicCounter nextCheckingOffset = new CyclicCounter(1, 6);
   private User owner;
   private BlockLocation container;
   private boolean notifyOnTrades;
   @Nullable
   private UnmodifiableItemStack hireCost;
   private final RateLimiter checkContainerLimiter;
   public static final Property<UUID> OWNER_UNIQUE_ID;
   public static final Property<String> OWNER_NAME;
   public static final Property<User> OWNER;
   public static final Property<Boolean> NOTIFY_ON_TRADES;
   public static final Property<UnmodifiableItemStack> HIRE_COST_ITEM;
   public static final Property<Integer> CONTAINER_X;
   public static final Property<Integer> CONTAINER_Y;
   public static final Property<Integer> CONTAINER_Z;
   public static final Property<BlockLocation> CONTAINER;

   protected AbstractPlayerShopkeeper() {
      this.owner = SKUser.EMPTY;
      this.container = BlockLocation.EMPTY;
      this.notifyOnTrades = (Boolean)NOTIFY_ON_TRADES.getDefaultValue();
      this.hireCost = null;
      this.checkContainerLimiter = new RateLimiter(5, nextCheckingOffset.getAndIncrement());
   }

   protected void loadFromCreationData(int id, ShopCreationData shopCreationData) throws ShopkeeperCreateException {
      super.loadFromCreationData(id, shopCreationData);
      PlayerShopCreationData playerShopCreationData = (PlayerShopCreationData)shopCreationData;
      Player owner = (Player)Unsafe.assertNonNull(playerShopCreationData.getCreator());
      Block container = playerShopCreationData.getShopContainer();
      this._setOwner(owner.getUniqueId(), (String)Unsafe.assertNonNull(owner.getName()));
      this._setContainer(container.getX(), container.getY(), container.getZ());
   }

   protected void setup() {
      this.registerViewProviderIfMissing(DefaultUITypes.HIRING(), () -> {
         return new PlayerShopHiringViewProvider(this);
      });
      super.setup();
   }

   public void loadDynamicState(ShopkeeperData shopkeeperData) throws InvalidDataException {
      super.loadDynamicState(shopkeeperData);
      this.loadOwner(shopkeeperData);
      this.loadContainer(shopkeeperData);
      this.loadNotifyOnTrades(shopkeeperData);
      this.loadForHire(shopkeeperData);
   }

   public void saveDynamicState(ShopkeeperData shopkeeperData, boolean saveAll) {
      super.saveDynamicState(shopkeeperData, saveAll);
      this.saveOwner(shopkeeperData);
      this.saveContainer(shopkeeperData);
      this.saveNotifyOnTrades(shopkeeperData);
      this.saveForHire(shopkeeperData);
   }

   protected int updateItems(String logPrefix, @ReadWrite ShopkeeperData shopkeeperData) {
      int updatedItems = super.updateItems(logPrefix, shopkeeperData);
      updatedItems += updateHireCost(logPrefix, shopkeeperData);
      return updatedItems;
   }

   private static int updateHireCost(String logPrefix, @ReadWrite ShopkeeperData shopkeeperData) {
      try {
         UnmodifiableItemStack hireCost = (UnmodifiableItemStack)shopkeeperData.get(HIRE_COST_ITEM);
         UnmodifiableItemStack updatedHireCost = ItemUpdates.updateItem(hireCost);
         if (updatedHireCost != hireCost) {
            assert !ItemUtils.isEmpty(updatedHireCost);

            shopkeeperData.set(HIRE_COST_ITEM, updatedHireCost);
            Log.debug(DebugOptions.itemUpdates, logPrefix + "Updated hire cost item.");
            return 1;
         }
      } catch (InvalidDataException var4) {
         Log.warning((String)(logPrefix + "Failed to load '" + HIRE_COST_ITEM.getName() + "'!"), (Throwable)var4);
      }

      return 0;
   }

   protected void onAdded(ShopkeeperAddedEvent.Cause cause) {
      super.onAdded(cause);
      this.protectContainer();
   }

   protected void onRemoval(ShopkeeperRemoveEvent.Cause cause) {
      super.onRemoval(cause);
      this.unprotectContainer();
   }

   public void delete(@Nullable Player player) {
      if (Settings.deletingPlayerShopReturnsCreationItem && player != null && this.isOwner(player)) {
         ItemStack shopCreationItem = ShopCreationItem.create();
         Map<Integer, ItemStack> remaining = player.getInventory().addItem(new ItemStack[]{shopCreationItem});
         if (!remaining.isEmpty()) {
            Location playerLocation = player.getEyeLocation();
            Location shopLocation = this.getShopObject().getLocation();
            Location dropLocation;
            if (shopLocation != null && LocationUtils.getDistanceSquared(shopLocation, playerLocation) <= 100.0D) {
               dropLocation = shopLocation;
            } else {
               dropLocation = playerLocation;
            }

            World world = (World)Unsafe.assertNonNull(dropLocation.getWorld());
            world.dropItem(dropLocation, shopCreationItem);
         }
      }

      super.delete(player);
   }

   protected void populateMessageArguments(Map<String, Supplier<?>> messageArguments) {
      super.populateMessageArguments(messageArguments);
      messageArguments.put("owner_name", this::getOwnerName);
      messageArguments.put("owner_uuid", this::getOwnerUUID);
   }

   protected void onShopkeeperMoved() {
      super.onShopkeeperMoved();
      if (!Objects.equals(this.getWorldName(), this.container.getWorldName())) {
         this._setContainer(this.container);
      }

   }

   public void onPlayerInteraction(Player player) {
      Validate.notNull(player, (String)"player is null");
      PlayerInventory playerInventory = player.getInventory();
      ItemStack itemInMainHand = playerInventory.getItemInMainHand();
      if (Settings.namingOfPlayerShopsViaItem && Settings.DerivedSettings.namingItemData.matches(itemInMainHand) && this.canEdit(player, false)) {
         String newName = ItemUtils.getDisplayNameOrEmpty(itemInMainHand);
         ShopkeeperNaming shopkeeperNaming = SKShopkeepersPlugin.getInstance().getShopkeeperNaming();
         if (shopkeeperNaming.requestNameChange(player, this, newName)) {
            Bukkit.getScheduler().runTask(ShopkeepersPlugin.getInstance(), () -> {
               ItemStack newItemInMainHand = ItemUtils.decreaseItemAmount(itemInMainHand, 1);
               playerInventory.setItemInMainHand(newItemInMainHand);
            });
         }

      } else {
         if (!player.isSneaking() && this.isForHire()) {
            this.openHireWindow(player);
         } else {
            super.onPlayerInteraction(player);
         }

      }
   }

   private void loadOwner(ShopkeeperData shopkeeperData) throws InvalidDataException {
      assert shopkeeperData != null;

      this._setOwner((User)shopkeeperData.get(OWNER));
   }

   private void saveOwner(ShopkeeperData shopkeeperData) {
      assert shopkeeperData != null;

      shopkeeperData.set(OWNER, this.owner);
   }

   public void setOwner(Player player) {
      this.setOwner(player.getUniqueId(), (String)Unsafe.assertNonNull(player.getName()));
   }

   public void setOwner(User owner) {
      this._setOwner(owner);
      this.markDirty();
   }

   public void setOwner(UUID ownerUUID, String ownerName) {
      this._setOwner(ownerUUID, ownerName);
      this.markDirty();
   }

   private void _setOwner(UUID ownerUUID, String ownerName) {
      this._setOwner(SKUser.of(ownerUUID, ownerName));
   }

   private void _setOwner(User owner) {
      Validate.notNull(owner, (String)"owner is null");
      this.owner = owner;
      this.getShopObject().onShopOwnerChanged();
   }

   public User getOwnerUser() {
      return this.owner;
   }

   public UUID getOwnerUUID() {
      return this.owner.getUniqueId();
   }

   public String getOwnerName() {
      return this.owner.getLastKnownName();
   }

   public String getOwnerString() {
      return TextUtils.getPlayerString(this.owner);
   }

   public boolean isOwner(Player player) {
      return player.getUniqueId().equals(this.getOwnerUUID());
   }

   @Nullable
   public Player getOwner() {
      return Bukkit.getPlayer(this.getOwnerUUID());
   }

   private void loadNotifyOnTrades(ShopkeeperData shopkeeperData) throws InvalidDataException {
      assert shopkeeperData != null;

      this._setNotifyOnTrades((Boolean)shopkeeperData.get(NOTIFY_ON_TRADES));
   }

   private void saveNotifyOnTrades(ShopkeeperData shopkeeperData) {
      assert shopkeeperData != null;

      shopkeeperData.set(NOTIFY_ON_TRADES, this.notifyOnTrades);
   }

   public boolean isNotifyOnTrades() {
      return this.notifyOnTrades;
   }

   public void setNotifyOnTrades(boolean notifyOnTrades) {
      if (this.notifyOnTrades != notifyOnTrades) {
         this._setNotifyOnTrades(notifyOnTrades);
         this.markDirty();
      }
   }

   private void _setNotifyOnTrades(boolean notifyOnTrades) {
      this.notifyOnTrades = notifyOnTrades;
   }

   private void loadForHire(ShopkeeperData shopkeeperData) throws InvalidDataException {
      assert shopkeeperData != null;

      this._setForHire((UnmodifiableItemStack)shopkeeperData.get(HIRE_COST_ITEM));
   }

   private void saveForHire(ShopkeeperData shopkeeperData) {
      assert shopkeeperData != null;

      shopkeeperData.set(HIRE_COST_ITEM, this.hireCost);
   }

   public boolean isForHire() {
      return this.hireCost != null;
   }

   public void setForHire(@Nullable @ReadOnly ItemStack hireCost) {
      this.setForHire(ItemUtils.unmodifiableClone(hireCost));
   }

   public void setForHire(@Nullable UnmodifiableItemStack hireCost) {
      this._setForHire(hireCost);
      this.markDirty();
   }

   private void _setForHire(@Nullable UnmodifiableItemStack hireCost) {
      boolean isForHire = this.isForHire();
      if (ItemUtils.isEmpty(hireCost)) {
         this.hireCost = null;
         if (isForHire) {
            this.setName("");
         }
      } else {
         this.hireCost = (UnmodifiableItemStack)Unsafe.assertNonNull(hireCost);
         this.setName(Messages.forHireTitle);
      }

   }

   @Nullable
   public UnmodifiableItemStack getHireCost() {
      return this.hireCost;
   }

   private void loadContainer(ShopkeeperData shopkeeperData) throws InvalidDataException {
      assert shopkeeperData != null;

      this._setContainer((BlockLocation)shopkeeperData.get(CONTAINER));
   }

   private void saveContainer(ShopkeeperData shopkeeperData) {
      assert shopkeeperData != null;

      shopkeeperData.set(CONTAINER, this.container);
   }

   private void protectContainer() {
      ProtectedContainers protectedContainers = SKShopkeepersPlugin.getInstance().getProtectedContainers();
      protectedContainers.addContainer(this.container, this);
   }

   private void unprotectContainer() {
      ProtectedContainers protectedContainers = SKShopkeepersPlugin.getInstance().getProtectedContainers();
      protectedContainers.removeContainer(this.container, this);
   }

   protected void _setContainer(int containerX, int containerY, int containerZ) {
      this._setContainer(new BlockLocation(this.getWorldName(), containerX, containerY, containerZ));
   }

   protected void _setContainer(BlockLocation container) {
      Validate.notNull(container, (String)"container is null");
      if (this.isValid()) {
         this.unprotectContainer();
      }

      BlockLocation newContainer = container;
      String shopkeeperWorldName = this.getWorldName();
      if (!Objects.equals(container.getWorldName(), shopkeeperWorldName)) {
         MutableBlockLocation containerCopy = container.mutableCopy();
         containerCopy.setWorldName(shopkeeperWorldName);
         newContainer = containerCopy;
      }

      this.container = ((BlockLocation)newContainer).immutable();
      if (this.isValid()) {
         this.protectContainer();
      }

   }

   public int getContainerX() {
      return this.container.getX();
   }

   public int getContainerY() {
      return this.container.getY();
   }

   public int getContainerZ() {
      return this.container.getZ();
   }

   public BlockLocation getContainerLocation() {
      return this.container;
   }

   public void setContainer(int containerX, int containerY, int containerZ) {
      this._setContainer(containerX, containerY, containerZ);
      this.markDirty();
   }

   @Nullable
   public Block getContainer() {
      return this.container.getBlock();
   }

   @Nullable
   public Inventory getContainerInventory() {
      Block container = this.getContainer();
      return container != null && ShopContainers.isSupportedContainer(container.getType()) ? ShopContainers.getInventory(container) : null;
   }

   public ItemStack[] getContainerContents() {
      Inventory containerInventory = this.getContainerInventory();
      return containerInventory == null ? InventoryUtils.emptyItemStackArray() : (ItemStack[])Unsafe.cast(containerInventory.getContents());
   }

   public int getCurrencyInContainer() {
      int totalCurrency = 0;
      ItemStack[] contents = this.getContainerContents();
      ItemStack[] var3 = contents;
      int var4 = contents.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ItemStack itemStack = var3[var5];
         if (itemStack != null) {
            Currency currency = Currencies.match(itemStack);
            if (currency != null) {
               totalCurrency += itemStack.getAmount() * currency.getValue();
            }
         }
      }

      return totalCurrency;
   }

   @Nullable
   protected final TradingRecipe createSellingRecipe(UnmodifiableItemStack itemBeingSold, int price, boolean outOfStock) {
      Validate.notNull(itemBeingSold, (String)"itemBeingSold is null");
      Validate.isTrue(price > 0, "price has to be positive");
      UnmodifiableItemStack item1 = null;
      UnmodifiableItemStack item2 = null;
      int remainingPrice = price;
      Currency baseCurrency;
      int maxStackSize;
      UnmodifiableItemStack currencyItem;
      if (Currencies.isHighCurrencyEnabled() && price > Settings.highCurrencyMinCost) {
         baseCurrency = Currencies.getHigh();
         maxStackSize = Math.min(price / baseCurrency.getValue(), baseCurrency.getMaxStackSize());
         if (maxStackSize > 0) {
            remainingPrice = price - maxStackSize * baseCurrency.getValue();
            currencyItem = baseCurrency.getItemData().createUnmodifiableItemStack(maxStackSize);
            item1 = currencyItem;
         }
      }

      if (remainingPrice > 0) {
         baseCurrency = Currencies.getBase();
         maxStackSize = baseCurrency.getMaxStackSize();
         if (remainingPrice > maxStackSize) {
            int maxPrice = getMaximumSellingPrice();
            Log.warning(this.getLogPrefix() + "Skipping offer with invalid price (" + price + "). Maximum price is " + maxPrice + ".");
            return null;
         }

         currencyItem = baseCurrency.getItemData().createUnmodifiableItemStack(remainingPrice);
         if (item1 == null) {
            item1 = currencyItem;
         } else {
            item2 = currencyItem;
         }
      }

      assert item1 != null;

      return new SKTradingRecipe(itemBeingSold, item1, item2, outOfStock);
   }

   @Nullable
   protected final TradingRecipe createBuyingRecipe(UnmodifiableItemStack itemBeingBought, int price, boolean outOfStock) {
      Currency currency = Currencies.getBase();
      int maxPrice = currency.getStackValue();
      if (price > maxPrice) {
         Log.warning(this.getLogPrefix() + "Skipping offer with invalid price (" + price + "). Maximum price is " + maxPrice + ".");
         return null;
      } else {
         UnmodifiableItemStack currencyItem = currency.getItemData().createUnmodifiableItemStack(price);
         return new SKTradingRecipe(currencyItem, itemBeingBought, (UnmodifiableItemStack)null, outOfStock);
      }
   }

   private static int getMaximumSellingPrice() {
      int maxPrice = 0;
      int currenciesCount = Currencies.getAll().size();
      Currency currency1 = (Currency)Currencies.getAll().get(currenciesCount - 1);
      int maxPrice = maxPrice + currency1.getStackValue();
      if (currenciesCount > 1) {
         Currency currency2 = (Currency)Currencies.getAll().get(currenciesCount - 2);
         maxPrice += currency2.getStackValue();
      }

      return maxPrice;
   }

   public boolean openHireWindow(Player player) {
      return this.openWindow(DefaultUITypes.HIRING(), player);
   }

   public boolean openContainerWindow(Player player) {
      Inventory containerInventory = this.getContainerInventory();
      if (containerInventory == null) {
         Log.debug(() -> {
            return "Cannot open container inventory for player '" + player.getName() + "': The block is no longer a valid container!";
         });
         return false;
      } else {
         Log.debug(() -> {
            return "Opening container inventory for player '" + player.getName() + "'.";
         });
         return player.openInventory(containerInventory) != null;
      }
   }

   public List<String> getInformation() {
      List<String> information = super.getInformation();
      MessageArguments messageArguments = this.getMessageArguments("shop_");
      information.addAll(StringUtils.replaceArguments((Collection)Messages.playerShopInformation, (MessageArguments)messageArguments));
      return information;
   }

   protected void onTick() {
      this.onTickCheckDeleteIfContainerBroken();
      super.onTick();
   }

   private void onTickCheckDeleteIfContainerBroken() {
      if (Settings.deleteShopkeeperOnBreakContainer) {
         if (this.checkContainerLimiter.request()) {
            Block containerBlock = this.getContainer();
            if (containerBlock != null && !ShopContainers.isSupportedContainer(containerBlock.getType())) {
               SKShopkeepersPlugin.getInstance().getRemoveShopOnContainerBreak().handleBlockBreakage(containerBlock);
            }

         }
      }
   }

   static {
      OWNER_UNIQUE_ID = (new BasicProperty()).dataKeyAccessor("owner uuid", UUIDSerializers.LENIENT).build();
      OWNER_NAME = (new BasicProperty()).dataKeyAccessor("owner", StringSerializers.SCALAR).validator(StringValidators.NON_EMPTY).build();
      OWNER = (new BasicProperty()).name("owner").dataAccessor(new DataAccessor<User>() {
         public void save(DataContainer dataContainer, @Nullable User value) {
            if (value != null) {
               dataContainer.set((DataSaver)AbstractPlayerShopkeeper.OWNER_UNIQUE_ID, value.getUniqueId());
               dataContainer.set((DataSaver)AbstractPlayerShopkeeper.OWNER_NAME, value.getLastKnownName());
            } else {
               dataContainer.set((DataSaver)AbstractPlayerShopkeeper.OWNER_UNIQUE_ID, (Object)null);
               dataContainer.set((DataSaver)AbstractPlayerShopkeeper.OWNER_NAME, (Object)null);
            }

         }

         public User load(DataContainer dataContainer) throws InvalidDataException {
            UUID ownerUniqueId = (UUID)dataContainer.get((DataLoader)AbstractPlayerShopkeeper.OWNER_UNIQUE_ID);
            String ownerName = (String)dataContainer.get((DataLoader)AbstractPlayerShopkeeper.OWNER_NAME);
            return SKUser.of(ownerUniqueId, ownerName);
         }
      }).build();
      NOTIFY_ON_TRADES = (new BasicProperty()).dataKeyAccessor("notifyOnTrades", BooleanSerializers.LENIENT).defaultValue(true).omitIfDefault().build();
      HIRE_COST_ITEM = (new BasicProperty()).dataKeyAccessor("hirecost", ItemStackSerializers.UNMODIFIABLE).validator(ItemStackValidators.Unmodifiable.NON_EMPTY).nullable().defaultValue((Object)null).build();
      ShopkeeperDataMigrator.registerMigration(new Migration("hire-cost-item", MigrationPhase.ofShopkeeperClass(AbstractPlayerShopkeeper.class)) {
         public boolean migrate(ShopkeeperData shopkeeperData, String logPrefix) throws InvalidDataException {
            UnmodifiableItemStack hireCost = (UnmodifiableItemStack)shopkeeperData.get(AbstractPlayerShopkeeper.HIRE_COST_ITEM);
            if (hireCost == null) {
               return false;
            } else {
               assert !ItemUtils.isEmpty(hireCost);

               boolean itemMigrated = false;
               UnmodifiableItemStack migratedHireCost = ItemMigration.migrateItemStack(hireCost);
               if (!ItemUtils.isSimilar(hireCost, migratedHireCost)) {
                  if (ItemUtils.isEmpty(migratedHireCost)) {
                     throw new InvalidDataException("Hire cost item migration failed: " + String.valueOf(hireCost));
                  }

                  hireCost = migratedHireCost;
                  itemMigrated = true;
               }

               if (!itemMigrated) {
                  return false;
               } else {
                  shopkeeperData.set(AbstractPlayerShopkeeper.HIRE_COST_ITEM, hireCost);
                  Log.debug(DebugOptions.itemMigrations, () -> {
                     return logPrefix + "Migrated hire cost item.";
                  });
                  return true;
               }
            }
         }
      });
      CONTAINER_X = (new BasicProperty()).dataKeyAccessor("chestx", NumberSerializers.INTEGER).build();
      CONTAINER_Y = (new BasicProperty()).dataKeyAccessor("chesty", NumberSerializers.INTEGER).build();
      CONTAINER_Z = (new BasicProperty()).dataKeyAccessor("chestz", NumberSerializers.INTEGER).build();
      CONTAINER = (new BasicProperty()).name("container").dataAccessor(new DataAccessor<BlockLocation>() {
         public void save(DataContainer dataContainer, @Nullable BlockLocation value) {
            if (value != null) {
               dataContainer.set((DataSaver)AbstractPlayerShopkeeper.CONTAINER_X, value.getX());
               dataContainer.set((DataSaver)AbstractPlayerShopkeeper.CONTAINER_Y, value.getY());
               dataContainer.set((DataSaver)AbstractPlayerShopkeeper.CONTAINER_Z, value.getZ());
            } else {
               dataContainer.set((DataSaver)AbstractPlayerShopkeeper.CONTAINER_X, (Object)null);
               dataContainer.set((DataSaver)AbstractPlayerShopkeeper.CONTAINER_Y, (Object)null);
               dataContainer.set((DataSaver)AbstractPlayerShopkeeper.CONTAINER_Z, (Object)null);
            }

         }

         public BlockLocation load(DataContainer dataContainer) throws InvalidDataException {
            int containerX = (Integer)dataContainer.get((DataLoader)AbstractPlayerShopkeeper.CONTAINER_X);
            int containerY = (Integer)dataContainer.get((DataLoader)AbstractPlayerShopkeeper.CONTAINER_Y);
            int containerZ = (Integer)dataContainer.get((DataLoader)AbstractPlayerShopkeeper.CONTAINER_Z);
            return new BlockLocation(containerX, containerY, containerZ);
         }
      }).build();
   }
}
