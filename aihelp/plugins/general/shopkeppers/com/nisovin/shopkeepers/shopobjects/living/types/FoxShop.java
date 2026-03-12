package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.BooleanSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.EnumSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import java.util.List;
import java.util.Objects;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Fox.Type;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class FoxShop extends SittableShop<Fox> {
   public static final Property<Type> FOX_TYPE;
   public static final Property<Boolean> SLEEPING;
   public static final Property<Boolean> CROUCHING;
   private final PropertyValue<Type> foxTypeProperty;
   private final PropertyValue<Boolean> sleepingProperty;
   private final PropertyValue<Boolean> crouchingProperty;

   public FoxShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<FoxShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(FOX_TYPE);
      FoxShop var10002 = (FoxShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.foxTypeProperty = var10001.onValueChanged(var10002::applyFoxType).build(this.properties);
      var10001 = new PropertyValue(SLEEPING);
      var10002 = (FoxShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.sleepingProperty = var10001.onValueChanged(var10002::applySleeping).build(this.properties);
      var10001 = new PropertyValue(CROUCHING);
      var10002 = (FoxShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.crouchingProperty = var10001.onValueChanged(var10002::applyCrouching).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.foxTypeProperty.load(shopObjectData);
      this.sleepingProperty.load(shopObjectData);
      this.crouchingProperty.load(shopObjectData);
      if (this.isSleeping() && this.isCrouching()) {
         this.setCrouching(false);
      }

   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.foxTypeProperty.save(shopObjectData);
      this.sleepingProperty.save(shopObjectData);
      this.crouchingProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyFoxType();
      this.applySleeping();
      this.applyCrouching();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getFoxTypeEditorButton());
      editorButtons.add(this.getSleepingEditorButton());
      editorButtons.add(this.getCrouchingEditorButton());
      return editorButtons;
   }

   public Type getFoxType() {
      return (Type)this.foxTypeProperty.getValue();
   }

   public void setFoxType(Type foxType) {
      this.foxTypeProperty.setValue(foxType);
   }

   public void cycleFoxType(boolean backwards) {
      this.setFoxType((Type)EnumUtils.cycleEnumConstant(Type.class, this.getFoxType(), backwards));
   }

   private void applyFoxType() {
      Fox entity = (Fox)this.getEntity();
      if (entity != null) {
         entity.setFoxType(this.getFoxType());
      }
   }

   private ItemStack getFoxTypeEditorItem() {
      ItemStack iconItem = new ItemStack(Material.LEATHER_CHESTPLATE);
      switch(this.getFoxType()) {
      case SNOW:
         ItemUtils.setLeatherColor(iconItem, Color.WHITE);
         break;
      case RED:
      default:
         ItemUtils.setLeatherColor(iconItem, Color.ORANGE);
      }

      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonFoxVariant, Messages.buttonFoxVariantLore);
      return iconItem;
   }

   private Button getFoxTypeEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return FoxShop.this.getFoxTypeEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            FoxShop.this.cycleFoxType(backwards);
            return true;
         }
      };
   }

   public boolean isSleeping() {
      return (Boolean)this.sleepingProperty.getValue();
   }

   public void setSleeping(boolean sleeping) {
      this.sleepingProperty.setValue(sleeping);
      if (sleeping && this.isCrouching()) {
         this.setCrouching(false);
      }

   }

   public void cycleSleeping() {
      this.setSleeping(!this.isSleeping());
   }

   private void applySleeping() {
      Fox entity = (Fox)this.getEntity();
      if (entity != null) {
         entity.setSleeping(this.isSleeping());
      }
   }

   private ItemStack getSleepingEditorItem() {
      Material iconItemType = this.isSleeping() ? Material.GREEN_BED : Material.RED_BED;
      ItemStack iconItem = new ItemStack(iconItemType);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonFoxSleeping, Messages.buttonFoxSleepingLore);
      return iconItem;
   }

   private Button getSleepingEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return FoxShop.this.getSleepingEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            FoxShop.this.cycleSleeping();
            this.updateAllIcons(editorView);
            return true;
         }
      };
   }

   public boolean isCrouching() {
      return (Boolean)this.crouchingProperty.getValue();
   }

   public void setCrouching(boolean crouching) {
      this.crouchingProperty.setValue(crouching);
      if (crouching && this.isSleeping()) {
         this.setSleeping(false);
      }

   }

   public void cycleCrouching() {
      this.setCrouching(!this.isCrouching());
   }

   private void applyCrouching() {
      Fox entity = (Fox)this.getEntity();
      if (entity != null) {
         entity.setCrouching(this.isCrouching());
      }
   }

   private ItemStack getCrouchingEditorItem() {
      Material iconItemType = this.isCrouching() ? Material.GREEN_CARPET : Material.RED_CARPET;
      ItemStack iconItem = new ItemStack(iconItemType);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonFoxCrouching, Messages.buttonFoxCrouchingLore);
      return iconItem;
   }

   private Button getCrouchingEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return FoxShop.this.getCrouchingEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            FoxShop.this.cycleCrouching();
            this.updateAllIcons(editorView);
            return true;
         }
      };
   }

   static {
      FOX_TYPE = (new BasicProperty()).dataKeyAccessor("foxType", EnumSerializers.lenient(Type.class)).defaultValue(Type.RED).build();
      SLEEPING = (new BasicProperty()).dataKeyAccessor("sleeping", BooleanSerializers.LENIENT).defaultValue(false).build();
      CROUCHING = (new BasicProperty()).dataKeyAccessor("crouching", BooleanSerializers.LENIENT).defaultValue(false).build();
   }
}
