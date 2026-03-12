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
import com.nisovin.shopkeepers.util.bukkit.RegistryUtils;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.KeyedSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.BooleanSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.EnumSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import java.util.List;
import java.util.Objects;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Wolf.Variant;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class WolfShop extends SittableShop<Wolf> {
   public static final Property<Boolean> ANGRY;
   public static final Property<DyeColor> COLLAR_COLOR;
   public static final Property<Variant> VARIANT;
   private final PropertyValue<Boolean> angryProperty;
   private final PropertyValue<DyeColor> collarColorProperty;
   private final PropertyValue<Variant> variantProperty;

   public WolfShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<WolfShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(ANGRY);
      WolfShop var10002 = (WolfShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.angryProperty = var10001.onValueChanged(var10002::applyAngry).build(this.properties);
      var10001 = new PropertyValue(COLLAR_COLOR);
      var10002 = (WolfShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.collarColorProperty = var10001.onValueChanged(var10002::applyCollarColor).build(this.properties);
      var10001 = new PropertyValue(VARIANT);
      var10002 = (WolfShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.variantProperty = var10001.onValueChanged(var10002::applyVariant).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.angryProperty.load(shopObjectData);
      this.collarColorProperty.load(shopObjectData);
      this.variantProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.angryProperty.save(shopObjectData);
      this.collarColorProperty.save(shopObjectData);
      this.variantProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyAngry();
      this.applyCollarColor();
      this.applyVariant();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getAngryEditorButton());
      editorButtons.add(this.getCollarColorEditorButton());
      editorButtons.add(this.getVariantEditorButton());
      return editorButtons;
   }

   public void onTick() {
      super.onTick();
      this.applyAngry();
   }

   public boolean isAngry() {
      return (Boolean)this.angryProperty.getValue();
   }

   public void setAngry(boolean angry) {
      this.angryProperty.setValue(angry);
   }

   public void cycleAngry() {
      this.setAngry(!this.isAngry());
   }

   private void applyAngry() {
      Wolf entity = (Wolf)this.getEntity();
      if (entity != null) {
         entity.setAngry(this.isAngry());
      }
   }

   private ItemStack getAngryEditorItem() {
      Material iconItemType = this.isAngry() ? Material.RED_WOOL : Material.WHITE_WOOL;
      ItemStack iconItem = new ItemStack(iconItemType);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonWolfAngry, Messages.buttonWolfAngryLore);
      return iconItem;
   }

   private Button getAngryEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return WolfShop.this.getAngryEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            WolfShop.this.cycleAngry();
            return true;
         }
      };
   }

   @Nullable
   public DyeColor getCollarColor() {
      return (DyeColor)this.collarColorProperty.getValue();
   }

   public void setCollarColor(@Nullable DyeColor collarColor) {
      this.collarColorProperty.setValue(collarColor);
   }

   public void cycleCollarColor(boolean backwards) {
      this.setCollarColor((DyeColor)EnumUtils.cycleEnumConstantNullable(DyeColor.class, this.getCollarColor(), backwards));
   }

   private void applyCollarColor() {
      Wolf entity = (Wolf)this.getEntity();
      if (entity != null) {
         DyeColor collarColor = this.getCollarColor();
         if (collarColor == null) {
            entity.setTamed(false);
         } else {
            entity.setTamed(true);
            entity.setCollarColor(collarColor);
         }

      }
   }

   private ItemStack getCollarColorEditorItem() {
      DyeColor collarColor = this.getCollarColor();
      ItemStack iconItem;
      if (collarColor == null) {
         iconItem = new ItemStack(Material.BARRIER);
      } else {
         iconItem = new ItemStack(ItemUtils.getWoolType(collarColor));
      }

      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonCollarColor, Messages.buttonCollarColorLore);
      return iconItem;
   }

   private Button getCollarColorEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return WolfShop.this.getCollarColorEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            WolfShop.this.cycleCollarColor(backwards);
            return true;
         }
      };
   }

   public Variant getVariant() {
      return (Variant)this.variantProperty.getValue();
   }

   public void setVariant(Variant variant) {
      this.variantProperty.setValue(variant);
   }

   public void cycleVariant(boolean backwards) {
      this.setVariant((Variant)RegistryUtils.cycleKeyed(Variant.class, this.getVariant(), backwards));
   }

   private void applyVariant() {
      Wolf entity = (Wolf)this.getEntity();
      if (entity != null) {
         entity.setVariant(this.getVariant());
      }
   }

   private ItemStack getVariantEditorItem() {
      ItemStack iconItem = new ItemStack(Material.LEATHER_CHESTPLATE);
      String var2 = this.getVariant().toString();
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -1713530855:
         if (var2.equals("minecraft:chestnut")) {
            var3 = 6;
         }
         break;
      case -1150609006:
         if (var2.equals("minecraft:ashen")) {
            var3 = 3;
         }
         break;
      case -1149900814:
         if (var2.equals("minecraft:black")) {
            var3 = 2;
         }
         break;
      case -1134838520:
         if (var2.equals("minecraft:rusty")) {
            var3 = 4;
         }
         break;
      case -1134127287:
         if (var2.equals("minecraft:snowy")) {
            var3 = 1;
         }
         break;
      case -1130404007:
         if (var2.equals("minecraft:woods")) {
            var3 = 5;
         }
         break;
      case -1006518057:
         if (var2.equals("minecraft:pale")) {
            var3 = 8;
         }
         break;
      case 1082537732:
         if (var2.equals("minecraft:spotted")) {
            var3 = 0;
         }
         break;
      case 1199493354:
         if (var2.equals("minecraft:striped")) {
            var3 = 7;
         }
      }

      switch(var3) {
      case 0:
         ItemUtils.setLeatherColor(iconItem, Color.ORANGE);
         break;
      case 1:
         ItemUtils.setLeatherColor(iconItem, Color.WHITE);
         break;
      case 2:
         ItemUtils.setLeatherColor(iconItem, DyeColor.BLACK.getColor());
         break;
      case 3:
         ItemUtils.setLeatherColor(iconItem, Color.fromRGB(140, 144, 167));
      case 4:
         break;
      case 5:
         ItemUtils.setLeatherColor(iconItem, Color.fromRGB(88, 68, 34));
         break;
      case 6:
         ItemUtils.setLeatherColor(iconItem, Color.fromRGB(159, 119, 115));
         break;
      case 7:
         ItemUtils.setLeatherColor(iconItem, Color.fromRGB(202, 182, 114));
         break;
      case 8:
      default:
         ItemUtils.setLeatherColor(iconItem, Color.fromRGB(220, 220, 220));
      }

      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonWolfVariant, Messages.buttonWolfVariantLore);
      return iconItem;
   }

   private Button getVariantEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return WolfShop.this.getVariantEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            WolfShop.this.cycleVariant(backwards);
            return true;
         }
      };
   }

   static {
      ANGRY = (new BasicProperty()).dataKeyAccessor("angry", BooleanSerializers.LENIENT).defaultValue(false).build();
      COLLAR_COLOR = (new BasicProperty()).dataKeyAccessor("collarColor", EnumSerializers.lenient(DyeColor.class)).nullable().defaultValue((Object)null).build();
      VARIANT = (new BasicProperty()).dataKeyAccessor("wolfVariant", KeyedSerializers.forRegistry(Variant.class)).defaultValue(Variant.PALE).build();
   }
}
