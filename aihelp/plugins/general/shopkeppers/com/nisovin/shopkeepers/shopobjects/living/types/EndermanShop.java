package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopEquipment;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.player.PlaceholderItems;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.shopobjects.living.SKLivingShopObject;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Enderman;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EndermanShop extends SKLivingShopObject<Enderman> {
   public EndermanShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<? extends EndermanShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyCarriedBlock();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      return editorButtons;
   }

   protected void onEquipmentChanged() {
      super.onEquipmentChanged();
      this.applyCarriedBlock();
   }

   private void applyCarriedBlock() {
      Enderman entity = (Enderman)this.getEntity();
      if (entity != null) {
         BlockData blockData = null;
         LivingShopEquipment equipment = this.getEquipment();
         UnmodifiableItemStack item = equipment.getItem(EquipmentSlot.HAND);
         if (!ItemUtils.isEmpty(item)) {
            assert item != null;

            Material blockType = item.getType();
            Material substitutedMaterial = PlaceholderItems.getSubstitutedMaterial(ItemUtils.asItemStack(item));
            if (substitutedMaterial != null && substitutedMaterial.isBlock()) {
               blockType = substitutedMaterial;
            }

            if (blockType.isBlock()) {
               ItemMeta itemMeta = item.getItemMeta();
               if (itemMeta instanceof BlockDataMeta) {
                  BlockDataMeta blockDataMeta = (BlockDataMeta)itemMeta;
                  if (blockDataMeta.hasBlockData()) {
                     blockData = blockDataMeta.getBlockData(blockType);
                  }
               }

               if (blockData == null) {
                  blockData = blockType.createBlockData();
               }
            }
         }

         entity.setCarriedBlock(blockData);
      }
   }
}
