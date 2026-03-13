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
import com.nisovin.shopkeepers.util.data.serialization.java.BooleanSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BabyableShop<E extends Ageable> extends SKLivingShopObject<E> {
   public static final Property<Boolean> BABY;
   private final PropertyValue<Boolean> babyProperty;

   public BabyableShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<? extends BabyableShop<E>> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(BABY);
      BabyableShop var10002 = (BabyableShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.babyProperty = var10001.onValueChanged(var10002::applyBaby).build(this.properties);
   }

   protected boolean isBabyable() {
      String var1 = this.getEntityType().name();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1942082154:
         if (var1.equals("PARROT")) {
            var2 = 0;
         }
         break;
      case 2166692:
         if (var1.equals("FROG")) {
            var2 = 3;
         }
         break;
      case 78304826:
         if (var1.equals("PIGLIN_BRUTE")) {
            var2 = 2;
         }
         break;
      case 1148457240:
         if (var1.equals("WANDERING_TRADER")) {
            var2 = 1;
         }
      }

      switch(var2) {
      case 0:
      case 1:
      case 2:
      case 3:
         return false;
      default:
         return true;
      }
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      if (this.isBabyable()) {
         this.babyProperty.load(shopObjectData);
      }

   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      if (this.isBabyable()) {
         this.babyProperty.save(shopObjectData);
      }

   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyBaby();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      if (this.isBabyable()) {
         editorButtons.add(this.getBabyEditorButton());
      }

      return editorButtons;
   }

   public boolean isBaby() {
      return (Boolean)this.babyProperty.getValue();
   }

   public void setBaby(boolean baby) {
      if (this.isBabyable()) {
         this.babyProperty.setValue(baby);
      }
   }

   public void cycleBaby() {
      this.setBaby(!this.isBaby());
   }

   private void applyBaby() {
      if (this.isBabyable()) {
         E entity = (Ageable)this.getEntity();
         if (entity != null) {
            if (this.isBaby()) {
               entity.setBaby();
            } else {
               entity.setAdult();
               this.teleportBack();
            }

         }
      }
   }

   private ItemStack getBabyEditorItem() {
      ItemStack iconItem = new ItemStack(Material.EGG);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonBaby, Messages.buttonBabyLore);
      return iconItem;
   }

   private Button getBabyEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return BabyableShop.this.getBabyEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            BabyableShop.this.cycleBaby();
            return true;
         }
      };
   }

   static {
      BABY = (new BasicProperty()).dataKeyAccessor("baby", BooleanSerializers.LENIENT).defaultValue(false).build();
   }
}
