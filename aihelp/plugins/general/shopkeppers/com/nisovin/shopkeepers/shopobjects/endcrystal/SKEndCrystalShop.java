package com.nisovin.shopkeepers.shopobjects.endcrystal;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.endcrystal.EndCrystalShopObject;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObject;
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
import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKEndCrystalShop extends BaseEntityShopObject<EnderCrystal> implements EndCrystalShopObject {
   public static final Property<Boolean> SHOW_BOTTOM;
   private final PropertyValue<Boolean> showBottomProperty;

   public SKEndCrystalShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<SKEndCrystalShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(SHOW_BOTTOM);
      SKEndCrystalShop var10002 = (SKEndCrystalShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.showBottomProperty = var10001.onValueChanged(var10002::applyShowBottom).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.showBottomProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.showBottomProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyShowBottom();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getShowBottomEditorButton());
      return editorButtons;
   }

   public boolean isShowBottom() {
      return (Boolean)this.showBottomProperty.getValue();
   }

   public void setShowBottom(boolean showBottom) {
      this.showBottomProperty.setValue(showBottom);
   }

   public void cycleShowBottom(boolean backwards) {
      this.setShowBottom(!this.isShowBottom());
   }

   private void applyShowBottom() {
      EnderCrystal entity = (EnderCrystal)this.getEntity();
      if (entity != null) {
         entity.setShowingBottom(this.isShowBottom());
      }
   }

   private ItemStack getShowBottomEditorItem() {
      ItemStack iconItem = new ItemStack(this.isShowBottom() ? Material.HEAVY_WEIGHTED_PRESSURE_PLATE : Material.STONE_PRESSURE_PLATE);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonEndCrystalBottomSlab, Messages.buttonEndCrystalBottomSlabLore);
      return iconItem;
   }

   private Button getShowBottomEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return SKEndCrystalShop.this.getShowBottomEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            SKEndCrystalShop.this.cycleShowBottom(backwards);
            return true;
         }
      };
   }

   static {
      SHOW_BOTTOM = (new BasicProperty()).dataKeyAccessor("showBottom", BooleanSerializers.STRICT).useDefaultIfMissing().defaultValue(false).build();
   }
}
