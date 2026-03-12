package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.config.Settings;
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
import org.bukkit.entity.Goat;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class GoatShop extends BabyableShop<Goat> {
   public static final Property<Boolean> SCREAMING;
   public static final Property<Boolean> LEFT_HORN;
   public static final Property<Boolean> RIGHT_HORN;
   private final PropertyValue<Boolean> screamingProperty;
   private final PropertyValue<Boolean> leftHornProperty;
   private final PropertyValue<Boolean> rightHornProperty;

   public GoatShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<GoatShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(SCREAMING);
      GoatShop var10002 = (GoatShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.screamingProperty = var10001.onValueChanged(var10002::applyScreaming).build(this.properties);
      var10001 = new PropertyValue(LEFT_HORN);
      var10002 = (GoatShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.leftHornProperty = var10001.onValueChanged(var10002::applyLeftHorn).build(this.properties);
      var10001 = new PropertyValue(RIGHT_HORN);
      var10002 = (GoatShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.rightHornProperty = var10001.onValueChanged(var10002::applyRightHorn).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.screamingProperty.load(shopObjectData);
      this.leftHornProperty.load(shopObjectData);
      this.rightHornProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.screamingProperty.save(shopObjectData);
      this.leftHornProperty.save(shopObjectData);
      this.rightHornProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyScreaming();
      this.applyLeftHorn();
      this.applyRightHorn();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      if (!Settings.silenceShopEntities) {
         editorButtons.add(this.getScreamingEditorButton());
      }

      editorButtons.add(this.getLeftHornEditorButton());
      editorButtons.add(this.getRightHornEditorButton());
      return editorButtons;
   }

   public boolean isScreaming() {
      return (Boolean)this.screamingProperty.getValue();
   }

   public void setScreaming(boolean screaming) {
      this.screamingProperty.setValue(screaming);
   }

   public void cycleScreaming(boolean backwards) {
      this.setScreaming(!this.isScreaming());
   }

   private void applyScreaming() {
      Goat entity = (Goat)this.getEntity();
      if (entity != null) {
         entity.setScreaming(this.isScreaming());
      }
   }

   private ItemStack getScreamingEditorItem() {
      ItemStack iconItem;
      if (this.isScreaming()) {
         iconItem = new ItemStack(Material.CARVED_PUMPKIN);
      } else {
         iconItem = new ItemStack(Material.PUMPKIN);
      }

      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonGoatScreaming, Messages.buttonGoatScreamingLore);
      return iconItem;
   }

   private Button getScreamingEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return GoatShop.this.getScreamingEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            GoatShop.this.cycleScreaming(backwards);
            return true;
         }
      };
   }

   public boolean hasLeftHorn() {
      return (Boolean)this.leftHornProperty.getValue();
   }

   public void setLeftHorn(boolean hasLeftHorn) {
      this.leftHornProperty.setValue(hasLeftHorn);
   }

   public void cycleLeftHorn(boolean backwards) {
      this.setLeftHorn(!this.hasLeftHorn());
   }

   private void applyLeftHorn() {
      Goat entity = (Goat)this.getEntity();
      if (entity != null) {
         entity.setLeftHorn(this.hasLeftHorn());
      }
   }

   private ItemStack getLeftHornEditorItem() {
      ItemStack iconItem;
      if (this.hasLeftHorn()) {
         iconItem = new ItemStack(Material.GOAT_HORN);
      } else {
         iconItem = new ItemStack(Material.BARRIER);
      }

      return ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonGoatLeftHorn, Messages.buttonGoatLeftHornLore);
   }

   private Button getLeftHornEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return GoatShop.this.getLeftHornEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            GoatShop.this.cycleLeftHorn(backwards);
            return true;
         }
      };
   }

   public boolean hasRightHorn() {
      return (Boolean)this.rightHornProperty.getValue();
   }

   public void setRightHorn(boolean hasRightHorn) {
      this.rightHornProperty.setValue(hasRightHorn);
   }

   public void cycleRightHorn(boolean backwards) {
      this.setRightHorn(!this.hasRightHorn());
   }

   private void applyRightHorn() {
      Goat entity = (Goat)this.getEntity();
      if (entity != null) {
         entity.setRightHorn(this.hasRightHorn());
      }
   }

   private ItemStack getRightHornEditorItem() {
      ItemStack iconItem;
      if (this.hasRightHorn()) {
         iconItem = new ItemStack(Material.GOAT_HORN);
      } else {
         iconItem = new ItemStack(Material.BARRIER);
      }

      return ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonGoatRightHorn, Messages.buttonGoatRightHornLore);
   }

   private Button getRightHornEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return GoatShop.this.getRightHornEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            GoatShop.this.cycleRightHorn(backwards);
            return true;
         }
      };
   }

   static {
      SCREAMING = (new BasicProperty()).dataKeyAccessor("screaming", BooleanSerializers.LENIENT).defaultValue(false).build();
      LEFT_HORN = (new BasicProperty()).dataKeyAccessor("leftHorn", BooleanSerializers.LENIENT).defaultValue(true).build();
      RIGHT_HORN = (new BasicProperty()).dataKeyAccessor("rightHorn", BooleanSerializers.LENIENT).defaultValue(true).build();
   }
}
