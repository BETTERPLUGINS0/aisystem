package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopEquipment;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.compat.MC_1_21_11;
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
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

public class HorseShop extends AbstractHorseShop<Horse> {
   public static final Property<Color> COLOR;
   public static final Property<Style> STYLE;
   public static final Property<HorseShop.HorseArmor> ARMOR;
   private final PropertyValue<Color> colorProperty;
   private final PropertyValue<Style> styleProperty;
   private final PropertyValue<HorseShop.HorseArmor> armorProperty;

   public HorseShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<HorseShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(COLOR);
      HorseShop var10002 = (HorseShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.colorProperty = var10001.onValueChanged(var10002::applyColor).build(this.properties);
      var10001 = new PropertyValue(STYLE);
      var10002 = (HorseShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.styleProperty = var10001.onValueChanged(var10002::applyStyle).build(this.properties);
      var10001 = new PropertyValue(ARMOR);
      var10002 = (HorseShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.armorProperty = var10001.onValueChanged(var10002::applyArmor).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.colorProperty.load(shopObjectData);
      this.styleProperty.load(shopObjectData);
      this.armorProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.colorProperty.save(shopObjectData);
      this.styleProperty.save(shopObjectData);
      this.armorProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyColor();
      this.applyStyle();
      this.applyArmor();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getColorEditorButton());
      editorButtons.add(this.getStyleEditorButton());
      editorButtons.add(this.getArmorEditorButton());
      return editorButtons;
   }

   protected void onEquipmentChanged() {
      super.onEquipmentChanged();
      this.applyArmor();
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
      Horse entity = (Horse)this.getEntity();
      if (entity != null) {
         entity.setColor(this.getColor());
      }
   }

   private ItemStack getColorEditorItem() {
      ItemStack iconItem = new ItemStack(Material.LEATHER_CHESTPLATE);
      switch(this.getColor()) {
      case BLACK:
         ItemUtils.setLeatherColor(iconItem, org.bukkit.Color.fromRGB(31, 31, 31));
         break;
      case BROWN:
         ItemUtils.setLeatherColor(iconItem, org.bukkit.Color.fromRGB(54, 25, 8));
         break;
      case CHESTNUT:
         ItemUtils.setLeatherColor(iconItem, org.bukkit.Color.fromRGB(110, 59, 38));
         break;
      case CREAMY:
         ItemUtils.setLeatherColor(iconItem, org.bukkit.Color.fromRGB(98, 65, 28));
         break;
      case DARK_BROWN:
         ItemUtils.setLeatherColor(iconItem, org.bukkit.Color.fromRGB(39, 21, 13));
         break;
      case GRAY:
         ItemUtils.setLeatherColor(iconItem, org.bukkit.Color.SILVER);
         break;
      case WHITE:
      default:
         ItemUtils.setLeatherColor(iconItem, org.bukkit.Color.WHITE);
      }

      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonHorseColor, Messages.buttonHorseColorLore);
      return iconItem;
   }

