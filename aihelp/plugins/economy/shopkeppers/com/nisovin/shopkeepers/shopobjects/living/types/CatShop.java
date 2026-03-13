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
import com.nisovin.shopkeepers.util.data.serialization.java.EnumSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Cat.Type;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CatShop extends SittableShop<Cat> {
   public static final Property<Type> CAT_TYPE;
   public static final Property<DyeColor> COLLAR_COLOR;
   private final PropertyValue<Type> catTypeProperty;
   private final PropertyValue<DyeColor> collarColorProperty;
   private static final Map<Type, Color> CAT_TYPE_EDITOR_ITEM_COLORS;

   public CatShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<CatShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(CAT_TYPE);
      CatShop var10002 = (CatShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.catTypeProperty = var10001.onValueChanged(var10002::applyCatType).build(this.properties);
      var10001 = new PropertyValue(COLLAR_COLOR);
      var10002 = (CatShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.collarColorProperty = var10001.onValueChanged(var10002::applyCollarColor).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.catTypeProperty.load(shopObjectData);
      this.collarColorProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.catTypeProperty.save(shopObjectData);
      this.collarColorProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyCatType();
      this.applyCollarColor();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getCatTypeEditorButton());
      editorButtons.add(this.getCollarColorEditorButton());
      return editorButtons;
   }

   public Type getCatType() {
      return (Type)this.catTypeProperty.getValue();
   }

   public void setCatType(Type catType) {
      this.catTypeProperty.setValue(catType);
   }

   public void cycleCatType(boolean backwards) {
      this.setCatType((Type)RegistryUtils.cycleKeyed(Type.class, this.getCatType(), backwards));
   }

   private void applyCatType() {
      Cat entity = (Cat)this.getEntity();
      if (entity != null) {
         entity.setCatType(this.getCatType());
      }
   }

   private ItemStack getCatTypeEditorItem() {
      Type catType = this.getCatType();
      ItemStack iconItem = new ItemStack(Material.LEATHER_CHESTPLATE);
      Color color = (Color)CAT_TYPE_EDITOR_ITEM_COLORS.getOrDefault(catType, Color.PURPLE);
      ItemUtils.setLeatherColor(iconItem, color);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonCatVariant, Messages.buttonCatVariantLore);
      return iconItem;
   }

   private Button getCatTypeEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return CatShop.this.getCatTypeEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            CatShop.this.cycleCatType(backwards);
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
      Cat entity = (Cat)this.getEntity();
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
            return CatShop.this.getCollarColorEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            CatShop.this.cycleCollarColor(backwards);
            return true;
         }
      };
   }

   static {
      CAT_TYPE = (new BasicProperty()).dataKeyAccessor("catType", KeyedSerializers.forRegistry(Type.class)).defaultValue(Type.TABBY).build();
      COLLAR_COLOR = (new BasicProperty()).dataKeyAccessor("collarColor", EnumSerializers.lenient(DyeColor.class)).nullable().defaultValue((Object)null).build();
      CAT_TYPE_EDITOR_ITEM_COLORS = Map.ofEntries(new Entry[]{Map.entry(Type.TABBY, Color.BLACK.mixColors(new Color[]{Color.ORANGE})), Map.entry(Type.ALL_BLACK, Color.BLACK), Map.entry(Type.BLACK, Color.BLACK.mixDyes(new DyeColor[]{DyeColor.GRAY})), Map.entry(Type.BRITISH_SHORTHAIR, Color.SILVER), Map.entry(Type.CALICO, Color.ORANGE.mixDyes(new DyeColor[]{DyeColor.BROWN})), Map.entry(Type.JELLIE, Color.GRAY), Map.entry(Type.PERSIAN, Color.WHITE.mixDyes(new DyeColor[]{DyeColor.ORANGE})), Map.entry(Type.RAGDOLL, Color.WHITE.mixDyes(new DyeColor[]{DyeColor.BROWN})), Map.entry(Type.RED, Color.ORANGE), Map.entry(Type.SIAMESE, Color.GRAY.mixDyes(new DyeColor[]{DyeColor.BROWN})), Map.entry(Type.WHITE, Color.WHITE)});
   }
}
