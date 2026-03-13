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
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Pig.Variant;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PigShop extends BabyableShop<Pig> {
   public static final Property<Boolean> SADDLE;
   public static final Property<Variant> VARIANT;
   private final PropertyValue<Boolean> saddleProperty;
   private final PropertyValue<Variant> variantProperty;
   private static final Map<Variant, Color> VARIANT_EDITOR_ITEM_COLORS;

   public PigShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<PigShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(SADDLE);
      PigShop var10002 = (PigShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.saddleProperty = var10001.onValueChanged(var10002::applySaddle).build(this.properties);
      var10001 = new PropertyValue(VARIANT);
      var10002 = (PigShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.variantProperty = var10001.onValueChanged(var10002::applyVariant).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.saddleProperty.load(shopObjectData);
      this.variantProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.saddleProperty.save(shopObjectData);
      this.variantProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applySaddle();
      this.applyVariant();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getSaddleEditorButton());
      editorButtons.add(this.getVariantEditorButton());
      return editorButtons;
   }

   public boolean hasSaddle() {
      return (Boolean)this.saddleProperty.getValue();
   }

   public void setSaddle(boolean saddle) {
      this.saddleProperty.setValue(saddle);
   }

   public void cycleSaddle() {
      this.setSaddle(!this.hasSaddle());
   }

   private void applySaddle() {
      Pig entity = (Pig)this.getEntity();
      if (entity != null) {
         entity.setSaddle(this.hasSaddle());
      }
   }

   private ItemStack getSaddleEditorItem() {
      ItemStack iconItem = new ItemStack(Material.SADDLE);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonSaddle, Messages.buttonSaddleLore);
      return iconItem;
   }

   private Button getSaddleEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return PigShop.this.getSaddleEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            PigShop.this.cycleSaddle();
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
      Pig entity = (Pig)this.getEntity();
      if (entity != null) {
         entity.setVariant(this.getVariant());
      }
   }

   private ItemStack getVariantEditorItem() {
      ItemStack iconItem = new ItemStack(Material.LEATHER_CHESTPLATE);
      Color color = (Color)VARIANT_EDITOR_ITEM_COLORS.getOrDefault(this.getVariant(), Color.BLACK);
      ItemUtils.setLeatherColor(iconItem, color);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonPigVariant, Messages.buttonPigVariantLore);
      return iconItem;
   }

   private Button getVariantEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return PigShop.this.getVariantEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            PigShop.this.cycleVariant(backwards);
            return true;
         }
      };
   }

   static {
      SADDLE = (new BasicProperty()).dataKeyAccessor("saddle", BooleanSerializers.LENIENT).defaultValue(false).build();
      VARIANT = (new BasicProperty()).dataKeyAccessor("variant", KeyedSerializers.forRegistry(Variant.class)).defaultValue(Variant.TEMPERATE).build();
      VARIANT_EDITOR_ITEM_COLORS = Map.ofEntries(new Entry[]{Map.entry(Variant.TEMPERATE, Color.fromRGB(252, 183, 179)), Map.entry(Variant.WARM, Color.fromRGB(203, 114, 56)), Map.entry(Variant.COLD, Color.fromRGB(226, 201, 148))});
   }
}
