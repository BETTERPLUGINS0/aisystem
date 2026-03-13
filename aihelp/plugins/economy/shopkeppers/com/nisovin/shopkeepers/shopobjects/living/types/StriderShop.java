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
import org.bukkit.entity.Strider;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class StriderShop extends BabyableShop<Strider> {
   public static final Property<Boolean> SADDLE;
   private final PropertyValue<Boolean> saddleProperty;

   public StriderShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<StriderShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(SADDLE);
      StriderShop var10002 = (StriderShop)Unsafe.initialized(this);
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
      editorButtons.add(this.getSaddleEditorButton());
      return editorButtons;
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
      Strider entity = (Strider)this.getEntity();
      if (entity != null) {
         entity.setSaddle(this.hasSaddle());
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
            return StriderShop.this.getSaddleEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            StriderShop.this.cycleSaddle();
            return true;
         }
      };
   }

   static {
      SADDLE = (new BasicProperty()).dataKeyAccessor("saddle", BooleanSerializers.LENIENT).defaultValue(false).build();
   }
}
