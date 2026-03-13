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
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import org.bukkit.DyeColor;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Frog.Variant;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class FrogShop extends BabyableShop<Frog> {
   public static final Property<Variant> VARIANT;
   private final PropertyValue<Variant> variantProperty;
   private static final Map<Variant, DyeColor> VARIANT_EDITOR_ITEM_COLORS;

   public FrogShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<FrogShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(VARIANT);
      FrogShop var10002 = (FrogShop)Unsafe.initialized(this);
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
      this.setVariant((Variant)RegistryUtils.cycleKeyed(Variant.class, this.getVariant(), backwards));
   }

   private void applyVariant() {
      Frog entity = (Frog)this.getEntity();
      if (entity != null) {
         entity.setVariant(this.getVariant());
      }
   }

   private ItemStack getVariantEditorItem() {
      Variant frogVariant = this.getVariant();
      DyeColor dyeColor = (DyeColor)VARIANT_EDITOR_ITEM_COLORS.getOrDefault(frogVariant, DyeColor.PURPLE);
      ItemStack iconItem = new ItemStack(ItemUtils.getWoolType(dyeColor));
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonFrogVariant, Messages.buttonFrogVariantLore);
      return iconItem;
   }

   private Button getVariantEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return FrogShop.this.getVariantEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            FrogShop.this.cycleVariant(backwards);
            return true;
         }
      };
   }

   static {
      VARIANT = (new BasicProperty()).dataKeyAccessor("frogVariant", KeyedSerializers.forRegistry(Variant.class)).defaultValue(Variant.TEMPERATE).build();
      VARIANT_EDITOR_ITEM_COLORS = Map.ofEntries(new Entry[]{Map.entry(Variant.TEMPERATE, DyeColor.ORANGE), Map.entry(Variant.WARM, DyeColor.LIGHT_GRAY), Map.entry(Variant.COLD, DyeColor.GREEN)});
   }
}
