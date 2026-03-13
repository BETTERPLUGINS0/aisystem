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
import com.nisovin.shopkeepers.util.data.serialization.java.EnumSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.TropicalFish.Pattern;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TropicalFishShop extends SKLivingShopObject<TropicalFish> {
   public static final Property<Pattern> PATTERN;
   public static final Property<DyeColor> BODY_COLOR;
   public static final Property<DyeColor> PATTERN_COLOR;
   private final PropertyValue<Pattern> patternProperty;
   private final PropertyValue<DyeColor> bodyColorProperty;
   private final PropertyValue<DyeColor> patternColorProperty;

   public TropicalFishShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<TropicalFishShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(PATTERN);
      TropicalFishShop var10002 = (TropicalFishShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.patternProperty = var10001.onValueChanged(var10002::applyPattern).build(this.properties);
      var10001 = new PropertyValue(BODY_COLOR);
      var10002 = (TropicalFishShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.bodyColorProperty = var10001.onValueChanged(var10002::applyBodyColor).build(this.properties);
      var10001 = new PropertyValue(PATTERN_COLOR);
      var10002 = (TropicalFishShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.patternColorProperty = var10001.onValueChanged(var10002::applyPatternColor).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.patternProperty.load(shopObjectData);
      this.bodyColorProperty.load(shopObjectData);
      this.patternColorProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.patternProperty.save(shopObjectData);
      this.bodyColorProperty.save(shopObjectData);
      this.patternColorProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyPattern();
      this.applyBodyColor();
      this.applyPatternColor();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getPatternEditorButton());
      editorButtons.add(this.getBodyColorEditorButton());
      editorButtons.add(this.getPatternColorEditorButton());
      return editorButtons;
   }

   public Pattern getPattern() {
      return (Pattern)this.patternProperty.getValue();
   }

   public void setPattern(Pattern pattern) {
      this.patternProperty.setValue(pattern);
   }

   public void cyclePattern(boolean backwards) {
      this.setPattern((Pattern)EnumUtils.cycleEnumConstant(Pattern.class, this.getPattern(), backwards));
   }

   private void applyPattern() {
      TropicalFish entity = (TropicalFish)this.getEntity();
      if (entity != null) {
         entity.setPattern(this.getPattern());
      }
   }

   private ItemStack getPatternEditorItem() {
      ItemStack iconItem = new ItemStack(Material.TROPICAL_FISH);
      String patternName = EnumUtils.formatEnumName(this.getPattern().name());
      String displayName = StringUtils.replaceArguments(Messages.buttonTropicalFishPattern, "pattern", patternName);
      List<String> lore = StringUtils.replaceArguments((Collection)Messages.buttonTropicalFishPatternLore, (Object[])("pattern", patternName));
      ItemUtils.setDisplayNameAndLore(iconItem, displayName, lore);
      return iconItem;
   }

   private Button getPatternEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return TropicalFishShop.this.getPatternEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            TropicalFishShop.this.cyclePattern(backwards);
            return true;
         }
      };
   }

   public DyeColor getBodyColor() {
      return (DyeColor)this.bodyColorProperty.getValue();
   }

   public void setBodyColor(DyeColor color) {
      this.bodyColorProperty.setValue(color);
   }

   public void cycleBodyColor(boolean backwards) {
      this.setBodyColor((DyeColor)EnumUtils.cycleEnumConstant(DyeColor.class, this.getBodyColor(), backwards));
   }

   private void applyBodyColor() {
      TropicalFish entity = (TropicalFish)this.getEntity();
      if (entity != null) {
         entity.setBodyColor(this.getBodyColor());
      }
   }

   private ItemStack getBodyColorEditorItem() {
      ItemStack iconItem = new ItemStack(ItemUtils.getWoolType(this.getBodyColor()));
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonTropicalFishBodyColor, Messages.buttonTropicalFishBodyColorLore);
      return iconItem;
   }

   private Button getBodyColorEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return TropicalFishShop.this.getBodyColorEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            TropicalFishShop.this.cycleBodyColor(backwards);
            return true;
         }
      };
   }

   public DyeColor getPatternColor() {
      return (DyeColor)this.patternColorProperty.getValue();
   }

   public void setPatternColor(DyeColor color) {
      this.patternColorProperty.setValue(color);
   }

   public void cyclePatternColor(boolean backwards) {
      this.setPatternColor((DyeColor)EnumUtils.cycleEnumConstant(DyeColor.class, this.getPatternColor(), backwards));
   }

   private void applyPatternColor() {
      TropicalFish entity = (TropicalFish)this.getEntity();
      if (entity != null) {
         entity.setPatternColor(this.getPatternColor());
      }
   }

   private ItemStack getPatternColorEditorItem() {
      ItemStack iconItem = new ItemStack(ItemUtils.getWoolType(this.getPatternColor()));
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonTropicalFishPatternColor, Messages.buttonTropicalFishPatternColorLore);
      return iconItem;
   }

   private Button getPatternColorEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return TropicalFishShop.this.getPatternColorEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            TropicalFishShop.this.cyclePatternColor(backwards);
            return true;
         }
      };
   }

   static {
      PATTERN = (new BasicProperty()).dataKeyAccessor("pattern", EnumSerializers.lenient(Pattern.class)).defaultValue(Pattern.KOB).build();
      BODY_COLOR = (new BasicProperty()).dataKeyAccessor("bodyColor", EnumSerializers.lenient(DyeColor.class)).defaultValue(DyeColor.WHITE).build();
      PATTERN_COLOR = (new BasicProperty()).dataKeyAccessor("patternColor", EnumSerializers.lenient(DyeColor.class)).defaultValue(DyeColor.WHITE).build();
   }
}
