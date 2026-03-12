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
import org.bukkit.entity.Zombie;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ZombieShop<E extends Zombie> extends SKLivingShopObject<E> {
   public static final Property<Boolean> BABY;
   private final PropertyValue<Boolean> babyProperty;

   public ZombieShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<? extends ZombieShop<E>> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(BABY);
      ZombieShop var10002 = (ZombieShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.babyProperty = var10001.onValueChanged(var10002::applyBaby).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.babyProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.babyProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyBaby();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getBabyEditorButton());
      return editorButtons;
   }

   public boolean isBaby() {
      return (Boolean)this.babyProperty.getValue();
   }

   public void setBaby(boolean baby) {
      this.babyProperty.setValue(baby);
   }

   public void cycleBaby() {
      this.setBaby(!this.isBaby());
   }

   private void applyBaby() {
      E entity = (Zombie)this.getEntity();
      if (entity != null) {
         if (this.isBaby()) {
            entity.setBaby();
         } else {
            entity.setAdult();
            this.teleportBack();
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
            return ZombieShop.this.getBabyEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            ZombieShop.this.cycleBaby();
            return true;
         }
      };
   }

   static {
      BABY = (new BasicProperty()).dataKeyAccessor("baby", BooleanSerializers.LENIENT).defaultValue(false).build();
   }
}