   private Button getColorEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return HorseShop.this.getColorEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            HorseShop.this.cycleColor(backwards);
            return true;
         }
      };
   }

   public Style getStyle() {
      return (Style)this.styleProperty.getValue();
   }

   public void setStyle(Style style) {
      this.styleProperty.setValue(style);
   }

   public void cycleStyle(boolean backwards) {
      this.setStyle((Style)EnumUtils.cycleEnumConstant(Style.class, this.getStyle(), backwards));
   }

   private void applyStyle() {
      Horse entity = (Horse)this.getEntity();
      if (entity != null) {
         entity.setStyle(this.getStyle());
      }
   }

   private ItemStack getStyleEditorItem() {
      ItemStack iconItem = new ItemStack(Material.WHITE_BANNER);
      BannerMeta itemMeta = (BannerMeta)Unsafe.castNonNull(iconItem.getItemMeta());
      itemMeta.addPattern(new Pattern(DyeColor.BROWN, PatternType.CURLY_BORDER));
      itemMeta.addPattern(new Pattern(DyeColor.BROWN, PatternType.TRIANGLES_BOTTOM));
      itemMeta.addPattern(new Pattern(DyeColor.BROWN, PatternType.TRIANGLES_TOP));
      iconItem.setItemMeta(itemMeta);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonHorseStyle, Messages.buttonHorseStyleLore);
      return iconItem;
   }

   private Button getStyleEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return HorseShop.this.getStyleEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            HorseShop.this.cycleStyle(backwards);
            return true;
         }
      };
   }

   @Nullable
   public HorseShop.HorseArmor getArmor() {
      return (HorseShop.HorseArmor)this.armorProperty.getValue();
   }

   public void setArmor(@Nullable HorseShop.HorseArmor armor) {
      this.armorProperty.setValue(armor);
   }

   public void cycleArmor(boolean backwards) {
      this.setArmor((HorseShop.HorseArmor)EnumUtils.cycleEnumConstantNullable(HorseShop.HorseArmor.class, this.getArmor(), backwards, (horseArmor) -> {
         return horseArmor == null || horseArmor.isEnabled();
      }));
   }

   private void applyArmor() {
      Horse entity = (Horse)this.getEntity();
      if (entity != null) {
         if (EquipmentUtils.EQUIPMENT_SLOT_BODY.isPresent()) {
            LivingShopEquipment shopEquipment = this.getEquipment();
            UnmodifiableItemStack bodyItem = shopEquipment.getItem((EquipmentSlot)EquipmentUtils.EQUIPMENT_SLOT_BODY.get());
            if (!ItemUtils.isEmpty(bodyItem)) {
               return;
            }
         }

         HorseShop.HorseArmor armor = this.getArmor();
         ItemStack armorItem = armor != null && armor.isEnabled() ? new ItemStack((Material)Unsafe.assertNonNull(armor.getMaterial())) : null;
         entity.getInventory().setArmor(armorItem);
      }
   }

   private ItemStack getArmorEditorItem() {
      HorseShop.HorseArmor armor = this.getArmor();
      Material iconType = armor != null && armor.isEnabled() ? (Material)Unsafe.assertNonNull(armor.getMaterial()) : Material.BARRIER;
      ItemStack iconItem = new ItemStack(iconType);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonHorseArmor, Messages.buttonHorseArmorLore);
      return iconItem;
   }

   private Button getArmorEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return HorseShop.this.getArmorEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            HorseShop.this.cycleArmor(backwards);
            return true;
         }
      };
   }

   static {
      COLOR = (new BasicProperty()).dataKeyAccessor("color", EnumSerializers.lenient(Color.class)).defaultValue(Color.BROWN).build();
      STYLE = (new BasicProperty()).dataKeyAccessor("style", EnumSerializers.lenient(Style.class)).defaultValue(Style.NONE).build();
      ARMOR = (new BasicProperty()).dataKeyAccessor("armor", EnumSerializers.lenient(HorseShop.HorseArmor.class)).nullable().defaultValue((Object)null).build();
   }

   public static enum HorseArmor {
      LEATHER(Material.LEATHER_HORSE_ARMOR),
      IRON(Material.IRON_HORSE_ARMOR),
      GOLD(Material.GOLDEN_HORSE_ARMOR),
      DIAMOND(Material.DIAMOND_HORSE_ARMOR),
      NETHERITE(MC_1_21_11.NETHERITE_HORSE_ARMOR);

      @Nullable
      private final Material material;

      private HorseArmor(@Nullable Material param3) {
         this.material = material;
      }

      @Nullable
      public Material getMaterial() {
         return this.material;
      }

      public boolean isEnabled() {
         return this.material != null;
      }

      // $FF: synthetic method
      private static HorseShop.HorseArmor[] $values() {
         return new HorseShop.HorseArmor[]{LEATHER, IRON, GOLD, DIAMOND, NETHERITE};
      }
   }
}
