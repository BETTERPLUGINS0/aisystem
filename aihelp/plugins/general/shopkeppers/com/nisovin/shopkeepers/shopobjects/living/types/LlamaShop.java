package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopEquipment;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.util.bukkit.EquipmentUtils;
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
import org.bukkit.Material;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Llama.Color;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LlamaShop<E extends Llama> extends ChestedHorseShop<E> {
   public static final Property<Color> COLOR;
   public static final Property<DyeColor> CARPET_COLOR;
   private final PropertyValue<Color> colorProperty;
   private final PropertyValue<DyeColor> carpetColorProperty;

   public LlamaShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<? extends LlamaShop<E>> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(COLOR);
      LlamaShop var10002 = (LlamaShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.colorProperty = var10001.onValueChanged(var10002::applyColor).build(this.properties);
      var10001 = new PropertyValue(CARPET_COLOR);
      var10002 = (LlamaShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.carpetColorProperty = var10001.onValueChanged(var10002::applyCarpetColor).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.colorProperty.load(shopObjectData);
      this.carpetColorProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.colorProperty.save(shopObjectData);
      this.carpetColorProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyColor();
      this.applyCarpetColor();
      E entity = (Llama)Unsafe.assertNonNull((Llama)this.getEntity());
      entity.setTamed(true);
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getColorEditorButton());
      editorButtons.add(this.getCarpetColorEditorButton());
      return editorButtons;
   }

   protected void onEquipmentChanged() {
      super.onEquipmentChanged();
      this.applyCarpetColor();
   }

   public Color getColor() {
      return (Color)this.colorProperty.getValue();
   }

   public void setColor(Color color) {
      this.colorProperty.setValue(color);
   }

   public void cycleColor(boolean backwards) {
      this.setColor((Color)EnumUtils.cycleEnumConstant(Color.class, this.getColor(), backwards));
   }

   private void applyColor() {
      E entity = (Llama)this.getEntity();
      if (entity != null) {
         entity.setColor(this.getColor());
      }
   }

   private ItemStack getColorEditorItem() {
      ItemStack iconItem = new ItemStack(Material.LEATHER_CHESTPLATE);
      switch(this.getColor()) {
      case BROWN:
         ItemUtils.setLeatherColor(iconItem, DyeColor.BROWN.getColor());
         break;
      case GRAY:
         ItemUtils.setLeatherColor(iconItem, org.bukkit.Color.SILVER);
         break;
      case WHITE:
         ItemUtils.setLeatherColor(iconItem, org.bukkit.Color.WHITE);
         break;
      case CREAMY:
      default:
         ItemUtils.setLeatherColor(iconItem, org.bukkit.Color.WHITE.mixDyes(new DyeColor[]{DyeColor.ORANGE}));
      }

      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonLlamaVariant, Messages.buttonLlamaVariantLore);
      return iconItem;
   }

   private Button getColorEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return LlamaShop.this.getColorEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            LlamaShop.this.cycleColor(backwards);
            return true;
         }
      };
   }

   @Nullable
   public DyeColor getCarpetColor() {
      return (DyeColor)this.carpetColorProperty.getValue();
   }

   public void setCarpetColor(@Nullable DyeColor carpetColor) {
      this.carpetColorProperty.setValue(carpetColor);
   }

   public void cycleCarpetColor(boolean backwards) {
      this.setCarpetColor((DyeColor)EnumUtils.cycleEnumConstantNullable(DyeColor.class, this.getCarpetColor(), backwards));
   }

   private void applyCarpetColor() {
      E entity = (Llama)this.getEntity();
      if (entity != null) {
         if (EquipmentUtils.EQUIPMENT_SLOT_BODY.isPresent()) {
            LivingShopEquipment shopEquipment = this.getEquipment();
            UnmodifiableItemStack bodyItem = shopEquipment.getItem((EquipmentSlot)EquipmentUtils.EQUIPMENT_SLOT_BODY.get());
            if (!ItemUtils.isEmpty(bodyItem)) {
               return;
            }
         }

         ItemStack decor = null;
         DyeColor carpetColor = this.getCarpetColor();
         if (carpetColor != null) {
            decor = new ItemStack(ItemUtils.getCarpetType(carpetColor));
         }

         entity.getInventory().setDecor(decor);
      }
   }

   private ItemStack getCarpetColorEditorItem() {
      DyeColor carpetColor = this.getCarpetColor();
      ItemStack iconItem;
      if (carpetColor != null) {
         iconItem = new ItemStack(ItemUtils.getCarpetType(carpetColor));
      } else {
         iconItem = new ItemStack(Material.BARRIER);
      }

      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonLlamaCarpetColor, Messages.buttonLlamaCarpetColorLore);
      return iconItem;
   }

   private Button getCarpetColorEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return LlamaShop.this.getCarpetColorEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            LlamaShop.this.cycleCarpetColor(backwards);
            return true;
         }
      };
   }

   static {
      COLOR = (new BasicProperty()).dataKeyAccessor("color", EnumSerializers.lenient(Color.class)).defaultValue(Color.CREAMY).build();
      CARPET_COLOR = (new BasicProperty()).dataKeyAccessor("carpetColor", EnumSerializers.lenient(DyeColor.class)).nullable().defaultValue((Object)null).build();
   }
}
