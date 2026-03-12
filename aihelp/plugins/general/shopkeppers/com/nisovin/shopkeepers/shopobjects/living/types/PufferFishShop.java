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
import com.nisovin.shopkeepers.util.data.property.validation.java.IntegerValidators;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.NumberSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.MathUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.entity.PufferFish;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PufferFishShop extends SKLivingShopObject<PufferFish> {
   public static final int MIN_PUFF_STATE = 0;
   public static final int MAX_PUFF_STATE = 2;
   public static final Property<Integer> PUFF_STATE;
   private final PropertyValue<Integer> puffStateProperty;

   public PufferFishShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<PufferFishShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(PUFF_STATE);
      PufferFishShop var10002 = (PufferFishShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.puffStateProperty = var10001.onValueChanged(var10002::applyPuffState).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.puffStateProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.puffStateProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyPuffState();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getPuffStateEditorButton());
      return editorButtons;
   }

   public int getPuffState() {
      return (Integer)this.puffStateProperty.getValue();
   }

   public void setPuffState(int puffState) {
      this.puffStateProperty.setValue(puffState);
   }

   public void cyclePuffState(boolean backwards) {
      int newPuffState = this.getPuffState() + (backwards ? -1 : 1);
      newPuffState = MathUtils.rangeModulo(newPuffState, 0, 2);
      this.setPuffState(newPuffState);
   }

   private void applyPuffState() {
      PufferFish entity = (PufferFish)this.getEntity();
      if (entity != null) {
         entity.setPuffState(this.getPuffState());
      }
   }

   private ItemStack getPuffStateEditorItem() {
      ItemStack iconItem = new ItemStack(Material.PUFFERFISH);
      String puffState;
      switch(this.getPuffState()) {
      case 0:
         puffState = "□□";
         break;
      case 1:
         puffState = "■□";
         break;
      case 2:
      default:
         puffState = "■■";
      }

      String displayName = StringUtils.replaceArguments(Messages.buttonPufferFishPuffState, "puffState", puffState);
      List<String> lore = StringUtils.replaceArguments((Collection)Messages.buttonPufferFishPuffStateLore, (Object[])("puffState", puffState));
      ItemUtils.setDisplayNameAndLore(iconItem, displayName, lore);
      return iconItem;
   }

   private Button getPuffStateEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return PufferFishShop.this.getPuffStateEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            PufferFishShop.this.cyclePuffState(backwards);
            return true;
         }
      };
   }

   static {
      PUFF_STATE = (new BasicProperty()).dataKeyAccessor("puffState", NumberSerializers.INTEGER).validator(IntegerValidators.bounded(0, 2)).defaultValue(0).build();
   }
}
