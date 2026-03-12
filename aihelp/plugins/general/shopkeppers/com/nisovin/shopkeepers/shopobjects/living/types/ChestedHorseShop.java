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
import org.bukkit.entity.ChestedHorse;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ChestedHorseShop<E extends ChestedHorse> extends AbstractHorseShop<E> {
   public static final Property<Boolean> CARRYING_CHEST;
   private final PropertyValue<Boolean> carryingChestProperty;

   public ChestedHorseShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<? extends ChestedHorseShop<E>> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(CARRYING_CHEST);
      ChestedHorseShop var10002 = (ChestedHorseShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.carryingChestProperty = var10001.onValueChanged(var10002::applyCarryingChest).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.carryingChestProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.carryingChestProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyCarryingChest();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getCarryingChestEditorButton());
      return editorButtons;
   }

   public boolean isCarryingChest() {
      return (Boolean)this.carryingChestProperty.getValue();
   }

   public void setCarryingChest(boolean carryingChest) {
      this.carryingChestProperty.setValue(carryingChest);
   }

   public void cycleCarryingChest() {
      this.setCarryingChest(!this.isCarryingChest());
   }

   private void applyCarryingChest() {
      E entity = (ChestedHorse)this.getEntity();
      if (entity != null) {
         entity.setCarryingChest(this.isCarryingChest());
      }
   }

   private ItemStack getCarryingChestEditorItem() {
      ItemStack iconItem = new ItemStack(Material.CHEST);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonCarryingChest, Messages.buttonCarryingChestLore);
      return iconItem;
   }

   private Button getCarryingChestEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return ChestedHorseShop.this.getCarryingChestEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            ChestedHorseShop.this.cycleCarryingChest();
            return true;
         }
      };
   }

   static {
      CARRYING_CHEST = (new BasicProperty()).dataKeyAccessor("carryingChest", BooleanSerializers.LENIENT).defaultValue(false).build();
   }
}
