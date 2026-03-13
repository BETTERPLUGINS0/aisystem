package com.nisovin.shopkeepers.shopobjects.living;

import com.nisovin.shopkeepers.api.events.ShopkeeperEditedEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopEquipment;
import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopObject;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.items.ItemUpdates;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObject;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.ui.equipmentEditor.EquipmentEditorUI;
import com.nisovin.shopkeepers.ui.equipmentEditor.EquipmentEditorUIState;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.bukkit.EquipmentUtils;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.inventory.PotionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Steerable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKLivingShopObject<E extends LivingEntity> extends BaseEntityShopObject<E> implements LivingShopObject {
   public static final Property<LivingShopEquipment> EQUIPMENT;
   private final PropertyValue<LivingShopEquipment> equipmentProperty;

   protected SKLivingShopObject(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<?> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(EQUIPMENT);
      SKLivingShopObject var10002 = (SKLivingShopObject)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.equipmentProperty = var10001.onValueChanged(var10002::onEquipmentPropertyChanged).build(this.properties);
      Validate.isTrue(context instanceof LivingShopObjectCreationContext, "Expecting LivingShopObjectCreationContext!");
      Validate.isTrue(shopObjectType instanceof SKLivingShopObjectType, "Expecting SKLivingShopObjectType!");
      this.setEquipmentChangedListener();
   }

   protected LivingShopObjectCreationContext getContext() {
      return (LivingShopObjectCreationContext)this.context;
   }

   public SKLivingShopObjectType<?> getType() {
      return (SKLivingShopObjectType)super.getType();
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.equipmentProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.equipmentProperty.save(shopObjectData);
   }

   public int updateItems(String logPrefix, @ReadWrite ShopObjectData shopObjectData) {
      int updatedItems = super.updateItems(logPrefix, shopObjectData);
      updatedItems += updateEquipmentItems(logPrefix, shopObjectData);
      return updatedItems;
   }

   private static int updateEquipmentItems(String logPrefix, @ReadWrite ShopObjectData shopObjectData) {
      LivingShopEquipment shopEquipment;
      try {
         shopEquipment = (LivingShopEquipment)shopObjectData.get(EQUIPMENT);
      } catch (InvalidDataException var8) {
         Log.warning((String)(logPrefix + "Failed to load '" + EQUIPMENT.getName() + "'!"), (Throwable)var8);
         return 0;
      }

      int updatedItems = 0;
      Iterator var4 = EquipmentUtils.EQUIPMENT_SLOTS.iterator();

      while(var4.hasNext()) {
         EquipmentSlot slot = (EquipmentSlot)var4.next();
         UnmodifiableItemStack item = shopEquipment.getItem(slot);
         if (item != null) {
            UnmodifiableItemStack newItem = ItemUpdates.updateItem(item);
            if (newItem != item) {
               Log.debug(DebugOptions.itemUpdates, logPrefix + "Updated equipment item for slot " + slot.name());
               shopEquipment.setItem(slot, newItem);
               ++updatedItems;
            }
         }
      }

      if (updatedItems > 0) {
         shopObjectData.set(EQUIPMENT, shopEquipment);
      }

      return updatedItems;
   }

   protected void prepareEntity(@NonNull E entity) {
      super.prepareEntity(entity);
      EntityEquipment equipment = entity.getEquipment();
      if (equipment != null) {
         equipment.clear();
      }

      if (entity instanceof Steerable) {
         Steerable steerable = (Steerable)entity;
         steerable.setSaddle(false);
      }

   }

   protected void onSpawn() {
      super.onSpawn();
      E entity = (LivingEntity)Unsafe.assertNonNull((LivingEntity)this.getEntity());
      entity.setRemoveWhenFarAway(false);
      entity.setCanPickupItems(false);
      if (entity instanceof Ageable) {
         Ageable ageable = (Ageable)entity;
         ageable.setAdult();
      }

      if (entity instanceof Breedable) {
         Breedable breedable = (Breedable)entity;
         breedable.setBreed(false);
         breedable.setAgeLock(true);
      }

      if (entity instanceof Raider) {
         Raider raider = (Raider)entity;
         raider.setCanJoinRaid(false);
      }

   }

   protected void overwriteAI() {
      super.overwriteAI();
      E entity = (LivingEntity)Unsafe.assertNonNull((LivingEntity)this.getEntity());
      entity.setCollidable(false);
      Compat.getProvider().overwriteLivingEntityAI(entity);
      this.setNoAI(entity);
   }

   protected final void setNoAI(@NonNull E entity) {
      entity.setAI(false);
      Compat.getProvider().setOnGround(entity, true);
   }

   protected void checkActive() {
      super.checkActive();
      this.updatePotionEffects();
   }

   public void tickAI() {
      LivingEntity entity = (LivingEntity)this.getEntity();
      if (entity != null) {
         Compat.getProvider().tickAI(entity, Settings.entityBehaviorTickPeriod);
      }
   }

   protected Collection<? extends PotionEffect> getDefaultPotionEffects() {
      return Collections.emptySet();
   }

   private void updatePotionEffects() {
      E entity = (LivingEntity)this.getEntity();
      if (entity != null) {
         Collection<? extends PotionEffect> defaultPotionEffects = this.getDefaultPotionEffects();
         Collection<PotionEffect> activePotionEffects = (Collection)Unsafe.castNonNull(entity.getActivePotionEffects());
         defaultPotionEffects.forEach((effect) -> {
            PotionEffect activeEffect = PotionUtils.findIgnoreDuration(activePotionEffects, effect);
            if (activeEffect == null || activeEffect.getDuration() != -1 && activeEffect.getDuration() <= 200) {
               if (activeEffect != null) {
                  entity.removePotionEffect(effect.getType());
               }

               entity.addPotionEffect(effect);
            }
         });
         activePotionEffects.forEach((effect) -> {
            if (PotionUtils.findIgnoreDuration(defaultPotionEffects, effect) == null) {
               entity.removePotionEffect(effect.getType());
            }
         });
      }
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      if (!this.getEditableEquipmentSlots().isEmpty()) {
         editorButtons.add(this.getEquipmentEditorButton());
      }

      return editorButtons;
   }

   protected List<? extends EquipmentSlot> getEditableEquipmentSlots() {
      if (Settings.enableAllEquipmentEditorSlots) {
         return EquipmentUtils.EQUIPMENT_SLOTS;
      } else {
         String var1 = this.getEntityType().name();
         byte var2 = -1;
         switch(var1.hashCode()) {
         case -1940664743:
            if (var1.equals("VINDICATOR")) {
               var2 = 13;
            }
            break;
         case -1781303918:
            if (var1.equals("ENDERMAN")) {
               var2 = 14;
            }
            break;
         case -1670492626:
            if (var1.equals("CAMEL_HUSK")) {
               var2 = 10;
            }
            break;
         case -1428246125:
            if (var1.equals("NAUTILUS")) {
               var2 = 2;
            }
            break;
         case -1288904373:
            if (var1.equals("SKELETON_HORSE")) {
               var2 = 12;
            }
            break;
         case -1163786087:
            if (var1.equals("STRIDER")) {
               var2 = 1;
            }
            break;
         case -499196828:
            if (var1.equals("ZOMBIE_HORSE")) {
               var2 = 11;
            }
            break;
         case 79214:
            if (var1.equals("PIG")) {
               var2 = 0;
            }
            break;
         case 2378017:
            if (var1.equals("MULE")) {
               var2 = 7;
            }
            break;
         case 17859468:
            if (var1.equals("ZOMBIE_NAUTILUS")) {
               var2 = 3;
            }
            break;
         case 63888534:
            if (var1.equals("CAMEL")) {
               var2 = 9;
            }
            break;
         case 68928445:
            if (var1.equals("HORSE")) {
               var2 = 6;
            }
            break;
         case 72516629:
            if (var1.equals("LLAMA")) {
               var2 = 4;
            }
            break;
         case 943567908:
            if (var1.equals("TRADER_LLAMA")) {
               var2 = 5;
            }
            break;
         case 2022138428:
            if (var1.equals("DONKEY")) {
               var2 = 8;
            }
         }

         switch(var2) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
            return Collections.emptyList();
         case 13:
            return EquipmentUtils.EQUIPMENT_SLOTS_HEAD;
         case 14:
            return EquipmentUtils.EQUIPMENT_SLOTS_MAINHAND;
         default:
            return EquipmentUtils.getSupportedEquipmentSlots(this.getEntityType());
         }
      }
   }

   public LivingShopEquipment getEquipment() {
      return (LivingShopEquipment)this.equipmentProperty.getValue();
   }

   private void onEquipmentPropertyChanged() {
      this.setEquipmentChangedListener();
      this.onEquipmentChanged();
   }

   private void setEquipmentChangedListener() {
      ((SKLivingShopEquipment)this.getEquipment()).setChangedListener(this::handleEquipmentChanged);
   }

   private void handleEquipmentChanged() {
      this.shopkeeper.markDirty();
      this.onEquipmentChanged();
   }

   protected void onEquipmentChanged() {
      this.applyEquipment();
   }

   private void applyEquipment() {
      E entity = (LivingEntity)this.getEntity();
      if (entity != null) {
         EntityEquipment entityEquipment = entity.getEquipment();
         if (entityEquipment != null) {
            LivingShopEquipment shopEquipment = this.getEquipment();
            Iterator var4 = EquipmentUtils.EQUIPMENT_SLOTS.iterator();

            while(var4.hasNext()) {
               EquipmentSlot slot = (EquipmentSlot)var4.next();
               ItemStack item = ItemUtils.asItemStackOrNull(shopEquipment.getItem(slot));
               this.setEquipment(entityEquipment, slot, item);
            }

         }
      }
   }

   protected void setEquipment(EntityEquipment entityEquipment, EquipmentSlot slot, @ReadOnly ItemStack item) {
      assert entityEquipment != null && slot != null;

      ItemStack itemToSet = item;
      EntityType entityType = this.getEntityType();
      if (slot == EquipmentSlot.HEAD && entityType != EntityType.PHANTOM && EntityUtils.burnsInSunlight(entityType)) {
         if (ItemUtils.isEmpty(item)) {
            itemToSet = new ItemStack(Material.STONE_BUTTON);
         } else {
            assert item != null;

            itemToSet = ItemUtils.setUnbreakable(item.clone());
         }
      }

      entityEquipment.setItem(slot, itemToSet);
   }

   private ItemStack getEquipmentEditorItem() {
      return ItemUtils.setDisplayNameAndLore(new ItemStack(Material.ARMOR_STAND), Messages.buttonEquipment, Messages.buttonEquipmentLore);
   }

   private Button getEquipmentEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return SKLivingShopObject.this.getEquipmentEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            editorView.closeDelayedAndRunTask(() -> {
               SKLivingShopObject.this.openEquipmentEditor(editorView.getPlayer(), false);
            });
            return true;
         }

         protected void onActionSuccess(EditorView editorView, InventoryClickEvent clickEvent) {
         }
      };
   }

   public boolean openEquipmentEditor(Player player, boolean editAllSlots) {
      EquipmentEditorUIState config = new EquipmentEditorUIState(editAllSlots ? EquipmentUtils.EQUIPMENT_SLOTS : this.getEditableEquipmentSlots(), this.getEquipment().getItems(), (equipmentSlot, item) -> {
         this.getEquipment().setItem(equipmentSlot, item);
         Shopkeeper shopkeeper = this.getShopkeeper();
         Bukkit.getPluginManager().callEvent(new ShopkeeperEditedEvent(shopkeeper, player));
         shopkeeper.save();
      });
      return EquipmentEditorUI.request(this.shopkeeper, player, config);
   }

   static {
      EQUIPMENT = (new BasicProperty()).dataKeyAccessor("equipment", SKLivingShopEquipment.SERIALIZER).defaultValueSupplier(SKLivingShopEquipment::new).omitIfDefault().build();
   }
}
