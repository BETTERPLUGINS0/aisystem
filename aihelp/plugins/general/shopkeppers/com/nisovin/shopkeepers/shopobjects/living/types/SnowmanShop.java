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
import org.bukkit.entity.Snowman;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SnowmanShop extends SKLivingShopObject<Snowman> {
   public static final Property<Boolean> PUMPKIN_HEAD;
   private final PropertyValue<Boolean> pumpkinHeadProperty;

   public SnowmanShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<SnowmanShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(PUMPKIN_HEAD);
      SnowmanShop var10002 = (SnowmanShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.pumpkinHeadProperty = var10001.onValueChanged(var10002::applyPumpkinHead).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.pumpkinHeadProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.pumpkinHeadProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyPumpkinHead();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getPumpkinHeadEditorButton());
      return editorButtons;
   }

   public boolean hasPumpkinHead() {
      return (Boolean)this.pumpkinHeadProperty.getValue();
   }

   public void setPumpkinHead(boolean pumpkinHead) {
      this.pumpkinHeadProperty.setValue(pumpkinHead);
   }

   public void cyclePumpkinHead() {
      this.setPumpkinHead(!this.hasPumpkinHead());
   }

   private void applyPumpkinHead() {
      Snowman entity = (Snowman)this.getEntity();
      if (entity != null) {
         entity.setDerp(!this.hasPumpkinHead());
      }
   }

   private ItemStack getPumpkinHeadEditorItem() {
      Material iconItemType = this.hasPumpkinHead() ? Material.CARVED_PUMPKIN : Material.PUMPKIN;
      ItemStack iconItem = new ItemStack(iconItemType);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonSnowmanPumpkinHead, Messages.buttonSnowmanPumpkinHeadLore);
      return iconItem;
   }

   private Button getPumpkinHeadEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return SnowmanShop.this.getPumpkinHeadEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            SnowmanShop.this.cyclePumpkinHead();
            return true;
         }
      };
   }

   static {
      PUMPKIN_HEAD = (new BasicProperty()).dataKeyAccessor("pumpkinHead", BooleanSerializers.LENIENT).defaultValue(false).build();
   }
}
