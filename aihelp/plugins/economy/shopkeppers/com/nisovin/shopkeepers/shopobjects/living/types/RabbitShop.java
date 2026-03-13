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
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class RabbitShop extends BabyableShop<Rabbit> {
   public static final Property<Type> RABBIT_TYPE;
   private final PropertyValue<Type> rabbitTypeProperty;

   public RabbitShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<RabbitShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(RABBIT_TYPE);
      RabbitShop var10002 = (RabbitShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.rabbitTypeProperty = var10001.onValueChanged(var10002::applyRabbitType).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.rabbitTypeProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.rabbitTypeProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyRabbitType();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getRabbitTypeEditorButton());
      return editorButtons;
   }

   public Type getRabbitType() {
      return (Type)this.rabbitTypeProperty.getValue();
   }

   public void setRabbitType(Type rabbitType) {
      this.rabbitTypeProperty.setValue(rabbitType);
   }

   public void cycleRabbitType(boolean backwards) {
      this.setRabbitType((Type)EnumUtils.cycleEnumConstant(Type.class, this.getRabbitType(), backwards));
   }

   private void applyRabbitType() {
      Rabbit entity = (Rabbit)this.getEntity();
      if (entity != null) {
         Type rabbitType = this.getRabbitType();
         if (rabbitType == Type.THE_KILLER_BUNNY) {
            String customName = entity.getCustomName();
            entity.setCustomName(" ");
            entity.setRabbitType(rabbitType);
            entity.setCustomName(customName);
            this.overwriteAI();
         } else {
            entity.setRabbitType(rabbitType);
         }

      }
   }

   private ItemStack getRabbitTypeEditorItem() {
      ItemStack iconItem = new ItemStack(Material.LEATHER_CHESTPLATE);
      switch(this.getRabbitType()) {
      case BROWN:
         ItemUtils.setLeatherColor(iconItem, Color.fromRGB(141, 118, 88));
         break;
      case WHITE:
         ItemUtils.setLeatherColor(iconItem, Color.WHITE);
         break;
      case BLACK:
         ItemUtils.setLeatherColor(iconItem, Color.fromRGB(31, 31, 31));
         break;
      case BLACK_AND_WHITE:
         ItemUtils.setLeatherColor(iconItem, Color.GRAY);
         break;
      case GOLD:
         ItemUtils.setLeatherColor(iconItem, Color.fromRGB(246, 224, 136));
         break;
      case SALT_AND_PEPPER:
         ItemUtils.setLeatherColor(iconItem, Color.fromRGB(138, 120, 98));
         break;
      case THE_KILLER_BUNNY:
         ItemUtils.setLeatherColor(iconItem, Color.fromRGB(142, 11, 28));
         break;
      default:
         ItemUtils.setLeatherColor(iconItem, Color.PURPLE);
      }

      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonRabbitVariant, Messages.buttonRabbitVariantLore);
      return iconItem;
   }

   private Button getRabbitTypeEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return RabbitShop.this.getRabbitTypeEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            RabbitShop.this.cycleRabbitType(backwards);
            return true;
         }
      };
   }

   static {
      RABBIT_TYPE = (new BasicProperty()).dataKeyAccessor("rabbitType", EnumSerializers.lenient(Type.class)).defaultValue(Type.BROWN).build();
   }
}
