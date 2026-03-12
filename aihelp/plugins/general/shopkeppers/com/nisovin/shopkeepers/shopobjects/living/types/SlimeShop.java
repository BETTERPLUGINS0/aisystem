package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.config.Settings;
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
import com.nisovin.shopkeepers.util.data.property.validation.java.IntegerValidators;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.NumberSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.MathUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.entity.Slime;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SlimeShop extends SKLivingShopObject<Slime> {
   public static final int MIN_SIZE = 1;
   public static final int MAX_SIZE = 10;
   public static final Property<Integer> SIZE;
   private final PropertyValue<Integer> sizeProperty;

   public SlimeShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<SlimeShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(SIZE);
      SlimeShop var10002 = (SlimeShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.sizeProperty = var10001.onValueChanged(var10002::applySize).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.sizeProperty.load(shopObjectData);
      this.setSize(this.getSize());
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.sizeProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applySize();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getSizeEditorButton());
      return editorButtons;
   }

   public int getSize() {
      return (Integer)this.sizeProperty.getValue();
   }

   public void setSize(int size) {
      int clampedSize = this.clampSize(size);
      if (clampedSize != size) {
         Log.warning(this.shopkeeper.getLogPrefix() + "Slime size clamped to valid bounds: " + size + " -> " + clampedSize);
      }

      this.sizeProperty.setValue(clampedSize);
   }

   private int clampSize(int size) {
      return MathUtils.clamp(size, 1, Settings.slimeMaxSize);
   }

   public void cycleSize(boolean backwards) {
      int size = this.getSize();
      int nextSize;
      if (backwards) {
         nextSize = size - 1;
      } else {
         nextSize = size + 1;
      }

      nextSize = MathUtils.rangeModulo(nextSize, 1, Settings.slimeMaxSize);
      this.setSize(nextSize);
   }

   private void applySize() {
      Slime entity = (Slime)this.getEntity();
      if (entity != null) {
         entity.setSize(this.getSize());
      }
   }

   private ItemStack getSizeEditorItem() {
      int size = this.getSize();
      ItemStack iconItem = new ItemStack(Material.SLIME_BLOCK);
      String displayName = StringUtils.replaceArguments(Messages.buttonSlimeSize, "size", size);
      List<String> lore = StringUtils.replaceArguments((Collection)Messages.buttonSlimeSizeLore, (Object[])("size", size));
      ItemUtils.setDisplayNameAndLore(iconItem, displayName, lore);
      return iconItem;
   }

   private Button getSizeEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return SlimeShop.this.getSizeEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            SlimeShop.this.cycleSize(backwards);
            return true;
         }
      };
   }

   static {
      SIZE = (new BasicProperty()).dataKeyAccessor("slimeSize", NumberSerializers.INTEGER).validator(IntegerValidators.bounded(1, 10)).defaultValue(1).build();
   }
}
