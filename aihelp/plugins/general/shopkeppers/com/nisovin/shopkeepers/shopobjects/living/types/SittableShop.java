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
import com.nisovin.shopkeepers.util.data.serialization.java.BooleanSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Sittable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SittableShop<E extends Ageable & Sittable> extends BabyableShop<E> {
   public static final Property<Boolean> SITTING;
   private final PropertyValue<Boolean> sittingProperty;

   public SittableShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<? extends SittableShop<E>> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(SITTING);
      SittableShop var10002 = (SittableShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.sittingProperty = var10001.onValueChanged(var10002::applySitting).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.sittingProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.sittingProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applySitting();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getSittingEditorButton());
      return editorButtons;
   }

   public boolean isSitting() {
      return (Boolean)this.sittingProperty.getValue();
   }

   public void setSitting(boolean sitting) {
      this.sittingProperty.setValue(sitting);
   }

   public void cycleSitting() {
      this.setSitting(!this.isSitting());
   }

   private void applySitting() {
      Sittable entity = (Sittable)this.getEntity();
      if (entity != null) {
         entity.setSitting(this.isSitting());
      }
   }

   private ItemStack getSittingEditorItem() {
      ItemStack iconItem = new ItemStack(Material.IRON_HORSE_ARMOR);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonSitting, Messages.buttonSittingLore);
      return iconItem;
   }

   private Button getSittingEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return SittableShop.this.getSittingEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            SittableShop.this.cycleSitting();
            return true;
         }
      };
   }

   static {
      SITTING = (new BasicProperty()).dataKeyAccessor("sitting", BooleanSerializers.LENIENT).defaultValue(false).build();
   }
}
