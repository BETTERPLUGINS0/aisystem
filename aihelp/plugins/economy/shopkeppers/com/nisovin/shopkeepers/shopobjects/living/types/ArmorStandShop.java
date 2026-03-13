package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.shopobjects.living.SKLivingShopObject;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.BooleanSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ArmorStandShop extends SKLivingShopObject<ArmorStand> {
   public static final Property<Boolean> BASE_PLATE;
   public static final Property<Boolean> SHOW_ARMS;
   public static final Property<Boolean> SMALL;
   private final PropertyValue<Boolean> basePlateProperty;
   private final PropertyValue<Boolean> showArmsProperty;
   private final PropertyValue<Boolean> smallProperty;

   public ArmorStandShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<ArmorStandShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(BASE_PLATE);
      ArmorStandShop var10002 = (ArmorStandShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.basePlateProperty = var10001.onValueChanged(var10002::applyBasePlate).build(this.properties);
      var10001 = new PropertyValue(SHOW_ARMS);
      var10002 = (ArmorStandShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.showArmsProperty = var10001.onValueChanged(var10002::applyShowArms).build(this.properties);
      var10001 = new PropertyValue(SMALL);
      var10002 = (ArmorStandShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.smallProperty = var10001.onValueChanged(var10002::applySmall).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.basePlateProperty.load(shopObjectData);
      this.showArmsProperty.load(shopObjectData);
      this.smallProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.basePlateProperty.save(shopObjectData);
      this.showArmsProperty.save(shopObjectData);
      this.smallProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyBasePlate();
      this.applyShowArms();
      this.applySmall();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getBasePlateEditorButton());
      editorButtons.add(this.getShowArmsEditorButton());
      editorButtons.add(this.getSmallEditorButton());
      return editorButtons;
   }

   public boolean hasBasePlate() {
      return (Boolean)this.basePlateProperty.getValue();
   }

   public void setBasePlate(boolean basePlate) {
      this.basePlateProperty.setValue(basePlate);
   }

   public void cycleBasePlate(boolean backwards) {
      this.setBasePlate(!this.hasBasePlate());
   }

   private void applyBasePlate() {
      ArmorStand entity = (ArmorStand)this.getEntity();
      if (entity != null) {
         entity.setBasePlate(this.hasBasePlate());
      }
   }

   private ItemStack getBasePlateEditorItem() {
      ItemStack iconItem = new ItemStack(this.hasBasePlate() ? Material.HEAVY_WEIGHTED_PRESSURE_PLATE : Material.STONE_PRESSURE_PLATE);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonArmorStandBasePlate, Messages.buttonArmorStandBasePlateLore);
      return iconItem;
   }

   private Button getBasePlateEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return ArmorStandShop.this.getBasePlateEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            ArmorStandShop.this.cycleBasePlate(backwards);
            return true;
         }
      };
   }

   public boolean isShowArms() {
      return (Boolean)this.showArmsProperty.getValue();
   }

   public void setShowArms(boolean showArms) {
      this.showArmsProperty.setValue(showArms);
   }

   public void cycleShowArms(boolean backwards) {
      this.setShowArms(!this.isShowArms());
   }

   private void applyShowArms() {
      ArmorStand entity = (ArmorStand)this.getEntity();
      if (entity != null) {
         entity.setArms(this.isShowArms());
      }
   }

   private ItemStack getShowArmsEditorItem() {
      ItemStack iconItem = new ItemStack(this.isShowArms() ? Material.WOODEN_SWORD : Material.STICK);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonArmorStandShowArms, Messages.buttonArmorStandShowArmsLore);
      return iconItem;
   }

   private Button getShowArmsEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return ArmorStandShop.this.getShowArmsEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            ArmorStandShop.this.cycleShowArms(backwards);
            return true;
         }
      };
   }

   public boolean isSmall() {
      return (Boolean)this.smallProperty.getValue();
   }

   public void setSmall(boolean small) {
      this.smallProperty.setValue(small);
   }

   public void cycleSmall(boolean backwards) {
      this.setSmall(!this.isSmall());
   }

   private void applySmall() {
      ArmorStand entity = (ArmorStand)this.getEntity();
      if (entity != null) {
         entity.setSmall(this.isSmall());
      }
   }

   private ItemStack getSmallEditorItem() {
      ItemStack iconItem = new ItemStack(this.isSmall() ? Material.OAK_SLAB : Material.OAK_PLANKS);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonArmorStandSmall, Messages.buttonArmorStandSmallLore);
      return iconItem;
   }

   private Button getSmallEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return ArmorStandShop.this.getSmallEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            ArmorStandShop.this.cycleSmall(backwards);
            return true;
         }
      };
   }

   static {
      BASE_PLATE = (new BasicProperty()).dataKeyAccessor("basePlate", BooleanSerializers.STRICT).useDefaultIfMissing().defaultValue(true).build();
      SHOW_ARMS = (new BasicProperty()).dataKeyAccessor("showArms", BooleanSerializers.STRICT).useDefaultIfMissing().defaultValue(false).build();
      SMALL = (new BasicProperty()).dataKeyAccessor("small", BooleanSerializers.STRICT).useDefaultIfMissing().defaultValue(false).build();
   }
}
