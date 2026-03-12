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
import com.nisovin.shopkeepers.util.data.serialization.java.EnumSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import java.util.List;
import java.util.Objects;
import org.bukkit.DyeColor;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Parrot.Variant;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ParrotShop extends SittableShop<Parrot> {
   public static final Property<Variant> VARIANT;
   private final PropertyValue<Variant> variantProperty;

   public ParrotShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<ParrotShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(VARIANT);
      ParrotShop var10002 = (ParrotShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.variantProperty = var10001.onValueChanged(var10002::applyVariant).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.variantProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.variantProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyVariant();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getVariantEditorButton());
      return editorButtons;
   }

   public Variant getVariant() {
      return (Variant)this.variantProperty.getValue();
   }

   public void setVariant(Variant variant) {
      this.variantProperty.setValue(variant);
   }

   public void cycleVariant(boolean backwards) {
      this.setVariant((Variant)EnumUtils.cycleEnumConstant(Variant.class, this.getVariant(), backwards));
   }

   private void applyVariant() {
      Parrot entity = (Parrot)this.getEntity();
      if (entity != null) {
         entity.setVariant(this.getVariant());
      }
   }

   private ItemStack getVariantEditorItem() {
      ItemStack iconItem;
      switch(this.getVariant()) {
      case BLUE:
         iconItem = new ItemStack(ItemUtils.getWoolType(DyeColor.BLUE));
         break;
      case CYAN:
         iconItem = new ItemStack(ItemUtils.getWoolType(DyeColor.LIGHT_BLUE));
         break;
      case GRAY:
         iconItem = new ItemStack(ItemUtils.getWoolType(DyeColor.LIGHT_GRAY));
         break;
      case GREEN:
         iconItem = new ItemStack(ItemUtils.getWoolType(DyeColor.LIME));
         break;
      case RED:
      default:
         iconItem = new ItemStack(ItemUtils.getWoolType(DyeColor.RED));
      }

      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonParrotVariant, Messages.buttonParrotVariantLore);
      return iconItem;
   }

   private Button getVariantEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return ParrotShop.this.getVariantEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            ParrotShop.this.cycleVariant(backwards);
            return true;
         }
      };
   }

   static {
      VARIANT = (new BasicProperty()).dataKeyAccessor("parrotVariant", EnumSerializers.lenient(Variant.class)).defaultValue(Variant.RED).build();
   }
}
