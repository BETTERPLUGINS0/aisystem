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
import org.bukkit.entity.GlowSquid;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class GlowSquidShop extends SKLivingShopObject<GlowSquid> {
   public static final Property<Boolean> DARK;
   private final PropertyValue<Boolean> darkGlowSquidProperty;

   public GlowSquidShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<GlowSquidShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(DARK);
      GlowSquidShop var10002 = (GlowSquidShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.darkGlowSquidProperty = var10001.onValueChanged(var10002::applyDark).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.darkGlowSquidProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.darkGlowSquidProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyDark();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getDarkEditorButton());
      return editorButtons;
   }

   public boolean isDark() {
      return (Boolean)this.darkGlowSquidProperty.getValue();
   }

   public void setDark(boolean dark) {
      this.darkGlowSquidProperty.setValue(dark);
   }

   public void cycleDark(boolean backwards) {
      this.setDark(!this.isDark());
   }

   private void applyDark() {
      GlowSquid entity = (GlowSquid)this.getEntity();
      if (entity != null) {
         entity.setDarkTicksRemaining(this.isDark() ? Integer.MAX_VALUE : 0);
      }
   }

   private ItemStack getDarkEditorItem() {
      ItemStack iconItem;
      if (this.isDark()) {
         iconItem = new ItemStack(Material.INK_SAC);
      } else {
         iconItem = new ItemStack(Material.GLOW_INK_SAC);
      }

      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonGlowSquidDark, Messages.buttonGlowSquidDarkLore);
      return iconItem;
   }

   private Button getDarkEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return GlowSquidShop.this.getDarkEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            GlowSquidShop.this.cycleDark(backwards);
            return true;
         }
      };
   }

   static {
      DARK = (new BasicProperty()).dataKeyAccessor("darkGlowSquid", BooleanSerializers.LENIENT).defaultValue(false).build();
   }
}
