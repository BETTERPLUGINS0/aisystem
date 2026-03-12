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
import com.nisovin.shopkeepers.util.bukkit.EquipmentUtils;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.BooleanSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class AbstractHorseShop<E extends AbstractHorse> extends BabyableShop<E> {
   public static final Property<Boolean> SADDLE;
   private final PropertyValue<Boolean> saddleProperty;

   public AbstractHorseShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<? extends AbstractHorseShop<E>> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(SADDLE);
      AbstractHorseShop var10002 = (AbstractHorseShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.saddleProperty = var10001.onValueChanged(var10002::applySaddle).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.saddleProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.saddleProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applySaddle();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      if (EquipmentUtils.supportsSaddle(this.getEntityType())) {
         editorButtons.add(this.getSaddleEditorButton());
      }

      return editorButtons;
   }

   public boolean hasSaddle() {
      return (Boolean)this.saddleProperty.getValue();
   }

   public void setSaddle(boolean saddle) {
      if (EquipmentUtils.supportsSaddle(this.getEntityType())) {
         this.saddleProperty.setValue(saddle);
      }
   }

   public void cycleSaddle() {
      this.setSaddle(!this.hasSaddle());
   }

   private void applySaddle() {
      if (EquipmentUtils.supportsSaddle(this.getEntityType())) {
         AbstractHorse entity = (AbstractHorse)this.getEntity();
         if (entity != null) {
            ItemStack saddleItem = this.hasSaddle() ? new ItemStack(Material.SADDLE) : null;
            EntityEquipment equipment = entity.getEquipment();

            assert equipment != null;

            equipment.setItem(EquipmentSlot.SADDLE, saddleItem);
         }
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
            return AbstractHorseShop.this.getSaddleEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            AbstractHorseShop.this.cycleSaddle();
            return true;
         }
      };
   }

   static {
      SADDLE = (new BasicProperty()).dataKeyAccessor("saddle", BooleanSerializers.LENIENT).defaultValue(false).omitIfDefault().build();
   }
}
