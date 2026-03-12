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
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Sheep;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SheepShop extends BabyableShop<Sheep> {
   public static final Property<DyeColor> COLOR;
   public static final Property<Boolean> SHEARED;
   private final PropertyValue<DyeColor> colorProperty;
   private final PropertyValue<Boolean> shearedProperty;

   public SheepShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<SheepShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(COLOR);
      SheepShop var10002 = (SheepShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.colorProperty = var10001.onValueChanged(var10002::applyColor).build(this.properties);
      var10001 = new PropertyValue(SHEARED);
      var10002 = (SheepShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.shearedProperty = var10001.onValueChanged(var10002::applySheared).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.colorProperty.load(shopObjectData);
      this.shearedProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.colorProperty.save(shopObjectData);
      this.shearedProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyColor();
      this.applySheared();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getColorEditorButton());
      editorButtons.add(this.getShearedEditorButton());
      return editorButtons;
   }

   public DyeColor getColor() {
      return (DyeColor)this.colorProperty.getValue();
   }

   public void setColor(DyeColor color) {
      this.colorProperty.setValue(color);
   }

   public void cycleColor(boolean backwards) {
      this.setColor((DyeColor)EnumUtils.cycleEnumConstant(DyeColor.class, this.getColor(), backwards));
   }

   private void applyColor() {
      Sheep entity = (Sheep)this.getEntity();
      if (entity != null) {
         entity.setColor(this.getColor());
      }
   }

   private ItemStack getColorEditorItem() {
      ItemStack iconItem = new ItemStack(ItemUtils.getWoolType(this.getColor()));
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonSheepColor, Messages.buttonSheepColorLore);
      return iconItem;
   }

   private Button getColorEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return SheepShop.this.getColorEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            SheepShop.this.cycleColor(backwards);
            return true;
         }
      };
   }

   public boolean isSheared() {
      return (Boolean)this.shearedProperty.getValue();
   }

   public void setSheared(boolean sheared) {
      this.shearedProperty.setValue(sheared);
   }

   public void cycleSheared() {
      this.setSheared(!this.isSheared());
   }

   private void applySheared() {
      Sheep entity = (Sheep)this.getEntity();
      if (entity != null) {
         entity.setSheared(this.isSheared());
      }
   }

   private ItemStack getShearedEditorItem() {
      ItemStack iconItem = new ItemStack(Material.SHEARS);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonSheepSheared, Messages.buttonSheepShearedLore);
      return iconItem;
   }

   private Button getShearedEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return SheepShop.this.getShearedEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            SheepShop.this.cycleSheared();
            return true;
         }
      };
   }

   static {
      COLOR = (new BasicProperty()).dataKeyAccessor("color", EnumSerializers.lenient(DyeColor.class)).defaultValue(DyeColor.WHITE).build();
      SHEARED = (new BasicProperty()).dataKeyAccessor("sheared", BooleanSerializers.LENIENT).defaultValue(false).build();
   }
}
