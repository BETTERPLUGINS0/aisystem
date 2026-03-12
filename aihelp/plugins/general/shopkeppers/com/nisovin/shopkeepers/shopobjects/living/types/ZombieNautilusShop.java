package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopEquipment;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.shopobjects.living.SKLivingShopObject;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.util.bukkit.EquipmentUtils;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.NamespacedKeySerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.BooleanSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.EnumSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.inventory.NautilusArmor;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ZombieNautilusShop extends SKLivingShopObject<Tameable> {
   public static final Property<Boolean> SADDLE;
   public static final Property<NamespacedKey> VARIANT;
   public static final Property<NautilusArmor> ARMOR;
   private final PropertyValue<Boolean> saddleProperty;
   private final PropertyValue<NamespacedKey> variantProperty;
   private final PropertyValue<NautilusArmor> armorProperty;
   private static final NamespacedKey VARIANT_WARM;

   public ZombieNautilusShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<ZombieNautilusShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(SADDLE);
      ZombieNautilusShop var10002 = (ZombieNautilusShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.saddleProperty = var10001.onValueChanged(var10002::applySaddle).build(this.properties);
      var10001 = new PropertyValue(VARIANT);
      var10002 = (ZombieNautilusShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.variantProperty = var10001.onValueChanged(var10002::applyVariant).build(this.properties);
      var10001 = new PropertyValue(ARMOR);
      var10002 = (ZombieNautilusShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.armorProperty = var10001.onValueChanged(var10002::applyArmor).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.saddleProperty.load(shopObjectData);
      this.variantProperty.load(shopObjectData);
      this.armorProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.saddleProperty.save(shopObjectData);
      this.variantProperty.save(shopObjectData);
      this.armorProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applySaddle();
      this.applyVariant();
      this.applyArmor();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getSaddleEditorButton());
      editorButtons.add(this.getVariantEditorButton());
      editorButtons.add(this.getArmorEditorButton());
      return editorButtons;
   }

   protected void onEquipmentChanged() {
      super.onEquipmentChanged();
      this.applyArmor();
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
      Tameable entity = (Tameable)this.getEntity();
      if (entity != null) {
         ItemStack saddleItem = this.hasSaddle() ? new ItemStack(Material.SADDLE) : null;
         EntityEquipment equipment = entity.getEquipment();

         assert equipment != null;

         equipment.setItem(EquipmentSlot.SADDLE, saddleItem);
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
            return ZombieNautilusShop.this.getSaddleEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            ZombieNautilusShop.this.cycleSaddle();
            return true;
         }
      };
   }

   public NamespacedKey getVariant() {
      return (NamespacedKey)this.variantProperty.getValue();
   }

   public void setVariant(NamespacedKey variant) {
      this.variantProperty.setValue(variant);
   }

   public void cycleVariant(boolean backwards) {
      this.setVariant(Compat.getProvider().cycleZombieNautilusVariant(this.getVariant(), backwards));
   }

   private void applyVariant() {
      LivingEntity entity = (LivingEntity)this.getEntity();
      if (entity != null) {
         Compat.getProvider().setZombieNautilusVariant(entity, this.getVariant());
      }
   }

   private ItemStack getVariantEditorItem() {
      NamespacedKey variant = this.getVariant();
      Material itemType = variant.equals(VARIANT_WARM) ? Material.BRAIN_CORAL : Material.DEAD_BRAIN_CORAL;
      ItemStack iconItem = new ItemStack(itemType);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonZombieNautilusVariant, Messages.buttonZombieNautilusVariantLore);
      return iconItem;
   }

   private Button getVariantEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return ZombieNautilusShop.this.getVariantEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            ZombieNautilusShop.this.cycleVariant(backwards);
            return true;
         }
      };
   }

   @Nullable
   public NautilusArmor getArmor() {
      return (NautilusArmor)this.armorProperty.getValue();
   }

   public void setArmor(@Nullable NautilusArmor armor) {
      this.armorProperty.setValue(armor);
   }

   public void cycleArmor(boolean backwards) {
      this.setArmor((NautilusArmor)EnumUtils.cycleEnumConstantNullable(NautilusArmor.class, this.getArmor(), backwards, (horseArmor) -> {
         return horseArmor == null || horseArmor.isEnabled();
      }));
   }

   private void applyArmor() {
      LivingEntity entity = (LivingEntity)this.getEntity();
      if (entity != null) {
         if (EquipmentUtils.EQUIPMENT_SLOT_BODY.isPresent()) {
            LivingShopEquipment shopEquipment = this.getEquipment();
            UnmodifiableItemStack bodyItem = shopEquipment.getItem((EquipmentSlot)EquipmentUtils.EQUIPMENT_SLOT_BODY.get());
            if (!ItemUtils.isEmpty(bodyItem)) {
               return;
            }
         }

         NautilusArmor armor = this.getArmor();
         ItemStack armorItem = armor != null && armor.isEnabled() ? new ItemStack((Material)Unsafe.assertNonNull(armor.getMaterial())) : null;
         EntityEquipment equipment = entity.getEquipment();

         assert equipment != null;

         equipment.setItem(EquipmentSlot.BODY, armorItem);
      }
   }

   private ItemStack getArmorEditorItem() {
      NautilusArmor armor = this.getArmor();
      Material iconType = armor != null && armor.isEnabled() ? (Material)Unsafe.assertNonNull(armor.getMaterial()) : Material.BARRIER;
      ItemStack iconItem = new ItemStack(iconType);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonNautilusArmor, Messages.buttonNautilusArmorLore);
      return iconItem;
   }

   private Button getArmorEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return ZombieNautilusShop.this.getArmorEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            ZombieNautilusShop.this.cycleArmor(backwards);
            return true;
         }
      };
   }

   static {
      SADDLE = (new BasicProperty()).dataKeyAccessor("saddle", BooleanSerializers.LENIENT).defaultValue(false).build();
      VARIANT = (new BasicProperty()).dataKeyAccessor("variant", NamespacedKeySerializers.DEFAULT).defaultValue(NamespacedKey.minecraft("temperate")).build();
      ARMOR = (new BasicProperty()).dataKeyAccessor("armor", EnumSerializers.lenient(NautilusArmor.class)).nullable().defaultValue((Object)null).build();
      VARIANT_WARM = NamespacedKey.minecraft("warm");
   }
}
